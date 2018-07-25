package com.android.calendarlibrary.listener;

import org.joda.time.LocalDate;

/**
 * @Author : chenyongci
 * @date : 2018/7/25
 * @desc :
 */

public interface OnMonthChangeListener {
    /**
     * 月份切换监听器
     * @param month	当前月份
     * @param mSelected	选中日期
     */
    void monthChange(String month, LocalDate mSelected);
}
