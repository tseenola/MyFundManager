package com.tseenola.jijin.myjijing;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.gson.Gson;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;
import com.tseenola.jijin.myjijing.biz.fundstrategy.model.DataNetWorthTrend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.litepal.crud.DataSupport;

import java.util.List;

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
    public void testMACD() throws Exception {

    }

    @Test
    public void testGetAccountId(){
        String data = "4F65x5A2bLyMWVQj3Aqp+B4w+ivaA7n5Oi2SuYtCJ9o=";
        String s = Uri.encode(data);
        int i = 0;
    }
}
