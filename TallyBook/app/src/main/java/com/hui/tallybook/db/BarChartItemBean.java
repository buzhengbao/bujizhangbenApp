package com.hui.tallybook.db;

//用于描述绘制柱状图是每个柱子描述的对象
public class BarChartItemBean {
    int year;
    int month;
    int day;
    float money;

    public BarChartItemBean(int year, int month, int day, float money) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.money = money;
    }

    public BarChartItemBean() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

}
