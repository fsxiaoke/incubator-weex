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
package com.taobao.weex.dom;

import com.taobao.weex.utils.TypefaceUtil;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

/**
 * @author xiongtj
 * 使TextView中不同大小字体垂直居�?
 */
public class WXVerticalCenterSpan extends ReplacementSpan {


    private final int mStyle;
    private final int mWeight;
    private final String mFontFamily;
    private String mInstanceId;
    private int mFontColor;
    private float mFontSizePx;

    public WXVerticalCenterSpan(String instanceId,int fontStyle, int fontWeight, String fontFamily,
                                int fontSize,int fontColor) {
        mInstanceId = instanceId;
        mStyle = fontStyle;
        mWeight = fontWeight;
        mFontFamily = fontFamily;
        mFontColor = fontColor;
        mFontSizePx = fontSize;
    }



    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        return (int) p.measureText(text.toString());
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     Paint paint) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        Paint.FontMetricsInt fm = p.getFontMetricsInt();
        // 此处重新计算y坐标，使字体居中
        canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) / 2 - (bottom + top) / 2), p);
        canvas.drawText(text.subSequence(start, end).toString(), x, (top + bottom)/2 - (fm.bottom - fm.top) / 2 - fm.top, paint);
    }

    private Paint getCustomTextPaint(Paint paint) {
        TypefaceUtil.applyFontStyle(mInstanceId,paint, mStyle, mWeight, mFontFamily);
        paint.setTextSize(mFontSizePx);   //设定字体大小, sp转换为px
        paint.setColor(mFontColor);
        return paint;
    }
}
