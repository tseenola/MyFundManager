package com.tseenola.jijin.myjijing;

import android.app.Application;

import com.tseenola.jijin.myjijing.utils.ThreadUtil;

import org.litepal.LitePal;

/**
 * Created by lenovo on 2018/6/2.
 * 描述：
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(this);
        ThreadUtil.initRunOnUiThreadHandler();
    }
}
