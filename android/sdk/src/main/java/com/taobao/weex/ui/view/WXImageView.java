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
package com.taobao.weex.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.utils.ImageDrawable;
import com.taobao.weex.utils.WXLogUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Arrays;

public class WXImageView extends android.support.v7.widget.AppCompatImageView implements WXGestureObservable,
        IRenderStatus<WXImage>,
        IRenderResult<WXImage>, WXImage.Measurable {

  private WeakReference<WXImage> mWeakReference;
  private WXGesture wxGesture;
  private float[] borderRadius;
  private boolean gif;
  private boolean isBitmapReleased = false;
  private boolean enableBitmapAutoManage = true;


  public WXImageView(Context context) {
    super(context);
    mPath = new Path();
    // set odd mode
    mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

    mPaint = new Paint();
    mPaint.setAntiAlias(true);
  }

  @Override
  public void setImageResource(int resId) {
    Drawable drawable = getResources().getDrawable(resId);
    setImageDrawable(drawable);
  }

  public void setImageDrawable(@Nullable Drawable drawable, boolean isGif) {
    this.gif = isGif;
    ViewGroup.LayoutParams layoutParams;
    if ((layoutParams = getLayoutParams()) != null) {
      Drawable wrapDrawable = ImageDrawable.createImageDrawable(drawable,
              getScaleType(), borderRadius,
              layoutParams.width - getPaddingLeft() - getPaddingRight(),
              layoutParams.height - getPaddingTop() - getPaddingBottom(),
              isGif);
      if (wrapDrawable instanceof ImageDrawable) {
        ImageDrawable imageDrawable = (ImageDrawable) wrapDrawable;
        if (!Arrays.equals(imageDrawable.getCornerRadii(), borderRadius)) {
          imageDrawable.setCornerRadii(borderRadius);
        }
      }
      super.setImageDrawable(wrapDrawable);
      if (mWeakReference != null) {
        WXImage component = mWeakReference.get();
        if (component != null) {
          component.readyToRender();
        }
      }
    }
  }

  @Override
  public void setImageDrawable(@Nullable Drawable drawable) {
    setImageDrawable(drawable, gif);
  }

  @Override
  public void setImageBitmap(@Nullable Bitmap bm) {
    setImageDrawable(bm == null ? null : new BitmapDrawable(getResources(), bm));
  }

  @Override
  public void registerGestureListener(WXGesture wxGesture) {
    this.wxGesture = wxGesture;
  }

  @Override
  public WXGesture getGestureListener() {
    return wxGesture;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    boolean result = super.onTouchEvent(event);
    if (wxGesture != null) {
      result |= wxGesture.onTouch(this, event);
    }
    return result;
  }

  public void setBorderRadius(@NonNull float[] borderRadius) {
    this.borderRadius = borderRadius;
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if (changed) {
      setImageDrawable(getDrawable(), gif);
    }
  }

  @Override
  public void holdComponent(WXImage component) {
    mWeakReference = new WeakReference<>(component);
  }

  @Nullable
  @Override
  public WXImage getComponent() {
    return null != mWeakReference ? mWeakReference.get() : null;
  }

  @Override
  public int getNaturalWidth() {
    Drawable drawable = getDrawable();
    if (drawable != null) {
      if (drawable instanceof ImageDrawable) {
        return ((ImageDrawable) drawable).getBitmapWidth();
      } else if (drawable instanceof BitmapDrawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        if (bitmap != null) {
          return bitmap.getWidth();
        } else {
          WXLogUtils.w("WXImageView", "Bitmap on " + drawable.toString() + " is null");
        }
      } else {
        WXLogUtils.w("WXImageView", "Not supported drawable type: " + drawable.getClass().getSimpleName());
      }
    }
    return -1;
  }

  @Override
  public int getNaturalHeight() {
    Drawable drawable = getDrawable();
    if (drawable != null) {
      if (drawable instanceof ImageDrawable) {
        return ((ImageDrawable) drawable).getBitmapHeight();
      } else if (drawable instanceof BitmapDrawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        if (bitmap != null) {
          return bitmap.getHeight();
        } else {
          WXLogUtils.w("WXImageView", "Bitmap on " + drawable.toString() + " is null");
        }
      } else {
        WXLogUtils.w("WXImageView", "Not supported drawable type: " + drawable.getClass().getSimpleName());
      }
    }
    return -1;
  }

  private boolean mOutWindowVisibilityChangedReally;
  @Override
  public void dispatchWindowVisibilityChanged(int visibility) {
    mOutWindowVisibilityChangedReally = true;
    super.dispatchWindowVisibilityChanged(visibility);
    mOutWindowVisibilityChangedReally = false;
  }

  @Override
  protected void onWindowVisibilityChanged(int visibility) {
    super.onWindowVisibilityChanged(visibility);
    if(mOutWindowVisibilityChangedReally){
      if(visibility == View.VISIBLE){
        autoRecoverImage();
      }else{
        autoReleaseImage();
      }
    }
  }


  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    autoRecoverImage();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    autoReleaseImage();

  }


  @Override
  public void onStartTemporaryDetach () {
    super.onStartTemporaryDetach();
    autoReleaseImage();

  }


  @Override
  public void onFinishTemporaryDetach () {
    super.onFinishTemporaryDetach();
    autoRecoverImage();
  }


  public void setEnableBitmapAutoManage(boolean enableBitmapAutoManage) {
    this.enableBitmapAutoManage = enableBitmapAutoManage;
  }

  public void autoReleaseImage(){
    if(enableBitmapAutoManage) {
      if (!isBitmapReleased) {
        isBitmapReleased = true;
        WXImage image = getComponent();
        if (image != null) {
          image.autoReleaseImage();
        }
      }
    }
  }

  public void autoRecoverImage(){
    if(enableBitmapAutoManage){
      if(isBitmapReleased){
        WXImage image = getComponent();
        if(image != null){
          image.autoRecoverImage();
        }
        isBitmapReleased = false;
      }
    }
  }




  private Path mPath;

  private Paint mPaint;

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if(gif) {
      canvas.save();
      canvas.drawPath(mPath, mPaint);
      canvas.restore();
    }
  }


  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if(gif) {
      initPaintColor();
      addRoundRectPath(w, h);
    }
  }

  private void initPaintColor() {
    int paintColor = getPaintColor(getParent());
    if (Color.TRANSPARENT == paintColor) {
      // get theme background color
      TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{
              android.R.attr.colorBackground
      });
      paintColor = array.getColor(0, Color.TRANSPARENT);
      array.recycle();
    }

    mPaint.setColor(paintColor);
  }


  /**
   * @param vp parent view
   * @return paint color
   */
  private int getPaintColor(ViewParent vp) {
    if (null == vp) {
      return Color.TRANSPARENT;
    }

    if (vp instanceof View) {
      View parentView = (View) vp;
      int color = getViewBackgroundColor(parentView);

      if (Color.TRANSPARENT != color) {
        return color;
      } else {
        getPaintColor(parentView.getParent());
      }
    }

    return Color.TRANSPARENT;
  }

  /**
   * @param view
   * @return
   */
  private int getViewBackgroundColor(View view) {
    Drawable drawable = view.getBackground();

    if (null != drawable) {
      Class<Drawable> drawableClass = (Class<Drawable>) drawable.getClass();
      if (null == drawableClass) {
        return Color.TRANSPARENT;
      }

      try {
        Field field = drawableClass.getDeclaredField("mColorState");
        field.setAccessible(true);
        Object colorState = field.get(drawable);
        Class colorStateClass = colorState.getClass();
        Field colorStateField = colorStateClass.getDeclaredField("mUseColor");
        colorStateField.setAccessible(true);
        int viewColor = (int) colorStateField.get(colorState);
        if (Color.TRANSPARENT != viewColor) {
          return viewColor;
        }
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    return Color.TRANSPARENT;
  }


  private void addRoundRectPath(int w, int h) {
    mPath.reset();

    //add round rect
    mPath.addRoundRect(new RectF(0, 0, w, h), borderRadius, Path.Direction.CCW);

    //        Path brainPath = new Path();
    //        brainPath.addRect(new RectF(0, 0, w, h), Path.Direction.CCW);
    //        brainPath.addCircle(w / 2, h / 2, Math.min(w, h) / 2, Path.Direction.CW);
    //
    //        mPath.setFillType(Path.FillType.WINDING);
    //        mPath.addPath(brainPath);
  }

}
