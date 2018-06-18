package com.tseenola.jijin.myjijing.biz.fundstrategy.presenter;

import com.tseenola.jijin.myjijing.base.presenter.IBasePrt;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;

/**
 * Created by lenovo on 2018/6/7.
 * 描述：
 */

public interface IFundStrategyPrt extends IBasePrt{


    void showHistryByChart(FundInfo pFundInfo);
}
