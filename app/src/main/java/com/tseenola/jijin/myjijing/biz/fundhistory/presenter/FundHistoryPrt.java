package com.tseenola.jijin.myjijing.biz.fundhistory.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundhistory.view.FundHistoryAty;
import com.tseenola.jijin.myjijing.biz.fundhistory.view.IFundHistoryAty;
import com.tseenola.jijin.myjijing.utils.Constant;

import org.litepal.crud.DataSupport;

/**
 * Created by lenovo on 2018/6/1.
 * 描述：
 */

public class FundHistoryPrt implements IFundHistoryPrt {
    private IFundHistoryAty mContext;

    public FundHistoryPrt(IFundHistoryAty pContext) {
        mContext = (IFundHistoryAty) pContext;
    }

    @Override
    public void loadDatas(Object pO,String pTaskName) {
        String fundCode = (String) pO;
        String url = String.format(Constant.Url.FUND_HISTORY,fundCode);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
                        Log.d("vbvb", "onSuccess: ");
                        String result = pResponseInfo.result;
                        if (!TextUtils.isEmpty(pResponseInfo.result) &&
                                result.contains("fS_name") &&
                                result.contains("fS_code") &&
                                result.contains("fund_Rate") &&
                                result.contains("fund_minsg") &&
                                result.contains("Data_netWorthTrend")&&
                                result.contains("Data_grandTotal")){
                            String results [] = result.split(";");
                            FundInfo lFundInfo = new FundInfo();
                            for(int i = 0;i<results.length;i++){
                                if(!TextUtils.isEmpty(results[i])){
                                    if (results[i].contains("fS_name")){
                                        lFundInfo.setfSName(getFundAttr(results[i]));
                                    } else if (results[i].contains("fS_code")) {
                                        lFundInfo.setfSCode(getFundAttr(results[i]));
                                    } else if (results[i].contains("fund_Rate")) {
                                        lFundInfo.setFundRate(getFundAttr(results[i]));
                                    }else if (results[i].contains("fund_minsg")) {//最小申购金额
                                        lFundInfo.setFundMinsg(getFundAttr(results[i]));
                                    }else if (results[i].contains("Data_netWorthTrend")) {//单位净值走势
                                        StringBuilder lDataNetBuilder = new StringBuilder("{\"DataNetWorthTrend\":");
                                        lDataNetBuilder.append(getFundList(results[i]));
                                        lDataNetBuilder.append("}");
                                        lFundInfo.setDataNetWorthTrend(lDataNetBuilder.toString());
                                    }else if(results[i].contains("Data_grandTotal")){
                                        StringBuilder lDataGrand = new StringBuilder("{\"DataGrandTotal\":");
                                        lDataGrand.append(getFundList(results[i]));
                                        lDataGrand.append("}");
                                        lFundInfo.setDataGrandTotal(lDataGrand.toString());
                                    }
                                }
                            }
                            mContext.onLoadDatasSucc(lFundInfo,Constant.DATA_SOURCE.FROM_NET);
                        }
                    }

                    @Override
                    public void onFailure(HttpException pE, String pS) {
                        mContext.onLoadDataFail(pS,Constant.DATA_SOURCE.FROM_NET);
                    }
                });


    }

    private String getFundList(String pS) {
        int startIndex = pS.indexOf("[{");
        int endIndex = pS.lastIndexOf("}]");
        return pS.substring(startIndex,endIndex+2);
    }

    private String getFundAttr(String pS) {
        int startIndex = pS.indexOf("\"");
        int endIndex = pS.lastIndexOf("\"");
        return pS.substring(startIndex,endIndex+1).replaceAll("\"","");
    }

    @Override
    public void saveFundInfo(FundInfo pFundInfos) {
        Log.d("vbvb", "saveFundList: 更新数据库基金列表");
        if (DataSupport.where("fS_code = ?",pFundInfos.getfSCode()).count(FundInfo.class)<=0){
            boolean succ = pFundInfos.save();
            Toast.makeText((FundHistoryAty)mContext, "保存:"+succ, Toast.LENGTH_SHORT).show();
        }else {
            int num = pFundInfos.updateAll("fS_code = ?",pFundInfos.getfSCode());
            Toast.makeText((FundHistoryAty)mContext, "更新："+num, Toast.LENGTH_SHORT).show();
            Log.d("vbvb", "saveFundInfo: "+num);
        }
    }
}
