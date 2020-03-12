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
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class FsTabLayout extends TabLayout {

    public FsTabLayout(Context context) {
        super(context);
    }

    public FsTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FsTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean haveDropTab;

    public boolean isHaveDropTab(){
        return haveDropTab;
    }

    private CustomViewPager mViewPager;
    public void setViewPager(CustomViewPager pager){
        mViewPager = pager;
    }

    public boolean setCurrentPagerWithDropTab(int index){
        if(haveDropTab&& mViewPager!=null  && index!=mViewPager.getCurrentItem()&& index <mViewPager.getAdapter().getCount()){
            if(index == mViewPager.getAdapter().getCount() -1) {
                setSelectedTabIndicatorColor(Color.TRANSPARENT);//隐藏tab选中
                mViewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.none);
            }
            mViewPager.setCurrentItem(index);
            return true;
        }
        return false;
    }

    public void setHaveDropTab(boolean have){
        haveDropTab = have;
        setAllowedSwipeDirection();
    }


    public void setAllowedSwipeDirection(){
        if(mViewPager!=null) {
            if (haveDropTab) {
                if (getSelectedTabPosition() == getTabCount() - 1) {
                    mViewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
                } else {
                    mViewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                }
            } else {
                mViewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
            }
        }
    }



    public interface ScrollViewListener {

        void onScrollChanged(ScrollType scrollType);

    }

    private Handler mHandler;
    private ScrollViewListener scrollViewListener;
    /**
     * 滚动状态   IDLE 滚动停止  TOUCH_SCROLL 手指拖动滚动         FLING滚动
     * @version XHorizontalScrollViewgallery
     * @author DZC
     * @Time  2014-12-7 上午11:06:52
     *
     *
     */
    enum ScrollType{IDLE,TOUCH_SCROLL,FLING};

    /**
     * 记录当前滚动的距离
     */
    private int currentX = -9999999;
    /**
     * 当前滚动状态
     */
    private ScrollType scrollType = ScrollType.IDLE;
    /**
     * 滚动监听间隔
     */
    private int scrollDealy = 50;
    /**
     * 滚动监听runnable
     */
    private Runnable scrollRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if(getScrollX()==currentX){
                //滚动停止  取消监听线程
                scrollType = ScrollType.IDLE;
                if(scrollViewListener!=null){
                    scrollViewListener.onScrollChanged(scrollType);
                }
                mHandler.removeCallbacks(this);
                return;
            }else{
                //手指离开屏幕    view还在滚动的时候
                scrollType = ScrollType.FLING;
                if(scrollViewListener!=null){
                    scrollViewListener.onScrollChanged(scrollType);
                }
            }
            currentX = getScrollX();
            mHandler.postDelayed(this, scrollDealy);
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                this.scrollType = ScrollType.TOUCH_SCROLL;
                if(scrollViewListener!=null) {
                    scrollViewListener.onScrollChanged(scrollType);
                }
                //手指在上面移动的时候   取消滚动监听线程
                mHandler.removeCallbacks(scrollRunnable);
                break;
            case MotionEvent.ACTION_UP:
                //手指移动的时候
                mHandler.post(scrollRunnable);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 必须先调用这个方法设置Handler  不然会出错
     *  2014-12-7 下午3:55:39
     * @author DZC
     * @return void
     * @param handler
     * @TODO
     */
    public void setHandler(Handler handler){
        this.mHandler = handler;
    }
    /**
     * 设置滚动监听
     *  2014-12-7 下午3:59:51
     * @author DZC
     * @return void
     * @param listener
     * @TODO
     */
    public void setOnScrollStateChangedListener(ScrollViewListener listener){
        this.scrollViewListener = listener;
    }
}
