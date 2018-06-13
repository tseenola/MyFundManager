package com.tseenola.jijin.myjijing.biz.fundstrategy.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.adapter.FundStrategyAdapter;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;
import com.tseenola.jijin.myjijing.biz.fundstrategy.presenter.FundStrategyPrt;
import com.tseenola.jijin.myjijing.biz.fundstrategy.presenter.IFundStrategyPrt;
import com.tseenola.jijin.myjijing.utils.Constant;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/6/7.
 * 描述：
 */

public class FundStrategyAty extends BaseAty implements IFundStrategyAty {
    @Bind(R.id.lv_FundList)
    ListView mLvFundList;
    @Bind(R.id.bt_GetStrategy)
    Button mBtGetStrategy;
    private IFundStrategyPrt mFundStrategyPrt;
    private FundStrategyAdapter mFundStrategyAdapter;
    private List<FundListInfo> mFundListInfos;
    private int mCurIndex;
    private List<FundListInfo> mStrategyList;

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
        mFundListInfos = DataSupport.where("selected = 1").find(FundListInfo.class);
        mFundStrategyAdapter = new FundStrategyAdapter(this, mFundListInfos, R.layout.item_fundlist);
        mLvFundList.setAdapter(mFundStrategyAdapter);
    }

    @OnClick(R.id.bt_GetStrategy)
    public void onClick() {
        Map<Integer,Boolean> lBooleanMap =  mFundStrategyAdapter.getCbSelectedMap();
        Set<Integer> lIntegers = lBooleanMap.keySet();
        mStrategyList = new ArrayList<>();
        for (Integer teger :
                lIntegers) {
            mStrategyList.add(mFundListInfos.get(teger));
        }

        if (mStrategyList.size()>0){
            mCurIndex = 0;
            mFundStrategyPrt.loadDatas(mStrategyList.get(mCurIndex), Constant.TaskName.DOWN_FUND_HISTORY);
        }else {
            Toast.makeText(this, "请选择要更新的基金", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadDataFail(Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        super.onLoadDataFail(pO, pDataSource);
        mCurIndex++;
        if (mCurIndex<mStrategyList.size()){
            mFundStrategyPrt.loadDatas(mStrategyList.get(mCurIndex), Constant.TaskName.DOWN_FUND_HISTORY);
        }else {
            onCancelled(null);
        }
    }

    @Override
    public void onLoadDatasSucc(Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        super.onLoadDatasSucc(pO, pDataSource);
        mCurIndex++;
        if (mCurIndex<mStrategyList.size()){
            mFundStrategyPrt.loadDatas(mStrategyList.get(mCurIndex), Constant.TaskName.DOWN_FUND_HISTORY);
        }else {
            onCancelled(null);
        }
    }
}
