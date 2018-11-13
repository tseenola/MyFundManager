package com.tseenola.jijin.myjijing.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tseenola.jijin.myjijing.biz.huobi.model.HistoryKLine;
import com.tseenola.jijin.myjijing.biz.huobi.model.MACDUtils;
import com.tseenola.jijin.myjijing.biz.mail.SendMailUtil;
import com.tseenola.jijin.myjijing.utils.ThreadUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by lenovo on 2018/11/6.
 * 描述：
 */
public class MacdBgService extends Service {
    private static final int STATUS_NULL = 0;//非持有状态
    private static final int STATUS_HOLD = 1;//持有状态
    private String dmain = "https://api.huobi.br.com";
    //private String dmain = "https://api.huobi.pro";
    private String mSymbol = "htusdt";
    private String mPeriod = "1day";
    private String mSize = "500";
    private String kLineUrl = "/market/history/kline?symbol=%s&period=%s&size=%s";
    private HistoryKLine mHistoryKLine;
    private List<PointValue> mPointValues_Y;
    private List<PointValue> mPointValues_Y_MACD;
    private List<PointValue> mPointValues_Y_DIF;
    private List<PointValue> mPointValues_Y_DEA;

    @Nullable
    @Override
    public IBinder onBind(Intent pIntent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //抓取数据
        getDataAndAnlyse();
        Log.d("vbvb", "onCreate: 线程：onCreate"+Thread.currentThread().getId());
    }

