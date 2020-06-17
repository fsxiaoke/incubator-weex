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
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.taobao.weex.R;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.ui.view.WXHorizontalScrollView;
import com.taobao.weex.ui.view.WXScrollView;
import com.taobao.weex.ui.view.coordinatortablayout.AdvanceSwipeRefreshLayout;
import com.taobao.weex.ui.view.coordinatortablayout.CoordinatorTabLayout;
import com.taobao.weex.utils.WXUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

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

    FrameLayout mContainer;
    public WXScrollView mScrollView;
    private void initScrollView(){
        if(mScrollView == null){
            mScrollView = new WXScrollView(getContext());
            ViewGroup.LayoutParams  params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mScrollView.setLayoutParams(params);
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
        initScrollView();
        ViewGroup.LayoutParams  params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mTabLayout = new CoordinatorTabLayout(context);

        mSwiper = new AdvanceSwipeRefreshLayout(context);
        mContainer = new FrameLayout(getContext());
        mSwiper.setLayoutParams(params);
        mSwiper.addView(mContainer, params);
        mContainer.addView(mTabLayout, params);

        mSwiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("zds", "onRefresh event");
                if (getEvents().contains(Constants.Event.ONREFRESH)) {
                    mSwiper.setRefreshing(true);
                    fireEvent(Constants.Event.ONREFRESH, new HashMap<String, Object>());
                }
            }
        });
        mSwiper.setOnPreInterceptTouchEventDelegate(new AdvanceSwipeRefreshLayout.OnPreInterceptTouchEventDelegate() {
            @Override
            public boolean shouldDisallowInterceptTouchEvent(MotionEvent ev) {
                Log.e("zds", "mTabLayout gettop: " + mTabLayout.getAppBar().getTop());
                return mTabLayout.getAppBar().getTop() < 0;
            }
        });

        mTabLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int lastOffset;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (lastOffset != i && getEvents().contains(Constants.Event.SCROLL)) {
                    Map<String, Object> event = new HashMap<>(1);
                    event.put("offset", i);
                    fireEvent(Constants.Event.SCROLL, event);
                    lastOffset = i;
                }
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

        if(view instanceof RelativeLayout){
            mTabLayout.addTabView((RelativeLayout) view);
        }

        if(view instanceof CustomViewPager){

            mTabLayout.addPageView((CustomViewPager) view);
        }
        changeHostView();
    }
    @Override
    public void remove(WXComponent child, boolean destroy) {


        View view = child.getHostView();
        if (view instanceof WXFrameLayout) {
            mTabLayout.removeTopView(view);
        }

        if(view instanceof RelativeLayout){
            mTabLayout.removeTabView((RelativeLayout) view);
        }

        if(view instanceof CustomViewPager){

            mTabLayout.removePageView((CustomViewPager) view);
        }

        super.remove(child,destroy);


        changeHostView();
    }

    private boolean isStickyPager(){
        View view = mContainer.getChildAt(0);
        if(view != null && view instanceof CoordinatorTabLayout){
            return true;
        }
        return false;
    }
    private void changeHostView(){
       boolean isSticky = isStickyPager();
       boolean isTabOrPagerEmpty = mTabLayout.isTabOrPagerEmpty();
        if(isSticky && isTabOrPagerEmpty){
            ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            View topView = mTabLayout.removeAndGetTopView();
            if(topView !=null) {
                mContainer.removeView(mTabLayout);
                mContainer.addView(mScrollView, 0, params);
                mScrollView.addView(topView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }else if(!isSticky && !isTabOrPagerEmpty){
//            ScrollView scrollView = getScrollView();
            ViewGroup.LayoutParams  params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            View topView = mScrollView.getChildAt(0);
            mScrollView.removeAllViews();
            mTabLayout.addTopView(topView);
            mContainer.removeView(mScrollView);
            mContainer.addView(mTabLayout, 0,params);

        }

    }



    @WXComponentProp(name = Constants.Name.DISPLAY)
    public void setRefreshing(boolean display) {
        if(mSwiper != null)
            mSwiper.setRefreshing(display);
    }

    @WXComponentProp(name = Constants.Name.ENABLE)
    public void setEnable(boolean enable) {
        if(mSwiper != null)
            mSwiper.setEnabled(enable);
    }


}
