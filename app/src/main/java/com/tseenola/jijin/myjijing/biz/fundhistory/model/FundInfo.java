package com.tseenola.jijin.myjijing.biz.fundhistory.model;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by lenovo on 2018/6/1.
 * 描述：
 */

public class FundInfo extends DataSupport{
    private String fS_name;
    private String fS_code;
    private String fund_Rate;
    private String Data_netWorthTrend;
    private String fund_minsg;

    public String getfS_name() {
        return fS_name;
    }

    public void setfS_name(String pFS_name) {
        fS_name = pFS_name;
    }

    public String getfS_code() {
        return fS_code;
    }

    public void setfS_code(String pFS_code) {
        fS_code = pFS_code;
    }

    public String getFund_Rate() {
        return fund_Rate;
    }

    public void setFund_Rate(String pFund_Rate) {
        fund_Rate = pFund_Rate;
    }

    public String getData_netWorthTrend() {
        return Data_netWorthTrend;
    }

    public void setData_netWorthTrend(String pData_netWorthTrend) {
        Data_netWorthTrend = pData_netWorthTrend;
    }

    public String getFund_minsg() {
        return fund_minsg;
    }

    public void setFund_minsg(String pFund_minsg) {
        fund_minsg = pFund_minsg;
    }
}
