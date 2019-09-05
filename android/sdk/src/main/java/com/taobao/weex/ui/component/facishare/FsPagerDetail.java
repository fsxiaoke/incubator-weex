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
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXIndicator;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXCircleIndicator;
import com.taobao.weex.ui.view.WXCirclePageAdapter;
import com.taobao.weex.utils.WXUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component(lazyload = false)

public class FsPagerDetail extends WXVContainer<ViewPager> {

  public static final String INDEX = "index";
  public static final String INFINITE = "infinite";


  Map<String, Object> params = new HashMap<>();
  private float offsetXAccuracy = 0.1f;
  private int initIndex = -1;
  private boolean keepIndex = false;
  private Runnable initRunnable;


  public static class Creator implements ComponentCreator {
    public WXComponent createInstance(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
      return new FsPagerDetail(instance, parent, basicComponentData);
    }
  }

  /**
   * Scrollable sliderview
   */
  /**
   * package
   **/
  ViewPager mViewPager;
  /**
   * Circle indicator
   */

  /**
   * Adapter for sliderview
   */
  protected WXCirclePageAdapter mAdapter;


  protected OnPageChangeListener mPageChangeListener = new SliderPageChangeListener();

  @Deprecated
  public FsPagerDetail(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy, BasicComponentData basicComponentData) {
    this(instance, parent, basicComponentData);
  }

  public FsPagerDetail(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
    super(instance, parent, basicComponentData);
  }

