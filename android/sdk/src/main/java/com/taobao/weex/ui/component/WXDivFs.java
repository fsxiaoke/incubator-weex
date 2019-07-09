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
package com.taobao.weex.ui.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.flat.FlatComponent;
import com.taobao.weex.ui.flat.WidgetContainer;
import com.taobao.weex.ui.flat.widget.WidgetGroup;
import com.taobao.weex.ui.view.WXFrameLayout;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * div component
 */
@Component(lazyload = false)
public class WXDivFs extends WXDiv {


  public static class Ceator implements ComponentCreator {
    public WXComponent createInstance(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
      return new WXDivFs(instance, parent, basicComponentData);
    }
  }

  @Deprecated
  public WXDivFs(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy, BasicComponentData basicComponentData) {
    this(instance, parent, basicComponentData);
  }

  public WXDivFs(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
    super(instance, parent, basicComponentData);
  }
  protected void setHostLayoutParams(WXFrameLayout host, int width, int height, int left, int right, int top, int bottom) {
    ViewGroup.LayoutParams lp;
    if (mParent == null) {
      FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
      this.setMarginsSupportRTL(params, left, top, right, bottom);
      lp = params;
    } else {
      lp = mParent.getChildLayoutParams(this, host, width, height, left, right, top, bottom);
    }
    if (lp != null) {
      lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
      host.setLayoutParams(lp);
    }
  }
}
