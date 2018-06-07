package com.tseenola.jijin.myjijing.utils;

import android.os.Handler;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2016/10/15.
 * 描述：
 */

public class ThreadUtil {
    public static final int CACHED = 0;
    public static final int FIXED = 1;
    public static final int SINGLE_SCHEDULE = 2;
    private static ExecutorService mCachedService;
    private static ExecutorService mFixedService;
    private static ScheduledExecutorService mSingleScheduledService;
    private static long mMainThread;
    private static Handler mHandler;

    private ThreadUtil(){}

    /**
     * 这个函数要放在入口Application中执行，用于初始化ThreadUtil
     */
    public static void initRunOnUiThreadHandler() {
        mHandler = new Handler();
        mMainThread = (long) Process.myTid();
    }

    public static ExecutorService runCachedService(@NonNull Runnable runnable) {
        mCachedService = ThreadFactory(mCachedService, 0);
        mCachedService.execute(runnable);
        return mCachedService;
    }

    public static ExecutorService runFixedService(@NonNull Runnable runnable, int nThread) {
        mFixedService = ThreadFactory(mFixedService, 1, nThread == 0?1:nThread);
        mFixedService.execute(runnable);
        return mFixedService;
    }

    public static ScheduledExecutorService runSingleScheduledService(@NonNull Runnable runnable, long initialDelay, long delay, @NonNull TimeUnit unit) {
        mSingleScheduledService = (ScheduledExecutorService)ThreadFactory(mSingleScheduledService, 2);
        mSingleScheduledService.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
        return mSingleScheduledService;
    }

    @NonNull
    private static ExecutorService ThreadFactory(@Nullable ExecutorService service, int... params) {
        if(service == null) {
            synchronized(ThreadUtil.class) {
                if(service == null) {
                    switch(params[0]) {
                        case 0:
                            service = Executors.newCachedThreadPool();
                            break;
                        case 1:
                            service = Executors.newFixedThreadPool(params[1]);
                            break;
                        case 2:
                            service = Executors.newSingleThreadScheduledExecutor();
                    }
                }
            }
        }

        return (ExecutorService)service;
    }

    public static boolean runOnUiThread(@NonNull Runnable runnable) {
        long curThreadId = (long) Process.myTid();
        if(curThreadId == mMainThread) {
            runnable.run();
            return false;
        } else {
            return mHandler.post(runnable);
        }
    }

    public static boolean runOnUiThreadAtTime(@NonNull Runnable runnable, long runAtTime) {
        long curThreadId = (long) Process.myTid();
        if(curThreadId == mMainThread) {
            runnable.run();
            return false;
        } else {
            return mHandler.postAtTime(runnable, runAtTime);
        }
    }


    public static boolean runOnUiThreadDelay(@NonNull Runnable runnable, long delay) {
        long curThreadId = (long) Process.myTid();
        if(curThreadId == mMainThread) {
            runnable.run();
            return false;
        } else {
            return mHandler.postDelayed(runnable, delay);
        }
    }
}