  @Override
  protected ViewPager initComponentHostView(@NonNull Context context) {
//    FrameLayout view = new FrameLayout(context);
//    // init view pager
//    if (getAttrs() != null) {
//      Object obj = getAttrs().get(INFINITE);
//      isInfinite = WXUtils.getBoolean(obj, true);
//    }
    LinearLayout.LayoutParams pagerParams = new LinearLayout.LayoutParams(
        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    mViewPager = new ViewPager(context);
//    mViewPager.setCircle(isInfinite);
    mViewPager.setLayoutParams(pagerParams);
    mViewPager.setOffscreenPageLimit(1);
    // init adapter
    mAdapter = new WXCirclePageAdapter(false);
    mViewPager.setAdapter(mAdapter);
    // add to parent
//    view.addView(mViewPager);
    mViewPager.addOnPageChangeListener(mPageChangeListener);

    registerActivityStateListener();

    return mViewPager;
  }

  /**
   * Slider is not a regular container,top/left/right/bottom not apply to view,expect indicator.
   */
  @Override
  public LayoutParams getChildLayoutParams(WXComponent child, View childView, int width, int height, int left, int right, int top, int bottom) {
    LayoutParams lp = childView.getLayoutParams();
    if (lp == null) {
      lp = new FrameLayout.LayoutParams(width, height);
    } else {
      lp.width = width;
      lp.height = height;
    }

    if (lp instanceof ViewGroup.MarginLayoutParams) {
      //expect indicator .
      if (child instanceof WXIndicator) {
        ((ViewGroup.MarginLayoutParams) lp).setMargins(left, top, right, bottom);
      } else {
        ((ViewGroup.MarginLayoutParams) lp).setMargins(0, 0, 0, 0);
      }
    }
    return lp;
  }


//  @Override
//  public void addEvent(String type) {
//    super.addEvent(type);
//    if (Constants.Event.SCROLL.equals(type)) {
//      if (mViewPager == null) {
//        return;
//      }
//      mViewPager.addOnPageChangeListener(new SliderOnScrollListener(this));
//    }
//  }

//  @Override
//  public boolean containsGesture(WXGestureType WXGestureType) {
//    //Enable gesture for slider
//    return super.containsGesture(WXGestureType);
//  }

  @Override
  public ViewGroup getRealView() {
    return mViewPager;
  }
  @Override
  public void setLayout(WXComponent component) {
    if (TextUtils.isEmpty(component.getComponentType())
            || TextUtils.isEmpty(component.getRef()) || component.getLayoutPosition() == null
            || component.getLayoutSize() == null) {
      return;
    }

    CSSShorthand cs=new CSSShorthand();

    component.setMargins(cs);
    component.getLayoutPosition().update(0,0,0,0);
    super.setLayout(component);
  }
  @Override
  public void addSubView(View view, int index) {
    if (view == null || mAdapter == null) {
      return;
    }

    if (view instanceof WXCircleIndicator) {
      return;
    }
    mAdapter.addPageView(view,index);
//    hackTwoItemsInfiniteScroll();
    if (initIndex != -1 && mAdapter.getRealCount() > initIndex) {
      if(initRunnable == null){
        initRunnable = new Runnable() {
          @Override
          public void run() {
            initIndex = getInitIndex();
            mViewPager.setCurrentItem(initIndex);
            initIndex = -1;
            initRunnable = null;
          }
        };
      }
      mViewPager.removeCallbacks(initRunnable);
      mViewPager.postDelayed(initRunnable, 50);
    } else {
//      if (!keepIndex) {
//        mViewPager.setCurrentItem(0);
//      }
    }
//    if (mIndicator != null) {
//      mIndicator.getHostView().forceLayout();
//      mIndicator.getHostView().requestLayout();
//    }
  }

  protected void setHostLayoutParams(ViewPager host, int width, int height, int left, int right, int top, int bottom) {
    LayoutParams lp;
    if (mParent == null) {
      FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
      this.setMarginsSupportRTL(params, left, top, right, bottom);
      lp = params;
    } else {
      lp = mParent.getChildLayoutParams(this, host, width, height, left, right, top, bottom);
    }
    lp.height=LayoutParams.MATCH_PARENT;
    if (lp != null) {
      host.setLayoutParams(lp);
    }
  }

  @Override
  public void remove(WXComponent child, boolean destroy) {
    if (child == null || child.getHostView() == null || mAdapter == null) {
      return;
    }

    mAdapter.removePageView(child.getHostView());
//    hackTwoItemsInfiniteScroll();
    super.remove(child,destroy);
  }

  @Override
  public void destroy() {
    super.destroy();
    if (mViewPager != null) {
//      mViewPager.stopAutoScroll();
      mViewPager.removeAllViews();
//      mViewPager.destory();
    }
  }

  @Override
  public void onActivityResume() {
    super.onActivityResume();
//    if (mViewPager != null && mViewPager.isAutoScroll()) {
//      mViewPager.startAutoScroll();
//    }
  }

  @Override
  public void onActivityStop() {
    super.onActivityStop();
    if (mViewPager != null) {
//      mViewPager.pauseAutoScroll();
    }
  }


  private int getInitIndex(){
    Object index = getAttrs().get(Constants.Name.INDEX);
    int select = WXUtils.getInteger(index, initIndex);
    if(mAdapter == null || mAdapter.getCount() == 0){
      return  0;
    }
    if(select >= mAdapter.getRealCount()){
      select = select%mAdapter.getRealCount();
    }
    return select;
  }

//
//  @Override
//  protected boolean setProperty(String key, Object param) {
//    switch (key) {
//      case Constants.Name.VALUE:
//        String value = WXUtils.getString(param, null);
//        if (value != null) {
//          setValue(value);
//        }
//        return true;
//      case Constants.Name.AUTO_PLAY:
//        String aotu_play = WXUtils.getString(param, null);
//        if (aotu_play != null) {
//          setAutoPlay(aotu_play);
//        }
//        return true;
//      case Constants.Name.SHOW_INDICATORS:
//        String indicators = WXUtils.getString(param, null);
//        if (indicators != null) {
//          setShowIndicators(indicators);
//        }
//        return true;
//      case Constants.Name.INTERVAL:
//        Integer interval = WXUtils.getInteger(param, null);
//        if (interval != null) {
//          setInterval(interval);
//        }
//        return true;
//      case Constants.Name.INDEX:
//        Integer index = WXUtils.getInteger(param, null);
//        if (index != null) {
//          setIndex(index);
//        }
//        return true;
//      case Constants.Name.OFFSET_X_ACCURACY:
//        Float accuracy = WXUtils.getFloat(param, 0.1f);
//        if (accuracy != 0) {
//          setOffsetXAccuracy(accuracy);
//        }
//        return true;
//      case Constants.Name.SCROLLABLE:
//        boolean scrollable = WXUtils.getBoolean(param, true);
//        setScrollable(scrollable);
//        return true;
//      case Constants.Name.KEEP_INDEX:
//        this.keepIndex = WXUtils.getBoolean(param, false);
//        return true;
//    }
//    return super.setProperty(key, param);
//  }
//
//  @Deprecated
//  @WXComponentProp(name = Constants.Name.VALUE)
//  public void setValue(String value) {
//    if (value == null || getHostView() == null) {
//      return;
//    }
//    int i;
//    try {
//      i = Integer.parseInt(value);
//    } catch (NumberFormatException e) {
//      WXLogUtils.e("", e);
//      return;
//    }
//
//    setIndex(i);
//  }
//
//  @WXComponentProp(name = Constants.Name.AUTO_PLAY)
//  public void setAutoPlay(String autoPlay) {
//    if (TextUtils.isEmpty(autoPlay) || autoPlay.equals("false")) {
//      mViewPager.stopAutoScroll();
//    } else {
//      mViewPager.stopAutoScroll();
//      mViewPager.startAutoScroll();
//    }
//  }
//
//  @WXComponentProp(name = Constants.Name.SHOW_INDICATORS)
//  public void setShowIndicators(String show) {
//    if (TextUtils.isEmpty(show) || show.equals("false")) {
//      mShowIndicators = false;
//    } else {
//      mShowIndicators = true;
//    }
//
//    if (mIndicator == null) {
//      return;
//    }
//    mIndicator.setShowIndicators(mShowIndicators);
//  }
//
//  @WXComponentProp(name = Constants.Name.INTERVAL)
//  public void setInterval(int intervalMS) {
//    if (mViewPager != null && intervalMS > 0) {
//      mViewPager.setIntervalTime(intervalMS);
//    }
//  }
//
  @WXComponentProp(name = Constants.Name.INDEX)
  public void setIndex(int index) {
    if (mViewPager != null && mAdapter != null) {
      if (index >= mAdapter.getRealCount() || index < 0) {
        initIndex = index;
        return;
      }
      mViewPager.setCurrentItem(index);
    }
  }



//  @WXComponentProp(name = Constants.Name.SCROLLABLE)
//  public void setScrollable(boolean scrollable) {
//    if (mViewPager != null && mAdapter != null) {
//      mViewPager.setScrollable(scrollable);
//    }
//  }
//
//  @WXComponentProp(name = Constants.Name.OFFSET_X_ACCURACY)
//  public void setOffsetXAccuracy(float accuracy) {
//    this.offsetXAccuracy = accuracy;
//  }

  protected class SliderPageChangeListener implements OnPageChangeListener {

    private int lastPos = -1;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int pos) {
//      if (mAdapter.getRealPosition(pos) == lastPos) {
//        return;
//      }
//      if (WXEnvironment.isApkDebugable()) {
//        WXLogUtils.d("onPageSelected >>>>" + mAdapter.getRealPosition(pos) + " lastPos: " + lastPos);
//      }
//      if (mAdapter == null || mAdapter.getRealCount() == 0) {
//        return;
//      }
//
//      int realPosition = mAdapter.getRealPosition(pos);
//      if (mChildren == null || realPosition >= mChildren.size()) {
//        return;
//      }
//
//      if (getEvents().size() == 0) {
//        return;
//      }
//      WXEvent event = getEvents();
//      String ref = getRef();
//      if (event.contains(Constants.Event.CHANGE) && WXViewUtils.onScreenArea(getHostView())) {
//        params.put(INDEX, realPosition);
//
//        Map<String, Object> domChanges = new HashMap<>();
//        Map<String, Object> attrsChanges = new HashMap<>();
//        attrsChanges.put(INDEX, realPosition);
//        domChanges.put("attrs", attrsChanges);
//        WXSDKManager.getInstance().fireEvent(getInstanceId(), ref,
//            Constants.Event.CHANGE, params, domChanges);
//      }
//
//      mViewPager.requestLayout();
//      getHostView().invalidate();
//      lastPos = mAdapter.getRealPosition(pos);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
//      FrameLayout root = getHostView();
//      if (null != root) {
//        root.invalidate();
//      }
    }
  }

