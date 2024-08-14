package com.hui.tallybook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.hui.tallybook.utils.FloatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责管理数据库的类
 * 主要对于表当中的内容进行操作，增删改查
 */
public class DBManager {
    private static SQLiteDatabase db;
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context); //得到帮助类对象
        db = helper.getWritableDatabase();//得到数据库对象
    }

    /**
     * 读取数据库当中的数据，写入内存集合里
     * kind：表示收入或者支出*
     * */
    public static List<TypeBean> getTypeList(int kind){
        List<TypeBean> list = new ArrayList<>();
        //读取数据库当中的数据
        String sql = "select * from typetb where kind = "+kind;
        Cursor cursor = db.rawQuery(sql,null);
        //循环读取游标内容，存储到对象当中
        while (cursor.moveToNext()){
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            TypeBean typeBean = new TypeBean(id,typename,imageId,sImageId,kind);
            list.add(typeBean);
            Log.d("qwe", String.valueOf(typeBean));
        }
        return list;
    }

    //向记账表当中插入一条元素
    public static void insertItemTOAccount(AccountBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("sImageId",bean.getsImageId());
        values.put("remark",bean.getRemark());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        db.insert("accounttb", null, values);
    }
    /**
     * 获取记账表当中某一天的所有数据
     * */
    public static List<AccountBean> getAccountListOneDayFromAccount(int year,int month,int day){
        Log.d("123", "getAccountListOneDayFromAccount: 成功");
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            String remark = cursor.getString(cursor.getColumnIndexOrThrow("remark"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            AccountBean accountBean = new AccountBean(id,typename,sImageId,remark,money,time,year,month,day,kind);
            list.add(accountBean);

        }
        return list;

    }

    /**
     * 获取某一天的支或者收入的总金额 Kind：支出为0，收入为1
     * */
    public static float getSumMoneyOneDay(int year,int month,int day,int kind){
        Log.d("123", "获取日收支: 成功");
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        if(cursor.moveToFirst()){
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }


    /**
     * 获取某一月的支或者收入的总金额 Kind：支出为0，收入为1
     * */
    public static float getSumMoneyOneMonth(int year,int month,int kind){
        Log.d("123", "获取月收支: 成功");
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if(cursor.moveToFirst()){
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }

    /**
     * 统计某月支出或者收入情况有多少条 收入：1 支出：0
     * */

    public static  int getCountItemOneMonth(int year, int month, int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql,new String[]{year + "", month + "", kind + ""});
        if(cursor.moveToFirst()){
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count(money)"));
            total = count;
        };
        return total;
    }

    /**
     * 获取某一年的支或者收入的总金额 Kind：支出为0，收入为1
     * */
    public static float getSumMoneyOneYear(int year,int kind){
        Log.d("123", "获取年收支: 成功");
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        if(cursor.moveToFirst()){
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }

    /**
     * 根据传入的Id，删除accounttb中的一条数据
     * */
    public static int deleteItemFromAccounttbById(int id){
        int i = db.delete("accounttb", "id =?", new String[]{id + ""});
        return i;
    }

    /**
     * 根据备注搜索收支数据
     * */
    public static List<AccountBean> getAccountListFromAccountByRemark(String remark){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where remark like '%"+remark+"%'";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String re = cursor.getString(cursor.getColumnIndexOrThrow("remark"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean accountBean = new AccountBean(id,typename,sImageId,re,money,time,year,month,day,kind);
            list.add(accountBean);
        }
        return list;
    }

    /**
     * 获取记账表当中某一月的所有数据
     * */
    public static List<AccountBean> getAccountListOneMonthFromAccount(int year,int month){
        Log.d("123", "getAccountListOneDayFromAccount: 成功");
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            String remark = cursor.getString(cursor.getColumnIndexOrThrow("remark"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean accountBean = new AccountBean(id,typename,sImageId,remark,money,time,year,month,day,kind);
            list.add(accountBean);

        }
        return list;

    }

    /**
     * 查询记账表中有几个年份信息
     */
    public static List<Integer>getYearListFromAccounttb(){
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            list.add(year);
        }
        return list;
    }

    /**
     * 删除accounttb表格当中的所有数据
     * */
    public static void deleteAllAccounttb(){
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }

    /**
     * 查询指定年份月份的收支的类型总钱数
     * */
    public static List<ChartItemBean> getChartItemBeanListFromAccounttb(int year,int month, int kind){
        List<ChartItemBean> list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);//求出收支总数
        String sql = "select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while(cursor.moveToNext()){
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            float total = cursor.getFloat(cursor.getColumnIndexOrThrow("total"));
            //计算比例
            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            ChartItemBean chartItemBean = new ChartItemBean(sImageId,typename,ratio,total);
            list.add(chartItemBean);
        }

        return list;
    }



    //获取这个月某一天收入支出的最大值
    public static float getMaxMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToNext()){
            float maxMoney = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            return maxMoney;
        }

        return 0;
    }

    //根据指定月份获取每日收入或者支出的总钱数的集合
    public static List<BarChartItemBean> getSumMoneyOneDayInMonth(int year,int month, int kind){
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        List<BarChartItemBean>list = new ArrayList<>();
        while (cursor.moveToNext()){
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            float sumMoney = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            BarChartItemBean barChartItemBean = new BarChartItemBean(year, month, day,sumMoney);
            list.add(barChartItemBean);
        }
        return list;
    }

}
