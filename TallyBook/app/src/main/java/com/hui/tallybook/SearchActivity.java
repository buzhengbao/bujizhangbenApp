package com.hui.tallybook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hui.tallybook.adapter.AccountAdapter;
import com.hui.tallybook.db.AccountBean;
import com.hui.tallybook.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;
    List<AccountBean>mDatas;//数据源
    AccountAdapter adapter;//适配器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this,mDatas);
        searchLv.setAdapter(adapter);
        searchLv.setEmptyView(emptyTv); //设置无数据时，显示的控件
    }

    private void initView() {
        searchLv = findViewById(R.id.search_lv);
        searchEt = findViewById(R.id.search_et);
        emptyTv = findViewById(R.id.search_tv_no_result);

    }

    public void onClick(View view) {
        if(view.getId() == R.id.search_iv_back){
            finish();
        }
        else if(view.getId() == R.id.search_iv_search){ //执行搜索
            String msg = searchEt.getText().toString().trim();
            if(TextUtils.isEmpty(msg)){
                Toast.makeText(this,"请输入搜索内容", Toast.LENGTH_SHORT).show();
                return;
            }
            //开始搜索
            List<AccountBean> list = DBManager.getAccountListFromAccountByRemark(msg);
            mDatas.clear();
            mDatas.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }
}