package com.tseenola.jijin.myjijing.biz.huobi.view;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.tseenola.jijin.myjijing.LineAty;
import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.biz.huobi.model.HistoryKLine;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * Created by lenovo on 2018/10/13.
 * 描述：
 */

public class BollBackTestAty extends LineAty {
    @Bind(R.id.tv_Info)
    TextView tvInfo;
    @Bind(R.id.bt_RunBackTest)
    Button btRunBackTest;
    @Bind(R.id.line_chart)
    LineChartView lineChart;

    @Override
    public void initView() {
        setContentView(R.layout.activity_boll_back_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_RunBackTest)
    public void onViewClicked() {
        /**
         * boll线策略1 ，
         * 1.收盘价从下穿过上轨，买进。
         * 2.收盘价从上穿过中轨，卖出
         */
        double preCloseVal = 0d;
        StringBuilder info = new StringBuilder("");
        for (int i = 0; i < mPointValues_Y.size(); i++) {

            double closeVal = mPointValues_Y.get(i).getY();
            double bollUpVal = mPointValues_Y_BollUp.get(i).getY();
            double avgVal = mPointValues_Y_Avg.get(i).getY();
            if (i == 0) {
                preCloseVal = closeVal;
            }

            if (closeVal > bollUpVal && preCloseVal < closeVal) {//1.收盘价从下穿过上轨
                tvInfo.append("买入："+i +" close:"+closeVal + "\n");
            } else if (closeVal < avgVal && preCloseVal > closeVal) {//2.收盘价从上穿过中轨
                tvInfo.append("卖出："+i + " close:"+closeVal + "\n");
            }

        }

        /**
         * boll线策略2.
         * 1.收盘价从下穿过下轨，买入
         * 2.
         */
    }

    public static void launch (Context pContext, HistoryKLine pHistoryKLine){
        pContext.startActivity(new Intent(pContext,BollBackTestAty.class));
        mHistoryKLine = pHistoryKLine;
    }
}
