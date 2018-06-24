package com.tseenola.jijin.myjijing.utils;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.tseenola.jijin.myjijing.MyApplication;

/**
 * Created by lenovo on 2018/6/15.
 * 描述：
 */

public class DialogUtils {
    private static MaterialDialog mDialog;

    public static void showCirDailog(final String pShowText) {
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(MyApplication.getInstance())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                MyApplication.getInstance().startActivity(intent);
            } else {
                //绘ui代码, 这里说明6.0系统已经有权限了
                show(pShowText);
            }
        } else {
                //绘ui代码,这里android6.0以下的系统直接绘出即可
            show(pShowText);
        }
    }

    private static void show(final String pShowText) {
        ThreadUtil.runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 if (mDialog==null){
                     Log.d("vbvb", "showCirDailog: new 了一个dialog");
                     mDialog = new MaterialDialog.Builder(MyApplication.getInstance())
                             .content(pShowText)
                             .progress(true, 0)
                             .theme(Theme.LIGHT)
                             .build();
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//6.0
                         mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                     }else {
                         mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                     }
                     mDialog.show();
                 }else {
                     mDialog.setContent(pShowText);
                     if (!mDialog.isShowing()){
                         mDialog.setContent(pShowText);
                         mDialog.show();
                     }
                 }
             }
         });
    }

    public static void dissmisCirDialog(){
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog!=null && mDialog.isShowing()){
                    Log.d("vbvb", "dissmisCirDialog: 关闭dialog");
                    mDialog.dismiss();
                }
            }
        });
    }

}
