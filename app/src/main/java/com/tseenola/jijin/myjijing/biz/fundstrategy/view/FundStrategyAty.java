package com.tseenola.jijin.myjijing.biz.fundstrategy.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.fundstrategy.presenter.FundStrategyPrt;
import com.tseenola.jijin.myjijing.utils.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/6/7.
 * 描述：
 */

public class FundStrategyAty extends BaseAty implements IFundStrategyAty {
    @Bind(R.id.bt_UpdatePureData)
    Button mBtUpdatePureData;
    @Bind(R.id.et_DayRange)
    EditText mEtDayRange;
    @Bind(R.id.bt_GetBestFundList)
    Button mBtGetBestFundList;
    private FundStrategyPrt mFundStrategyPrt;

    @Override
    public void bindPresenter() {
        mFundStrategyPrt = new FundStrategyPrt(this);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_fund_stratety);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.bt_UpdatePureData, R.id.bt_GetBestFundList})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_UpdatePureData:
                //1.找到基金列表。
                //2.获取每个基金的histrory,并且保存
                mFundStrategyPrt.loadDatas("", Constant.TaskName.DOWN_All_FUNDS_HISTORY);
                break;
            case R.id.bt_GetBestFundList:
                break;
            default:
                break;
        }
    }
}
