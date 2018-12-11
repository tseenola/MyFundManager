package com.tseenola.jijin.myjijing;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.gson.Gson;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;
import com.tseenola.jijin.myjijing.biz.fundstrategy.model.DataNetWorthTrend;
import com.tseenola.jijin.myjijing.utils.WRUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.litepal.crud.DataSupport;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private List<FundListInfo> mFundListInfos;

    @Test
    public void useAppContext() throws Exception {
        mFundListInfos = DataSupport.where("selected = 1").find(FundListInfo.class);

        for(int i = 0;i<mFundListInfos.size();i++){
            Log.d("vbvb", "useAppContext: first "+mFundListInfos.get(i).getFundCode());
        }

        Log.d("vbvb", "useAppContext: ==========================================================");


        int pDay = 3;
        List<FundInfo> lFundInfos = DataSupport.findAll(FundInfo.class);
        for(int i = 0;i<lFundInfos.size();i++){
            Gson lGson = new Gson();
            DataNetWorthTrend lWorthTrends = lGson.fromJson(lFundInfos.get(i).getDataNetWorthTrend(),DataNetWorthTrend.class);
            List<DataNetWorthTrend.DataNetWorthTrendBean> lDataNetWorthTrendBeens = lWorthTrends.getDataNetWorthTrend();
            int totalDays = lDataNetWorthTrendBeens.size();
            double lastNetWorth = lDataNetWorthTrendBeens.get(totalDays-1).getY();
            Log.d("vbvb", "useAppContext: lastNetWorth:"+lastNetWorth);
            for (int j = totalDays-2;
                 j >= (totalDays>pDay?totalDays-pDay:pDay-1); j--) {
                 Double y = lDataNetWorthTrendBeens.get(j).getY();
                 if (lastNetWorth>y){
                    //移除
                     for(int z = mFundListInfos.size() - 1; z >= 0; z--){
                         FundListInfo item = mFundListInfos.get(z);
                         if(lFundInfos.get(i).getfSCode().equals(item.getFundCode())){
                             mFundListInfos.remove(item);
                         }
                     }
                 }
                 Log.d("vbvb", "useAppContext: code :"+lFundInfos.get(i).getfSCode()+" y:"+y);
            }
            Log.d("vbvb", "useAppContext: 结束");
        }


        for(int i = 0;i<mFundListInfos.size();i++){
            Log.d("vbvb", "useAppContext: last "+mFundListInfos.get(i).getFundCode());
        }

        Log.d("vbvb", "useAppContext: ==========================================================");
    }


    @Test
    public void testWR() throws Exception {
        List<Double> lList = new ArrayList<>();
        lList.add(15d);
        lList.add(12d);
        lList.add(9d);
        lList.add(13d);


        double wr = WRUtils.getWR(lList,5);
        int i = 0;
    }

    @Test
    public void testGetAccountId(){
        String appSecretKey = "5cb99be0-b3387cx9-0ce79aa7-xxxxx";
        String payload = "GET\nbe.huobi.com\n/v1/account/accounts\nAccessKeyId=a1a17ddf-466fde2x-db04b9aa-xxxxx&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2017-06-02T06%3A13%3A49";
        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secKey =
                    new SecretKeySpec(appSecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key: " + e.getMessage());
        }
        byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String actualSign = Base64.getEncoder().encodeToString(hash);
        int i = 0;
    }
}
