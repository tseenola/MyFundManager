package com.tseenola.jijin.myjijing.biz.fundbaseinfo.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tseenola.jijin.myjijing.base.presenter.IBasePrt;
import com.tseenola.jijin.myjijing.base.view.IBaseAty;
import com.tseenola.jijin.myjijing.biz.fundbaseinfo.model.FundBaseInfo;
import com.tseenola.jijin.myjijing.utils.Constant;

/**
 * Created by lenovo on 2018/5/31.
 * 描述：
 */

public class FundBaseInfoPrt implements IBasePrt {
    private String mFundCode;
    private IBaseAty mContext;
    public FundBaseInfoPrt(IBaseAty pContext) {
        mContext = (IBaseAty) pContext;
    }

    @Override
    public void loadDatas(Object pO,String pTaskName) {
        mFundCode = (String)pO;
        String url = String.format(Constant.Url.FUND_DETAIL,mFundCode);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
                        String result = pResponseInfo.result.replaceAll("jsonpgz\\(","").replaceAll("\\);","");
                        Log.d("vbvb", "onSuccess: "+result);
                        Gson lGson = new Gson();
                        FundBaseInfo lFundDetail = lGson.fromJson(result, FundBaseInfo.class);
                        mContext.onLoadDatasSucc(lFundDetail,Constant.DATA_SOURCE.FROM_NET);
                    }

                    @Override
                    public void onFailure(HttpException pE, String pS) {
                        mContext.onLoadDataFail(pS,Constant.DATA_SOURCE.FROM_NET);
                    }
                });
    }
}
