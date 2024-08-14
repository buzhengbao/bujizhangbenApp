package com.hui.tallybook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.OutcomeReceiver;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.hui.tallybook.R;
import com.hui.tallybook.adapter.RecordPagerAdapter;
import com.hui.tallybook.frag_record.IncomeFragment;
import com.hui.tallybook.frag_record.BaseRecordFragment;
import com.hui.tallybook.frag_record.OutcomeFragment;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //1、查找控件
            tabLayout = findViewById(R.id.record_tab_layout);
            viewPager = findViewById(R.id.record_view_pager);
        //2、设置Viewpager加载页面
        initPager();
    }
    private void initPager() {
        //初始化ViewPager页面集合
        List<Fragment>fragmentList = new ArrayList<>();
        //创建收入和支出页面，放置在Fragment集合中
        OutcomeFragment outFrag = new OutcomeFragment();//支出页面
        IncomeFragment inFrag = new IncomeFragment();//收入页面
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        //创建适配器
        RecordPagerAdapter adapter = new RecordPagerAdapter(getSupportFragmentManager(),fragmentList);
        //设置适配器
        viewPager.setAdapter(adapter);
        //设置TabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClick(View view) {
        if (R.id.record_iv_back == view.getId()){
            finish();
          }
        }

}