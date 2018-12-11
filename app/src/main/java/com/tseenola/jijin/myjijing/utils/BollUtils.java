package com.tseenola.jijin.myjijing.utils;

import java.util.List;

/**
 * Created by lenovo on 2018/8/12.
 * 描述：计算boll线
 */

public class BollUtils {
    /**
     * 获取标准差
     */
    public double getStandardDiviation(List<Double> pInputData) {
        double result;
        //绝对值化很重要
        result = Math.sqrt(Math.abs(getVariance(pInputData)));
        return result;
    }


    /**
     * 求给定双精度数组中值的方差
     *
     * @param pInputData
     *            输入数据数组
     * @return 运算结果
     */
    public double getVariance(List<Double> pInputData) {
        int count = getCount(pInputData);
        double sqrsum = getSquareSum(pInputData);
        double average = getAverage(pInputData);
        double result;
        result = (sqrsum - count * average * average) / count;

        return result;
    }

    /**
     * 求给定双精度数组中值的数目
     *
     * @param pInputData
     *            Data 输入数据数组
     * @return 运算结果
     */
    public int getCount(List<Double> pInputData) {
        if (pInputData == null) {
            return -1;
        }

        return pInputData.size();
    }

    /**
     * 求给定双精度数组中值的平方和
     *
     * @param pInputData
     *            输入数据数组
     * @return 运算结果
     */
    public double getSquareSum(List<Double> pInputData) {
        if(pInputData==null||pInputData.size()==0) {
            return -1;
        }
        int len=pInputData.size();
        double sqrsum = 0.0;
        for (int i = 0; i <len; i++) {
            sqrsum = sqrsum + pInputData.get(i) * pInputData.get(i);
        }
        return sqrsum;
    }

    /**
     * 求给定双精度数组中值的平均值
     *
     * @param pInputData
     *            输入数据数组
     * @return 运算结果
     */
    public double getAverage(List<Double> pInputData) {
        if (pInputData == null || pInputData.size() == 0) {
            return -1;
        }
        int len = pInputData.size();
        double result;
        result = getSum(pInputData) / len;

        return result;
    }

    /**
     * 求给定双精度数组中值的和
     *
     * @param pInputData
     *            输入数据数组
     * @return 运算结果
     */
    public double getSum(List<Double> pInputData) {
        if (pInputData == null || pInputData.size() == 0) {
            return -1;
        }
        int len = pInputData.size();
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum = sum + pInputData.get(i);
        }

        return sum;
    }
}
