/*
 * Copyright (C) 2019 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.didi.chameleon.sdk.extend;

import com.didi.chameleon.sdk.ICmlActivityInstance;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;

/**
 * Created by xiongtj on 2019/04/10.
 */
@CmlModule(alias = "fsEnv")
public class CmlFsEnvModule {

    public CmlFsEnvModule() {
    }

    @CmlMethod(alias = "getHost")
    public void getHost(ICmlActivityInstance instance,
                                    CmlCallback callback) {
        if(instance == null || instance.getContext() == null) {
            return;
        }
        callback.onCallback("https://www.fxiaoke.com");
    }
}