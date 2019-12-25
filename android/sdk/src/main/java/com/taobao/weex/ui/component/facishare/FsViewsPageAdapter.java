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

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FsViewsPageAdapter extends PagerAdapter {
    List<View> pages=new ArrayList<>();
    public void addPageView(View view) {
        pages.add(view);
        notifyDataSetChanged();
    }
    public void removePageView(View view) {
        pages.remove(view);
        notifyDataSetChanged();
    }
    @Override
    public int getItemPosition(@NonNull Object object) {
        int ret = POSITION_NONE;
        int i = 0;
        for (View v :
                pages) {
            if (v.equals(object)) {
                ret = i;
                break;
            }
            i++;

        }
        return ret;
    }

    public void addPageView(View view,int index) {
        int count = pages.size();
        index = index >= count ? -1 : index;
        if (index == -1) {
            pages.add(view);
        } else {
            pages.add(index,view);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return pages.size();
    }

    // 来判断显示的是否是同�?张图片，这里我们将两个参数相比较返回即可
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片�?�?
    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView(pages.get(position));
    }

    // 当要显示的图片可以进行缓存的时�?�，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即�?
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View v=pages.get(position);
        if (v.getParent()!=null){
            ((ViewGroup)v.getParent()).removeView(v);
        }else{
        }
        view.addView(v);
        return pages.get(position);
    }
}
