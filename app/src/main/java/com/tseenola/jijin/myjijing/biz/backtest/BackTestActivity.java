package com.tseenola.jijin.myjijing.biz.backtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundstrategy.model.DataNetWorthTrend;
import com.tseenola.jijin.myjijing.utils.DateUtils;
import com.tseenola.jijin.myjijing.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/8/4.
 * 描述：
 */

public class BackTestActivity extends BaseAty {
    private static FundInfo mFundInfo;
    @Bind(R.id.dp_Date)
    DatePicker mDpDate;
    @Bind(R.id.tv_Result)
    TextView mTvResult;
    @Bind(R.id.bt_Analysis)
    Button mBtAnalysis;
    @Bind(R.id.et_BackTestDays)
    EditText mEtBackTestDays;
    @Bind(R.id.et_SaleWhenRise)
    EditText mEtSaleWhenRise;
    @Bind(R.id.et_SaleWhenFall)
    EditText mEtSaleWhenFall;

    @Override
    public void initData() {


    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_fund_stratety_back_test);
        ButterKnife.bind(this);
    }

    @Override
    public void bindPresenter() {

    }

    @OnClick(R.id.bt_Analysis)
    public void onClick() {
        mTvResult.setText("");
        int backTestDays = Integer.parseInt(mEtBackTestDays.getText().toString().trim());

        String mTime = mDpDate.getYear() + "/" + StringUtil.fillContentBy(StringUtil.Dir.left, "0", mDpDate.getMonth() + 1 + "", 2)
                + "/" + StringUtil.fillContentBy(StringUtil.Dir.left, "0", mDpDate.getDayOfMonth() + "", 2);
        Log.d("vbvb", "onClick: " + mTime);
        Gson lGson = new Gson();
        DataNetWorthTrend lWorthTrends = lGson.fromJson(mFundInfo.getDataNetWorthTrend(), DataNetWorthTrend.class);
        List<DataNetWorthTrend.DataNetWorthTrendBean> lDataNetWorthTrendBeens = lWorthTrends.getDataNetWorthTrend();

        boolean haveTime = false;
        int curindex = 0;
        for (int i = 0; i < lDataNetWorthTrendBeens.size(); i++) {
            String time = DateUtils.getFormateTimeByStamp(lDataNetWorthTrendBeens.get(i).getX(), "yyyy/MM/dd");
            if (mTime.equals(time)) {
                haveTime = true;
                Log.d("vbvb", "onClick: i " + i);
                if (i < backTestDays - backTestDays / 7 * 2) {
                    Toast.makeText(this, "当前选定时间无足够的回测时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                curindex = i;
                break;
            }
        }
        if (!haveTime) {
            Toast.makeText(this, "输入时间无对应净值", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 买入规则 如果当前的净值是回测范围最低净值。就买入
         */
        double minJInzhi = lDataNetWorthTrendBeens.get(curindex).getY();
        int minIndex = curindex;
        for (int i = curindex - 1; i > curindex - (backTestDays - backTestDays / 7 * 2); i--) {
            if (minJInzhi > lDataNetWorthTrendBeens.get(i).getY()) {
                minIndex = i;
                minJInzhi = lDataNetWorthTrendBeens.get(i).getY();
            }
        }
        mTvResult.setText("净值最小时间：" + DateUtils.getFormateTimeByStamp(lDataNetWorthTrendBeens.get(minIndex).getX(), "yyyy/MM/dd") + "\n");
        mTvResult.append("最小净值" + minJInzhi+"\n");
        /**
         * 卖出规则
         * 1.如果当前净值涨幅超过了买入禁止5%就卖出(0.05)
         */

        for(int i = curindex;i<lDataNetWorthTrendBeens.size();i++){
            if ((lDataNetWorthTrendBeens.get(i).getY()-minJInzhi)/minJInzhi>0.05) {
                mTvResult.append("涨幅达到5%时间："+DateUtils.getFormateTimeByStamp(lDataNetWorthTrendBeens.get(i).getX(), "yyyy/MM/dd") + "\n");
                mTvResult.append("涨幅达到5%时净值："+lDataNetWorthTrendBeens.get(i).getY());
                break;
            }
        }


        /**
         * 卖出规则
         * 2.如果当前净值跌幅超过了买入净值5%就卖出
         */

        for(int i = curindex;i<lDataNetWorthTrendBeens.size();i++){
            double fallRate = -(Double.parseDouble(mEtSaleWhenFall.getText().toString().trim())/100);

            if ((lDataNetWorthTrendBeens.get(i).getY()-minJInzhi)/minJInzhi<fallRate) {
                mTvResult.append("跌幅达到5%时间："+DateUtils.getFormateTimeByStamp(lDataNetWorthTrendBeens.get(i).getX(), "yyyy/MM/dd") + "\n");
                mTvResult.append("跌幅达到5%时净值："+lDataNetWorthTrendBeens.get(i).getY());
                break;
            }
        }

        /**
         * 卖出规则
         * 3.如果当日净值涨幅超过了买入净值2%考虑卖出
         */
    }

    public static void launch(Context pContext, FundInfo pFundInfo) {
        mFundInfo = pFundInfo;
        pContext.startActivity(new Intent(pContext, BackTestActivity.class));
    }
}
