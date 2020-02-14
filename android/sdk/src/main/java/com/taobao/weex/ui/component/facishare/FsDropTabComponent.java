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
package com.taobao.weex.ui.component.facishare;

import java.lang.reflect.InvocationTargetException;

import com.taobao.weex.R;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXBaseRefreshLayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Component for scroller. It also support features like
 * "appear", "disappear" and "sticky"
 */
@Component(lazyload = false)

public class FsDropTabComponent extends WXVContainer<FrameLayout>  {



    public static class Creator implements ComponentCreator {
        @Override
        public WXComponent createInstance(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            // For performance message collection
//            instance.setUseScroller(true);
            return new FsDropTabComponent(instance, parent, basicComponentData);
        }
    }


    public FsDropLayout mRootView = null;

    public FsDropTabComponent(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, instanceId, isLazy, basicComponentData);
    }

    public FsDropTabComponent(WXSDKInstance instance, WXVContainer parent, boolean lazy, BasicComponentData basicComponentData) {
        super(instance, parent, lazy, basicComponentData);
    }

    public FsDropTabComponent(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    @Override
    protected FsDropLayout initComponentHostView(@NonNull Context context) {

        mRootView =
                (FsDropLayout) LayoutInflater.from(context).inflate( R.layout.weex_droptablayout,
                null);
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        mRootView.setLayoutParams(params);
        return  mRootView;

    }

    @Override
    public void addSubView(View child, int index) {
        if (child == null || mRootView == null) {
            return;
        }


        ViewGroup.LayoutParams params = child.getLayoutParams();

        if(params == null){
            params =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        if(params instanceof FrameLayout.LayoutParams){
            ((FrameLayout.LayoutParams)params).gravity = Gravity.CENTER;
            child.setLayoutParams(params);
        }


        mRootView.addView(child);

    }




    @Override
    public ViewGroup.LayoutParams getChildLayoutParams(WXComponent child, View childView, int width, int height, int left, int right, int top, int bottom) {
        ViewGroup.LayoutParams lp = childView.getLayoutParams();
        if (lp == null) {
            lp = new FrameLayout.LayoutParams(width, height);
        } else {
            lp.width = width;
            lp.height = height;
        }

        ((ViewGroup.MarginLayoutParams) lp).setMargins(0, 0, 0, 0);
        return lp;
    }

    @Override
    public void setLayout(WXComponent component) {
        if (TextUtils.isEmpty(component.getComponentType())
                || TextUtils.isEmpty(component.getRef()) || component.getLayoutPosition() == null
                || component.getLayoutSize() == null) {
            return;
        }
        if (component.getHostView() != null) {
            int layoutDirection = component.isLayoutRTL() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR;
            ViewCompat.setLayoutDirection(component.getHostView(), layoutDirection);
        }
        CSSShorthand cs=new CSSShorthand();

        component.setMargins(cs);
        component.getLayoutPosition().update(0,0,0,0);
        super.setLayout(component);
    }




}
