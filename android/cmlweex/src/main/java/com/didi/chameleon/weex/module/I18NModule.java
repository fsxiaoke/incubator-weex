/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.didi.chameleon.weex.module;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xiongtj
 */
public class I18NModule extends WXModule {
    private static final String TAG = I18NModule.class.getSimpleName();


    @JSMethod(uiThread = false)
    public String getText(String key){
        return key;
    }


    @JSMethod(uiThread = false)
    public Map<String,String> getTextList(List<String> list){
        Map<String,String> retMap = new HashMap<>();
        if(list!=null){
            int size = list.size();
            for(int i = 0; i< size; i++){
                String key = list.get(i);
                retMap.put(key,key);
            }
        }
        return retMap;
    }
}
