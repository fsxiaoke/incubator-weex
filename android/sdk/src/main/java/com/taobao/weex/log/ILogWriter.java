package com.taobao.weex.log;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by lid on 2017/8/23.
 */

public interface ILogWriter {
    void start(String path/*log dir*/,String logfilePrefix) throws IOException;
    boolean writelog(String line);
}
