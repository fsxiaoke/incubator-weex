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
package com.taobao.weex.ui.component.facishare;



import android.content.Context;
import android.support.v4.view.ViewPager;

import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

  private float initialXValue;
  private SwipeDirection direction;

  public CustomViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.direction = SwipeDirection.all;
  }

  public CustomViewPager(Context context) {
    super(context);
    this.direction = SwipeDirection.right;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (this.IsSwipeAllowed(event)) {
      return super.onTouchEvent(event);
    }

    return false;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    if (this.IsSwipeAllowed(event)) {
      return super.onInterceptTouchEvent(event);
    }

    return false;
  }

  private boolean IsSwipeAllowed(MotionEvent event) {
    if(this.direction == SwipeDirection.all) return true;

    if(direction == SwipeDirection.none )//disable any swipe
      return false;

    if(event.getAction()==MotionEvent.ACTION_DOWN) {
      initialXValue = event.getX();
      return true;
    }

    if(event.getAction()==MotionEvent.ACTION_MOVE) {
      try {
        float diffX = event.getX() - initialXValue;
        if (diffX > 0 && direction == SwipeDirection.right ) {
          // swipe from left to right detected
          return false;
        }else if (diffX < 0 && direction == SwipeDirection.left ) {
          // swipe from right to left detected
          return false;
        }
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }

    return true;
  }

  public void setAllowedSwipeDirection(SwipeDirection direction) {
    this.direction = direction;
  }


  public enum SwipeDirection {
    all, left, right, none ;
  }
}


