package com.hui.tallybook.db;

public class ChartItemBean {
    int sImages;
    String type;
    float ratio;
    float totalMoney;

    public ChartItemBean() {
    }

    public int getsImages() {
        return sImages;
    }

    public void setsImages(int sImages) {
        this.sImages = sImages;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public ChartItemBean(int sImages, String type, float ratio, float totalMoney) {
        this.sImages = sImages;
        this.type = type;
        this.ratio = ratio;
        this.totalMoney = totalMoney;
    }
}
