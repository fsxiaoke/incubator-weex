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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.taobao.weex.R;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXBaseRefreshLayout;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.utils.WXViewUtils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Component for scroller. It also support features like
 * "appear", "disappear" and "sticky"
 */
@Component(lazyload = false)

public class FsTabComponent extends WXVContainer<RelativeLayout> implements TabLayout.OnTabSelectedListener,
        FsTabLayout.ScrollViewListener {


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mTabLayout.setSelectedTabIndicatorColor(tabSelectColor);//隐藏tab选中
        if (getEvents().contains(Constants.Event.ON_TAB_SELECTED)) {
            fireEvent(Constants.Event.ON_TAB_SELECTED, getTabEvent(tab));
        }

    }

    public Map<String, Object> getTabEvent(TabLayout.Tab tab){
        Map<String, Object> event = new HashMap<>(1);

        event.put("index", tab.getPosition());
        return  event;
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (getEvents().contains(Constants.Event.ON_TAB_UNSELECTED)) {
            fireEvent(Constants.Event.ON_TAB_UNSELECTED, getTabEvent(tab));
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        mTabLayout.setSelectedTabIndicatorColor(tabSelectColor);//隐藏tab选中
        boolean set = mTabLayout.setCurrentPagerWithDropTab(tab.getPosition(),childCount());

        if(set){
            fireEvent(Constants.Event.ON_TAB_SELECTED, getTabEvent(tab));
        }else{
            if (getEvents().contains(Constants.Event.ON_TAB_RESELECTED)) {
                fireEvent(Constants.Event.ON_TAB_RESELECTED, getTabEvent(tab));
            }
        }

    }



    @Override
    public void onScrollChanged(FsTabLayout.ScrollType scrollType) {
        if(scrollType == FsTabLayout.ScrollType.IDLE){//停止滚动

            if (getEvents().contains(Constants.Event.ON_TAB_STOP_SCROLL)) {
                fireStopEvent();
            }
        }

    }
    int tabItemWidth;
    int screenItemSize;
    private void fireStopEvent(){
        if(tabItemWidth== 0 ){
            tabItemWidth = mTabLayout.getTabAt(0).getCustomView().getWidth();
        }
        if(screenItemSize==0){
            screenItemSize = Math.round((float)WXViewUtils.getScreenWidth(WXEnvironment.sApplication)/(float)tabItemWidth);
            if(screenItemSize==0){
                screenItemSize = 1;
            }
        }
        int startIndex = Math.round((float)mTabLayout.getScrollX()/(float)tabItemWidth);
        int endIndex = startIndex + screenItemSize -1;

        Map<String, Object> event = new HashMap<>(1);

        event.put("startIndex", startIndex);
        event.put("endIndex", endIndex);
        event.put("scrollX", mTabLayout.getScrollX());
        fireEvent(Constants.Event.ON_TAB_STOP_SCROLL, event);
    }

    public static class Creator implements ComponentCreator {
        @Override
        public WXComponent createInstance(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            // For performance message collection
//            instance.setUseScroller(true);
            return new FsTabComponent(instance, parent, basicComponentData);
        }
    }


    public FsTabLayout mTabLayout = null;
    public FrameLayout mDropLayout = null;

    public FsTabComponent(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, instanceId, isLazy, basicComponentData);
    }

    public FsTabComponent(WXSDKInstance instance, WXVContainer parent, boolean lazy, BasicComponentData basicComponentData) {
        super(instance, parent, lazy, basicComponentData);
    }

    public FsTabComponent(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    @Override
    protected RelativeLayout initComponentHostView(@NonNull Context context) {

        RelativeLayout root = (RelativeLayout) LayoutInflater.from(context).inflate( R.layout.weex_tablayout,
                null);
        mTabLayout = root.findViewById(R.id.fs_tab);
        mDropLayout = root.findViewById(R.id.drop_container);
//        mTabLayout = new TabLayout(context);
        //tab可滚动
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.addOnTabSelectedListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        root.setLayoutParams(params);
        mTabLayout.setHandler(new Handler());
        mTabLayout.setOnScrollStateChangedListener(this);
        return  root;

    }


    Runnable initRunnable = null;
    int initIndex = 0;
    @Override
    public void addSubView(final View child, int index) {
        if (child == null || mTabLayout == null) {
            return;
        }

        if (child instanceof WXBaseRefreshLayout) {
            return;
        }


        if (child instanceof FsDropLayout){
            mDropLayout.addView(child);
            child.post(new Runnable() {
                @Override
                public void run() {
                    View parent = mTabLayout.getChildAt(0);
                    if(parent != null){
                        parent.setPadding(0,0,child.getWidth(),0);
                    }
                }
            });

            mTabLayout.setHaveDropTab(true);
            return;
        }

//        mTabLayout.addTab(mTabLayout.newTab().setText("text"+index));
        TabLayout.Tab tab = mTabLayout.newTab().setCustomView(child);
        child.setTag(tab);
//        mTabLayout.addTab(tab);



        int count = mTabLayout.getTabCount();
        index = index >= count ? -1 : index;
        if (index == -1) {
            mTabLayout.addTab(tab,false);
        } else {
            mTabLayout.addTab(tab,index,false);
        }



        mTabLayout.initTabCount(childCount());

        if (initIndex != -1 && mTabLayout.getTabCount()==mTabLayout.mRealCount&& mTabLayout.getTabCount() > initIndex) {
            if(initRunnable == null){
                initRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mTabLayout.getTabAt(initIndex).select();
                        mTabLayout.setAllowedSwipeDirection();
                        initRunnable = null;
                    }
                };
            }
            mTabLayout.removeCallbacks(initRunnable);
            mTabLayout.postDelayed(initRunnable, 50);
        }
