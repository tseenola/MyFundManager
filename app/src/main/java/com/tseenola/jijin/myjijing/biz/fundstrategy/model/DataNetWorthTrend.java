package com.tseenola.jijin.myjijing.biz.fundstrategy.model;

import java.util.List;

/**
 * Created by lenovo on 2018/6/16.
 * 描述：
 */

public class DataNetWorthTrend {

    /**
     * x : 1008604800000
     * y : 1
     * equityReturn : 0
     * unitMoney :
     */

    private List<DataNetWorthTrendBean> DataNetWorthTrend;

    public List<DataNetWorthTrendBean> getDataNetWorthTrend() {
        return DataNetWorthTrend;
    }

    public void setDataNetWorthTrend(List<DataNetWorthTrendBean> DataNetWorthTrend) {
        this.DataNetWorthTrend = DataNetWorthTrend;
    }

    public static class DataNetWorthTrendBean {
        private long x;
        private double y;
        private double equityReturn;

        public long getX() {
            return x;
        }

        public void setX(long x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(long y) {
            this.y = y;
        }

        public double getEquityReturn() {
            return equityReturn;
        }

        public void setEquityReturn(double equityReturn) {
            this.equityReturn = equityReturn;
        }
    }
}
