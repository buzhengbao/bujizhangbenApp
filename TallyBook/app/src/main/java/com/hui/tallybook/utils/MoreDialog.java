package com.hui.tallybook.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.hui.tallybook.AboutActivity;
import com.hui.tallybook.HistoryActivity;
import com.hui.tallybook.MonthChartActivity;
import com.hui.tallybook.R;
import com.hui.tallybook.SettingActivity;

public class MoreDialog extends Dialog implements View.OnClickListener{

    Button aboutBtn,settingBtn,historyBtn, infoBtn;
    ImageView closeIv;

    public MoreDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_more);
        aboutBtn = findViewById(R.id.more_btn_about);
        settingBtn = findViewById(R.id.more_btn_setting);
        historyBtn = findViewById(R.id.more_btn_record);
        infoBtn = findViewById(R.id.more_btn_info);
        closeIv = findViewById(R.id.dialog_more_iv_close);

        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        closeIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if(v.getId() == R.id.more_btn_about){
            intent.setClass(getContext(), AboutActivity.class);
            getContext().startActivity(intent);
        }
        else if(v.getId() == R.id.more_btn_setting){
            intent.setClass(getContext(), SettingActivity.class);
            getContext().startActivity(intent);
        }
        else if(v.getId() == R.id.more_btn_record){
            intent.setClass(getContext(), HistoryActivity.class);
            getContext().startActivity(intent);
        }
        else if(v.getId() == R.id.more_btn_info){
            intent.setClass(getContext(), MonthChartActivity.class);
            getContext().startActivity(intent);
        }
        else if(v.getId() == R.id.dialog_more_iv_close){

        }
        cancel();
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
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
//        Window.setAttributes(wlp);
    }
}
