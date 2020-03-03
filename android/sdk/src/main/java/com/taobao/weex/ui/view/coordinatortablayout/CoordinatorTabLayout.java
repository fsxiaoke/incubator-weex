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

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.taobao.weex.R;
import com.taobao.weex.ui.component.facishare.CustomViewPager;
import com.taobao.weex.ui.component.facishare.FsTabLayout;
import com.taobao.weex.ui.view.coordinatortablayout.listener.LoadHeaderImagesListener;
import com.taobao.weex.ui.view.coordinatortablayout.listener.OnTabSelectedListener;


/**
 * @author hugeterry(http://hugeterry.cn)
 */

public class CoordinatorTabLayout extends CoordinatorLayout {
    private int[] mImageArray, mColorArray;

    private Context mContext;
//    private Toolbar mToolbar;
//    private ActionBar mActionbar;
    private LinearLayout mBarLayout;
    private LinearLayout mPagerContainer;
    private FsTabLayout mTabLayout;
    private LinearLayout mTabLayoutContainer;
    private CustomViewPager mViewPage;
//    private ImageView mImageView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private LoadHeaderImagesListener mLoadHeaderImagesListener;
    private OnTabSelectedListener mOnTabSelectedListener;
    private LinearLayout mFeedRootLayout;
    public CoordinatorTabLayout(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    public CoordinatorTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (!isInEditMode()) {
            initView(context);
            initWidget(context, attrs);
        }
    }

    public CoordinatorTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (!isInEditMode()) {
            initView(context);
            initWidget(context, attrs);
        }
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.weex_coordinatortablayout, this, true);
        initToolbar();
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbarlayout);
        mBarLayout = (LinearLayout) findViewById(R.id.appbarId);
        mTabLayoutContainer = (LinearLayout) findViewById(R.id.tabLayout);
        mFeedRootLayout = findViewById(R.id.feedRootLayout);
    }

    public void setLayoutParams(ViewGroup.LayoutParams params){
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        super.setLayoutParams(params);
    }




    public void addTabView(RelativeLayout v){
        if(mTabLayoutContainer != null){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            FsTabLayout tab = v.findViewById(R.id.fs_tab);
            mTabLayoutContainer.addView(v,params);
            mTabLayout = tab;

            setupTabListener();
            setupTabViewPager();
        }
    }
    public View getAppBar(){
        return mBarLayout;
    }

    public void addTopView(View v){
        if(mFeedRootLayout != null){
            mFeedRootLayout.addView(v);
        }
    }

    public void addPageView(CustomViewPager v){
        mPagerContainer = findViewById(R.id.vp);
        if(mPagerContainer != null){
            this.mViewPage = v;
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((LinearLayout.LayoutParams) params).setMargins(0,0,0,0);
            mPagerContainer.addView(v, params);
            setupViewPageListener();
            setupTabViewPager();
        }
    }

    private void setupTabViewPager(){
        if(mViewPage !=null && mTabLayout!=null){
            mTabLayout.setViewPager(mViewPage);
        }
    }



    private void initWidget(Context context, AttributeSet attrs) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs
