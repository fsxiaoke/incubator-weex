/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.taobao.weex.disk;

import android.text.TextUtils;

import com.taobao.weex.utils.WXFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lid on 2017/8/23.
 * 该类对于被管理的目录缓存属于不严谨的管理，
 * 因为，目录内容的变更不一定会触发该类的add/remove/update，业务使用者可能会遗漏对这些方法的调用
 * 优点：业务使用起来比较灵活，能够满足任意业务逻辑的扩展，几乎没有耦合，同时又不需要业务担心cache目录的无限扩张
 * 缺点：对cache的evict操作可能会不太及时，每次业务init时，会做一次全量evict，此时会根据lastModified去evict
 *
 * 该类不负责文件内容的存取，但是负责删除，同时也给业务使用者提供了删除回调，供使用者二次确认是否可以删除
 * 一个key可能会对应多个文件，删除时会将key对应的所有文件全部删除
 */

public class FsLazyLruDiskCache {
    public interface IKeyGenerater{
        /**外部如果设置了该接口，在addItem时会调用该方法，如果没设置，默认key就是文件名
         * @param path
         * @return
         */
        String generateKey(String path);
    }
    public interface IEvictListerner{
        /**
         * 当删除entry时会回调该接口，业务侧根据需要决定是否删除
         * 如果返回false，就不删除了，返回true就删除该entry
         */
        boolean toEvict(String path/*dir or file*/,long size, long createtime,long lastmodifytime);
    }
    public FsLazyLruDiskCache(){
    }
    public FsLazyLruDiskCache(ExecutorService exe){
        executorService=exe;
    }
    public class Config{
        long size=Invalid_Int;//最大Cache Size
        int count=Invalid_Int;//cache中最大entry数量

        public Config setSize(int size) {
            this.size = size;
            return this;
        }

        public Config setCount(int count) {
            this.count = count;
            return this;
        }
    }
    class Entry{
        String path;
        long size;//如果是目录会包括所有子目录的size，可能会不实时
        long createtime;
        long lastmodifytime;
        long lastupdatetime;

        @Override
        public boolean equals(Object obj) {
            boolean ret=false;
            Entry target=(Entry)obj;
            if ((target.path==null&&this.path==null)||
                    (target.path!=null&&this.path!=null&&this.path.equals(target.path))){
                ret=true;
            }
            return ret;
        }
    }
    static final int Invalid_Int=-1;
    static int s_updatelimit=300000;
    LinkedHashMap mlruEntrys=new LinkedHashMap<String,List<Entry>>(0,0.75f,true);


