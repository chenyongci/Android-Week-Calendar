## 主要特性

* 设置自定义日期角标
* 农历切换
* 左右滑动切换上下周月，上下滑动切换周月模式
* 抽屉式周月切换效果
* 标记指定日期（marker）
* 跳转到指定日期

<div style = "float:center">
    <img src="https://github.com/chenyongci/android-week-calendar/blob/master/screenshot/preview.gif" width="240">

![预览图片](/screenshot/shot.jpg)

## 使用方法

#### 在layout中使用:

```xml
<com.android.calendarlibrary.CollapseCalendarView
        android:id="@+id/calendar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

#### 使用此方法初始化日历标记数据

```java
mManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH,
                LocalDate.now().withYear(100),
                LocalDate.now().plusYears(100));
calendarView.init(mManager);
```

#### 使用方法设置日历数据

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

####  使用方法添加上相关监听

月份切换监听器
```java
mManager.setMonthChangeListener(new OnMonthChangeListener() {
       @Override
      public void monthChange(String month, LocalDate mSelected) {
      // TODO Auto-generated method stub
      //Toast.makeText(MainActivity.this, month, Toast.LENGTH_SHORT).show();
      }
});
```
日期选中监听器
```java
calendarView.setDateSelectListener(new OnDateSelect() {
       @Override
       public void onDateSelected(LocalDate date) {
       // Toast.makeText(MainActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
       }
});
```
## APK
安装 [apk](https://www.pgyer.com/BMmh) 文件预览效果，或者通过下面二维码去下载安装：

![DEMO下载二维码](/screenshot/BMmh.png)

## 感谢
[android-collapse-calendar-view](https://github.com/blazsolar/android-collapse-calendar-view)

## License


    Copyright 2018 chenyongci

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
