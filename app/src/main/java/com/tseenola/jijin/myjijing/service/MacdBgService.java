package com.tseenola.jijin.myjijing.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
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
import com.tseenola.jijin.myjijing.utils.DateUtils;
import com.tseenola.jijin.myjijing.utils.LogUtil;
import com.tseenola.jijin.myjijing.utils.ThreadUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private String [] mSymbols = {"btcusdt"
            ,"ethusdt"
            ,"xrpusdt"
            ,"bchusdt"
            ,"ltcusdt"
            ,"etcusdt"
            ,"eosusdt"
            ,"htusdt"
            ,"adausdt"
            ,"dashusdt"
            ,"omgusdt"
            ,"zecusdt"
            ,"iotausdt"
            ,"xmrusdt"
            ,"hb10usdt"
            ,"gxcusdt"
            ,"hitusdt"
            ,"btmusdt"
            ,"paiusdt"
            ,"hptusdt"
            ,"elausdt"
            ,"ontusdt"};
    /*private String [] mSymbols = {
            "btcusdt"
            ,"ethusdt"
            ,"xrpusdt"
            ,"bchusdt"
            ,"ltcusdt"
            ,"etcusdt"
            ,"eosusdt"
            ,"htusdt"
            ,"adausdt"
            ,"dashusdt"
            ,"omgusdt"
            ,"zecusdt"
            ,"iotausdt"
            ,"xmrusdt"
            ,"hb10usdt"
            ,"gxcusdt"
            ,"hitusdt"
            ,"btmusdt"
            ,"paiusdt"
            ,"hptusdt"
            ,"elausdt"
            ,"ontusdt"
            ,"iostusdt"
            ,"qtumusdt"
            ,"trxusdt"
            ,"dtausdt"
            ,"zilusdt"
            ,"ocnusdt"
            ,"cmtusdt"
            ,"elfusdt"
            ,"gntusdt"
            ,"socusdt"
            ,"nasusdt"
            ,"ctxcusdt"
            ,"ruffusdt"
            ,"vetusdt"
            ,"letusdt"
            ,"wiccusdt"
            ,"xlmusdt"
            ,"hcusdt"
            ,"neousdt"
            ,"cvcusdt"
            ,"thetausdt"
            ,"btsusdt"
            ,"itcusdt"
            ,"actusdt"
            ,"mdsusdt"
            ,"storjusdt"
            ,"xemusdt"
            ,"sntusdt"
            ,"steemusdt"
            ,"smtusdt"
            ,"bixusdt"
            ,"usdthusd"
            ,"btchusd"
            ,"ethhusd"
            ,"eoshusd"
            ,"shebtc"
            ,"dacbtc"
            ,"utkbtc"
            ,"sspbtc"
            ,"lxtbtc"
            ,"fairbtc"
            ,"uuubtc"
            ,"waxbtc"
            ,"ftibtc"
            ,"yeebtc"
            ,"hptbtc"
            ,"nccbtc"
            ,"cnnbtc"
            ,"elabtc"
            ,"datxbtc"
            ,"btgbtc"
            ,"rcccbtc"
            ,"polybtc"
            ,"lymbtc"
            ,"rtebtc"
            ,"xembtc"
            ,"kcashbtc"
            ,"btmbtc"
            ,"abtbtc"
            ,"qashbtc"
            ,"bcdbtc"
            ,"manabtc"
            ,"steembtc"
            ,"mtnbtc"
            ,"gtcbtc"
            ,"letbtc"
            ,"bkbtbtc"
            ,"ektbtc"
            ,"kncbtc"
            ,"aidocbtc"
            ,"paibtc"
            ,"dbcbtc"
            ,"seelebtc"
            ,"paybtc"
            ,"bcvbtc"
            ,"bftbtc"
            ,"bifibtc"
            ,"uipbtc"
            ,"mexbtc"
            ,"aacbtc"
            ,"ncashbtc"
            ,"qspbtc"
            ,"xrpbtc"
            ,"meetbtc"
            ,"qtumbtc"
            ,"sbtcbtc"
            ,"mtbtc"
            ,"htbtc"
            ,"eosbtc"
            ,"iotabtc"
            ,"swftcbtc"
            ,"dashbtc"
            ,"thetabtc"
            ,"elfbtc"
            ,"propybtc"
            ,"ontbtc"
            ,"cvcoinbtc"
            ,"wavesbtc"
            ,"cdcbtc"
            ,"stkbtc"
            ,"chatbtc"
            ,"evxbtc"
            ,"ctxcbtc"
            ,"grsbtc"
            ,"neobtc"
            ,"xmrbtc"
            ,"tosbtc"
            ,"cmtbtc"
            ,"trxbtc"
            ,"hitbtc"
            ,"hotbtc"
            ,"btsbtc"
            ,"xzcbtc"
            ,"wiccbtc"
            ,"sntbtc"
            ,"zilbtc"
            ,"ethbtc"
            ,"smtbtc"
            ,"gasbtc"
            ,"reqbtc"
            ,"nasbtc"
            ,"powrbtc"
            ,"topcbtc"
            ,"dtabtc"
            ,"ardrbtc"
            ,"mtlbtc"
            ,"qunbtc"
            ,"pntbtc"
            ,"actbtc"
            ,"dgdbtc"
            ,"wprbtc"
            ,"pcbtc"
            ,"etcbtc"
            ,"wtcbtc"
            ,"lskbtc"
            ,"saltbtc"
            ,"omgbtc"
            ,"adabtc"
            ,"portalbtc"
            ,"iicbtc"
            ,"ocnbtc"
            ,"ruffbtc"
            ,"blzbtc"
            ,"ltcbtc"
            ,"hcbtc"
            ,"yccbtc"
            ,"bixbtc"
            ,"egccbtc"
            ,"rdnbtc"
            ,"mtxbtc"
            ,"dcrbtc"
            ,"batbtc"
            ,"zecbtc"
            ,"lbabtc"
            ,"aebtc"
            ,"ostbtc"
            ,"cvcbtc"
            ,"socbtc"
            ,"icxbtc"
            ,"getbtc"
            ,"storjbtc"
            ,"lunbtc"
            ,"manbtc"
            ,"bcxbtc"
            ,"sncbtc"
            ,"zrxbtc"
            ,"phxbtc"
            ,"tnbbtc"
            ,"xvgbtc"
            ,"muskbtc"
            ,"18cbtc"
            ,"ucbtc"
            ,"srnbtc"
            ,"dgbbtc"
            ,"rcnbtc"
            ,"kanbtc"
            ,"gscbtc"
            ,"xlmbtc"
            ,"tntbtc"
            ,"engbtc"
            ,"zenbtc"
            ,"iostbtc"
            ,"datbtc"
            ,"appcbtc"
            ,"vetbtc"
            ,"itcbtc"
            ,"zjltbtc"
            ,"wanbtc"
            ,"nanobtc"
            ,"mcobtc"
            ,"mdsbtc"
            ,"gnxbtc"
            ,"adxbtc"
            ,"idtbtc"
            ,"zlabtc"
            ,"butbtc"
            ,"gntbtc"
            ,"astbtc"
            ,"linkbtc"
            ,"xmxbtc"
            ,"edubtc"
            ,"triobtc"
            ,"gvebtc"
            ,"gxcbtc"
            ,"renbtc"
            ,"boxbtc"
            ,"ekobtc"
            ,"bchbtc"
            ,"eoseth"
            ,"hteth"
            ,"adaeth"
            ,"omgeth"
            ,"xmreth"
            ,"iotaeth"
            ,"nanoeth"
            ,"zeneth"
            ,"elaeth"
            ,"paieth"
            ,"btmeth"
            ,"hiteth"
            ,"dbceth"
            ,"cmteth"
            ,"edueth"
            ,"zlaeth"
            ,"iosteth"
            ,"trxeth"
            ,"onteth"
            ,"polyeth"
            ,"knceth"
            ,"qtumeth"
            ,"dcreth"
            ,"zileth"
            ,"boxeth"
            ,"tnteth"
            ,"lbaeth"
            ,"ocneth"
            ,"bateth"
            ,"chateth"
            ,"dateth"
            ,"kaneth"
            ,"gxceth"
            ,"naseth"
            ,"tnbeth"
            ,"dtaeth"
            ,"waxeth"
            ,"elfeth"
            ,"ctxceth"
            ,"ekoeth"
            ,"rcneth"
            ,"soceth"
            ,"veteth"
            ,"manaeth"
            ,"ruffeth"
            ,"ncasheth"
            ,"blzeth"
            ,"abteth"
            ,"xlmeth"
            ,"mcoeth"
            ,"acteth"
            ,"gnteth"
            ,"appceth"
            ,"zrxeth"
            ,"topceth"
            ,"powreth"
            ,"wtceth"
            ,"mtneth"
            ,"swftceth"
            ,"gaseth"
            ,"wicceth"
            ,"gnxeth"
            ,"aidoceth"
            ,"qasheth"
            ,"mtxeth"
            ,"reqeth"
            ,"itceth"
            ,"yeeeth"
            ,"waneth"
            ,"srneth"
            ,"hceth"
            ,"thetaeth"
            ,"linketh"
            ,"ardreth"
            ,"btseth"
            ,"leteth"
            ,"dgbeth"
            ,"cvceth"
            ,"osteth"
            ,"wpreth"
            ,"bfteth"
            ,"propyeth"
            ,"lsketh"
            ,"mdseth"
            ,"icxeth"
            ,"xzceth"
            ,"xvgeth"
            ,"stketh"
            ,"rdneth"
            ,"luneth"
            ,"quneth"
            ,"steemeth"
            ,"qspeth"
            ,"payeth"
            ,"meeteth"
            ,"evxeth"
            ,"grseth"
            ,"salteth"
            ,"engeth"
            ,"snceth"
            ,"dgdeth"
            ,"asteth"
            ,"bixeth"
            ,"adxeth"
            ,"utketh"
            ,"smteth"
            ,"waveseth"
            ,"aeeth"
            ,"ekteth"
            ,"musketh"
            ,"uuueth"
            ,"xmxeth"
            ,"gsceth"
            ,"sheeth"
            ,"toseth"
            ,"cnneth"
            ,"bkbteth"
            ,"gveeth"
            ,"rccceth"
            ,"seeleeth"
            ,"ncceth"
            ,"zjlteth"
            ,"uceth"
            ,"kcasheth"
            ,"ftieth"
            ,"pnteth"
            ,"maneth"
            ,"mexeth"
            ,"pceth"
            ,"cvcoineth"
            ,"gtceth"
            ,"sspeth"
            ,"cdceth"
            ,"uipeth"
            ,"trioeth"
            ,"idteth"
            ,"buteth"
            ,"iiceth"
            ,"aaceth"
            ,"lxteth"
            ,"18ceth"
            ,"geteth"
            ,"rteeth"
            ,"portaleth"
            ,"daceth"
            ,"mteth"
            ,"ycceth"
            ,"lymeth"
            ,"bcveth"
            ,"reneth"
            ,"faireth"
            ,"egcceth"
            ,"hoteth"
            ,"datxeth"
            ,"ltcht"
            ,"eosht"
            ,"xrpht"
            ,"etcht"
            ,"bchht"
            ,"dashht"
            ,"hptht"
            ,"iostht"
            ,"kcashht"
            ,"mtht"};*/
    private String mSymbol = "htusdt";
    private String mPeriod = "1day";
    private String mSize = "400";
    private String kLineUrl = "/market/history/kline?symbol=%s&period=%s&size=%s";
    private HistoryKLine mHistoryKLine;
    private List<PointValue> mPointValues_Y;
    private List<PointValue> mPointValues_Y_MACD;
    private List<PointValue> mPointValues_Y_DIF;
    private List<PointValue> mPointValues_Y_DEA;
    private List<String> mDate;
    private int mCurSymbo = 0;
    //金价低于这个就买入
    private static final double GOLD_PRICE_THRESHOLD_BUY = 265;
    //金价高于这个就卖出
    private static final double GOLD_PRICE_THRESHOLD_SALE = 276;
    private PowerManager.WakeLock wakeLock;

    @Nullable
    @Override
    public IBinder onBind(Intent pIntent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MacdBgService.class.getName());
        wakeLock.acquire();
        //抓取数据
        getDataAndAnlyse();
        Log.d("vbvb", "onCreate: 线程：onCreate"+Thread.currentThread().getId());
    }

    private void getDataAndAnlyse() {
        ThreadUtil.runSingleScheduledService(new Runnable() {
            @Override
            public void run() {
                getGoldPrice();//抓取黄金
            }
        },0,8,TimeUnit.HOURS);

        ThreadUtil.runSingleScheduledService(new Runnable() {
            @Override
            public void run() {
                SendMailUtil.send("641380205@qq.com","数据抓取-我还活着","心跳");
            }
        },0,2,TimeUnit.HOURS);

        ThreadUtil.runSingleScheduledService(new Runnable() {
            @Override
            public void run() {
                mCurSymbo = 0;
                getData();//抓取火
            }
        },0,8,TimeUnit.HOURS);
    }

    private void analyseData() {
        int curStatus = STATUS_NULL;
        double shouYiRateSum = 0;//收益率
        double curHoldVal = 0d;//当前持有价格
        double curHoldMACDAvg = 0d;//买入后macd平均值
        int holdDay = 0;//持有天数
        double curHoldMACDSum = 0d;
        StringBuilder lBuySaleBuilder = new StringBuilder("火：Symbol:"+mSymbols[mCurSymbo]+",Period:"+mPeriod+",Size:"+mSize+",实际数据数量："+mPointValues_Y_MACD.size());
        for (int lI = 0; lI < mPointValues_Y_MACD.size(); lI++) {
            if (lI<10){
                continue;
            }
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
                        String msg = "\n\n"+mDate.get(lI)+" ,macd:"+String.format("%.8f",macd)+" >0, macd avg:" +String.format("%.8f",curHoldMACDAvg)+ ",天数："+holdDay+ " ,closeVal:"+String.format("%.8f",closeVal)+" ====================>买入\n";
                        lBuySaleBuilder.append(msg);
                        Log.d("vbvb", msg);
                        //如果买入信号是最后进一条数据那么说明是今天，就发送邮件通知
                        if (lI==mPointValues_Y_MACD.size()-1){
                            SendMailUtil.send("641380205@qq.com","火-买-"+mSymbols[mCurSymbo]+"-总收："+String.format("%.2f",shouYiRateSum * 100)+"%",lBuySaleBuilder.toString());
                        }
                    }
                }else if (curStatus == STATUS_HOLD){
                    holdDay ++;
                    curHoldMACDSum += macd;
                    curHoldMACDAvg = curHoldMACDSum/holdDay;
                    if (macd>=curHoldMACDAvg){
                        //继续持有

                        //如果当前
                        String msg = mDate.get(lI)+" ,macd:"+String.format("%.8f",macd)+" >= macd avg:"+String.format("%.8f",curHoldMACDAvg)+" ,天数："+holdDay+ " ,closeVal:"+String.format("%.8f",closeVal)+" ==>继续持有\n";
                        Log.d("vbvb", msg);
                        lBuySaleBuilder.append(msg);
                        if (lI==mPointValues_Y_MACD.size()-1){
                            double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                            shouYiRateSum += curShouYiRate;
                            SendMailUtil.send("641380205@qq.com","火-持有-"+mSymbols[mCurSymbo]+"-总收："+String.format("%.2f",shouYiRateSum * 100)+"%"+"-当收："+String.format("%.6f",curShouYiRate)+"%",lBuySaleBuilder.toString());
                        }
                    }else {
                        //卖出
                        if (curStatus == STATUS_HOLD) {
                            double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                            shouYiRateSum += curShouYiRate;
                            curStatus = STATUS_NULL;
                            String msg = mDate.get(lI)+" ,macd:"+String.format("%.8f",macd)+" < macd avg:"+String.format("%.8f",curHoldMACDAvg)+" ,天数："+holdDay+ " ,closeVal:"+String.format("%.8f",closeVal)+" ,收益率："+String.format("%.2f",curShouYiRate*100)+" %========>卖出\n";
                            Log.d("vbvb", msg);
                            lBuySaleBuilder.append(msg);
                            if (lI==mPointValues_Y_MACD.size()-1){
                                SendMailUtil.send("641380205@qq.com","火-卖-"+mSymbols[mCurSymbo]+"-总收："+String.format("%.2f",shouYiRateSum * 100)+"%"+"-当收："+String.format("%.6f",curShouYiRate)+"%",lBuySaleBuilder.toString());
                            }
                        }
                    }
                }
            }else{//下穿卖出
                if (curStatus == STATUS_HOLD){
                    double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                    shouYiRateSum += curShouYiRate;
                    curStatus = STATUS_NULL;
                    String msg = (mDate.get(lI))+" ,macd:"+String.format("%.8f",macd)+" < 0 ,天数："+holdDay+ " ,closeVal:"+String.format("%.8f",closeVal)+" ,收益率："+String.format("%.8f",curShouYiRate*100)+" %========>卖出\n";
                    Log.d("vbvb", msg);
                    lBuySaleBuilder.append(msg);
                    if (lI==mPointValues_Y_MACD.size()-1){
                        SendMailUtil.send("641380205@qq.com","火-卖-"+mSymbols[mCurSymbo]+"-总收："+String.format("%.2f",shouYiRateSum * 100)+"%"+"-当收："+String.format("%.6f",curShouYiRate)+"%",lBuySaleBuilder.toString());
                    }
                }
            }
        }
    }

    private void parseData() {
        List<HistoryKLine.DataBean> lDataBeans = mHistoryKLine.getData();
        LinkedList<Double> stdData = new LinkedList<>();
        mPointValues_Y = new ArrayList<PointValue>();//y轴值，实际价格
        mPointValues_Y_MACD = new ArrayList<PointValue>();//y轴值，MACD
        mPointValues_Y_DIF = new ArrayList<PointValue>();//y轴值，DIF
        mPointValues_Y_DEA = new ArrayList<PointValue>();//y轴值，DEA
        mDate = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < lDataBeans.size(); i++) {
            mDate.add(DateUtils.getNextDay(date,-(lDataBeans.size() - i - 1),format));
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
        if (mCurSymbo>=mSymbols.length){
            SendMailUtil.send("641380205@qq.com","火-循环完成","");
            return;
        }
        Log.d("vbvb", "getData: 进行请求了");
        String url = dmain + String.format(kLineUrl, mSymbols[mCurSymbo], mPeriod,mSize);
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
                            if (mHistoryKLine.getData()!=null && mHistoryKLine.getData().size()>=60){
                                //处理数据
                                parseData();
                                //分析数据
                                analyseData();
                                mCurSymbo++;
                                //得出结论
                            }
                        }else {
                            SendMailUtil.send("641380205@qq.com","火-抓错"+mSymbols[mCurSymbo],mSymbols[mCurSymbo]+": "+pResponseInfo.result);
                            mCurSymbo++;
                        }
                        getData();//获取下一跳数据
                    }

                    @Override
                    public void onFailure(HttpException pE, String pS) {
                        SendMailUtil.send("641380205@qq.com","火-抓取出错"+mSymbols[mCurSymbo],mSymbols[mCurSymbo]+": "+pS+"\n"+pE.getMessage());
                        Log.d("vbvb", "onFailure: "+pS);
                        mCurSymbo++;
                        getData();//获取下一跳数据
                    }
                });
    }

    @Override
    public void onDestroy() {
        SendMailUtil.send("641380205@qq.com","火-出错-onDestroy","执行了onDestroy");
        if (wakeLock != null) { wakeLock.release(); wakeLock = null; }
        super.onDestroy();
    }

    /**
     * 获取黄金价格
     */
    protected void getGoldPrice(){
        String url = "http://www.sge.com.cn/";
        HttpUtils lHttpUtils = new HttpUtils();
        lHttpUtils.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
                        double goldPrice = getGoldPrice(pResponseInfo.result);
                        if (goldPrice<= GOLD_PRICE_THRESHOLD_BUY){
                            SendMailUtil.send("641380205@qq.com","黄金-价格-买入","今日金价："+goldPrice+"小于阈值建议买入");
                        }else if (goldPrice>=GOLD_PRICE_THRESHOLD_SALE){
                            SendMailUtil.send("641380205@qq.com","黄金-价格-卖出","今日金价："+goldPrice);
                        }else {
                            SendMailUtil.send("641380205@qq.com","黄金-价格","今日金价："+goldPrice);

                        }
                    }

                    @Override
                    public void onFailure(HttpException pE, String pS) {
                        SendMailUtil.send("641380205@qq.com","黄金-抓错",pS+"\n"+pE.getMessage());
                        Log.d("vbvb", "onFailure: "+pS);
                        LogUtil.info("vbvb", "MacdBgService onFailure()  : " +"黄金-抓错"+pS+"\n"+pE.getMessage());
                    }
                });
    }

    /**
     * 从html中获取黄金价格
     * @param htmlData
     * @return
     */
    private double getGoldPrice(String htmlData){
        try {
            int startIndex = htmlData.indexOf("<li><p>上海金早盘价（元）");
            int endIndex = startIndex;
            while(true){
                String temp = htmlData.substring(endIndex,endIndex+5);
                if(temp.equals("</li>")){
                    break;
                }
                endIndex ++ ;
            }
            String liData = htmlData.substring(startIndex,endIndex+5);
            int goldStartIndex = liData.indexOf("\">");
            int goldEndIndex = liData.indexOf("</span>");
            String goldPrice = liData.substring(goldStartIndex+2,goldEndIndex);
            return Double.parseDouble(goldPrice);
        } catch (Exception e) {
            SendMailUtil.send("641380205@qq.com","黄金-抓取出错",e.getMessage());
            e.printStackTrace();
            LogUtil.info("vbvb", "MacdBgService getGoldPrice()  : " + "黄金-抓取出错"+e.getMessage());
            return 0;
        }
    }
}
