package com.taobao.weex.log;

import com.taobao.weappplus_sdk.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

/**
 * Created by lid on 2017/8/24.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)
public class FsMMapWriterTest {
    static FsMMapWriter writer;
    public static ShadowLooper getLooper() {
        return Shadows.shadowOf(writer.getWorkerHandler().getLooper());
    }
    @Before
    public void setUp() throws Exception {
        writer= new FsMMapWriter();
        writer.start("./testdata","FsMMapWriterTest");
    }

    @Test
    public void open() throws Exception {

    }

    @Test
    public void writelog() throws Exception {
        writer.writelog("i love you");
        getLooper().idle();
        writer.writelog("i love you111");
        getLooper().idle();
    }

}