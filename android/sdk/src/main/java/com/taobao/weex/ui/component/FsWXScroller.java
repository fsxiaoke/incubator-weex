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

import java.lang.reflect.InvocationTargetException;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;

import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;

/**
 * Component for scroller. It also support features like
 * "appear", "disappear" and "sticky"
 */
@Component(lazyload = false)
public class FsWXScroller extends WXScroller {
  public FsWXScroller(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy,
                      BasicComponentData basicComponentData) {
    super(instance, parent, instanceId, isLazy, basicComponentData);
  }

  public FsWXScroller(WXSDKInstance instance, WXVContainer parent,
                      BasicComponentData basicComponentData) {
    super(instance, parent, basicComponentData);
  }

  public static class Creator implements ComponentCreator {
    @Override
    public WXComponent createInstance(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
      // For performance message collection
      instance.setUseScroller(true);
      return new FsWXScroller(instance, parent, basicComponentData);
    }
  }


  @Override
  public void setLayout(WXComponent component) {
    if (TextUtils.isEmpty(component.getComponentType())
            || TextUtils.isEmpty(component.getRef()) || component.getLayoutPosition() == null
            || component.getLayoutSize() == null) {
      return;
    }
    if (component.getHostView() != null) {
      int layoutDirection = component.isLayoutRTL() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR;
      ViewCompat.setLayoutDirection(component.getHostView(), layoutDirection);
    }
    CSSShorthand cs=new CSSShorthand();

    component.setMargins(cs);
    component.getLayoutPosition().update(0,0,0,0);
    super.setLayout(component);
  }

}
