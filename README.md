#### 主要特性
* 设置自定义日期角标
* 左右滑动切换上下周月，上下滑动切换周月模式
* 抽屉式周月切换效果
* 标记指定日期（marker）
* 跳转到指定日期

<div style = "float:center">
    <img src="https://github.com/chenyongci/android-week-calendar/blob/master/images/preview.gif" width="240">


#### 使用方法

#####在layout中使用:

```xml
<com.android.calendarlibrary.CollapseCalendarView
        android:id="@+id/calendar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

#####使用此方法初始化日历标记数据

```java
mManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH,
                LocalDate.now().withYear(100),
                LocalDate.now().plusYears(100));
calendarView.init(mManager);
```

#####使用方法设置日历数据

是否显示农历
```java
calendarView.showChinaDay(true);
```
回到今天
```java
calendarView.changeDate(LocalDate.now().toString());
```
周月切换
```java
 // TODO Auto-generated method stub
mManager.toggleView();
calendarView.populateLayout();
```
班排
```java
calendarView.setArrayData(json);
calendarView.populateLayout();
```

#####使用方法添加上相关监听

月份切换监听器
```java
mManager.setMonthChangeListener(new CalendarManager.OnMonthChangeListener() {
       @Override
      public void monthChange(String month, LocalDate mSelected) {
      // TODO Auto-generated method stub
      //Toast.makeText(MainActivity.this, month, Toast.LENGTH_SHORT).show();
      }
});
```
日期选中监听器
```java
calendarView.setDateSelectListener(new CollapseCalendarView.OnDateSelect() {
       @Override
       public void onDateSelected(LocalDate date) {
       // Toast.makeText(MainActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
       }
});
```

#####thanks

[**android-collapse-calendar-view**](https://github.com/blazsolar/android-collapse-calendar-view)