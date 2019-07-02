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
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.taobao.weex.R;
import com.taobao.weex.ui.view.coordinatortablayout.listener.LoadHeaderImagesListener;
import com.taobao.weex.ui.view.coordinatortablayout.listener.OnTabSelectedListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hugeterry(http://hugeterry.cn)
 */

public class CoordinatorTabLayout extends CoordinatorLayout {
    private int[] mImageArray, mColorArray;

    private Context mContext;
//    private Toolbar mToolbar;
//    private ActionBar mActionbar;
    private LinearLayout mTabLayout;
    private LinearLayout mPagerContainer;
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
        LayoutInflater.from(context).inflate(R.layout.weexview_coordinatortablayout, this, true);
        initToolbar();
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbarlayout);
        mTabLayout = (LinearLayout) findViewById(R.id.tabLayout);
        mFeedRootLayout = findViewById(R.id.feedRootLayout);

//        testPager();
    }

    void testPager(){
//        LinearLayout mPagerContainer = findViewById(R.id.vp);
//        ArrayList aList = new ArrayList<View>();
//        ViewPager vp = new ViewPager(mContext);
//        List<String> list = initList();
//        RecyclerView rc1 = new RecyclerView(mContext);
//        rc1.setLayoutManager(new LinearLayoutManager(rc1.getContext()));
//        rc1.setAdapter(new RecyclerAdapter(rc1.getContext(), list));
//        aList.add(rc1);
//
//        RecyclerView rc2 = new RecyclerView(mContext);
//        rc2.setLayoutManager(new LinearLayoutManager(rc2.getContext()));
//        rc2.setAdapter(new RecyclerAdapter(rc2.getContext(), list));
//        aList.add(rc2);
//
//        MyPagerRecycleAdapter mAdapter = new MyPagerRecycleAdapter(aList);
//        vp.setId(vp.hashCode());
//        vp.setAdapter(mAdapter);
//        mPagerContainer.addView(vp);
    }



    public void addTabView(View v){
        if(mTabLayout != null){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mTabLayout.addView(v,params);
        }
    }

    public void addTopView(View v){
        if(mFeedRootLayout != null){
            mFeedRootLayout.addView(v);
        }
    }

    public void addPageView(View v){
        mPagerContainer = findViewById(R.id.vp);
        if(mPagerContainer != null){
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((LinearLayout.LayoutParams) params).setMargins(0,0,0,0);
            mPagerContainer.addView(v, params);
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

    /**
     * 设置每个tab对应的头部图�?
     *
     * @param imageArray 图片数组
     * @return CoordinatorTabLayout
     */
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

    private void setupTabLayout() {
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
////                mImageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_dismiss));
////                if (mLoadHeaderImagesListener == null) {
////                    if (mImageArray != null) {
////                        mImageView.setImageResource(mImageArray[tab.getPosition()]);
////                    }
////                } else {
////                    mLoadHeaderImagesListener.loadHeaderImages(mImageView, tab);
////                }
////                if (mColorArray != null) {
////                    mCollapsingToolbarLayout.setContentScrimColor(
////                            ContextCompat.getColor(
////                                    mContext, mColorArray[tab.getPosition()]));
////                }
////
////                if (mOnTabSelectedListener != null) {
////                    mOnTabSelectedListener.onTabSelected(tab);
////                }
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                if (mOnTabSelectedListener != null) {
//                    mOnTabSelectedListener.onTabUnselected(tab);
//                }
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                if (mOnTabSelectedListener != null) {
//                    mOnTabSelectedListener.onTabReselected(tab);
//                }
//            }
//        });
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
     * 设置与该组件搭配的ViewPager
     *
     * @param viewPager 与TabLayout结合的ViewPager
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setupWithViewPager(ViewPager viewPager) {
        setupTabLayout();
//        mTabLayout.setupWithViewPager(viewPager);
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
    public LinearLayout getTabLayout() {
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


    public class MyPagerRecycleAdapter extends PagerAdapter {
        private ArrayList<View> viewLists;

        public MyPagerRecycleAdapter() {
        }

        public MyPagerRecycleAdapter(ArrayList<View> viewLists) {
            super();
            this.viewLists = viewLists;
        }

        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewLists.get(position));
            return viewLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewLists.get(position));
        }
    }

    private List<String> initList() {
        List<String> mdatas = new ArrayList<>();
        for (int i = 'A';i<'z';++i){
            mdatas.add(""+(char)i);
        }
        return mdatas;
    }

}