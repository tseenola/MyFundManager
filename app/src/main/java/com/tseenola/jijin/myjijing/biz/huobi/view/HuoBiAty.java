package com.tseenola.jijin.myjijing.biz.huobi.view;

import android.util.Log;
import android.view.View;
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
    @Bind(R.id.bt_ShowKLine)
    Button btShowKLine;
    @Bind(R.id.bt_KLineBollBackTest)
    Button btKLineBollBackTest;
    @Bind(R.id.bt_MACDBackTest)
    Button mBtMACDBackTest;
    private String dmain = "https://api.huobi.br.com";
    private String kLineUrl = "/market/history/kline?symbol=htusdt&period=15min&size=500";
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
        onStart("连接服务器");
        String url = dmain + kLineUrl;
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

    @OnClick({R.id.bt_GetKLine, R.id.bt_ShowKLine, R.id.bt_KLineBollBackTest,R.id.bt_MACDBackTest})
    public void onViewClicked(View view) {
        if (view.getId() != R.id.bt_GetKLine && mHistoryKLine == null) {
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
            default:
                break;
        }
    }

}
