package com.tseenola.jijin.myjijing.biz.fundlist.model;

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

}
