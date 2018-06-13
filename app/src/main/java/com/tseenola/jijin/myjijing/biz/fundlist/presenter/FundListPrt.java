package com.tseenola.jijin.myjijing.biz.fundlist.presenter;

import android.os.SystemClock;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;
import com.tseenola.jijin.myjijing.biz.fundlist.view.IFundListAty;
import com.tseenola.jijin.myjijing.utils.Constant;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static org.litepal.crud.DataSupport.findAll;

/**
 * Created by lenovo on 2018/5/30.
 * 描述：
 */

public class FundListPrt implements IFundListPrt {
    private IFundListAty mContext;
    public FundListPrt(IFundListAty pContext) {
        mContext = (IFundListAty) pContext;
    }

    @Override
    public void loadDatas(Object pO,String pTaskName) {
        /**
         * 加载数据
         * 基金列表1周更新一次，周期可以通过配置文件配置
         * 基金只显示非自选的基金
         */
        if (DataSupport.count(FundListInfo.class)==0){
            downLoadFundlist();
        }else {
            //先来个5天更新一次吧
            if (Calendar.getInstance().get(Calendar.DATE)%30==0){
                downLoadFundlist();
            }else {
                Log.d("vbvb", "从数据库更新: ");
                DataSupport.where("selected = ?","0").findAsync(FundListInfo.class).listen(new FindMultiCallback() {
                    @Override
                    public <T> void onFinish(List<T> t) {
                        List<FundListInfo> lFundListInfos = (List<FundListInfo>) t;
                        if (lFundListInfos==null ||lFundListInfos.size()<=0){
                            mContext.onLoadDataFail("从数据库获取基金列表失败",Constant.DATA_SOURCE.FROM_DB);
                        }else {
                            mContext.onLoadDatasSucc(lFundListInfos,Constant.DATA_SOURCE.FROM_DB);
                        }
                    }
                });
            }
        }
    }

    private void downLoadFundlist() {
        Log.d("vbvb", "从网站更新: ");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                Constant.Url.FUND_LIST,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> pResponseInfo) {
                        Log.d("vbvb", "downLoadFundlist onSuccess currentThread: "+Thread.currentThread().getId());
                        String result = pResponseInfo.result.replaceAll("var r = ","").replaceAll("\\[","").replaceAll("\\]","").replaceAll(";","").replaceAll(" ","")
                                .replaceAll("\"","");
                        String array [] = result.split(",");
                        List<FundListInfo> lFundInfos = new ArrayList<FundListInfo>();
                        for (int i = 0; i < array.length/5; i++) {
                            FundListInfo lFundInfo = new FundListInfo();
                            int j = 0;
                            lFundInfo.setFundCode(array[i*5+j]);
                            j++;
                            lFundInfo.setFundAbbr(array[i*5+j]);
                            j++;
                            lFundInfo.setFundName(array[i*5+j]);
                            j++;
                            lFundInfo.setFundType(array[i*5+j]);
                            j++;
                            lFundInfo.setFundPingYing(array[i*5+j]);
                            j++;
                            lFundInfos.add(lFundInfo);
                        }
                        mContext.onLoadDatasSucc(lFundInfos,Constant.DATA_SOURCE.FROM_NET);
                    }

                    @Override
                    public void onFailure(HttpException pE, String pS) {
                        Log.d("vbvb", "downLoadFundlist onFailure currentThread: "+Thread.currentThread().getId());
                        mContext.onLoadDataFail(pS,Constant.DATA_SOURCE.FROM_NET);
                    }
                });
    }

    @Override
    public void saveFundList(List<FundListInfo> pFundInfos) {
        Log.d("vbvb", "saveFundList: 更新数据库基金列表");
        DataSupport.deleteAll(FundListInfo.class);
        DataSupport.saveAll(pFundInfos);
    }
}
