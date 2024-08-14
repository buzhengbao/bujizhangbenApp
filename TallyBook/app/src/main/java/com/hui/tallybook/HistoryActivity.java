package com.hui.tallybook;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hui.tallybook.adapter.AccountAdapter;
import com.hui.tallybook.db.AccountBean;
import com.hui.tallybook.db.DBManager;
import com.hui.tallybook.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView historyLv;
    TextView timeTv;
    List<AccountBean>mDatas;

    AccountAdapter adapter;
    int year,month;

    int dialogSelPos = -1;
    int dialogSelMonth = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);
        mDatas = new ArrayList<>();
        //设置适配器
        adapter = new AccountAdapter(this,mDatas);
        historyLv.setAdapter(adapter);
        initTime();
        timeTv.setText(year + "年" + month + "月");
        loadData(year,month);
        setVClickListener();
    }

    //设置List View的长按事件
    private void setVClickListener() {
        historyLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AccountBean accountBean = mDatas.get(position);
                deleteItem(accountBean);
                return false;
            }
        });
    }

    private void deleteItem(AccountBean accountBean) {
        int delId = accountBean.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("你确定要删除这条记录吗").setNegativeButton("取消",null)
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteItemFromAccounttbById(delId);
                        mDatas.remove(accountBean);
                        adapter.notifyDataSetChanged();
                    }});
        builder.create().show();
    }

    //获取指定年份月份的列表
    private void loadData(int year, int month) {
        List<AccountBean> accountListOneMonthFromAccount = DBManager.getAccountListOneMonthFromAccount(year, month);
        mDatas.clear();
        mDatas.addAll(accountListOneMonthFromAccount);
        adapter.notifyDataSetChanged();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    public void onClick(View view) {
        if(view.getId() == R.id.history_iv_back){
            finish();
        }
        else if(view.getId() == R.id.history_iv_rili){
            CalendarDialog dialog = new CalendarDialog(this,dialogSelPos,dialogSelMonth);
            dialog.show();
            dialog.setDialogSize();
            dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                @Override
                public void onRefresh(int selPos, int year, int month) {
                    timeTv.setText(year+ "年" + month + "月");
                    loadData(year,month);
                    dialogSelPos = selPos;
                    dialogSelMonth = month;
                }
            });
        }
    }
}