package com.tseenola.jijin.myjijing.biz.fundstrategy.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundhistory.view.FundHistoryAty;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;
import com.tseenola.jijin.myjijing.biz.fundstrategy.view.FundStrategyAty;
import com.tseenola.jijin.myjijing.biz.fundstrategy.view.IFundStrategyAty;
import com.tseenola.jijin.myjijing.utils.Constant;

import org.litepal.crud.DataSupport;

/**
 * Created by lenovo on 2018/6/7.
 * 描述：
 */

public class FundStrategyPrt implements IFundStrategyPrt{
    private IFundStrategyAty mContext;
    public FundStrategyPrt(IFundStrategyAty pIFundStrategyAty){
        mContext = pIFundStrategyAty;
    }

    @Override
    public void loadDatas(Object pO, @Constant.TaskName.NameList String pTaskName) {
        getDatas((FundListInfo) pO);
    }

    private void getDatas(final FundListInfo pFundListInfo) {
        String fundCode = pFundListInfo.getFundCode();
        String url = String.format(Constant.Url.FUND_HISTORY,fundCode);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
                        try{
                            String result = pResponseInfo.result;
                            if (!TextUtils.isEmpty(pResponseInfo.result) &&
                                    result.contains("fS_name") &&
                                    result.contains("fS_code") &&
                                    result.contains("fund_Rate") &&
                                    result.contains("fund_minsg") &&
                                    result.contains("Data_netWorthTrend") &&
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
                                if (DataSupport.where("fSCode = ?",lFundInfo.getfSCode()).count(FundInfo.class)<=0){
                                    boolean ret = lFundInfo.save();
                                    Log.d("vbvb", "save: 新增列表：ret"+ret);
                                }else {
                                    int num = lFundInfo.updateAll("fSCode = ?",lFundInfo.getfSCode());
                                    Log.d("vbvb", "updateAll: 更新数据库基金列表 num:"+num);
                                }
                                mContext.onLoadDatasSucc(pFundListInfo.getFundCode(),Constant.DATA_SOURCE.FROM_NET);
                                Log.d("vbvb", "onSuccess: "+"加载："+lFundInfo.getfSCode()+"成功");
                            }else {
                                mContext.onLoadDataFail(pFundListInfo.getFundCode(),Constant.DATA_SOURCE.FROM_NET);
                            }
                        }catch(Exception pE){
                            mContext.onLoadDataFail(pFundListInfo.getFundCode(),Constant.DATA_SOURCE.FROM_NET);
                            pE.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException pE, String pS) {
                        Log.d("vbvb", "onFailure: reason:"+pS);
                        mContext.onLoadDataFail(pFundListInfo.getFundCode(),Constant.DATA_SOURCE.FROM_NET);
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
    public void showHistryByChart(FundInfo pFundInfo) {
        Intent lIntent = new Intent((FundStrategyAty)mContext,FundHistoryAty.class);
        lIntent.putExtra("FundInfo",pFundInfo);
        ((FundStrategyAty)mContext).startActivity(lIntent);
    }
}