  protected static class SliderOnScrollListener implements OnPageChangeListener {
    private float lastPositionOffset = 99f;
    private int selectedPosition;
    private FsPagerDetail target;

    public SliderOnScrollListener(FsPagerDetail target) {
      this.target = target;
//      this.selectedPosition = target.mViewPager.superGetCurrentItem();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//      if (lastPositionOffset == 99f) {
//        lastPositionOffset = positionOffset;
//        return;
//      }
//
//      float offset = positionOffset - lastPositionOffset;
//
//      if (Math.abs(offset) >= target.offsetXAccuracy) {
//        if (position == selectedPosition) {
//          //slide to left. positionOffset[0 -> 1]
//          Map<String,Object> event = new HashMap<>(1);
//          event.put(Constants.Name.OFFSET_X_RATIO, -positionOffset);
//          target.fireEvent(Constants.Event.SCROLL, event);
//        } else if (position < selectedPosition) {
//          //slide to right. positionOffset[1 -> 0]
//          Map<String,Object> event = new HashMap<>(1);
//          event.put(Constants.Name.OFFSET_X_RATIO, (1f - positionOffset));
//          target.fireEvent(Constants.Event.SCROLL, event);
//        }
//        lastPositionOffset = positionOffset;
//      }
    }

    @Override
    public void onPageSelected(int position) {
      selectedPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

      /**
       * @homeblog@vip.qq.com
       *
       *  add scrollstart & scrollend event
       *
       */
//      switch (state) {
//        case ViewPager.SCROLL_STATE_IDLE:
//          lastPositionOffset = 99f;
//          target.fireEvent("scrollend");
//          break;
//        case ViewPager.SCROLL_STATE_DRAGGING:
//          target.fireEvent("scrollstart");
//          break;
//        case ViewPager.SCROLL_STATE_SETTLING:
//          break;
//
//      }
    }
  }

//  private void hackTwoItemsInfiniteScroll() {
//    if (mViewPager == null || mAdapter == null) {
//      return;
//    }
//    if (false) {
//      if (mAdapter.getRealCount() == 2) {
//        final GestureDetector gestureDetector = new GestureDetector(getContext(), new FlingGestureListener(mViewPager));
//        mViewPager.setOnTouchListener(new View.OnTouchListener() {
//          @Override
//          public boolean onTouch(View v, MotionEvent event) {
//            return gestureDetector.onTouchEvent(event);
//          }
//        });
//      } else {
//        mViewPager.setOnTouchListener(null);
//      }
//    }
//  }
//
//  private static class FlingGestureListener extends GestureDetector.SimpleOnGestureListener {
//    private static final int SWIPE_MIN_DISTANCE = WXViewUtils.dip2px(50);
//    private static final int SWIPE_MAX_OFF_PATH = WXViewUtils.dip2px(250);
//    private static final int SWIPE_THRESHOLD_VELOCITY = WXViewUtils.dip2px(200);
//    private WeakReference<WXCircleViewPager> pagerRef;
//
//    FlingGestureListener(WXCircleViewPager pager) {
//      this.pagerRef = new WeakReference<>(pager);
//    }
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//      WXCircleViewPager mViewPager = pagerRef.get();
//      if (mViewPager == null) {
//        return false;
//      }
//
//      try {
//        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
//          return false;
//        }
//
//        if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
//                && mViewPager.superGetCurrentItem() == 1) {
//          // right to left swipe
//          mViewPager.setCurrentItem(0, false);
//          return true;
//        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
//                && mViewPager.superGetCurrentItem() == 0) {
//          // left to right swipe
//          mViewPager.setCurrentItem(1, false);
//          return true;
//        }
//      } catch (Exception e) {
//        // ignore
//      }
//      return false;
//    }
//  }
}
