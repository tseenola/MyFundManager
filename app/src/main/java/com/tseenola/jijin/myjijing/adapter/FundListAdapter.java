package com.tseenola.jijin.myjijing.adapter;

import android.content.Context;

import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;

import java.util.List;

/**
 * Created by lenovo on 2018/5/30.
 * 描述：
 */

public class FundListAdapter extends CommonAdapter<FundListInfo>{

    public FundListAdapter(Context context, List<FundListInfo> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(CommonViewHolder pCommonViewHolder, FundListInfo pFundInfo, int pPosition) {
        pCommonViewHolder.setText(R.id.tv_FundCode,pFundInfo.getFundCode());
        pCommonViewHolder.setText(R.id.tv_FundAbbr,pFundInfo.getFundAbbr());
        pCommonViewHolder.setText(R.id.tv_FundName,pFundInfo.getFundName());
        pCommonViewHolder.setText(R.id.tv_FundType,pFundInfo.getFundType());
    }
}
