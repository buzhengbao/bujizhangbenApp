package com.hui.tallybook.frag_record;

import android.icu.util.Calendar;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hui.tallybook.R;
import com.hui.tallybook.db.AccountBean;
import com.hui.tallybook.db.TypeBean;
import com.hui.tallybook.utils.KeyBoardUtils;
import com.hui.tallybook.utils.SelectTimeDialog;
import com.hui.tallybook.utils.remarkDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//记录页面当中的支出模块
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener{

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView type_imageView;
    TextView type_textView, remark_textView, time_textView;
    GridView type_gridView ;
    List<TypeBean> typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean; //将需要插入到记账本当中的数据保存对象中

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean(); //创建对象
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.other_selected);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        //给Grid View填充数据的方法
        loadDataToGridView();
        //设置Grid View每一项的点击事件
        setGridViewItemClick();
        return view;
    }
//获取当前时间，显示在timeTv上
    private void setInitTime() {
        Date date = new Date();
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String s = sdf.format(date);
        time_textView.setText(s);
        accountBean.setTime(s);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);

    }

    //设置Grid View每一个点击事件
    private void setGridViewItemClick() {
        type_gridView.setOnItemClickListener((parent, view, position, id) -> {
            adapter.selectPos = position;
            adapter.notifyDataSetChanged();//提示绘制发生变化了
            TypeBean typeBean = typeList.get(position);
            String typename = typeBean.getTypename();
            type_textView.setText(typename);
            accountBean.setTypename(typename);
            int simageId = typeBean.getSimageId();
            type_imageView.setImageResource(simageId);
            accountBean.setsImageId(simageId);

        });
    }

     public void loadDataToGridView() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        type_gridView.setAdapter(adapter);

    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        type_imageView = view.findViewById(R.id.frag_record_iv);
        type_gridView = view.findViewById(R.id.frag_record_gv);
        type_textView = view.findViewById(R.id.frag_record_tv_type);
        remark_textView = view.findViewById(R.id.frag_record_tv_remark);
        time_textView = view.findViewById(R.id.frag_record_tv_time);
        remark_textView.setOnClickListener(this);
        time_textView.setOnClickListener(this);
        //让自定义软键盘显示出来
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        //设置接口，监听确定按钮按钮被点击了
        boardUtils.setOnEnsureListener(() -> {
            //获取输入钱数
            String moneyStr = moneyEt.getText().toString();
            if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {
                getActivity().finish();
                return;
            }
            float money = Float.parseFloat(moneyStr);
            accountBean.setMoney(money);
            //获取记录的信息，保存在数据库当中
            saveAccountToDB();
            // 返回上一级页面
            getActivity().finish();
        });
    }

    //让子类一定要重写这个代码
    public abstract void saveAccountToDB() ;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.frag_record_tv_time) {
            showTimeDialog();
        }else {
            showRemarkDialog();
        }
    }

    /* 弹出显示时间的对话框*/
    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        //设定确定按钮被点击了的监听器
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                time_textView.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }


    //弹出备注对话框
    private void showRemarkDialog() {
        remarkDialog remarkDialog = new remarkDialog(getContext());
        remarkDialog.show();
        remarkDialog.setDialogSize();
        remarkDialog.setOnEnsureListener(() -> {
            String remark = remarkDialog.getRemark();
            if (!TextUtils.isEmpty(remark)){
                remark_textView.setText(remark);
                accountBean.setRemark(remark);
            }
            remarkDialog.cancel();
        });
    }
}