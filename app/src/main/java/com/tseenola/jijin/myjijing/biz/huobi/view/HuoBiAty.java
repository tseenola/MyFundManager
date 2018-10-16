package com.tseenola.jijin.myjijing.biz.huobi.view;

import android.util.Log;
import android.widget.Button;
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
    private String dmain = "https://api.huobi.br.com";
    private String kLineUrl = "/market/history/kline?symbol=btcusdt&period=1day";

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

    @OnClick(R.id.bt_GetKLine)
    public void onClick() {
        String url = dmain + kLineUrl;
        HttpUtils lHttpUtils = new HttpUtils();
        lHttpUtils.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
                        Gson lGson = new Gson();
                        Log.d("vbvb", "onSuccess: "+pResponseInfo.result);
                        HistoryKLine lHistoryKLine = lGson.fromJson(pResponseInfo.result,HistoryKLine.class);
                        LineAty.launch(HuoBiAty.this,lHistoryKLine);
                    }

                    @Override
                    public void onFailure(HttpException pE, String pS) {
                        Toast.makeText(HuoBiAty.this, pS, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
