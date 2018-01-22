package com.android.calendarlibrary.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author jty
 * @date 2015年5月22日
 */
public class ChinaDate {
	final private static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0,
		0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0,
		0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540,
		0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5,
		0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
		0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3,
		0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0,
		0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0,
		0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8,
		0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570,
		0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5,
		0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0,
		0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50,
		0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0,
		0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
		0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7,
		0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50,
		0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954,
		0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260,
		0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0,
		0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0,
		0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20,
		0x0ada0 };

//final private static int[] year20 = new int[] { 1, 4, 1, 2, 1, 2, 1, 1, 2,
//		1, 2, 1 };


//final private static int[] year2000 = new int[] { 0, 3, 1, 2, 1, 2, 1, 1,
//		2, 1, 2, 1 };

public final static String[] nStr1 = new String[] { "", "正", "二", "三", "四",
		"五", "六", "七", "八", "九", "十", "十一", "十二" };

private final static String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊",
		"己", "庚", "辛", "壬", "癸" };

private final static String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰",
		"巳", "午", "未", "申", "酉", "戌", "亥" };

private final static String[] Animals = new String[] { "鼠", "牛", "虎", "兔",
		"龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪" };

//private final static String[] solarTerm = new String[] { "小寒", "大寒", "立春",
//		"雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑",
//		"立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };

//private final static String[] sFtv = new String[] { "0101*元旦", "0214 情人节",
//		"0308 妇女节", "0312 植树节", "0315 消费者权益日", "0401 愚人节", "0501 劳动节",
//		"0504 青年节", "0512 护士节", "0601 儿童节", "0701 建党节", "0801 建军节",
//		"0808 父亲节", "0909 毛泽东逝世纪念", "0910 教师节", "0928 孔子诞辰", "1001*国庆节",
//		"1006 老人节", "1024 联合国日", "1112 孙中山诞辰", "1220 澳门回归", "1225 圣诞节",
//		"1226 毛泽东诞辰" };

//private final static String[] lFtv = new String[] { "0101*农历春节",
//		"0115 元宵节", "0505 端午节", "0707 七夕情人节", "0815 中秋节", "0909 重阳节",
//		"1208 腊八节", "1224 小年", "0100*除夕" };

/**
 * 传回农历 y年的总天数
 * 
 * @param y
 * @return
 */
final private static int lYearDays(int y) {
	int i, sum = 348;
	for (i = 0x8000; i > 0x8; i >>= 1) {
		if ((lunarInfo[y - 1900] & i) != 0)
			sum += 1;
	}
	return (sum + leapDays(y));
}

/**
 * 传回农历 y年闰月的天数
 * 
 * @param y
 * @return
 */
final private static int leapDays(int y) {
	if (leapMonth(y) != 0) {
		if ((lunarInfo[y - 1900] & 0x10000) != 0)
			return 30;
		else
			return 29;
	} else
		return 0;
}

/**
 * 传回农历 y年闰哪个月 1-12 , 没闰传回 0
 * 
 * @param y
 * @return
 */
final private static int leapMonth(int y) {
	return (int) (lunarInfo[y - 1900] & 0xf);
}

/**
 * 传回农历 y年m月的总天数
 * 
 * @param y
 * @param m
 * @return
 */
final private static int monthDays(int y, int m) {
	if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
		return 29;
	else
		return 30;
}

/**
 * 传回农历 y年的生肖
 * 
 * @param y
 * @return
 */
final public static String AnimalsYear(int y) {
	return Animals[(y - 4) % 12];
}

/**
 * 传入 月日的offset 传回干支,0=甲子
 * 
 * @param num
 * @return
 */
final private static String cyclicalm(int num) {
	return (Gan[num % 10] + Zhi[num % 12]);
}

/**
 * 传入 offset 传回干支, 0=甲子
 * 
 * @param y
 * @return
 */
final public static String cyclical(int y) {
	int num = y - 1900 + 36;
	return (cyclicalm(num));
}

