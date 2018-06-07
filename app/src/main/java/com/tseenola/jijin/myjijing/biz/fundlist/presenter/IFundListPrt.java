package com.tseenola.jijin.myjijing.biz.fundlist.presenter;

import com.tseenola.jijin.myjijing.base.presenter.IBasePrt;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;

import java.util.List;

/**
 * Created by lenovo on 2018/5/30.
 * 描述：
 */

public interface IFundListPrt extends IBasePrt {
    void saveFundList(List<FundListInfo> pFundInfos);
}
