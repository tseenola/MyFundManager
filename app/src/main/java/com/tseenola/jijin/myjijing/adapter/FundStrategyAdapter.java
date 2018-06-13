package com.tseenola.jijin.myjijing.adapter;

import android.content.Context;

import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;

import java.util.List;

/**
 * Created by lenovo on 2018/6/13.
 * 描述：
 */

public class FundStrategyAdapter extends FundListAdapter{
    public FundStrategyAdapter(Context context, List<FundListInfo> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    protected void upDateDB(FundListInfo pFundInfo, boolean pB) {
    }
}
