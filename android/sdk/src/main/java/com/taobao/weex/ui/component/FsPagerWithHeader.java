/*
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
package com.taobao.weex.ui.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.ui.view.WXHorizontalScrollView;
import com.taobao.weex.ui.view.WXTextView;
import com.taobao.weex.ui.view.coordinatortablayout.CoordinatorTabLayout;


import java.lang.reflect.InvocationTargetException;

/**
 * Component for scroller. It also support features like
 * "appear", "disappear" and "sticky"
 */
@Component(lazyload = false)

public class FsPagerWithHeader extends WXVContainer<CoordinatorTabLayout> {


    public static class Creator implements ComponentCreator {
        @Override
        public WXComponent createInstance(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            // For performance message collection
//            instance.setUseScroller(true);
            return new FsPagerWithHeader(instance, parent, basicComponentData);
        }
    }


    public CoordinatorTabLayout mTabLayout = null;

    public FsPagerWithHeader(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, instanceId, isLazy, basicComponentData);
    }

    public FsPagerWithHeader(WXSDKInstance instance, WXVContainer parent, boolean lazy, BasicComponentData basicComponentData) {
        super(instance, parent, lazy, basicComponentData);
    }

    public FsPagerWithHeader(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    @Override
    protected CoordinatorTabLayout initComponentHostView(@NonNull Context context) {

        mTabLayout = new CoordinatorTabLayout(context);
        return  mTabLayout;

    }

    @Override
    public void addSubView(View view, int index) {
        if (view == null) {
            return;
        }

        if (view instanceof WXFrameLayout) {
            mTabLayout.addTopView(view);
        }

        if(view instanceof WXTextView||view instanceof WXHorizontalScrollView){
            mTabLayout.addTabView(view);
        }

        if(view instanceof ViewPager){

            mTabLayout.addPageView(view);
        }



    }

}
