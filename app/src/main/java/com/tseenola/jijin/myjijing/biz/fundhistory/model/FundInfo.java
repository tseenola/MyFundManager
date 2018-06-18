package com.tseenola.jijin.myjijing.biz.fundhistory.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/6/1.
 * 描述：
 */

public class FundInfo extends DataSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fSName;
    private String fSCode;
    private String fundRate;
    private String DataNetWorthTrend;
    private String fundMinsg;
    private String DataGrandTotal;

    public String getfSName() {
        return fSName;
    }

    public void setfSName(String pFSName) {
        fSName = pFSName;
    }

    public String getfSCode() {
        return fSCode;
    }

    public void setfSCode(String pFSCode) {
        fSCode = pFSCode;
    }

    public String getFundRate() {
        return fundRate;
    }

    public void setFundRate(String pFundRate) {
        fundRate = pFundRate;
    }

    public String getDataNetWorthTrend() {
        return DataNetWorthTrend;
    }

    public void setDataNetWorthTrend(String pDataNetWorthTrend) {
        DataNetWorthTrend = pDataNetWorthTrend;
    }

    public String getFundMinsg() {
        return fundMinsg;
    }

    public void setFundMinsg(String pFundMinsg) {
        fundMinsg = pFundMinsg;
    }

    public String getDataGrandTotal() {
        return DataGrandTotal;
    }

    public void setDataGrandTotal(String pDataGrandTotal) {
        DataGrandTotal = pDataGrandTotal;
    }
}
