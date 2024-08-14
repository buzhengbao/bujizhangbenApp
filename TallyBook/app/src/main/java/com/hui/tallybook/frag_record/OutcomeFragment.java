package com.hui.tallybook.frag_record;

import android.util.Log;

import com.hui.tallybook.R;
import com.hui.tallybook.db.DBManager;
import com.hui.tallybook.db.TypeBean;

import java.util.List;


public class OutcomeFragment extends BaseRecordFragment {


    //重写
    @Override
    public void loadDataToGridView() {
        super.loadDataToGridView();
        //获取数据库当中的数据源
        List<TypeBean> inlist = DBManager.getTypeList(0);
        typeList.addAll(inlist);
        adapter.notifyDataSetChanged();
        type_textView.setText("餐饮");
        type_imageView.setImageResource(R.mipmap.out_food);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertItemTOAccount(accountBean);
    }
}