    IEvictListerner mel;
    IKeyGenerater mkg;
    Config mConfig;
    File mrootPath;
    long mcurTotalSize;
    long mcurTotalCount;
    ExecutorService executorService =
            new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());


    private final Callable<Void> cleanupCallable = new Callable<Void>() {
        public Void call() throws Exception {
            synchronized (FsLazyLruDiskCache.this) {
                trimToSize();
                trimToFileCount();
            }
            return null;
        }
    };
    private void trimToFileCount() throws IOException {
        while (mcurTotalCount > mConfig.count) {
            Map.Entry<String, List<Entry>> toEvict = (Map.Entry<String, List<Entry>>)mlruEntrys.entrySet().iterator().next();
            boolean b=removeKey(toEvict.getKey(),true);
            if (!b){
                break;
            }
        }
    }
    private void trimToSize() throws IOException {
        while (mcurTotalSize > mConfig.size) {
            Map.Entry<String, List<Entry>> toEvict = (Map.Entry<String, List<Entry>>)mlruEntrys.entrySet().iterator().next();
            boolean b=removeKey(toEvict.getKey(),true);
            if (!b){
                break;
            }
        }
    }
    class CalcCallable implements Callable<Void>{
        Entry mEntry;
        public void setEntry(Entry en){
            mEntry=en;
        }
        public Void call() throws Exception {
            synchronized (FsLazyLruDiskCache.this) {
                File f=new File(mEntry.path);
                mEntry.lastmodifytime=f.lastModified();
                mEntry.lastupdatetime=System.currentTimeMillis();
                mEntry.size=WXFileUtils.getDirSize(f);
                mcurTotalSize+=mEntry.size;
                executorService.submit(cleanupCallable);
            }
            return null;
        }
    }

    /**该函数会接管传入的目录,
     * 目录下的一级child会作为entry条目被管理起来
     * 如果entry是目录，每次init会计算一下其中所有child的信息
     * @param path
     */
    public synchronized void init(String path){
        if (TextUtils.isEmpty(path)||mrootPath!=null){
            return;
        }
        mrootPath=new File(path);
        if (mConfig==null){
            throw new RuntimeException("must config before init");
        }
        if (!mrootPath.exists()){
            mrootPath.mkdirs();
        }else{
            final File[] files= mrootPath.listFiles();
            if (files==null||files.length==0){

            }else {
                Arrays.sort(files, new Comparator<File>(){
                    @Override
                    public int compare(File o1, File o2) {
                        long f1=o1.lastModified();
                        long f2=o2.lastModified();
                        int ret=0;
                        if (f1==f2){
                            ret=0;
                        }else if (f1<f2){
                            ret=-1;
                        }else if (f1>f2){
                            ret=1;
                        }
                        return ret;
                    }
                });
                executorService.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        for (File f :
                                files) {
                            addItem(f.getAbsolutePath(),true);
                        }
                        executorService.submit(cleanupCallable);
                        return null;
                    }
                });
            }
        }

    }

    /**先调用config，再调用init
     * @param
     */
    public FsLazyLruDiskCache config(Config con){
        mConfig=con;
        return this;
    }

    public FsLazyLruDiskCache setEvictListerner(IEvictListerner mel) {
        this.mel = mel;
        return this;
    }

    public FsLazyLruDiskCache setKeyGenerater(IKeyGenerater mkg) {
        this.mkg = mkg;
        return this;
    }
    public synchronized boolean addItem(String path/*dir or file*/){
        return addItem(path,false);
    }

    /**
     * 缓存文件存储就绪后调用该方法
     * @param path
     * @param sync
     * @return
     */
    private synchronized boolean addItem(String path/*dir or file*/,boolean sync){
        boolean ret=false;
        do {
            if (mrootPath==null){
                break;
            }
            File f=new File(path);
            if (!f.exists()){
                break;
            }
            if (!f.getParentFile().getAbsolutePath().equals(mrootPath.getAbsolutePath())){
                throw new RuntimeException("you must add file in cache dir");
            }
            path=f.getAbsolutePath();
            String key=getkey(path);
            List myentrys=(List)mlruEntrys.get(key);
            if (myentrys==null){
                myentrys=new ArrayList<Entry>();
                mlruEntrys.put(key,myentrys);
            }
            Entry en=new Entry();
            en.path=path;
            en.createtime=Invalid_Int;
            en.lastmodifytime=f.lastModified();
            if (myentrys.contains(en)){
                break;
            }
            myentrys.add(en);
            mcurTotalCount++;
            if (sync){
                en.lastupdatetime=System.currentTimeMillis();
                en.size=WXFileUtils.getDirSize(f);
                mcurTotalSize+=en.size;
            }else{
                CalcCallable calcEntry= new CalcCallable();
                calcEntry.setEntry(en);
                executorService.submit(calcEntry);
            }

            ret=true;
        }while (false);
        return ret;
    }

    /**使用者认为该缓存文件需要删除时调用该方法
     * @param path
     * @return
     */
    public synchronized boolean removeItem(String path/*dir or file*/){
        boolean ret=false;
        do {
            File f= new File(path);
            path=f.getAbsolutePath();
            String key=getkey(path);
            ret=removeKey(key,false);

        }while (false);
        return ret;
    }
    private synchronized boolean removeKey(String key/*dir or file*/,boolean needEvictListerner){
        boolean ret=false;
        do {
            if (key==null){
                break;
            }
            List<Entry> myentrys=(List<Entry>)mlruEntrys.get(key);
            boolean needDelete=true;
            if (!needEvictListerner){

            }else {
                if (mel!=null&&myentrys!=null&&myentrys.size()>0){
                    boolean b=mel.toEvict(myentrys.get(0).path,myentrys.get(0).size,myentrys.get(0).createtime,myentrys.get(0).lastmodifytime);
                    if (!b){
                        needDelete=false;
                    }
                }
            }
            if (needDelete&&myentrys!=null&&myentrys.size()>0){
                mlruEntrys.remove(key);
                for (Entry en :
                        myentrys) {
                    mcurTotalSize-=en.size;
                    mcurTotalCount--;
                    File f=new File(en.path);
                    WXFileUtils.deleteDir(f);
                }
                ret=true;
            }else{

            }
        }while (false);
        return ret;
    }
    /**当path被使用或者发生变更时都要调用update
     *
     * 针对同一个entry，最小更新间隔为5分钟（函数内部控制）
     * @param path
     * @return
     */
    public synchronized boolean updateItem(String path/*dir or file*/){
        boolean ret=false;
        do {
            File f=new File(path);
            if (!f.exists()){
                break;
            }
            path=f.getAbsolutePath();
            String key=getkey(path);
            Entry en=getEntry(key,path);
            if (en==null){
                break;
            }
            long now=System.currentTimeMillis();
            if ((now-en.lastupdatetime)>s_updatelimit){
                CalcCallable calcEntry= new CalcCallable();
                calcEntry.setEntry(en);
                executorService.submit(calcEntry);
                ret=true;
            }else {

            }
        }while (false);
        return ret;
    }
    Entry getEntry(String key,String path){
        Entry ret=null;
        List<Entry> myentrys=(List<Entry>)mlruEntrys.get(key);
        if (myentrys!=null&&myentrys.size()>0){
            for (Entry en :
                    myentrys) {
                if (en.path.equals(path)){
                    ret=en;
                    break;
                }
            }
        }
        return ret;
    }
    String getkey(String path){
        String key=path;
        if (mkg!=null){
            key=mkg.generateKey(path);
        }
        return key;
    }
}