/**
 * 传出农历.year0 .month1 .day2 .yearCyl3 .monCyl4 .dayCyl5 .isLeap6
 * 
 * @param y
 * @param m
 * @return
 */
//final private long[] Lunar(int y, int m) {
//	long[] nongDate = new long[7];
//	int i = 0, temp = 0, leap = 0;
//	Date baseDate = new GregorianCalendar(1900 + 1900, 1, 31).getTime();
//	Date objDate = new GregorianCalendar(y + 1900, m, 1).getTime();
//	long offset = (objDate.getTime() - baseDate.getTime()) / 86400000L;
//	if (y < 2000)
//		offset += year19[m - 1];
//	if (y > 2000)
//		offset += year20[m - 1];
//	if (y == 2000)
//		offset += year2000[m - 1];
//	nongDate[5] = offset + 40;
//	nongDate[4] = 14;
//
//	for (i = 1900; i < 2050 && offset > 0; i++) {
//		temp = lYearDays(i);
//		offset -= temp;
//		nongDate[4] += 12;
//	}
//	if (offset < 0) {
//		offset += temp;
//		i--;
//		nongDate[4] -= 12;
//	}
//	nongDate[0] = i;
//	nongDate[3] = i - 1864;
//	leap = leapMonth(i); // 闰哪个月
//	nongDate[6] = 0;
//
//	for (i = 1; i < 13 && offset > 0; i++) {
//		// 闰月
//		if (leap > 0 && i == (leap + 1) && nongDate[6] == 0) {
//			--i;
//			nongDate[6] = 1;
//			temp = leapDays((int) nongDate[0]);
//		} else {
//			temp = monthDays((int) nongDate[0], i);
//		}
//
//		// 解除闰月
//		if (nongDate[6] == 1 && i == (leap + 1))
//			nongDate[6] = 0;
//		offset -= temp;
//		if (nongDate[6] == 0)
//			nongDate[4]++;
//	}
//
//	if (offset == 0 && leap > 0 && i == leap + 1) {
//		if (nongDate[6] == 1) {
//			nongDate[6] = 0;
//		} else {
//			nongDate[6] = 1;
//			--i;
//			--nongDate[4];
//		}
//	}
//	if (offset < 0) {
//		offset += temp;
//		--i;
//		--nongDate[4];
//	}
//	nongDate[1] = i;
//	nongDate[2] = offset + 1;
//	return nongDate;
//}

/**
 * 传出y年m月d日对应的农历.year0 .month1 .day2 .yearCyl3 .monCyl4 .dayCyl5 .isLeap6
 * 
 * @param y
 * @param m
 * @param d
 * @return
 */
final public static long[] calElement(int y, int m, int d) {
	long[] nongDate = new long[7];
	int i = 0, temp = 0, leap = 0;
	Date baseDate = new GregorianCalendar(0 + 1900, 0, 31).getTime();
	Date objDate = new GregorianCalendar(y, m - 1, d).getTime();
	long offset = (objDate.getTime() - baseDate.getTime()) / 86400000L;
	nongDate[5] = offset + 40;
	nongDate[4] = 14;

	for (i = 1900; i < 2050 && offset > 0; i++) {
		temp = lYearDays(i);
		offset -= temp;
		nongDate[4] += 12;
	}
	if (offset < 0) {
		offset += temp;
		i--;
		nongDate[4] -= 12;
	}
	nongDate[0] = i;
	nongDate[3] = i - 1864;
	leap = leapMonth(i); // 闰哪个月,没闰返回0
	nongDate[6] = 0;

	for (i = 1; i < 13 && offset > 0; i++) {
		// 闰月
		if (leap > 0 && i == (leap + 1) && nongDate[6] == 0) {
			--i;
			nongDate[6] = 1;
			temp = leapDays((int) nongDate[0]);
		} else {
			temp = monthDays((int) nongDate[0], i);
		}

		// 解除闰月
		if (nongDate[6] == 1 && i == (leap + 1))
			nongDate[6] = 0;
		offset -= temp;
		if (nongDate[6] == 0)
			nongDate[4]++;
	}

	if (offset == 0 && leap > 0 && i == leap + 1) {
		if (nongDate[6] == 1) {
			nongDate[6] = 0;
		} else {
			nongDate[6] = 1;
			--i;
			--nongDate[4];
		}
	}
	if (offset < 0) {
		offset += temp;
		--i;
		--nongDate[4];
	}
	nongDate[1] = i;
	nongDate[2] = offset + 1;
	return nongDate;
}

