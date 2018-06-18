package com.tseenola.jijin.myjijing.biz.fundstrategy.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.adapter.FundStrategyAdapter;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;
import com.tseenola.jijin.myjijing.biz.fundstrategy.model.DataNetWorthTrend;
import com.tseenola.jijin.myjijing.biz.fundstrategy.presenter.FundStrategyPrt;
import com.tseenola.jijin.myjijing.biz.fundstrategy.presenter.IFundStrategyPrt;
import com.tseenola.jijin.myjijing.utils.Constant;
import com.tseenola.jijin.myjijing.utils.DialogUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.PointValue;

import static android.media.CamcorderProfile.get;
import static org.litepal.crud.DataSupport.findAll;
import static org.litepal.crud.DataSupport.findFirst;
import static org.litepal.crud.DataSupport.where;

/**
 * Created by lenovo on 2018/6/7.
 * 描述：
 */

public class FundStrategyAty extends BaseAty implements IFundStrategyAty {
    @Bind(R.id.lv_FundList)
    ListView mLvFundList;
    @Bind(R.id.bt_GetStrategy)
    Button mBtGetStrategy;
    @Bind(R.id.bt_Analysis)
    Button mBtAnalysis;

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
        mLvFundList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                FundListInfo lFundListInfo = mFundListInfos.get(pI);

                FundInfo lFundInfos = DataSupport.where("fSCode = ?",lFundListInfo.getFundCode()).findFirst(FundInfo.class);
                if (lFundInfos!=null){
                    mFundStrategyPrt.showHistryByChart(lFundInfos);
                }else {
                    Toast.makeText(FundStrategyAty.this, "无历史数据", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onLoadDataFail(final Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        DialogUtils.showCirDailog((String) pO + "加载失败");
        mCurIndex++;
        if (mCurIndex <= mStrategyList.size()) {
            mFundStrategyPrt.loadDatas(mStrategyList.get(mCurIndex), Constant.TaskName.DOWN_FUND_HISTORY);
        } else {
            onCancelled(null);
        }
    }

    @Override
    public void onLoadDatasSucc(final Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        DialogUtils.showCirDailog((String) pO + "加载成功");
        Log.d("vbvb", "onLoadDatasSucc: " + pO);
        mCurIndex++;
        if (mCurIndex < mStrategyList.size()) {
            mFundStrategyPrt.loadDatas(mStrategyList.get(mCurIndex), Constant.TaskName.DOWN_FUND_HISTORY);
        } else {
            onCancelled(null);
        }
    }


    @OnClick({R.id.bt_GetStrategy, R.id.bt_Analysis})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_GetStrategy:
                Map<Integer, Boolean> lBooleanMap = mFundStrategyAdapter.getCbSelectedMap();
                Set<Integer> lIntegers = lBooleanMap.keySet();
                mStrategyList = new ArrayList<>();
                for (Integer teger :
                        lIntegers) {
                    mStrategyList.add(mFundListInfos.get(teger));
                }

                if (mStrategyList.size() > 0) {
                    onStart(null);
                    mCurIndex = 0;
                    mFundStrategyPrt.loadDatas(mStrategyList.get(mCurIndex), Constant.TaskName.DOWN_FUND_HISTORY);
                } else {
                    Toast.makeText(this, "请选择要更新的基金", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_Analysis:
                //分析范围可以按照天来选
                //按照选择的范围罗列出当前是净值最低的基金
                ChoiceStrategyFragment.getInstance(new ChoiceStrategyFragment.onButtonClickListener() {
                    @Override
                    public void onClick(int pDay) {
                        Toast.makeText(FundStrategyAty.this, ""+pDay, Toast.LENGTH_SHORT).show();
                        List<FundInfo> lFundInfos = DataSupport.findAll(FundInfo.class);
                        for(int i = 0;i<lFundInfos.size();i++){
                            Gson lGson = new Gson();
                            DataNetWorthTrend lWorthTrends = lGson.fromJson(lFundInfos.get(i).getDataNetWorthTrend(),DataNetWorthTrend.class);
                            List<DataNetWorthTrend.DataNetWorthTrendBean> lDataNetWorthTrendBeens = lWorthTrends.getDataNetWorthTrend();
                            int totalDays = lDataNetWorthTrendBeens.size();
                            double lastNetWorth = lDataNetWorthTrendBeens.get(totalDays-1).getY();
                            Log.d("vbvb", "useAppContext: lastNetWorth:"+lastNetWorth);
                            for (int j = totalDays-2;
                                 j >= (totalDays>pDay?totalDays-pDay:pDay-1); j--) {
                                Double y = lDataNetWorthTrendBeens.get(j).getY();
                                if (lastNetWorth>y){
                                    //移除
                                    for(int z = mFundListInfos.size() - 1; z >= 0; z--){
                                        FundListInfo item = mFundListInfos.get(z);
                                        if(lFundInfos.get(i).getfSCode().equals(item.getFundCode())){
                                            mFundListInfos.remove(item);
                                        }
                                    }
                                }
                                Log.d("vbvb", "useAppContext: code :"+lFundInfos.get(i).getfSCode()+" y:"+y);
                            }
                            Log.d("vbvb", "useAppContext: 结束");
                        }
                        //刷新
                        mFundStrategyAdapter.notifyDataSetChanged();
                    }
                }).show(getFragmentManager(),"");
                break;
            default:
                break;
        }
    }
}
