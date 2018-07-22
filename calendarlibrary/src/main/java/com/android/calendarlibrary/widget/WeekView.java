/*
 * Copyright 2017 chenyongci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.calendarlibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Blaz Solar on 24/02/14.
 */
public class WeekView extends ViewGroup {

	public static final String TAG = "WeekView";

	public WeekView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int maxSize = widthSize / 7;
		int baseSize = 0;

		int cnt = getChildCount();
		for(int i = 0; i < cnt; i++) {

			View child = getChildAt(i);

			if(child.getVisibility() == View.GONE) {
				continue;
			}

			child.measure(
					MeasureSpec.makeMeasureSpec(maxSize, MeasureSpec.AT_MOST),
					MeasureSpec.makeMeasureSpec(maxSize, MeasureSpec.AT_MOST)
					);

			baseSize = Math.max(baseSize, child.getMeasuredHeight());

		}

		for (int i = 0; i < cnt; i++) {

			View child = getChildAt(i);

			if (child.getVisibility() == GONE) {
				continue;
			}

			child.measure(
					MeasureSpec.makeMeasureSpec(baseSize, MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(baseSize, MeasureSpec.EXACTLY)
					);

		}

		setMeasuredDimension(widthSize, getLayoutParams().height >= 0 ? getLayoutParams().height : baseSize + getPaddingBottom() + getPaddingTop());

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int cnt = getChildCount();

		int width = getMeasuredWidth();
		int part = width / cnt;

		for(int i = 0; i < cnt; i++) {

			View child = getChildAt(i);
			if(child.getVisibility() == View.GONE) {
				continue;
			}

			int childWidth = child.getMeasuredWidth();

			int x = i * part + ((part - childWidth) / 2);
			child.layout(x, 0, x + childWidth, child.getMeasuredHeight());

		}

	}
}
