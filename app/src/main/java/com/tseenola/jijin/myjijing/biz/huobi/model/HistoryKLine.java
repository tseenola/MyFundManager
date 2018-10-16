package com.tseenola.jijin.myjijing.biz.huobi.model;

import java.util.List;

/**
 * Created by lenovo on 2018/10/13.
 * 描述：
 */

public class HistoryKLine {

    private String status;
    private String ch;
    private long ts;
    /**
     * id : 1539360000
     * open : 6341.34
     * close : 6312.67
     * low : 6283.84
     * high : 6343.69
     * amount : 2861.3147654985573
     * vol : 1.8069917721936967E7
     * count : 24478
     */

    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private long id;//K线id
        private double open;//开盘价
        private double close;//收盘价,当K线为最晚的一根时，是最新成交价
        private double low;//最低价
        private double high;//最高价
        private double amount;//成交量
        private double vol;//成交额, 即 sum(每一笔成交价 * 该笔的成交量)
        private long count;//笔

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public double getOpen() {
            return open;
        }

        public void setOpen(double open) {
            this.open = open;
        }

        public double getClose() {
            return close;
        }

        public void setClose(double close) {
            this.close = close;
        }

        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getVol() {
            return vol;
        }

        public void setVol(double vol) {
            this.vol = vol;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
}
