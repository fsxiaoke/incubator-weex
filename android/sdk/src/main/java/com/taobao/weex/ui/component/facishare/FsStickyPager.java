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
package com.taobao.weex.ui.component.facishare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.ui.view.WXHorizontalScrollView;
import com.taobao.weex.ui.view.coordinatortablayout.AdvanceSwipeRefreshLayout;
import com.taobao.weex.ui.view.coordinatortablayout.CoordinatorTabLayout;
import com.taobao.weex.utils.WXUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Component for scroller. It also support features like
 * "appear", "disappear" and "sticky"
 */
@Component(lazyload = false)

public class FsStickyPager extends WXVContainer<AdvanceSwipeRefreshLayout> {

    public static class Creator implements ComponentCreator {
        @Override
        public WXComponent createInstance(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new FsStickyPager(instance, parent, basicComponentData);
        }
    }

    public CoordinatorTabLayout mTabLayout = null;
    public AdvanceSwipeRefreshLayout mSwiper = null;

    public FsStickyPager(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, instanceId, isLazy, basicComponentData);
    }

    public FsStickyPager(WXSDKInstance instance, WXVContainer parent, boolean lazy, BasicComponentData basicComponentData) {
        super(instance, parent, lazy, basicComponentData);
    }

    public FsStickyPager(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    @Override
    protected AdvanceSwipeRefreshLayout initComponentHostView(@NonNull Context context) {

        mSwiper = new AdvanceSwipeRefreshLayout(context);
        mTabLayout = new CoordinatorTabLayout(context);
        mSwiper.addView(mTabLayout);

        mSwiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("zds", "onRefresh event");
                if (getEvents().contains(Constants.Event.ONREFRESH)) {
                    fireEvent(Constants.Event.ONREFRESH);
                }
            }
        });
        mSwiper.setOnPreInterceptTouchEventDelegate(new AdvanceSwipeRefreshLayout.OnPreInterceptTouchEventDelegate() {
            @Override
            public boolean shouldDisallowInterceptTouchEvent(MotionEvent ev) {
                return mTabLayout.getTabLayout().getTop() < 0;
            }
        });

        return  mSwiper;

    }

    protected void setHostLayoutParams(ViewPager host, int width, int height, int left, int right, int top, int bottom) {
        ViewGroup.LayoutParams lp;
        if (mParent == null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            this.setMarginsSupportRTL(params, left, top, right, bottom);
            lp = params;
        } else {
            lp = mParent.getChildLayoutParams(this, host, width, height, left, right, top, bottom);
        }
        lp.height= ViewGroup.LayoutParams.MATCH_PARENT;
        if (lp != null) {
            host.setLayoutParams(lp);
        }
    }

    @Override
    public void addSubView(View view, int index) {
        if (view == null) {
            return;
        }

        if (view instanceof WXFrameLayout) {
            mTabLayout.addTopView(view);
        }

        if(view instanceof TabLayout){
            mTabLayout.addTabView((TabLayout) view);
        }

        if(view instanceof ViewPager){

            mTabLayout.addPageView((ViewPager) view);
        }
    }

    @Override
    protected boolean setProperty(String key, Object param) {
        switch (key) {
            case Constants.Name.REFRESHING:
                boolean refresh = WXUtils.getBoolean(param, false);
                if(mSwiper != null)
                    mSwiper.setRefreshing(refresh);
                return true;
        }
        return true;
    }

}
