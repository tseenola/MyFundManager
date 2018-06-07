package com.tseenola.jijin.myjijing.biz.fundhistory.model;

import java.util.List;

/**
 * Created by lenovo on 2018/6/1.
 * 描述：
 */

public class FundVals{

    /**
     * x : 1430841600000
     * y : 1
     * equityReturn : 0
     * unitMoney :
     */

    private List<ValsBean> Vals;

    public List<ValsBean> getVals() {
        return Vals;
    }

    public void setVals(List<ValsBean> Vals) {
        this.Vals = Vals;
    }

    public static class ValsBean {
        private String x;
        private double y;
        private double equityReturn;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
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
