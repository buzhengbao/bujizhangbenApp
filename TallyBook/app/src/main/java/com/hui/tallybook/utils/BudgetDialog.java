package com.hui.tallybook.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hui.tallybook.R;

public class BudgetDialog extends Dialog implements View.OnClickListener{

    ImageView cancelIv;
    Button ensureBtn;
    EditText moneyEt;
    public interface OnEnsureListener {
        public void onEnsure(float money);

    };
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        cancelIv = findViewById(R.id.dialog_budget_iv_error);
        ensureBtn = findViewById(R.id.dialog_budget_btn_ensure);
        moneyEt = findViewById(R.id.dialog_budget_et);
        ensureBtn.setOnClickListener(this);
        cancelIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_budget_iv_error){
            cancel();
        }
        else if (v.getId() == R.id.dialog_budget_btn_ensure){
            //获取输入数据数值
            String data = moneyEt.getText().toString();
            if (TextUtils.isEmpty(data)) {
                Toast.makeText(getContext(), "请输入金额", Toast.LENGTH_SHORT).show();
                return;
            }
            float money = Float.parseFloat(data);
            if (money <= 0) {
                Toast.makeText(getContext(), "金额不能为负数", Toast.LENGTH_SHORT).show();
                return;
            }
            if (onEnsureListener!=null) {
                onEnsureListener.onEnsure(money);
            }
            cancel();
        }
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
