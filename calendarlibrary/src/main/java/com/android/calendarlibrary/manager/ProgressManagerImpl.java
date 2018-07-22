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

package com.android.calendarlibrary.manager;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.android.calendarlibrary.CollapseCalendarView;
import com.android.calendarlibrary.models.AbstractViewHolder;
import com.android.calendarlibrary.models.SizeViewHolder;
import com.android.calendarlibrary.models.StubViewHolder;


/**
 * Created by Blaz Solar on 17/04/14.
 */
public class ProgressManagerImpl extends ProgressManager {

    public ProgressManagerImpl(@NonNull CollapseCalendarView calendarView, int activeWeek, boolean fromMonth) {
        super(calendarView, activeWeek, fromMonth);

        if (!fromMonth) {
            initMonthView();
        } else {
            initWeekView();
        }

    }

    @Override
    public void finish(final boolean expanded) {

        mCalendarView.post(new Runnable() { // to prevent flickering
            @Override
            public void run() {
                mCalendarView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mWeeksView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                for (AbstractViewHolder view : mViews) {
                    view.onFinish(true);
                }

                if (!expanded) {
                    CalendarManager manager = mCalendarView.getManager();
                    if (mFromMonth) {
                        manager.toggleView();
                    } else {
                        manager.toggleToWeek(mActiveIndex);
                    }
                    mCalendarView.populateLayout();
                }
            }
        });
    }

    private void initMonthView() {

        mCalendarHolder = new SizeViewHolder(mCalendarView.getHeight(), 0);
        mCalendarHolder.setView(mCalendarView);
        mCalendarHolder.setDelay(0);
        mCalendarHolder.setDuration(1);

        mWeeksHolder = new SizeViewHolder(mWeeksView.getHeight(), 0);
        mWeeksHolder.setView(mWeeksView);
        mWeeksHolder.setDelay(0);
        mWeeksHolder.setDuration(1);

        mCalendarView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mCalendarView.getViewTreeObserver().removeOnPreDrawListener(this);

                mCalendarHolder.setMaxHeight(mCalendarView.getHeight());
                mWeeksHolder.setMaxHeight(mWeeksView.getHeight());

                mCalendarView.getLayoutParams().height = mCalendarHolder.getMinHeight();
                mWeeksView.getLayoutParams().height = mCalendarHolder.getMinHeight();

                initializeChildren();

                setInitialized(true);

                return false;
            }
        });
    }

    private void initWeekView() {

        mCalendarHolder = new SizeViewHolder(0, mCalendarView.getHeight());
        mCalendarHolder.setView(mCalendarView);
        mCalendarHolder.setDelay(0);
        mCalendarHolder.setDuration(1);

        mWeeksHolder = new SizeViewHolder(0, mWeeksView.getHeight());
        mWeeksHolder.setView(mWeeksView);
        mWeeksHolder.setDelay(0);
        mWeeksHolder.setDuration(1);

        initializeChildren();

        mCalendarView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mCalendarView.getViewTreeObserver().removeOnPreDrawListener(this);

                mCalendarHolder.setMinHeight(mCalendarView.getHeight());
                mWeeksHolder.setMinHeight(mWeeksView.getHeight());

                mCalendarView.getLayoutParams().height = mCalendarHolder.getMaxHeight();
                mWeeksView.getLayoutParams().height = mCalendarHolder.getMaxHeight();

                setInitialized(true);

                return false;
            }
        });
    }

    private void initializeChildren() {

        int childCount = mWeeksView.getChildCount();

        // FIXME do not assume that all views are the same height
        mViews = new AbstractViewHolder[childCount];
        for (int i = 0; i < childCount; i++) {

            View view = mWeeksView.getChildAt(i);

            int activeIndex = getActiveIndex();

            AbstractViewHolder holder;
            if (i == activeIndex) {
                holder = new StubViewHolder();
            } else {
                SizeViewHolder tmpHolder = new SizeViewHolder(0, view.getHeight());

                final int duration = mWeeksHolder.getMaxHeight() - view.getHeight();

                if (i < activeIndex) {
                    tmpHolder.setDelay(view.getTop() * 1.0f / duration);
                } else {
                    tmpHolder.setDelay((view.getTop() - view.getHeight()) * 1.0f / duration);
                }
                tmpHolder.setDuration(view.getHeight() * 1.0f / duration);

                holder = tmpHolder;

                view.setVisibility(View.GONE);
            }

            holder.setView(view);

            mViews[i] = holder;
        }

    }

}
