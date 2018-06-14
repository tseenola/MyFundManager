package com.tseenola.jijin.myjijing.biz.fundlist.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by lenovo on 2018/5/30.
 * 描述：
 */

public class FundListInfo extends DataSupport{
    private String FundCode;
    private String FundAbbr;
    private String FundName;
    private String FundType;
    private String FundPingYing;
    @Column(defaultValue = "0")
    private boolean selected;

    @Column(defaultValue = "1")
    private boolean test1;

    @Column(defaultValue = "1")
    private boolean test2;

    @Column(defaultValue = "1")
    private String test3;
    public String getFundCode() {
        return FundCode;
    }

    public void setFundCode(String pFundCode) {
        FundCode = pFundCode;
    }

    public String getFundAbbr() {
        return FundAbbr;
    }

    public void setFundAbbr(String pFundAbbr) {
        FundAbbr = pFundAbbr;
    }

    public String getFundName() {
        return FundName;
    }

    public void setFundName(String pFundName) {
        FundName = pFundName;
    }

    public String getFundType() {
        return FundType;
    }

    public void setFundType(String pFundType) {
        FundType = pFundType;
    }

    public String getFundPingYing() {
        return FundPingYing;
    }

    public void setFundPingYing(String pFundPingYing) {
        FundPingYing = pFundPingYing;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean pSelected) {
        selected = pSelected;
    }

    public boolean isTest1() {
        return test1;
    }

    public void setTest1(boolean pTest1) {
        test1 = pTest1;
    }

    public boolean isTest2() {
        return test2;
    }

    public void setTest2(boolean pTest2) {
        test2 = pTest2;
    }

    public String getTest3() {
        return test3;
    }

    public void setTest3(String pTest3) {
        test3 = pTest3;
    }
}
