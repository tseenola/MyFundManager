package com.tseenola.jijin.myjijing.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by lenovo on 2018/11/6.
 * 描述：
 */
public class MacdBgService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent pIntent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
