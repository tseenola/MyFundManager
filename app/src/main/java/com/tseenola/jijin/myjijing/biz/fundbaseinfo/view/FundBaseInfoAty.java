package com.tseenola.jijin.myjijing.biz.fundbaseinfo.view;


import android.widget.TextView;
import android.widget.Toast;

import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.fundbaseinfo.model.FundBaseInfo;
import com.tseenola.jijin.myjijing.biz.fundbaseinfo.presenter.FundBaseInfoPrt;
import com.tseenola.jijin.myjijing.utils.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2018/5/31.
 * 描述：
 */

public class FundBaseInfoAty extends BaseAty {
    @Bind(R.id.tv_FundCode)
    TextView mTvFundCode;
    @Bind(R.id.tv_FundName)
    TextView mTvFundName;
    @Bind(R.id.tv_LastWorkingDay)
    TextView mTvLastWorkingDay;
    @Bind(R.id.tv_LastDayFundVal)
    TextView mTvLastDayFundVal;
    @Bind(R.id.tv_CurrentFundVal)
    TextView mTvCurrentFundVal;
    @Bind(R.id.tv_CurrentFundRFRange)
    TextView mTvCurrentFundRFRange;
    private String mFundCode;
    private FundBaseInfoPrt mFundDetailPresenter;

    @Override
    public void bindPresenter() {
        mFundDetailPresenter = new FundBaseInfoPrt(this);
    }

    @Override
    public void initData() {
        onStart(null);
        mFundCode = getIntent().getStringExtra("fundCode");
        mFundDetailPresenter.loadDatas(mFundCode, Constant.TaskName.DOWN_FUND_BASE);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_fund_detail);
        ButterKnife.bind(this);
    }

    @Override
    public void onLoadDatasSucc(Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        FundBaseInfo lFundDetail = (FundBaseInfo) pO;
        mTvFundCode.setText(lFundDetail.getFundCode());
        mTvFundName.setText(lFundDetail.getFundName());
        mTvLastWorkingDay.setText(lFundDetail.getLastWorkingDay());
        mTvLastDayFundVal.setText(lFundDetail.getLastDayFundVal());
        mTvCurrentFundVal.setText(lFundDetail.getCurrentFundVal());
        mTvCurrentFundRFRange.setText(lFundDetail.getCurrentFundRFRange());
        super.onLoadDatasSucc(pO,pDataSource);
    }

    @Override
    public void onLoadDataFail(Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        super.onLoadDataFail(pO,pDataSource);
    }
}
