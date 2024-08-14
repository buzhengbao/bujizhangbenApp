package com.hui.tallybook.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hui.tallybook.R;
import com.hui.tallybook.adapter.CalendarAdapter;
import com.hui.tallybook.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener{
    ImageView errorIv;
    GridView gv;
    LinearLayout hsvLayout;

    List<TextView>hsvViewList;
    List<Integer>yearList;

    int selectPos = -1; //正在被点击年份的位置
    private CalendarAdapter adapter;
    int selectMonth = -1;
    public interface OnRefreshListener{
        public void onRefresh(int selPos, int year, int month);
    }
    OnRefreshListener onRefreshListener;
    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        this.onRefreshListener = onRefreshListener;
    }


    public CalendarDialog(@NonNull Context context, int selectPos, int selectMonth) {
        super(context);
        this.selectPos = selectPos;
        this.selectMonth = selectMonth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rili);
        gv = findViewById(R.id.dialog_rili_gv);
        errorIv = findViewById(R.id.dialog_rili_iv);
        hsvLayout = findViewById(R.id.dialog_rili_layout);
        errorIv.setOnClickListener(this);

        //向横向的Scroll View中添加View的方法
        addViewToLayout();
        initGridView();
        //设置Grid View每一个item的点击事件
        setGVLIstener();
    }

    private void setGVLIstener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selPos = position;
                adapter.notifyDataSetChanged();
                int month = position + 1;
                int year = adapter.year;
                //获取到被选中的年月份
                onRefreshListener.onRefresh(selectPos,year,month);
                cancel();
            }
        });
    }

    private void initGridView() {
        int selYear = yearList.get(selectPos);
        adapter = new CalendarAdapter(getContext(), selYear);
        if (selectMonth == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            adapter.selPos = month;
        }else{
            adapter.selPos = selectMonth - 1;
        }
        gv.setAdapter(adapter);
    }

    private void addViewToLayout() {
        hsvViewList = new ArrayList<>();//将添加进入线性布局当中的Text View进行统一管理的集合
        yearList = DBManager.getYearListFromAccounttb();
        //如果数据库中没有记录就添加今年的记录
        if(yearList.size() == 0){
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }

        //遍历年份，有几年，就向Scroll View中添加几个View
        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_dialogcal_hsv, null);
            hsvLayout.addView(view);   //将view添加到布局当中
            TextView hsvTv = view.findViewById(R.id.item_dialogcal_hsv_tv);
            hsvTv.setText(year+"");
            hsvViewList.add(hsvTv);
        }
        if (selectPos == -1) {
            selectPos = hsvViewList.size() - 1; //默认选最近的年份
        }
        changeTvbg(selectPos); //默认选中最后一个
        setHSVClickListener(); //设置每一个View的监听事件
    }

    //给横向的Scroll View当中的每一个Text View设置点击事件
    private void setHSVClickListener() {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView view = hsvViewList.get(i);
            int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTvbg(pos);
                    selectPos = pos;
                    //获取被选中的年份，如何下面的Grid View显示的数据源会发生变化
                    Integer year = yearList.get(selectPos);
                    adapter.setYear(year);
                }
            });
        }
    }


    //传入的被选中的位置，改变此位置上的背景和文字颜色
    private void changeTvbg(int selectPos) {
        for(int i = 0; i < hsvViewList.size(); i++){
            TextView tv = hsvViewList.get(i);
            tv.setBackgroundColor(R.drawable.dialog_btn_bg);
            tv.setTextColor(Color.BLACK);
        }

        TextView selView = hsvViewList.get(selectPos);
        selView.setBackgroundColor(R.drawable.main_recordbtn_bg);
        selView.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dialog_rili_iv){
            cancel();
        }
    }

    public void setDialogSize(){
        //获得当前窗口
        Window window = getWindow();
        //获取窗口对象
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获得屏幕尺寸
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        //设置dialog的尺寸
        wlp.width = width;
        wlp.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
//        Window.setAttributes(wlp);
    }
}