//        mTabLayout.setAllowedSwipeDirection();
    }


    @Override
    public void remove(WXComponent child, boolean destroy) {

        if (child !=null && child instanceof FsDropTabComponent){
            mDropLayout.removeView(child.getHostView());
            mTabLayout.getChildAt(0).setPadding(0,0,0,0);

            mTabLayout.setHaveDropTab(false);
            mTabLayout.initTabCount(childCount());
            if (destroy) {
                child.destroy();
            }
            return;
        }

        if (child == null || mChildren == null || mChildren.size() == 0) {
            return;
        }




        mChildren.remove(child);
        if(mTabLayout!=null) {
            Object tag = child.getHostView().getTag();
            if (tag != null) {
                TabLayout.Tab tab = (TabLayout.Tab) tag;
                mTabLayout.removeTab(tab);
            }
        }
        if (destroy) {
            child.destroy();
        }
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


    @WXComponentProp(name = Constants.Name.TAB_SELECTED_HEIGHT)
    public void setTabSelectedHeight(int height) {
        if(mTabLayout!=null){

            mTabLayout.setSelectedTabIndicatorHeight(height);
        }

    }

    int tabSelectColor;

    @WXComponentProp(name = Constants.Name.TAB_SELECTED_COLOR)
    public void setTabSelectedColor(String color) {
        if(mTabLayout!=null){
            tabSelectColor = Color.parseColor(color);
            mTabLayout.setSelectedTabIndicatorColor(tabSelectColor);
        }
    }



    @JSMethod
    public void setTabSelectedIndex(int index) {
        tabSelectedIndex(index);
    }


    @WXComponentProp(name = Constants.Name.TAB_SELECTED_INDEX)
    public void tabSelectedIndex(int index) {

        if(mTabLayout!=null){

            if (index!=0 && (index >= mTabLayout.getTabCount()|| index < 0)) {
                initIndex = index; //初始化index
                //dropTab点击
                mTabLayout.setCurrentPagerWithDropTab(index,childCount()); //兼容viewpager比tab多的情况（有dropTab）

                return;
            }

            TabLayout.Tab tab = mTabLayout.getTabAt(index);
            if(tab != null && !tab.isSelected()){
                tab.select();
            }else if(tab !=null){
                mTabLayout.setSelectedTabIndicatorColor(tabSelectColor);//隐藏tab选中
                mTabLayout.setCurrentPagerWithDropTab(index,childCount());
            }
        }else{
            initIndex = index;
        }

    }





    @WXComponentProp(name = Constants.Name.SCROLLABLE)
    public void setScrollable(boolean scrollable) {
        if(mTabLayout!=null){
            if(scrollable){
                mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }else{
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            }
        }

    }



}
