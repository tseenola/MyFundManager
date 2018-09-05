package com.tseenola.jijin.myjijing.biz.ma_min_backtest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundstrategy.model.DataNetWorthTrend;
import com.tseenola.jijin.myjijing.utils.DateUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/8/4.
 * 描述：
 * 1.如果当前低于平均值并且是最低值就买入
 * 2.买入后获取买入后的净值最大值，如果当前跌幅大于5%（具体值可设置）就卖出。
 */

public class MAMinBackTestActivity extends BaseAty {

    private static FundInfo mFundInfo;
    @Bind(R.id.tv_Info)
    TextView mTvInfo;
    @Bind(R.id.bt_Analysis)
    Button mBtAnalysis;
    @Bind(R.id.et_DieFu)
    EditText mEtDieFu;

    private int mCurBuyIndex = -1;//当前买入点(-1表示当前并未买入)

    private double mCurMaxPure = 1d;//买入后最大净值
    private double mDieFuRate;

    private double buy = 0d;
    private double sale = 0d;

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
        pContext.startActivity(new Intent(pContext, MAMinBackTestActivity.class));
    }

    @OnClick(R.id.bt_Analysis)
    public void onClick() {
        mTvInfo.setText("");
        double lUpRateSum = 0d;
        Gson lGson = new Gson();
        DataNetWorthTrend lWorthTrends = lGson.fromJson(mFundInfo.getDataNetWorthTrend(), DataNetWorthTrend.class);
        List<DataNetWorthTrend.DataNetWorthTrendBean> lDataNetWorthTrendBeens = lWorthTrends.getDataNetWorthTrend();
        int z = Integer.parseInt(mEtDieFu.getText().toString().trim());
        mDieFuRate = -(z/100d);

        Log.d("vbvb", "onClick: 跌幅"+mDieFuRate);
        //计算平均值
        double sum = 0d;
        double avg = 0d;
        boolean preIsbig = false;//上一笔是否大于平均值
        for (int lI = 0; lI < lDataNetWorthTrendBeens.size(); lI++) {
            DataNetWorthTrend.DataNetWorthTrendBean lWorthTrendBean = lDataNetWorthTrendBeens.get(lI);
            double pureVal = lWorthTrendBean.getY();
            sum += pureVal;
            avg = sum / (lI + 1);
            if (pureVal >= avg) {
                //当前价格与最高价格比较跌幅达到 5%
                if (mCurMaxPure <= pureVal){
                    mCurMaxPure = pureVal;
                }else {
                    double lDieFu = (pureVal-mCurMaxPure)/mCurMaxPure;
                    double lDieFu2 = new BigDecimal(lDieFu).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();

                    if (lDieFu2 < mDieFuRate){
                        sale = pureVal;
                        mCurBuyIndex = -1;
                        double upRate = new BigDecimal((sale-buy)/buy).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
                        mTvInfo.append(
                                " 当前净值："+pureVal+
                                        " 卖出"+DateUtils.getFormateTimeByStamp(lWorthTrendBean.getX(), "yyyy/MM/dd")+
                                        " 收益率：" + upRate+"\n");
                        lUpRateSum+=upRate;
                    }
                }
                preIsbig = true;
            } else {
                if (mCurBuyIndex>0){
                    if (preIsbig) {//死叉
                        //mTvInfo.append("死叉：" + DateUtils.getFormateTimeByStamp(lWorthTrendBean.getX(), "yyyy/MM/dd") + "\n");
                        mCurBuyIndex = -1;
                        sale = pureVal;
                        double upRate = new BigDecimal((sale-buy)/buy).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
                        mTvInfo.append("卖出："+sale+" "+
                                DateUtils.getFormateTimeByStamp(lWorthTrendBean.getX(), "yyyy/MM/dd")+
                                " 收益率：" + upRate+"\n");
                        lUpRateSum+=upRate;
                    }
                }

                preIsbig = false;
            }

        }
        mTvInfo.append("\n\n总收益率："+lUpRateSum+"\n");
    }
}
