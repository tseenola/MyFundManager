package com.tseenola.jijin.myjijing.biz.ma_backtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.backtest.BackTestActivity;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundstrategy.model.DataNetWorthTrend;
import com.tseenola.jijin.myjijing.utils.DateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.media.CamcorderProfile.get;

/**
 * Created by lenovo on 2018/8/4.
 * 描述：
 * 1.找出所有上窜平均线的点：金叉
 * 2.找出所有下窜平均线的点：死叉
 * 3.买入后获取买入后的净值最大值，如果当前跌幅大于5%（具体值可设置）就卖出。
 */

public class MABackTestActivity extends BaseAty {

    private static FundInfo mFundInfo;
    @Bind(R.id.tv_Info)
    TextView mTvInfo;
    @Bind(R.id.bt_Analysis)
    Button mBtAnalysis;

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_ma_backtest);
        ButterKnife.bind(this);
    }

    @Override
    public void bindPresenter() {

    }

    public static void launch(Context pContext, FundInfo pFundInfo) {
        mFundInfo = pFundInfo;
        pContext.startActivity(new Intent(pContext, MABackTestActivity.class));
    }

    @OnClick(R.id.bt_Analysis)
    public void onClick() {
        Gson lGson = new Gson();
        DataNetWorthTrend lWorthTrends = lGson.fromJson(mFundInfo.getDataNetWorthTrend(), DataNetWorthTrend.class);
        List<DataNetWorthTrend.DataNetWorthTrendBean> lDataNetWorthTrendBeens = lWorthTrends.getDataNetWorthTrend();
        //计算平均值
        double sum = 0d;
        double avg = 0d;
        boolean preIsbig = false;
        for (int lI = 0; lI < lDataNetWorthTrendBeens.size(); lI++) {
            DataNetWorthTrend.DataNetWorthTrendBean lWorthTrendBean = lDataNetWorthTrendBeens.get(lI);
            double pureVal =  lWorthTrendBean.getY();
            sum += pureVal;
            avg = sum/(lI+1);
            if (pureVal>=avg){
                if (!preIsbig){//金叉
                    mTvInfo.append("金叉："+ DateUtils.getFormateTimeByStamp(lWorthTrendBean.getX(), "yyyy/MM/dd") +"\n");
                }
                preIsbig = true;
            }else {
                if (preIsbig){//死叉
                    mTvInfo.append("死叉："+DateUtils.getFormateTimeByStamp(lWorthTrendBean.getX(), "yyyy/MM/dd") +"\n");
                }
                preIsbig = false;
            }
        }


    }
}
