package com.hui.tallybook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hui.tallybook.adapter.ChartVPAdapter;
import com.hui.tallybook.db.DBManager;
import com.hui.tallybook.frag_chart.IncomeChartFragment;
import com.hui.tallybook.frag_chart.OutcomeChartFragment;
import com.hui.tallybook.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {

    Button inBtn,outBtn;
    TextView dateTv,inTV,outTV;
    ViewPager chartVp;
    private int year;
    private int month;
    int selectPos = -1, selectMonth = -1;

    List<Fragment> chartFragList;
    private IncomeChartFragment incomeChartFragment;
    private OutcomeChartFragment outcomeChartFragment;
    private ChartVPAdapter chartVPAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year, month);
        initFrag();
        setVPSelectListener();
    }

    //滑动事件的监听
    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setButtonStyle(position);
            }
        });
    }

    //初始化Fragment
    private void initFrag() {
        chartFragList = new ArrayList<>();
        //添加Fragment对象
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();
        //添加数据到Fragment当中
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);
        //将Fragment添加到集合中
        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);
        //使用适配器
        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartVPAdapter);
        //将Fragment加载到Activity当中

    }

    //统计某年某月的收支情况数据
    private void initStatistics(int year, int month) {
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        int inCountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1);
        int outCountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0);
        dateTv.setText(year + "年" + month + "月账单");
        inTV.setText("共" + inCountItemOneMonth + "笔收入，￥ " + inMoneyOneMonth);
        outTV.setText("共" + outCountItemOneMonth + "笔支出，￥ " + outMoneyOneMonth);
    }

    //    初始化时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    //初始化控件
    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);
        dateTv = findViewById(R.id.chart_tv_date);
        inTV = findViewById(R.id.chart_tv_in);
        outTV = findViewById(R.id.chart_tv_out);
        chartVp = findViewById(R.id.chart_vp);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.chart_iv_back){
            finish();
        }else if(view.getId() == R.id.chart_iv_rili){
            showCalendarDialog();
        }else if(view.getId() == R.id.chart_btn_in){
            setButtonStyle(1);
            chartVp.setCurrentItem(1);
        }else if(view.getId() == R.id.chart_btn_out){
            setButtonStyle(0);
            chartVp.setCurrentItem(0);
        }
    }

    /**
     * 显示日历对话框
     * */
    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistics(year, month);
                incomeChartFragment.setData(year, month);
                outcomeChartFragment.setData(year, month);
            }
        });
    }

    /**
     * 设置Button的样式改变
     * */
    private void setButtonStyle(int kind){
        if(kind == 0){
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg1);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        }else if(kind == 1){
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg1);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}