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

import android.support.annotation.NonNull;

import com.taobao.weappplus_sdk.BuildConfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by lid on 2017/8/25.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)
public class FsLazyLruDiskCacheTest {
    FsLazyLruDiskCache cache;
    ExecutorService executor = new ExecutorService() {
        @Override
        public void execute(Runnable runnable) {

        }

        @Override
        public void shutdown() {

        }

        @NonNull
        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
            return false;
        }

        @NonNull
        @Override
        public <T> Future<T> submit(Callable<T> callable) {
            try {
                callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @NonNull
        @Override
        public <T> Future<T> submit(Runnable runnable, T t) {
            return null;
        }

        @Override
        public Future<?> submit(Runnable command) {
            command.run();
            return null;
        }

        @NonNull
        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
            return null;
        }

        @NonNull
        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException {
            return null;
        }

        @NonNull
        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }
    };
    @Before
    public void setUp() throws Exception {
        cache=new FsLazyLruDiskCache(executor);
        cache.config(cache.new Config().setCount(10).setSize(5*1024*1024));
        cache.setEvictListerner(new FsLazyLruDiskCache.IEvictListerner() {
            @Override
            public boolean toEvict(String path, long size, long createtime, long lastmodifytime) {
                return true;
            }
        });
        cache.setKeyGenerater(new FsLazyLruDiskCache.IKeyGenerater() {
            @Override
            public String generateKey(String path) {
                return path.contains("3")?"3":path;
            }
        });
        cache.init("./testdata");
    }

    @Test
    public void init() throws Exception {
        cache.init("./testdata");
//        CountDownLatch cdl=new CountDownLatch(2);
//        cdl.await();
    }
    @Test
    public void add() throws Exception {
        boolean b=cache.addItem("./testdata/2.dat");//该key在init时已经被添加�?
        Assert.assertFalse(b);

        File f= new File("./testdata/3.dat");
        f.createNewFile();
        boolean b1=cache.addItem("./testdata/3.dat");
        Assert.assertTrue(b1);
//        CountDownLatch cdl=new CountDownLatch(2);
//        cdl.await();
    }
    @Test
    public void update() throws Exception {
        FsLazyLruDiskCache.s_updatelimit=100;
        Thread.sleep(110);
        boolean b=cache.updateItem("./testdata/2.dat");
        Assert.assertTrue(b);

        boolean b1=cache.updateItem("./testdata/3.dat");
        Assert.assertTrue(b1);

        boolean b2=cache.updateItem("./testdata/2.dat");
        Assert.assertFalse(b2);
//        CountDownLatch cdl=new CountDownLatch(2);
//        cdl.await();
    }
}