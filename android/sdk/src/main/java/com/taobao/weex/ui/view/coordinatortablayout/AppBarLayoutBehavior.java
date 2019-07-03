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
package com.taobao.weex.ui.view.coordinatortablayout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import java.lang.reflect.Field;


public class AppBarLayoutBehavior extends AppBarLayout.Behavior {

    private static final String TAG = "CustomAppbarLayoutBehavior";

    private static final int TYPE_FLING = 1;

    private boolean isFlinging;
    private boolean shouldBlockNestedScroll;

    public AppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent:" + child.getTotalScrollRange());
        shouldBlockNestedScroll = false;
        if (isFlinging) {
            shouldBlockNestedScroll = true;
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                stopAppbarLayoutFling(child);  //手指触摸屏幕的时候停止fling事件
                break;
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    /**
     * 停止appbarLayout的fling事件
     * @param appBarLayout
     */
    private void stopAppbarLayoutFling(AppBarLayout appBarLayout) {
        //通过反射拿到HeaderBehavior中的flingRunnable变量
        try {
            Class<?> headerBehaviorType = this.getClass().getSuperclass().getSuperclass();
            Field flingRunnableField = headerBehaviorType.getDeclaredField("mFlingRunnable");
            Field scrollerField = headerBehaviorType.getDeclaredField("mScroller");
            flingRunnableField.setAccessible(true);
            scrollerField.setAccessible(true);

            Runnable flingRunnable = (Runnable) flingRunnableField.get(this);
            OverScroller overScroller = (OverScroller) scrollerField.get(this);
            if (flingRunnable != null) {
                Log.d(TAG, "存在flingRunnable");
                appBarLayout.removeCallbacks(flingRunnable);
                flingRunnableField.set(this, null);
            }
            if (overScroller != null && !overScroller.isFinished()) {
                overScroller.abortAnimation();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        Log.d(TAG, "onStartNestedScroll");
        stopAppbarLayoutFling(child);
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        Log.d(TAG, "onNestedPreScroll:" + child.getTotalScrollRange() + " ,dx:" + dx + " ,dy:" + dy + " ,type:" + type);

        //type返回1时，表示当前target处于非touch的滑动，
        //该bug的引起是因为appbar在滑动时，CoordinatorLayout内的实现NestedScrollingChild2接口的滑动子类还未结束其自身的fling
        //�?以这里监听子类的非touch时的滑动，然后block掉滑动事件传递给AppBarLayout
        if (type == TYPE_FLING) {
            isFlinging = true;
        }
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int
            dxUnconsumed, int dyUnconsumed, int type) {
        Log.d(TAG, "onNestedScroll: target:" + target.getClass() + " ," + child.getTotalScrollRange() + " ,dxConsumed:"
                + dxConsumed + " ,dyConsumed:" + dyConsumed + " " + ",type:" + type);
        if (!shouldBlockNestedScroll) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        Log.d(TAG, "onStopNestedScroll");
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        isFlinging = false;
        shouldBlockNestedScroll = false;
    }

}