/**
 * 传入天数可以 得到旧历的显示
 * @param day
 * @return
 */
public final static String getChinaDate(int day) {
	String a = "";
	if (day == 10)
		return "初十";
	if (day == 20)
		return "二十";
	if (day == 30)
		return "卅十";
	int two = (int) ((day) / 10);
	if (two == 0)
		a = "初";
	if (two == 1)
		a = "十";
	if (two == 2)
		a = "廿";
	if (two == 3)
		a = "卅";
	int one = (int) (day % 10);
	switch (one) {
	case 1:
		a += "一";
		break;
	case 2:
		a += "二";
		break;
	case 3:
		a += "三";
		break;
	case 4:
		a += "四";
		break;
	case 5:
		a += "五";
		break;
	case 6:
		a += "六";
		break;
	case 7:
		a += "七";
		break;
	case 8:
		a += "八";
		break;
	case 9:
		a += "九";
		break;
	}
	return a;
}

public static String today() {
	Calendar today = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
	int year = today.get(Calendar.YEAR);
	int month = today.get(Calendar.MONTH) + 1;
	int date = today.get(Calendar.DATE);
	long[] l = calElement(year, month, date);
	StringBuffer sToday = new StringBuffer();
	try {
		sToday.append(sdf.format(today.getTime()));
		sToday.append(" 农历");
		sToday.append(cyclical(year));
		sToday.append('(');
		sToday.append(AnimalsYear(year));
		sToday.append(")年");
		sToday.append(nStr1[(int) l[1]]);
		sToday.append("月");
		sToday.append(getChinaDate((int) (l[2])));
		return sToday.toString();
	} finally {
		sToday = null;
	}
}

private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 EEEEE");

/**
 * 农历日历工具使用演示
 * 
 * @param args
 */
public static void test() {
	Calendar cal= Calendar.getInstance();
	cal.set(Calendar.MONTH, 3);
	cal.set(Calendar.DATE, 1);
	long[] l;
	for(int i=0;i<100;i++){
		l = calElement(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DATE));
		StringBuffer sToday = new StringBuffer();
		sToday.append(sdf.format(cal.getTime()));
		sToday.append(" 农历");
		sToday.append(cyclical(cal.get(Calendar.YEAR)));
		sToday.append('(');
		sToday.append(AnimalsYear(cal.get(Calendar.YEAR)));
		sToday.append(")年");
		sToday.append(nStr1[(int) l[1]]);
		sToday.append("月");
		sToday.append(getChinaDate((int) (l[2])));
		
		cal.add(Calendar.DATE, 1);
	}
}

public static ArrayList<HashMap<String,String>> getChinaDates(int currentyear, int month){
	SimpleDateFormat myfmt = new SimpleDateFormat("yyyy-MM-dd");
	ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
	Calendar cal= Calendar.getInstance();   //Locale.SIMPLIFIED_CHINESE
	cal.set(Calendar.YEAR, currentyear);
	cal.set(Calendar.MONTH, month);
	cal.set(Calendar.DATE, 1);
	long[] l;
	HashMap<String,String> map;
	while(cal.get(Calendar.MONTH)==month){
		l = calElement(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DATE));
		map=new HashMap<String,String>();
		map.put("date", myfmt.format(cal.getTime()));
		String chinadate=getChinaDate((int) (l[2]));
		if(chinadate.equals("初一")){
			chinadate=nStr1[(int) l[1]]+"月";
		}
		map.put("chinadate", chinadate);
		list.add(map);
		cal.add(Calendar.DATE, 1);
	}
	return list;
}


}
