package com.hui.tallybook.utils;

import java.math.BigDecimal;

public class FloatUtils {

    //进行除法运算保留四个小数
    public static float div(float v1,float v2){
        float v3 = v1 / v2;
        BigDecimal bl = new BigDecimal(v3);
        float val = bl.setScale(4, 4).floatValue();
        return val;
    }

    //百分比转化
    public static String ratioToPercent(float val){
        float v = val * 100;
        BigDecimal bl = new BigDecimal(v);
        float v1 = bl.setScale(2, 4).floatValue();
        String per = v1 + "%";
        return per;
    }
}
