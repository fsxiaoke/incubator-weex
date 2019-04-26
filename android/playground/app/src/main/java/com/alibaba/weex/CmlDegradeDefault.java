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
package com.alibaba.weex;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.adapter.ICmlDegradeAdapter;

import java.util.HashMap;

public class CmlDegradeDefault implements ICmlDegradeAdapter {

    @Override
    public DegradeViewWrapper getDegradeView(int degradeCode) {
        return null;
    }

    @Override
    public void degradeActivity(@NonNull Activity activity, @NonNull String url, @Nullable HashMap<String, Object> options, int degradeCode) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        CmlEngine.getInstance().launchPage(activity, url, null);
    }
}
