package com.tseenola.jijin.myjijing.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.tseenola.jijin.myjijing.R;
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
    public void convert(CommonViewHolder pCommonViewHolder, FundListInfo pFundInfo, int pPosition) {
        super.convert(pCommonViewHolder, pFundInfo, pPosition);
        if (!TextUtils.isEmpty(pFundInfo.getFundRate())){
            pCommonViewHolder.setVisible(R.id.tv_FundRate, View.VISIBLE);
            pCommonViewHolder.setText(R.id.tv_FundRate,pFundInfo.getFundRate());
        }
    }

    @Override
    protected void upDateDB(FundListInfo pFundInfo, boolean pB) {
    }
}
