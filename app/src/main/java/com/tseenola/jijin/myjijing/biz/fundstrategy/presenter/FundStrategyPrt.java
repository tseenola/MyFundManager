package com.tseenola.jijin.myjijing.biz.fundstrategy.presenter;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;
import com.tseenola.jijin.myjijing.biz.fundstrategy.view.IFundStrategyAty;
import com.tseenola.jijin.myjijing.utils.Constant;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by lenovo on 2018/6/7.
 * 描述：
 */

public class FundStrategyPrt implements IFundStrategyPrt{

    private IFundStrategyAty mContext;
    private List<FundListInfo> mFundCodes;
    private int mCurIndex;

    public FundStrategyPrt(IFundStrategyAty pIFundStrategyAty){
        mContext = pIFundStrategyAty;
    }

    @Override
    public void loadDatas(Object pO, @Constant.TaskName.NameList String pTaskName) {
        final FundListInfo lFundListInfo = (FundListInfo) pO;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                mContext.onLoadDatasSucc(lFundListInfo.getFundCode(),Constant.DATA_SOURCE.FROM_NET);
            }
        }).start();

        /*mContext.onStart(null);
        List<FundListInfo> lFundListInfos = (List<FundListInfo>) pO;
        getDatas();*/
    }

    private void getDatas() {
        String fundCode = mFundCodes.get(mCurIndex).getFundCode();
        String url = String.format(Constant.Url.FUND_HISTORY,fundCode);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
                        String result = pResponseInfo.result;
                        if (!TextUtils.isEmpty(pResponseInfo.result) &&
                                result.contains("fS_name") &&
                                result.contains("fS_code") &&
                                result.contains("fund_Rate") &&
                                result.contains("fund_minsg") &&
                                result.contains("Data_netWorthTrend")){
                            String results [] = result.split(";");
                            FundInfo lFundInfo = new FundInfo();
                            for(int i = 0;i<results.length;i++){
                                if(!TextUtils.isEmpty(results[i])){
                                    if (results[i].contains("fS_name")){
                                        lFundInfo.setfS_name(getFundAttr(results[i]));
                                    } else if (results[i].contains("fS_code")) {
                                        lFundInfo.setfS_code(getFundAttr(results[i]));
                                    } else if (results[i].contains("fund_Rate")) {
                                        lFundInfo.setFund_Rate(getFundAttr(results[i]));
                                    }else if (results[i].contains("fund_minsg")) {//最小申购金额
                                        lFundInfo.setFund_minsg(getFundAttr(results[i]));
                                    }else if (results[i].contains("Data_netWorthTrend")) {//单位净值走势
                                        lFundInfo.setData_netWorthTrend(getFundList(results[i]));
                                    }
                                }
                            }
                            if (DataSupport.where("fS_code = ?",lFundInfo.getfS_code()).count(FundInfo.class)<=0){
                                boolean ret = lFundInfo.save();
                                Log.d("vbvb", "save: 新增列表：ret"+ret);
                            }else {
                                int num = lFundInfo.updateAll("fS_code = ?",lFundInfo.getfS_code());
                                Log.d("vbvb", "updateAll: 更新数据库基金列表 num:"+num);
                            }
                            mContext.onLoading("加载："+lFundInfo.getfS_code()+"成功",0,0,false);
                            Log.d("vbvb", "onSuccess: "+"加载："+lFundInfo.getfS_code()+"成功");
                        }
                    }

                    @Override
                    public void onFailure(HttpException pE, String pS) {
                        Log.d("vbvb", "onFailure: reason:"+pS);
                        mContext.onLoading("加载失败",0,0,false);
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
        return pS.substring(startIndex,endIndex+1);
    }
}
