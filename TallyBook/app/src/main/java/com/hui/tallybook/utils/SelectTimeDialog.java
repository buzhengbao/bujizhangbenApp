package com.hui.tallybook.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hui.tallybook.R;

/**
 * 在记录页面弹出时间对话框**/
public class SelectTimeDialog extends Dialog implements View.OnClickListener {
    EditText hourEt, minuteEt;
    DatePicker datePicker;
    Button ensureBt, cancelBt;
    private OnEnsureListener onEnsureListener;

    public interface OnEnsureListener{
        public void onEnsure(String time, int year, int month, int day);
    }
    public void setOnEnsureListener(OnEnsureListener onEnsureListener){
        this.onEnsureListener = onEnsureListener;
    }
    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calender);
        hourEt = findViewById(R.id.dialog_time_et_hour);
        minuteEt = findViewById(R.id.dialog_time_et_minute);
        datePicker = findViewById(R.id.dialog_date_picker);
        ensureBt = findViewById(R.id.dialog_time_btn_ensure);
        cancelBt = findViewById(R.id.dialog_time_btn_cancel);
        ensureBt.setOnClickListener(this);
        cancelBt.setOnClickListener(this);
        hideDatePickerHeader();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dialog_time_btn_ensure){
            int year = datePicker.getYear();
            int month = datePicker.getMonth()+1;
            int day = datePicker.getDayOfMonth();
            String monthStr = String.valueOf(month);
            if(month < 10){
                monthStr = "0" + month;
            }
            String dayStr = String.valueOf(day);
            if(day < 10){
                dayStr = "0" + day;
            }
            //获取时间
            String hourStr = hourEt.getText().toString();
            String minuteStr = minuteEt.getText().toString();
            int hour = 0;
            if(!TextUtils.isEmpty(hourStr)){
                hour = Integer.parseInt(hourStr);
                hour = hour%24;
            }
            int minute = 0;
            if(!TextUtils.isEmpty(minuteStr)){
                minute = Integer.parseInt(minuteStr);
                minute = minute%60;
            }
            hourStr=String.valueOf(hour);
            minuteStr=String.valueOf(minute);
            if(hour < 10){
                hourStr = "0" + hour;
            }
            if(minute < 10){
                minuteStr = "0" + minute;
            }
            String timeFormat = year + "年" + monthStr + "月" + dayStr + "日" + hourStr + ":" + minuteStr;
            if(onEnsureListener!= null){
                onEnsureListener.onEnsure(timeFormat, year, month, day);
            }
            cancel();
        }else{
            cancel();
        }
    }


    //隐藏DatePicker头布局
    public void hideDatePickerHeader(){
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if(rootView == null){
            return;
        }
        View headerView = rootView.getChildAt(0);
        if(headerView == null){
            return;
        }

        //5.0
        int headerId = getContext().getResources().getIdentifier("dialog_date_picker_selector_layout", "id", "android");
        if(headerId == headerView.getId()){
            headerView.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = rootView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(params);

            ViewGroup animatorView = (ViewGroup) datePicker.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animatorView.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animatorView.setLayoutParams(layoutParamsAnimator);

            View child = animatorView.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParamsChild);
            return;
        }

        //6.0
        headerId = getContext().getResources().getIdentifier("dialog_date_picker_header", "id", "android");
        if(headerId == headerView.getId()){
            headerView.setVisibility(View.GONE);
        }

    }
}