//                , R.styleable.weexCoordinatorTabLayout);
//
//        TypedValue typedValue = new TypedValue();
//        mContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
//        int contentScrimColor = typedArray.getColor(
//                R.styleable.weexCoordinatorTabLayout_contentScrim, typedValue.data);
//        mCollapsingToolbarLayout.setContentScrimColor(contentScrimColor);
//
//        int tabIndicatorColor = typedArray.getColor(R.styleable.weexCoordinatorTabLayout_tabIndicatorColor, Color.WHITE);
//        mTabLayout.setSelectedTabIndicatorColor(tabIndicatorColor);
//
//        int tabTextColor = typedArray.getColor(R.styleable.weexCoordinatorTabLayout_tabTextColor, Color.WHITE);
//        mTabLayout.setTabTextColors(ColorStateList.valueOf(tabTextColor));
//        typedArray.recycle();
    }

    private void initToolbar() {
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        ((AppCompatActivity) mContext).setSupportActionBar(mToolbar);
//        mActionbar = ((AppCompatActivity) mContext).getSupportActionBar();
    }

    /**
     * 设置Toolbar标题
     *
     * @param title 标题
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setTitle(String title) {
//        if (mActionbar != null) {
//            mActionbar.setTitle(title);
//        }
        return this;
    }

    /**
     * 设置Toolbar显示返回按钮及标�?
     *
     * @param canBack 是否返回
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setBackEnable(Boolean canBack) {
//        if (canBack && mActionbar != null) {
//            mActionbar.setDisplayHomeAsUpEnabled(true);
//            mActionbar.setHomeAsUpIndicator(R.drawable.weexic_arrow_white_24dp);
//        }
        return this;
    }


    public CoordinatorTabLayout setImageArray(@NonNull int[] imageArray) {
        mImageArray = imageArray;
        return this;
    }

    /**
     * 设置每个tab对应的头部照片和ContentScrimColor
     *
     * @param imageArray 图片数组
     * @param colorArray ContentScrimColor数组
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setImageArray(@NonNull int[] imageArray, @NonNull int[] colorArray) {
        mImageArray = imageArray;
        mColorArray = colorArray;
        return this;
    }

    /**
     * 设置每个tab对应的ContentScrimColor
     *
     * @param colorArray 图片数组
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setContentScrimColorArray(@NonNull int[] colorArray) {
        mColorArray = colorArray;
        return this;
    }

    private void setupViewPageListener(){
        if(mViewPage!=null){
            mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    if(mTabLayout!=null){
                        TabLayout.Tab tab = mTabLayout.getTabAt(i);
                        if(tab!=null && !tab.isSelected()){
                            tab.select();
                        }
                    }
                    if(mTabLayout.isHaveDropTab()){
                        int count = mViewPage.getAdapter().getCount();
                        if(i==count -1){
                            mViewPage.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.none);
                        }else if(i == count -2){
                            mViewPage.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
                        }else{
                            mViewPage.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                        }
                    }else{
                        mViewPage.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }

    }

    private void setupTabListener() {
        if(mTabLayout!=null) {
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (mViewPage != null && mViewPage.getCurrentItem() != tab.getPosition()) {
                        mViewPage.setCurrentItem(tab.getPosition());
                    }
                    if (mOnTabSelectedListener != null) {
                        mOnTabSelectedListener.onTabSelected(tab);
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    if (mOnTabSelectedListener != null) {
                        mOnTabSelectedListener.onTabUnselected(tab);
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    if (mOnTabSelectedListener != null) {
                        mOnTabSelectedListener.onTabReselected(tab);
                    }
                }
            });
        }
    }

    /**
     * 设置TabLayout TabMode
     *
     * @param mode
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setTabMode(@TabLayout.Mode int mode) {
//        mTabLayout.setTabMode(mode);
        return this;
    }



    /**
     * 获取该组件中的ActionBar
     */
//    public ActionBar getActionBar() {
//        return mActionbar;
//    }

    /**
     * 获取该组件中的TabLayout
     */
    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    /**
     * 获取该组件中的ImageView
     */
//    public ImageView getImageView() {
//        return mImageView;
//    }

    /**
     * 设置LoadHeaderImagesListener
     *
     * @param loadHeaderImagesListener 设置LoadHeaderImagesListener
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setLoadHeaderImagesListener(LoadHeaderImagesListener loadHeaderImagesListener) {
        mLoadHeaderImagesListener = loadHeaderImagesListener;
        return this;
    }

    /**
     * 设置onTabSelectedListener
     *
     * @param onTabSelectedListener 设置onTabSelectedListener
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout addOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        mOnTabSelectedListener = onTabSelectedListener;
        return this;
    }

    /**
     * 设置透明状�?�栏
     *
     * @param activity 当前展示的activity
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setTranslucentStatusBar(@NonNull Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return this;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

//        if (mToolbar != null) {
//            MarginLayoutParams layoutParams = (MarginLayoutParams) mToolbar.getLayoutParams();
//            layoutParams.setMargins(
//                    layoutParams.leftMargin,
//                    layoutParams.topMargin + SystemView.getStatusBarHeight(activity),
//                    layoutParams.rightMargin,
//                    layoutParams.bottomMargin);
//        }

        return this;
    }

    /**
     * 设置沉浸�?
     *
     * @param activity 当前展示的activity
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setTranslucentNavigationBar(@NonNull Activity activity) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            return this;
//        } else {
//            mToolbar.setPadding(0, SystemView.getStatusBarHeight(activity) >> 1, 0, 0);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//        } else {
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        return this;
    }




}