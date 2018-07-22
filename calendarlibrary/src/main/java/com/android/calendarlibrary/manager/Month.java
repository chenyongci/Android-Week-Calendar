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
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blaz Solar on 24/02/14.
 */
public class Month extends RangeUnit {

    @NonNull
    private final List<Week> mWeeks = new ArrayList<Week>();
    private int mSelectedIndex = -1;

    protected Month(@NonNull LocalDate date, @NonNull LocalDate today, @Nullable LocalDate minDate,
                    @Nullable LocalDate maxDate) {
        super(
                date.withDayOfMonth(1),
                date.withDayOfMonth(date.dayOfMonth().getMaximumValue()),
                today,
                minDate,
                maxDate
        );

        build();
    }

    @Override
    public boolean hasNext() {

        LocalDate maxDate = getMaxDate();
        if (maxDate == null) {
            return true;
        } else {

            LocalDate to = getTo();
            int year = maxDate.getYear();
            int yearTo = to.getYear();

            int month = maxDate.getMonthOfYear();
            int monthTo = to.getMonthOfYear();

            return year > yearTo ||
                    (year == yearTo && month > monthTo);

        }
    }

    @Override
    public boolean  hasPrev() {

        LocalDate minDate = getMinDate();
        if (minDate == null) {
            return true;
        } else {

            LocalDate from = getFrom();
            int year = minDate.getYear();
            int yearFrom = from.getYear();

            int month = minDate.getMonthOfYear();
            int monthFrom = from.getMonthOfYear();

            return year < yearFrom ||
                    (year == yearFrom && month < monthFrom);

        }
    }

    @Override
    public boolean next() {

        if (hasNext()) {
            setFrom(getTo().plusDays(1));
            setTo(getFrom().withDayOfMonth(getFrom().dayOfMonth().getMaximumValue()));

            build();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean prev() {

        if (hasPrev()) {
            setFrom(getFrom().minusDays(1).withDayOfMonth(1));
            setTo(getFrom().withDayOfMonth(getFrom().dayOfMonth().getMaximumValue()));

            build();
            return true;
        } else {
            return false;
        }

    }

    @Override
    public int getType() {
        return TYPE_MONTH;
    }

    @Override
    public void deselect(@NonNull LocalDate date) {
        if (date != null && isSelected() && isInView(date)) {
            for (Week week : mWeeks) {
                if (week.isSelected() && week.isIn(date)) {
                    mSelectedIndex = -1;
                    setSelected(false);
                    week.deselect(date);
                }
            }
        }
    }

    @Override
    public boolean select(@NonNull LocalDate date) {
        int cnt = mWeeks.size();
        for (int i = 0; i < cnt; i++) {
            Week week = mWeeks.get(i);
            if (week.select(date)) {
                mSelectedIndex = i;
                setSelected(true);
                return true;
            }
        }
        return false;
    }

    @NonNull
    public List<Week> getWeeks() {
        return mWeeks;
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    @Override
    public void build() {

        setSelected(false);
        mWeeks.clear();

        LocalDate date = getFrom().withDayOfWeek(1);
        for (int i = 0; i == 0 || getTo().compareTo(date) >= 0; i++) {
            mWeeks.add(new Week(date, getToday(), getMinDate(), getMaxDate()));
            date = date.plusWeeks(1);
        }

    }

    @Nullable
    @Override
    LocalDate getFirstDateOfCurrentMonth(@NonNull LocalDate currentMonth) {

        if (currentMonth != null) {
            int year = currentMonth.getYear();
            int month = currentMonth.getMonthOfYear();

            LocalDate from = getFirstEnabled();
            int fromYear = from.getYear();
            int fromMonth = from.getMonthOfYear();

            if (year == fromYear && month == fromMonth) {
                return from;
            }
        }

        return null;

    }
}
