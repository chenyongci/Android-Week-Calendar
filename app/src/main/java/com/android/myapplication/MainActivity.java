package com.android.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.android.calendarlibrary.CollapseCalendarView;
import com.android.calendarlibrary.listener.OnDateSelect;
import com.android.calendarlibrary.listener.OnMonthChangeListener;
import com.android.calendarlibrary.listener.OnTitleClickListener;
import com.android.calendarlibrary.manager.CalendarManager;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Author android
 * @date 2018/1/22
 * @return
 */
public class MainActivity extends AppCompatActivity {

    private CollapseCalendarView calendarView;
    private Switch switchChinaDay;

    private CalendarManager mManager;

    private JSONObject json;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        switchChinaDay = (Switch) findViewById(R.id.switch_china_day);
        initView();
        initListener();
        initData();
    }


    private void initView() {
        mManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH,
                LocalDate.now(),//默认这个月，如果LocalDate.now().minusYears(1));
                LocalDate.now().plusYears(1));//可以划之后一年
        calendarView.init(mManager);

        calendarView.showChinaDay(true);//显示农历

        switchChinaDay.setChecked(true);
    }

    private void initListener(){
        switchChinaDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                calendarView.showChinaDay(b);
                if(b){
                    switchChinaDay.setText("显示农历");
                }else{
                    switchChinaDay.setText("隐藏农历");
                }
            }
        });

        /**
         *月份切换监听器
         */
        mManager.setMonthChangeListener(new OnMonthChangeListener() {

            @Override
            public void monthChange(String month, LocalDate mSelected) {
                // TODO Auto-generated method stub
                //Toast.makeText(MainActivity.this, month, Toast.LENGTH_SHORT).show();
            }

        });
        /**
         * 日期选中监听器
         */
        calendarView.setDateSelectListener(new OnDateSelect() {

            @Override
            public void onDateSelected(LocalDate date) {
               // Toast.makeText(MainActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        calendarView.setTitleClickListener(new OnTitleClickListener() {

            @Override
            public void onTitleClick() {
               // Toast.makeText(MainActivity.this, "点击标题", Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * 回到今天
         */
        findViewById(R.id.btn_today).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                calendarView.changeDate(LocalDate.now().toString());
            }
        });
        /**
         * 周月切换
         */
        findViewById(R.id.btn_changemode).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mManager.toggleView();
                calendarView.populateLayout();
            }
        });
    }
    
    
    private void initData() {
        Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.MONTH, 9);
//		cal.set(Calendar.DAY_OF_MONTH, 0);
        json = new JSONObject();
        try {
            for (int i = 0; i < 30; i++) {
                JSONObject jsonObject2 = new JSONObject();
                if (i <= 6) {
                    jsonObject2.put("type", "休");
                } else if ( i > 6 && i< 11) {
                    jsonObject2.put("type", "班");
                }
                if (i%3 == 0) {
                    jsonObject2.put("list", new JSONArray());
                }

                json.put(sdf.format(cal.getTime()), jsonObject2);

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        //设置数据显示
        calendarView.setArrayData(json);
        //刷新日期
        calendarView.populateLayout();
    }
}
