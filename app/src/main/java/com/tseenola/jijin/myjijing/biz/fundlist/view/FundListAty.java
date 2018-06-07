package com.tseenola.jijin.myjijing.biz.fundlist.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.adapter.FundListAdapter;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.fundbaseinfo.view.FundBaseInfoAty;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;
import com.tseenola.jijin.myjijing.biz.fundlist.presenter.FundListPrt;
import com.tseenola.jijin.myjijing.biz.fundlist.presenter.IFundListPrt;
import com.tseenola.jijin.myjijing.utils.Constant;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class FundListAty extends BaseAty implements IFundListAty {

    @Bind(R.id.lv_Fund)
    ListView mLvFund;
    @Bind(R.id.et_FundCode)
    EditText mEtFundCode;
    @Bind(R.id.bt_Query)
    Button mBtQuery;
    private IFundListPrt mMainPresenter;
    private FundListAdapter mFundListAdapter;
    private List<FundListInfo> mFundInfos;

    @Override
    public void bindPresenter() {
        mMainPresenter = new FundListPrt(this);
    }

    @Override
    public void initData() {
        onStart(null);
        mMainPresenter.loadDatas(null, Constant.TaskName.DOWN_FUND_LIST);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_fund_list);
        ButterKnife.bind(this);
    }

    @Override
    public void onLoadDatasSucc(Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        mFundInfos = (List<FundListInfo>) pO;
        mFundListAdapter = new FundListAdapter(this, mFundInfos, R.layout.item_fundlist);
        mLvFund.setAdapter(mFundListAdapter);
        mLvFund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                Intent lIntent = new Intent(FundListAty.this, FundBaseInfoAty.class);
                lIntent.putExtra("fundCode", mFundInfos.get(pI).getFundCode().trim());
                startActivity(lIntent);
            }
        });
        if (pDataSource.equals(Constant.DATA_SOURCE.FROM_NET)) {
            mMainPresenter.saveFundList(mFundInfos);
        }
        Log.d("vbvb", "onLoadDatasSucc currentThread: " + Thread.currentThread().getId());
        super.onLoadDatasSucc(pO, pDataSource);
    }

    @Override
    public void onLoadDataFail(Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        Log.d("vbvb", "onLoadDataFail currentThread: " + Thread.currentThread().getId());
        Toast.makeText(this, (String) pO, Toast.LENGTH_SHORT).show();
        super.onLoadDataFail(pO, pDataSource);
    }


    @OnClick(R.id.bt_Query)
    public void onClick() {
        String fcode = mEtFundCode.getText().toString().trim();

        if (TextUtils.isEmpty(fcode)||fcode.length()!=6){
            Toast.makeText(this, "基金代码错误", Toast.LENGTH_SHORT).show();
            return;
        }
        List<FundListInfo> lFundListInfos = DataSupport.where("fundCode = ?",mEtFundCode.getText().toString()).find(FundListInfo.class);
        if (lFundListInfos==null || lFundListInfos.size()<=0){
            Toast.makeText(this, "无记录", Toast.LENGTH_SHORT).show();
            return;
        }
        mFundInfos.clear();
        mFundInfos.addAll(lFundListInfos);
        mFundListAdapter.notifyDataSetChanged();
    }
}
