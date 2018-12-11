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
import com.tseenola.jijin.myjijing.biz.mail.SendMailUtil;
import com.tseenola.jijin.myjijing.utils.DateUtils;
import com.tseenola.jijin.myjijing.utils.ThreadUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by lenovo on 2018/11/6.
 * 描述：均线斜率买卖规则
 * 1.如果20日均线斜率为上升趋势买入。如果图片boll线上轨就卖出
 *
 */
public class MaBgService extends Service {
    private static final int STATUS_NULL = 0;//非持有状态
    private static final int STATUS_HOLD = 1;//持有状态
    private String dmain = "https://api.huobi.br.com";
    //private String dmain = "https://api.huobi.pro";
    private String [] mSymbols = {
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
            ,"eoshusd"};
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
    private List<PointValue> mPointValues_AVG20;//收盘建20日均线
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
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MaBgService.class.getName());
        wakeLock.acquire();
        //抓取数据
        getDataAndAnlyse();
        Log.d("vbvb", "onCreate: 线程：onCreate"+Thread.currentThread().getId());
    }

    private void getDataAndAnlyse() {
        ThreadUtil.runSingleScheduledService(new Runnable() {
            @Override
            public void run() {
                mCurSymbo = 0;
                getData();//抓取火
            }
        },0,8,TimeUnit.HOURS);
    }

    private void analyseData() {

    }

    private void parseData() {
        List<HistoryKLine.DataBean> lDataBeans = mHistoryKLine.getData();
        mPointValues_Y = new ArrayList<PointValue>();//y轴值，实际价格
        mPointValues_AVG20 = new ArrayList<PointValue>();
        mDate = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < lDataBeans.size(); i++) {
            mDate.add(DateUtils.getNextDay(date,-(lDataBeans.size() - i - 1),format));
            // 收盘值
            double closeVal = lDataBeans.get(lDataBeans.size() - i - 1).getClose();
            mPointValues_Y.add(new PointValue(i, (float) closeVal));//净值

            //mPointValues_AVG20.add(new PointValue(i,));
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
                            if (mHistoryKLine.getData()!=null && mHistoryKLine.getData().size()>30){//小于1个月的就算啦
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
}
