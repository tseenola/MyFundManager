package com.tseenola.jijin.myjijing.biz.fundbaseinfo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2018/5/31.
 * 描述：
 */

public class FundBaseInfo {

    /**
     * fundcode : 000005
     * name : 嘉实增强信用定期债券
     * jzrq : 2018-05-30
     * dwjz : 1.0250
     * gsz : 1.0250
     * gszzl : 0.00
     * gztime : 2018-05-31 10:18
     */

    @SerializedName("fundcode")
    private String fundCode;
    @SerializedName("name")
    private String fundName;
    @SerializedName("jzrq")
    private String lastWorkingDay;
    @SerializedName("dwjz")
    private String lastDayFundVal;
    @SerializedName("gsz")
    private String currentFundVal;
    @SerializedName("gszzl")
    private String currentFundRFRange;
    @SerializedName("gztime")
    private String currentDataTime;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getLastWorkingDay() {
        return lastWorkingDay;
    }

    public void setLastWorkingDay(String lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }

    public String getLastDayFundVal() {
        return lastDayFundVal;
    }

    public void setLastDayFundVal(String lastDayFundVal) {
        this.lastDayFundVal = lastDayFundVal;
    }

    public String getCurrentFundVal() {
        return currentFundVal;
    }

    public void setCurrentFundVal(String todayFundVal) {
        this.currentFundVal = todayFundVal;
    }

    public String getCurrentFundRFRange() {
        return currentFundRFRange;
    }

    public void setCurrentFundRFRange(String todayFundRFRange) {
        this.currentFundRFRange = todayFundRFRange;
    }

    public String getCurrentDataTime() {
        return currentDataTime;
    }

    public void setCurrentDataTime(String dataTime) {
        this.currentDataTime = dataTime;
    }
}
