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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.ArrayList;

public class AdvanceSwipeRefreshLayout extends SwipeRefreshLayout {

    private OnPreInterceptTouchEventDelegate mOnPreInterceptTouchEventDelegate;
    ViewConfiguration mConfiguration;

    private int mTouchSlop;
    private float mPrevX;

    public AdvanceSwipeRefreshLayout(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public AdvanceSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mConfiguration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    public void setLayoutParams(ViewGroup.LayoutParams params){
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        super.setLayoutParams(params);
    }

    private boolean isTop(WebView webView) {
        return webView.getScrollY() == 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {



        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(ev).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = ev.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }




        boolean disallowIntercept = false;
        if (mOnPreInterceptTouchEventDelegate != null)

            disallowIntercept = mOnPreInterceptTouchEventDelegate.shouldDisallowInterceptTouchEvent(ev);


        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        WebView view = getTouchTargetWebView(this,x,y); //判断当前touch的是否是webview，如果是看是否webview需要触发滚动
        boolean webDo = view != null &&!isTop(view);
//        if (view != null &&!isTop(view)) {
//            return true;
//        }

        if (disallowIntercept||webDo) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }


    //(x,y)是否在view的区域内
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    private WebView getTouchTargetWebView(View view, int x, int y) {
        WebView targetView = null;
        // 判断view是否可以聚焦
        ArrayList<View> TouchableViews = view.getTouchables();
        for (View child : TouchableViews) {
            if (isTouchPointInView(child, x, y) && child instanceof WebView) {
                targetView = (WebView) child;
                break;
            }
        }
        return targetView;
    }

    public void setOnPreInterceptTouchEventDelegate(OnPreInterceptTouchEventDelegate listener) {
        mOnPreInterceptTouchEventDelegate = listener;
    }

    public interface OnPreInterceptTouchEventDelegate {
        boolean shouldDisallowInterceptTouchEvent(MotionEvent ev);
    }
}