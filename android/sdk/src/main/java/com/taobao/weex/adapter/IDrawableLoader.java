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

package com.taobao.weex.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.taobao.weex.WXSDKInstance;

public interface IDrawableLoader {

  interface DrawableTarget {
    void setDrawable(@Nullable Drawable drawable, boolean resetBounds);
  }

  interface StaticTarget extends DrawableTarget{
    void setDrawable(@Nullable Drawable drawable, boolean resetBounds);
  }

  interface AnimatedTarget extends DrawableTarget{
    void setAnimatedDrawable(@Nullable Drawable drawable);
  }

  void setDrawable(String url, DrawableTarget drawableTarget, DrawableStrategy drawableStrategy);
  void setDrawable(WXSDKInstance instance, String url, DrawableTarget drawableTarget, DrawableStrategy drawableStrategy);
}

