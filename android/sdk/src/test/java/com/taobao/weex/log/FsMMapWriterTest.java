package com.taobao.weex.log;

import com.taobao.weappplus_sdk.BuildConfig;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
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
        WXSDKEngine.initialize(RuntimeEnvironment.application,new InitConfig.Builder().build());
        WXSDKEngine.setLogLevel("debug");
        FsMMapWriter.s_MIN_mmapSize=512;
        writer= new FsMMapWriter();
        writer.start("./testdata","com.facishare.fsneice",512);
    }

    @Test
    public void open() throws Exception {

    }

    @Test
    public void writelog() throws Exception {
        String test1=fillLetter("ABCD\n");
        writer.writelog(test1);
        String test2=fillLetter("EFGH\n");
        writer.writelog(test2);

        writer.writelog("i love you111\n");
    }
    String fillLetter(String l){
        String ret="";
        for (int i=0;i<100;i++){
            ret+=l;
        }
        return ret;
    }
}