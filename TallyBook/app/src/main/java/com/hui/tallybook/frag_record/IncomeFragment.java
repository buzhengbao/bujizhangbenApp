package com.hui.tallybook.frag_record;

import com.hui.tallybook.R;
import com.hui.tallybook.db.DBManager;
import com.hui.tallybook.db.TypeBean;

import java.util.List;


public class IncomeFragment extends BaseRecordFragment {


    //重写
    @Override
    public void loadDataToGridView() {
        super.loadDataToGridView();
        //获取数据库当中的数据源
        List<TypeBean> inlist = DBManager.getTypeList(1);
        typeList.addAll(inlist);
        adapter.notifyDataSetChanged();
        type_textView.setText("工资");
        type_imageView.setImageResource(R.mipmap.in_wages);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertItemTOAccount(accountBean);
    }
}
