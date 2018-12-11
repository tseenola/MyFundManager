package com.tseenola.jijin.myjijing.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 2018/12/11.
 * 描述：计算WR指标
 * 公司：W%R=（Hn—C）÷（Hn—Ln）×100
 * Hn 代表过去n天收盘价的最高价
 * C 代表今天收盘价
 * Ln 代表过去n天收盘价最低价
 *
 * WR：指标反映了当前价格在最高价和最低价之间所处的位置
 * 当WR低于20表示超买，应该卖出
 * 当WR高于80表示超卖，应该买入
 */
public class WRUtils {
    /**
     *
     * @param pCloseValList
     * @param pRange 范围一般为 14天 即 14；
     * @return
     */
    public static double getWR(List<Double> pCloseValList,int pRange){
        if (pCloseValList == null || pCloseValList.size()<=0 || pRange<=0) {
            throw new IllegalArgumentException("参数合法 WRUtils getWR()");
        }
        double wr = 0d;
        //当日收盘价
        double todayCloseVal = pCloseValList.get(pCloseValList.size()-1);

        // 1.将收盘价复制到临时list,只要 pRange 天的
        int totalCount = pCloseValList.size();
        List<Double> lTemList = new ArrayList<>();
        if (totalCount<=pRange){
            lTemList = pCloseValList.subList(0,totalCount);
        }else {
            lTemList = pCloseValList.subList(totalCount-pRange,totalCount);
        }

        // 2.求出最高价，最低价
        Collections.sort(lTemList);
        double maxClose = lTemList.get(lTemList.size()-1);
        double minClose = lTemList.get(0);

        // 3.计算WR W%R=（Hn—C）÷（Hn—Ln）×100
        wr = (maxClose-todayCloseVal) / (maxClose-minClose) * 100;
        return wr;
    }
}
