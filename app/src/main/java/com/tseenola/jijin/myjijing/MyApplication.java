package com.tseenola.jijin.myjijing;

import android.app.Application;
import android.content.Context;

import com.tseenola.jijin.myjijing.utils.DialogUtils;
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
        mAppContext = this;
        LitePal.initialize(this);
        ThreadUtil.initRunOnUiThreadHandler();
    }

    private static MyApplication mAppContext;
    public synchronized static MyApplication getInstance() {
        if (mAppContext == null) {
            mAppContext = new MyApplication();
        }
        return mAppContext;
    }
}
