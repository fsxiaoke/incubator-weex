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
package com.taobao.weex.log;

import android.os.Handler;
import android.os.HandlerThread;

import com.taobao.weex.disk.FsLazyLruDiskCache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by lid on 2017/8/23.
 * 采用内存映射文件写入，速度快
 * 每2M一个日志文件，写满为止，当达到最大size时会自动生成一个新的日志文件
 * 首次使用先调用start，之后外部只需要writelog即可，内部负责自动切换文件
 * ADF
 */

public class FsMMapWriter implements ILogWriter {
    public interface IFileNameGenerater{
        String getNewFileName();
    }
    String mCurFile;
    IFileNameGenerater mIFileNameGenerater;
    RandomAccessFile mraf;
    FileChannel mfc;
    MappedByteBuffer mbb;
    HandlerThread workerThread;
    FsLazyLruDiskCache mFilecache;
    public Handler getWorkerHandler() {
        return workerHandler;
    }

    Handler workerHandler;
    byte[] lockerMbb=new byte[0];
    int pendingCount;
    long mmapSize=-1;
    public static long s_MIN_mmapSize=100*1024;
    public static long s_Default_mmapSize=1024*1024*2;
    public FsMMapWriter(){
        workerThread=new HandlerThread("FsMMapWriter");
        workerThread.start();
        workerHandler=new Handler(workerThread.getLooper());
    }

    public void setIFileNameGenerater(IFileNameGenerater mIFileNameGenerater) {
        this.mIFileNameGenerater = mIFileNameGenerater;
    }
    public void setFileCache(FsLazyLruDiskCache filecache){
        mFilecache=filecache;
    }
    void closeIO(Closeable io){
        try{
            io.close();
        }catch (IOException e){

        }
    }

    void open(String path) throws IOException {
        if (mraf!=null){
            closeIO(mraf);
        }
        mraf =new RandomAccessFile(path,"rw");
        if (mfc!=null){
            closeIO(mfc);
        }
        mfc=mraf.getChannel();
        if (mmapSize==-1){
            mmapSize=s_Default_mmapSize;
        }else {
        }
        mbb=mfc.map(FileChannel.MapMode.READ_WRITE,0,mmapSize);
        ensureMbb();
//        boolean ret=ensureMbb();
//        if (!ret){
//        }else{
            mCurFile=path;
//        }
    }
    void closeIO(){
        synchronized (lockerMbb){
            if (mbb!=null){
                mbb=null;
            }
            if (mfc!=null){
                try{
                    mfc.close();
                    mfc=null;
                }catch (IOException e){

                }
            }
            if (mraf!=null){
                try{
                    mraf.close();
                    mraf=null;
                }catch (IOException e){

                }
            }
        }

    }
    boolean ensureMbb(){
        boolean ret=false;
        int size=mbb.capacity();
        int contentPos=0;
        for (int i=0;i<size;i++){
            if (mbb.get(i)==0x00){
                contentPos=i;
                break;
            }
        }
        mbb.position(contentPos);
//        if ((size-contentPos)<200){
//
//        }else{
            ret=true;
//        }
        return ret;
    }

    /**
     * 首次使用该类需要先调用start，初始化第一个可用日志文件的资源
     * @param path 存放log的目录
     * @param logfilePrefix log文件名的前缀
     * @throws IOException
     */
    @Override
    public synchronized void start(String path/*log dir*/,String logfilePrefix,long mmapSize) throws IOException {
        if (path==null){
            throw new IOException("path is null");
        }
        if (mmapSize<s_MIN_mmapSize){
            throw new IOException("file size is too small");
        }
        File root=new File(path);
        if (root.exists()){
            if (root.isDirectory()){

            }else{
                throw new IOException("path is not dir");
            }
        }else{
            root.mkdirs();
        }

        Date d=new Date();
        String dateAppend="_"+(d.getYear()+1900)+d.getMonth()+d.getDate();
        String hopefile=logfilePrefix;
        File[] files=root.listFiles();
        File lastTarget=null;
        if (files!=null&&files.length>0){
            Arrays.sort(files, new Comparator<File>() {
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

            for (File f :
                    files) {
                try{
                    if (f.getName().split("_")[0].equals(logfilePrefix)){
                        lastTarget=f;
                    }
                }catch (Exception e){

                }
            }
        }

        String targetfile=null;
        if (lastTarget!=null){
            targetfile=lastTarget.getAbsolutePath();
            mFilecache.updateItem(targetfile);
        }else {
            targetfile=path+File.separator+hopefile+dateAppend+".dat";
            mFilecache.addItem(targetfile);
        }
        this.mmapSize=mmapSize;
        open(targetfile);
    }

    /**
     * 转线程，异步写
     * @param line
     */
    @Override
    public synchronized boolean writelog(String line) {
        boolean ret=false;
        do {
            if (mbb==null){
                break;
            }
            byte[] content;
            try {
                content=(line).getBytes("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ret=true;//到此处日志会丢失
                break;
            }
            if ((content.length+pendingCount)<(mbb.capacity()-mbb.position())){
                pendingCount=content.length;
            }else {
//                closeIO();
                //todo 空间不够了，需要切换日志文件
                String newf=null;
                if (mIFileNameGenerater!=null){
                    newf=mIFileNameGenerater.getNewFileName();
                }else{
                    newf=generateNewFileName();
                }

                try {
                    open(newf);
                } catch (IOException e) {
                    e.printStackTrace();
                    ret=true;//到此处日志会丢失
                    break;
                }
                mFilecache.addItem(newf);
            }
            final byte[] toWrite=content;
//            workerHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    synchronized (lockerMbb){
                        if (mbb!=null){
                            mbb.put(toWrite);
                        }
//                    }
                    pendingCount=0;
//                }
//            });
            ret=true;
        }while (false);
        return ret;

    }
    String generateNewFileName(){
        String ret=null;
        if (mCurFile==null){

        }else {
            int lastIdx=mCurFile.lastIndexOf("_");
            if (lastIdx!=-1){
                String postfixStr=mCurFile.substring(lastIdx+1);
                int lastidxDot=postfixStr.lastIndexOf(".");
                if (lastidxDot!=-1){
                    String filepostfix=postfixStr.substring(lastidxDot+1);
                    try {
                        String tspostfix=postfixStr.substring(0,lastidxDot);
                        Long.valueOf(tspostfix);
                        if (tspostfix.length()!=13){
                            ret=mCurFile.substring(0,mCurFile.lastIndexOf("."))+"_"+System.currentTimeMillis()+"."+filepostfix;
                        }else{
                            ret=mCurFile.substring(0,lastIdx)+"_"+System.currentTimeMillis()+"."+filepostfix;
                        }
                    }catch (NumberFormatException e){
                        ret=mCurFile.substring(0,mCurFile.lastIndexOf("."))+"_"+System.currentTimeMillis()+"."+filepostfix;
                    }
                }else{
                    try {
                        Long.valueOf(postfixStr);
                        ret=mCurFile.substring(0,lastIdx)+"_"+System.currentTimeMillis();
                    }catch (NumberFormatException e){
                        ret=mCurFile+"_"+System.currentTimeMillis();
                    }
                }
            }else{
                int lastdot=mCurFile.lastIndexOf(".");
                if (lastdot!=-1){
                    ret=mCurFile.substring(0,mCurFile.lastIndexOf("."))+"_"+System.currentTimeMillis()+"."+mCurFile.substring(lastdot+1);
                }else{
                    ret=mCurFile+"_"+System.currentTimeMillis();
                }
            }
        }
        return ret;
    }
}