    private void getDataAndAnlyse() {
        ThreadUtil.runSingleScheduledService(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },0,1,TimeUnit.HOURS);
    }

    private void analyseData() {
        int curStatus = STATUS_NULL;
        double shouYiRateSum = 0;//收益率
        double curHoldVal = 0d;//当前持有价格
        double curHoldMACDAvg = 0d;//买入后macd平均值
        int holdDay = 0;//持有天数
        double curHoldMACDSum = 0d;
        StringBuilder lBuySaleBuilder = new StringBuilder("火币：Symbol:"+mSymbol+",Period:"+mPeriod+",Size:"+mSize+",实际数据数量："+mPointValues_Y_MACD.size());
        for (int lI = 0; lI < mPointValues_Y_MACD.size(); lI++) {
            double closeVal = mPointValues_Y.get(lI).getY();
            double macd = mPointValues_Y_MACD.get(lI).getY();

            if (macd > 0 ) {//上穿买入
                if (curStatus == STATUS_NULL) {
                    double preMACD = 0d;
                    if (lI>0) {
                        preMACD = mPointValues_Y_MACD.get(lI-1).getY();
                    }else {
                        preMACD = macd;
                    }
                    if (macd > preMACD){//如果macd 是上涨状态就买入
                        curStatus = STATUS_HOLD;
                        curHoldVal = closeVal;
                        curHoldMACDAvg = macd;
                        holdDay = 1;
                        curHoldMACDSum = macd;
                        String msg = "\n\n"+(lI+1)+" ,macd:"+String.format("%.5f",macd)+" >0, 持有后macd平均值:" +String.format("%.5f",curHoldMACDAvg)+ ",持有天数："+holdDay+ " ,closeVal:"+String.format("%.5f",closeVal)+" ====================>买入\n";
                        lBuySaleBuilder.append(msg);
                        Log.d("vbvb", msg);
                        //如果买入信号是最后进一条数据那么说明是今天，就发送邮件通知
                        if (lI==mPointValues_Y_MACD.size()-1){
                            SendMailUtil.send("641380205@qq.com","火币-买入",msg);
                        }
                    }
                }else if (curStatus == STATUS_HOLD){
                    holdDay ++;
                    curHoldMACDSum += macd;
                    curHoldMACDAvg = curHoldMACDSum/holdDay;
                    if (macd>=curHoldMACDAvg){
                        //继续持有
                        String msg = (lI+1)+" ,macd:"+String.format("%.5f",macd)+" >= 持有后macd平均值:"+String.format("%.5f",curHoldMACDAvg)+" ,持有天数："+holdDay+ " ,closeVal:"+String.format("%.5f",closeVal)+" ==>继续持有\n";
                        Log.d("vbvb", msg);
                        lBuySaleBuilder.append(msg);
                    }else {
                        //卖出
                        if (curStatus == STATUS_HOLD) {
                            double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                            shouYiRateSum += curShouYiRate;
                            curStatus = STATUS_NULL;
                            String msg = (lI+1)+" ,macd:"+String.format("%.5f",macd)+" < 持有后macd平均值:"+String.format("%.5f",curHoldMACDAvg)+" ,持有天数："+holdDay+ " ,closeVal:"+String.format("%.5f",closeVal)+" ,收益率："+String.format("%.5f",curShouYiRate*100)+" %========>卖出\n";
                            Log.d("vbvb", msg);
                            if (lI==mPointValues_Y_MACD.size()-1){
                                SendMailUtil.send("641380205@qq.com","火币-卖出",msg);
                            }
                            lBuySaleBuilder.append(msg);
                        }
                    }
                }
            }else{//下穿卖出
                if (curStatus == STATUS_HOLD){
                    double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                    shouYiRateSum += curShouYiRate;
                    curStatus = STATUS_NULL;
                    String msg = (lI+1)+" ,macd:"+String.format("%.5f",macd)+" < 0 ,持有天数："+holdDay+ " ,closeVal:"+String.format("%.5f",closeVal)+" ,收益率："+String.format("%.5f",curShouYiRate*100)+" %========>卖出\n";
                    Log.d("vbvb", msg);
                    if (lI==mPointValues_Y_MACD.size()-1){
                        SendMailUtil.send("641380205@qq.com","火币-卖出",msg);
                    }
                    lBuySaleBuilder.append(msg);
                }
            }
        }
        String msg = "\n\n总收益率：" + String.format("%.2f",shouYiRateSum * 100) + "%";
        Log.d("vbvb", msg);
        if (curStatus==STATUS_HOLD){
            msg = msg+",当前状态持有中\n";
            lBuySaleBuilder.append(msg);
        }else {
            msg = msg+",当前状态非持有\n";
            lBuySaleBuilder.append(msg);
        }
        SendMailUtil.send("641380205@qq.com","火币-日常播报",lBuySaleBuilder.toString());
    }

    private void parseData() {
        List<HistoryKLine.DataBean> lDataBeans = mHistoryKLine.getData();
        LinkedList<Double> stdData = new LinkedList<>();
        mPointValues_Y = new ArrayList<PointValue>();//y轴值，实际价格
        mPointValues_Y_MACD = new ArrayList<PointValue>();//y轴值，MACD
        mPointValues_Y_DIF = new ArrayList<PointValue>();//y轴值，DIF
        mPointValues_Y_DEA = new ArrayList<PointValue>();//y轴值，DEA
        for (int i = 0; i < lDataBeans.size(); i++) {
            // 收盘值
            double closeVal = lDataBeans.get(lDataBeans.size() - i - 1).getClose();
            mPointValues_Y.add(new PointValue(i, (float) closeVal));//净值
            stdData.add(closeVal);

            HashMap<String, Double> lHashMap = MACDUtils.getMACD(stdData, 12, 26, 9);
            double macd = lHashMap.get("MACD");
            mPointValues_Y_MACD.add(new PointValue(i, (float) macd));

            double dif = lHashMap.get("DIF");
            mPointValues_Y_DIF.add(new PointValue(i, (float) dif));

            double dea = lHashMap.get("DEA");
            mPointValues_Y_DEA.add(new PointValue(i, (float) dea));
        }
    }

    /**
     * 抓取数据
     */
    private void getData(){
        Log.d("vbvb", "getData: 进行请求了");
        String url = dmain + String.format(kLineUrl, mSymbol, mPeriod,mSize);
        HttpUtils lHttpUtils = new HttpUtils();
        lHttpUtils.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
                        //给出结论或者进行通知
                        Gson lGson = new Gson();
                        Log.d("vbvb", "onSuccess: " + pResponseInfo.result);
                        if (pResponseInfo.result.contains("\"status\":\"ok\"")){
                            mHistoryKLine = lGson.fromJson(pResponseInfo.result, HistoryKLine.class);
                            //处理数据
                            parseData();
                            //分析数据
                            analyseData();
                            //得出结论
                        }else {
                            SendMailUtil.send("641380205@qq.com","火币-抓取出错",pResponseInfo.result);
                        }
                    }

                    @Override
                    public void onFailure(HttpException pE, String pS) {
                        SendMailUtil.send("641380205@qq.com","火币-抓取出错",pS+"\n"+pE.getMessage());
                        Log.d("vbvb", "onFailure: "+pS);
                        getData();
                    }
                });
    }
}
