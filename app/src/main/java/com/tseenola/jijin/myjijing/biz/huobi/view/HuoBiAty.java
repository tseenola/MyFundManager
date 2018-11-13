package com.tseenola.jijin.myjijing.biz.huobi.view;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tseenola.jijin.myjijing.LineAty;
import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.huobi.model.HistoryKLine;
import com.tseenola.jijin.myjijing.service.MacdBgService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/10/13.
 * 描述：
 */

public class HuoBiAty extends BaseAty {
    @Bind(R.id.bt_GetKLine)
    Button mBtGetKLine;
    @Bind(R.id.bt_ShowKLine)
    Button btShowKLine;
    @Bind(R.id.bt_KLineBollBackTest)
    Button btKLineBollBackTest;
    @Bind(R.id.bt_MACDBackTest)
    Button mBtMACDBackTest;
    @Bind(R.id.et_CoinType)
    EditText etCoinType;
    @Bind(R.id.et_Time)
    EditText etTime;
    @Bind(R.id.bt_StartMacdService)
    Button mBtStartMacdService;
    @Bind(R.id.et_Size)
    EditText mEtSize;
    private String dmain = "https://api.huobi.br.com";
    //private String dmain = "https://api.huobi.pro";
    private String kLineUrl = "/market/history/kline?symbol=%s&period=%s&size=%s";
    private HistoryKLine mHistoryKLine;


    @Override
    public void initData() {
        setContentView(R.layout.activity_huobi);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindPresenter() {

    }

    private void getKLine() {
        String conType = etCoinType.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String size = mEtSize.getText().toString().trim();

        if (TextUtils.isEmpty(conType) || TextUtils.isEmpty(time)) {
            Toast.makeText(this, "时间或者币种不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        onStart("连接服务器");
        String url = dmain + String.format(kLineUrl, conType, time,size);
        HttpUtils lHttpUtils = new HttpUtils();
        lHttpUtils.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
                        onLoadDatasSucc(null, null);
                        Gson lGson = new Gson();
                        Log.d("vbvb", "onSuccess: " + pResponseInfo.result);
                        mHistoryKLine = lGson.fromJson(pResponseInfo.result, HistoryKLine.class);
                        Toast.makeText(HuoBiAty.this, "获取成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HttpException pE, String pS) {
                        onLoadDataFail(null, null);
                        Toast.makeText(HuoBiAty.this, pS, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick({R.id.bt_GetKLine, R.id.bt_ShowKLine, R.id.bt_KLineBollBackTest, R.id.bt_MACDBackTest, R.id.bt_StartMacdService})
    public void onViewClicked(View view) {
        if (view.getId() != R.id.bt_GetKLine && view.getId() != R.id.bt_StartMacdService && mHistoryKLine == null) {
            Toast.makeText(this, "请先获取历史K线", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (view.getId()) {
            case R.id.bt_GetKLine:
                getKLine();
                break;
            case R.id.bt_ShowKLine:
                LineAty.launch(HuoBiAty.this, mHistoryKLine);
                break;
            case R.id.bt_KLineBollBackTest:
                BollBackTestAty.launch(this, mHistoryKLine);
                break;
            case R.id.bt_MACDBackTest:
                MACDBackTestAty.launch(this, mHistoryKLine);
                break;
            case R.id.bt_StartMacdService:
                startService(new Intent(this, MacdBgService.class));
                break;
            default:
                break;
        }
    }
    /**
     * https://api.huobi.br.com
     */
//    private void getAcountId() {
//
//        String secretKey = "90514900-9ece7df1-0d672f05-e3875";
//
//        String method = "GET\n";
//        String domain = "api.huobi.br.com\n";
//        String addr = "/v1/account/accounts\n";
//
//        String AccessKeyId="AccessKeyId=bdd8fa7e-8c837a64-2a509cb1-57ff7";
//        String SignatureMethod="SignatureMethod=HmacSHA256";
//        String SignatureVersion = "SignatureVersion=2";
//        String Timestamp = "Timestamp="+Uri.encode(TimeUtils.getUtcTime());
//
//        String data = AccessKeyId +"&"+ SignatureMethod +"&"+ SignatureVersion +"&"+ Timestamp;
//        String needEncryData = method + domain + addr + data;
//        String hmacData = KeyUtils.hmacSha256(secretKey,needEncryData);
//        String preSignaturej = KeyUtils.getBase64(hmacData);
//
//        String signature = "Signature="+Uri.encode(preSignaturej);
//
//
//        String url = "https://api.huobi.br.com/v1/account/accounts?"+data + "&" + signature;
//        Log.d("vbvb", "getAcountId: "+url);
//        com.lidroid.xutils.HttpUtils lHttpUtils = new com.lidroid.xutils.HttpUtils();
//        lHttpUtils.send(HttpRequest.HttpMethod.GET,
//                url,
//                new RequestCallBack<String>() {
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
//                        Log.d("vbvb", "onSuccess: "+pResponseInfo.result);
//                        Toast.makeText(HuoBiAty.this, pResponseInfo.result, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(HttpException pE, String pS) {
//                        Log.d("vbvb", "onFailure: "+pS);
//                    }
//                });
//    }
//
//    private void getAcountId2() {
//        String accessKey = "bdd8fa7e-8c837a64-2a509cb1-57ff7";
//        String secretKey = "90514900-9ece7df1-0d672f05-e3875";
//        String uri = "/v1/account/accounts";
//
//        String AccessKeyId="AccessKeyId=bdd8fa7e-8c837a64-2a509cb1-57ff7";
//        String SignatureMethod="SignatureMethod=HmacSHA256";
//        String SignatureVersion = "SignatureVersion=2";
//
//        Map<String,String> params = new HashMap<>();
//        getSignature(accessKey,secretKey,uri,params);
//        String preSignaturej = params.get("Signature");
//        String Timestamp = "Timestamp="+Uri.encode(params.get("Timestamp"));
//
//        String data = AccessKeyId +"&"+ SignatureMethod +"&"+ SignatureVersion +"&"+ Timestamp;
//
//        String signature = "Signature="+Uri.encode(preSignaturej);
//
//
//        String url = "https://api.huobi.pro/v1/account/accounts?"+data + "&" + signature;
//        //String url = "https://api.huobi.br.com/v1/account/accounts?"+data + "&" + signature;
//        Log.d("vbvb", "getAcountId: "+url);
//        com.lidroid.xutils.HttpUtils lHttpUtils = new com.lidroid.xutils.HttpUtils();
//        lHttpUtils.send(HttpRequest.HttpMethod.GET,
//                url,
//                new RequestCallBack<String>() {
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
//                        Log.d("vbvb", "onSuccess: "+pResponseInfo.result);
//                        Toast.makeText(HuoBiAty.this, pResponseInfo.result, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(HttpException pE, String pS) {
//                        Log.d("vbvb", "onFailure: "+pS);
//                    }
//                });
//    }
//
//    private String getSignature(String appKey,String appSecretKey,String uri,Map<String,String> params){
//        String method = "GET";
//        String host = "api.huobi.pro";
//        //String host = "api.huobi.br.com";
//
//        ApiSignature lApiSignature = new ApiSignature();
//        lApiSignature.createSignature(appKey,appSecretKey,method,host,uri,params);
//        Log.d("vbvb", "getAccountId: ");
//
//        return Uri.encode(params.get("Signature"));
//    }
}
