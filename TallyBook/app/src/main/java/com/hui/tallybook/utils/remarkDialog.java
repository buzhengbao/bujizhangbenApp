package com.hui.tallybook.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hui.tallybook.R;

public class remarkDialog extends Dialog implements View.OnClickListener {

    EditText editText;
    Button cancelButton, ensureButton;
    OnEnsureListener OnEnsureListener;

    //设定回调接口的方法
    public void setOnEnsureListener(remarkDialog.OnEnsureListener onEnsureListener) {
        OnEnsureListener = onEnsureListener;
    }

    public remarkDialog(@NonNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remark);//设置对话框显示
        editText = findViewById(R.id.dialog_remark_et);
        cancelButton = findViewById(R.id.dialog_remark_btn_cancel);
        ensureButton = findViewById(R.id.dialog_remark_btn_ensure);
        cancelButton.setOnClickListener(this);
        ensureButton.setOnClickListener(this);
    }

    public interface OnEnsureListener{
        public void onEnsure();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_remark_btn_cancel){
            cancel();
        }else{
            if (OnEnsureListener!= null){
                editText.clearFocus();
                OnEnsureListener.onEnsure();
            }
        }
    }

    //获取输入数据的方法
    public String getRemark(){
        return editText.getText().toString().trim();
    }
    //设置dialog的尺寸和屏幕尺寸一致
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
        handler.sendEmptyMessageDelayed(1,500);
    }
    //自动打开软键盘
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
