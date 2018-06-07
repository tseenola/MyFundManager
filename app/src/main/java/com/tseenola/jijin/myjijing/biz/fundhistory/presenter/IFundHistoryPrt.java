package com.tseenola.jijin.myjijing.biz.fundhistory.presenter;

import com.tseenola.jijin.myjijing.base.presenter.IBasePrt;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;

import java.util.List;

/**
 * Created by lenovo on 2018/6/1.
 * 描述：
 */

public interface IFundHistoryPrt extends IBasePrt{
    void saveFundInfo(FundInfo pFundInfos);
}
