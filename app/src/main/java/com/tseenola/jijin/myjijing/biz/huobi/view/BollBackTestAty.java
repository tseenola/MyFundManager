package com.tseenola.jijin.myjijing.biz.huobi.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
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

    private static final int STATUS_NULL = 0;//非持有状态
    private static final int STATUS_HOLD = 1;//持有状态
    @Bind(R.id.bt_RunBoll2BackTest)
    Button btRunBoll2BackTest;

    @Override
    public void initView() {
        setContentView(R.layout.activity_boll_back_test);
        ButterKnife.bind(this);
    }


    /**
     * boll线策略2.---买入投机，卖出稳健型
     * 1.非持有状态下-收盘价从下穿过下轨，买入
     * 2.持有状态下-收盘价从上穿过下轨道，卖出（防止继续跌）
     * 3.持有状态下-收盘价从下穿过中轨，卖出。
     */
    private void bollStdStrategy2() {
        tvInfo.setText("");
        double preCloseVal = 0d;
        double preBollDownVal = 0d;
        double preAvgVal = 0d;
        int curStatus = STATUS_NULL;
        double shouYiRateSum = 0;//收益率
        double curHoldVal = 0d;//当前持有价格
        for (int i = 0; i < mPointValues_Y.size(); i++) {
            double closeVal = mPointValues_Y.get(i).getY();
            double bollUpVal = mPointValues_Y_BollUp.get(i).getY();
            double bollDownVal = mPointValues_Y_BollLow.get(i).getY();
            double avgVal = mPointValues_Y_Avg.get(i).getY();
            if (i == 0) {
                preCloseVal = closeVal;
                preBollDownVal = bollDownVal;
                preAvgVal = avgVal;
            }

            if (closeVal > bollDownVal && preCloseVal < bollDownVal && curStatus == STATUS_NULL) {//1.收盘价从下穿过下轨
                tvInfo.append("买入：" + i + " close:" + closeVal + "\n");
                curStatus = STATUS_HOLD;
                curHoldVal = closeVal;
            } else if(closeVal < bollDownVal && preCloseVal > preBollDownVal && curStatus == STATUS_HOLD){//持有状态下-收盘价从上穿过下轨道，卖出（防止继续跌）
                double curShouYiRate = (closeVal-curHoldVal)/curHoldVal;
                shouYiRateSum += curShouYiRate;
                tvInfo.append("卖出：" + i + " close:" + closeVal + "收益率：" + curShouYiRate * 100 + "%\n");
                curStatus = STATUS_NULL;
            }else if (closeVal > avgVal && preCloseVal < preAvgVal && curStatus == STATUS_HOLD) {//3.收盘价从下穿过中轨，卖出
                double curShouYiRate = (closeVal-curHoldVal)/curHoldVal;
                shouYiRateSum += curShouYiRate;
                tvInfo.append("卖出：" + i + " close:" + closeVal + "收益率：" + curShouYiRate * 100 + "%\n");
                curStatus = STATUS_NULL;
            }
            preCloseVal = closeVal;
            preBollDownVal = bollDownVal;
            preAvgVal = avgVal;
        }
        tvInfo.append("总收益率：" + shouYiRateSum * 100 + "%\n");
    }

    /**
     * boll线策略1   买入稳健，卖出稳健型
     * 1.收盘价从下穿过上轨，买进。
     * 2.收盘价从上穿过中轨，卖出
     */
    private void bollStdStrategy() {
        tvInfo.setText("");
        double preCloseVal = 0d;
        int curStatus = STATUS_NULL;
        double shouYiRateSum = 0;//收益率
        double curHoldVal = 0d;//当前持有价格
        for (int i = 0; i < mPointValues_Y.size(); i++) {
            double closeVal = mPointValues_Y.get(i).getY();
            double bollUpVal = mPointValues_Y_BollUp.get(i).getY();
            double avgVal = mPointValues_Y_Avg.get(i).getY();
            if (i == 0) {
                preCloseVal = closeVal;
            }

            if (closeVal > bollUpVal && preCloseVal < closeVal && curStatus == STATUS_NULL) {//1.收盘价从下穿过上轨
                tvInfo.append("买入：" + i + " close:" + closeVal + "\n");
                curStatus = STATUS_HOLD;
                curHoldVal = closeVal;
            } else if (closeVal < avgVal && preCloseVal > closeVal && curStatus == STATUS_HOLD) {//2.收盘价从上穿过中轨
                double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                shouYiRateSum += curShouYiRate;
                tvInfo.append("卖出：" + i + " close:" + closeVal + "收益率：" + curShouYiRate * 100 + "%\n");
                curStatus = STATUS_NULL;
            }
            preCloseVal = closeVal;
        }
        tvInfo.append("收益率：" + shouYiRateSum * 100 + "%\n");
    }

    public static void launch(Context pContext, HistoryKLine pHistoryKLine) {
        pContext.startActivity(new Intent(pContext, BollBackTestAty.class));
        mHistoryKLine = pHistoryKLine;
    }

    @OnClick({R.id.bt_RunBackTest, R.id.bt_RunBoll2BackTest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_RunBackTest:
                bollStdStrategy();
                break;
            case R.id.bt_RunBoll2BackTest:
                bollStdStrategy2();
                break;
                default:
                    break;
        }
    }
}
