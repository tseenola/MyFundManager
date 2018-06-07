package com.tseenola.jijin.myjijing;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;

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
    @Test
    public void useAppContext() throws Exception {

        List<FundListInfo> newsList = DataSupport.select("FundCode").find(FundListInfo.class);
        Log.d("vbvb", "loadDatas: ");

    }
}
