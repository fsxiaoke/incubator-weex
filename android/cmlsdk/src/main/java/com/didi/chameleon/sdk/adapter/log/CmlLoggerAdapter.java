package com.didi.chameleon.sdk.adapter.log;

public interface CmlLoggerAdapter {

    void v(String tag, String msg);

    void d(String tag, String msg);

    void i(String tag, String msg);

    void w(Throwable tr);

    void w(String tag, String msg);

    void w(String tag, String msg, Throwable tr);

    void e(Throwable tr);

    void e(String tag, String msg);

    void e(String tag, String msg, Throwable tr);
}
