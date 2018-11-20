package com.tseenola.jijin.myjijing;

import android.app.Application;
import android.os.Environment;

import com.tseenola.jijin.myjijing.utils.LogUtil;
import com.tseenola.jijin.myjijing.utils.ThreadUtil;

import org.litepal.LitePal;

import java.io.File;

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
        initLog();
    }

    private void initLog() {
        // 设置日志保存路径
        File savePath = new File(Environment.getExternalStorageDirectory(), "Log");
        if (!savePath.exists()) {
            savePath.mkdir();
        }
        File logPath = new File(savePath, "MyLog");
        if (!logPath.exists()) {
            logPath.mkdir();
        }
        LogUtil.setLogPath(logPath);
    }

    private static MyApplication mAppContext;
    public synchronized static MyApplication getInstance() {
        if (mAppContext == null) {
            mAppContext = new MyApplication();
        }
        return mAppContext;
    }
}
