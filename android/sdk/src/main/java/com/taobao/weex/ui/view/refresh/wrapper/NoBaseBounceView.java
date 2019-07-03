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
package com.taobao.weex.ui.view.refresh.wrapper;

import android.content.Context;
import android.support.v7.widget.OrientationHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.refresh.core.WXRefreshView;
import com.taobao.weex.ui.view.refresh.core.WXSwipeLayout;

/**
 * BounceView(SwipeLayout) contains Scroller/List and refresh/loading view
 * @param <T> InnerView
 */
public abstract class NoBaseBounceView<T extends View> extends FrameLayout {
    private int mOrientation = OrientationHelper.VERTICAL;
    private T mInnerView;
    protected FrameLayout swipeLayout;
    public NoBaseBounceView(Context context, int orientation) {
        this(context, null,orientation);
    }



    public NoBaseBounceView(Context context, AttributeSet attrs, int orientation) {
        super(context, attrs);
        mOrientation = orientation;
    }

    public int getOrientation(){
        return mOrientation;
    }


    public void init(Context context) {
        createBounceView(context);
    }


    private FrameLayout createBounceView(Context context) {
        swipeLayout = new FrameLayout(context);
        swipeLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mInnerView = setInnerView(context);
        if (mInnerView == null)
            return null;
        swipeLayout.addView(mInnerView);
        addView(swipeLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return swipeLayout;
    }

    public void setOnRefreshListener(WXSwipeLayout.WXOnRefreshListener onRefreshListener) {
    }

    public void setOnLoadingListener(WXSwipeLayout.WXOnLoadingListener onLoadingListener) {
    }


    public void setHeaderView(WXComponent refresh) {

    }

    /**
     *
     * @param loading should be {@link WXRefreshView}
     */
    public void setFooterView(WXComponent loading) {

    }

    public void removeFooterView(WXComponent loading){

    }
    //TODO There are bugs, will be more than a rolling height
    public void removeHeaderView(WXComponent refresh){

    }


    public FrameLayout getSwipeLayout() {
        return swipeLayout;
    }

    public T getInnerView() {
        return mInnerView;
    }

    public abstract T setInnerView(Context context);

    public abstract void onRefreshingComplete();

    public abstract void onLoadmoreComplete();

}
