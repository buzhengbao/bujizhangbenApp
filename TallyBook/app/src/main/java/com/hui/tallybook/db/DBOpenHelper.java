package com.hui.tallybook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hui.tallybook.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context, "tally.db", null, 1);
    }

    //创建数据库的方法，只有项目第一次运行时，会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表示类型的表
        String sql = "create table typetb( id integer primary key autoincrement, typename varchar(10), imageId integer, sImageId integer, kind integer)";
        db.execSQL(sql);
        insertType(db);
        //创建记账表
        sql = "create table accounttb(id integer primary key autoincrement,typename varchar(10),sImageId integer,remark varchar(80),money float," +
                "time varchar(60),year integer,month integer,day integer,kind integer)";
        db.execSQL(sql);
    }

    private void insertType(SQLiteDatabase db) {
        //向表内插入元素
        String sq1 = "insert into typetb(typename, imageId, sImageId, kind) values(?,?,?,?)";
        db.execSQL(sq1,new Object[]{"餐饮", R.mipmap.food , R.mipmap.out_food, 0});
        db.execSQL(sq1,new Object[]{"衣服", R.mipmap.dress , R.mipmap.out_dress, 0});
        db.execSQL(sq1,new Object[]{"房租", R.mipmap.live , R.mipmap.out_live, 0});
        db.execSQL(sq1,new Object[]{"交通", R.mipmap.travel , R.mipmap.out_travel, 0});
        db.execSQL(sq1,new Object[]{"教育", R.mipmap.education , R.mipmap.out_education, 0});
        db.execSQL(sq1,new Object[]{"宠物", R.mipmap.pet , R.mipmap.out_pet, 0});
        db.execSQL(sq1,new Object[]{"娱乐", R.mipmap.entertainment , R.mipmap.out_entertainment, 0});
        db.execSQL(sq1,new Object[]{"还款", R.mipmap.repayment , R.mipmap.out_repayment, 0});
        db.execSQL(sq1,new Object[]{"医疗", R.mipmap.medical , R.mipmap.out_medical, 0});
        db.execSQL(sq1,new Object[]{"其他", R.mipmap.other , R.mipmap.out_other, 0});

        db.execSQL(sq1,new Object[]{"工资", R.mipmap.wages , R.mipmap.in_wages, 1});
        db.execSQL(sq1,new Object[]{"二手交易", R.mipmap.transaction , R.mipmap.in_transaction, 1});
        db.execSQL(sq1,new Object[]{"理财", R.mipmap.managemoney_matters , R.mipmap.in_managemoney_matters, 1});
        db.execSQL(sq1,new Object[]{"奖金", R.mipmap.bonus , R.mipmap.in_bonus, 1});
        db.execSQL(sq1,new Object[]{"兼职", R.mipmap.part_time_job , R.mipmap.in_parttime_job, 1});
        db.execSQL(sq1,new Object[]{"津贴", R.mipmap.allowance , R.mipmap.in_allowance, 1});
        db.execSQL(sq1,new Object[]{"其他", R.mipmap.other , R.mipmap.in_other, 1});
    }

    //数据库版本升级的方法，每次更新数据库版本时，都会被调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
