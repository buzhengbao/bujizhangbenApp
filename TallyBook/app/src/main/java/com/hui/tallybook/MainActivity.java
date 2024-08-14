package com.hui.tallybook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hui.tallybook.adapter.AccountAdapter;
import com.hui.tallybook.db.AccountBean;
import com.hui.tallybook.db.DBManager;
import com.hui.tallybook.utils.BudgetDialog;
import com.hui.tallybook.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ListView todayLv; // 今日记录
    ImageView searchIv;
    ImageButton editBtn, moreBtn;
    //申明数据源
    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int year, month, day;
    //头布局相关控件
    View headerView;
    TextView topOutTv, topInTv, topbudgetTv, topConTv, topbudgetTv1;
    ImageView topShowIv;
    SharedPreferences preferences;
    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        mDatas = new ArrayList<>();
        //添加List View的头布局
        addLvHeaderView();
        //设置适配器,加载每一行数据到表当中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }

    //初始化自带View方法

    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLvLongClickListener();
    }

    //长按事件
    private void setLvLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false;
                }
                int pos = position - 1;
                AccountBean clickBean = mDatas.get(pos);
                //弹出用户是否删除的对话框
                showDeleteDialog(clickBean);

                return false;
            }
        });
    }

    //弹出是否删除每一条记录的对话框
    private void showDeleteDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除").setMessage("确定要删除这条记录吗？").setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int clickId = clickBean.getId();
                        //执行删除操作
                        DBManager.deleteItemFromAccounttbById(clickId);
                        mDatas.remove(clickBean); //实时刷新，移除集合中的对象
                        adapter.notifyDataSetChanged(); //提示适配器更新数据
                        setTopTvShow(); //改变头布局内容
                    }
                });
        builder.create().show();


    }

    //添加头布局的方法
    private void addLvHeaderView() {
        //得到头布局
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头部控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topbudgetTv1 = headerView.findViewById(R.id.item_mainlv_top_tv3);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbudgetTv.setOnClickListener(this);
        topbudgetTv1.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
    }

    //获取今日的具体时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }

    //设置头布局当中文本内容的显示
    private void setTopTvShow() {
        //获取今日收支总金额
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "本日支出：" + outcomeOneDay + "  收入：" + incomeOneDay;
        topConTv.setText(infoOneDay);
        //获取本月收支总金额
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("￥" + incomeOneMonth);
        topOutTv.setText("￥" + outcomeOneMonth);

        //设置显示预算剩余
    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccount(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }




    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.main_iv_search){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.main_edit){
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.main_btn_more){
            MoreDialog moreDialog = new MoreDialog(this);
            moreDialog.show();
            moreDialog.setDialogSize();
        }
        else if(v.getId() == R.id.item_mainlv_top_tv_budget){
            showBudgetDialog();
        }
        else if(v.getId() == R.id.item_mainlv_top_tv3){
            showBudgetDialog();
        }
        else if(v.getId() == R.id.item_mainlv_top_iv_hide){
            //切换TextView的显示与隐藏
            toggleShow();
        }
        if(v == headerView){
            //头布局被点击
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }

    //显示预算对话框
    private void showBudgetDialog() {
        BudgetDialog budgetDialog = new BudgetDialog(this);
        budgetDialog.show();
        budgetDialog.setDialogSize();
        budgetDialog.setOnEnsureListener(money -> {
            //将预算金额写入共享参数中，进行存储
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat("budget", money);
            editor.commit();

            //计算剩余金额
            float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
            float syMonet = money - outcomeOneMonth;
            topbudgetTv.setText("￥" + syMonet);
        });
    }

    Boolean isShow = true;
    //点击头布局时，切换TextView的显示与隐藏
    private void toggleShow() {
        if (isShow) {
            PasswordTransformationMethod passwordTransformationMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordTransformationMethod);
            topOutTv.setTransformationMethod(passwordTransformationMethod);
            topbudgetTv.setTransformationMethod(passwordTransformationMethod);
            topShowIv.setImageResource(R.mipmap.hide);
            isShow = false;
        }else{
            HideReturnsTransformationMethod instance = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(instance);
            topOutTv.setTransformationMethod(instance);
            topbudgetTv.setTransformationMethod(instance);
            topShowIv.setImageResource(R.mipmap.show);
            isShow = true;
        }
    }
}