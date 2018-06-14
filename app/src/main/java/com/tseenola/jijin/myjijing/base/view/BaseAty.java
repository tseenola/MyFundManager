package com.tseenola.jijin.myjijing.base.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.utils.Constant;
import com.tseenola.jijin.myjijing.utils.ThreadUtil;

/**
 * Created by lenovo on 2018/5/30.
 * 描述：
 */

public abstract class BaseAty<T> extends Activity implements IBaseAty,AdapterView.OnItemClickListener{
    protected MaterialDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("vbvb", "BaseAty onCreate currentThread: "+Thread.currentThread().getId());
        bindPresenter();
        initView();
        initData();
    }

    public abstract void initData();

    public abstract void initView();

    @Override
    public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {

    }

    @Override
    public void onStart(final Object pO) {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("vbvb", "BaseAty onStart currentThread: "+Thread.currentThread().getId());
                if (pO!=null){
                    mDialog = new MaterialDialog.Builder(BaseAty.this)
                            .content((String) pO)
                            .progress(true, 0)
                            .show();
                }else {
                    mDialog = new MaterialDialog.Builder(BaseAty.this)
                            .content("开始加载")
                            .progress(true, 0)
                            .show();
                }
            }
        });

    }

    @Override
    public void onLoadDatasSucc(final Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        onCancelled(null);
        /*ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog==null) {
                    mDialog = new MaterialDialog.Builder(BaseAty.this)
                            .content("加载中")
                            .progress(true, 0)
                            .show();
                }
                Log.d("vbvb", "BaseAty onLoadDatasSucc currentThread: "+Thread.currentThread().getId());
                mDialog.setContent((String)pO);
            }
        });*/
    }

    @Override
    public void onLoadDataFail(final Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        onCancelled(null);
        /*ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog==null) {
                    mDialog = new MaterialDialog.Builder(BaseAty.this)
                            .content("加载中")
                            .progress(true, 0)
                            .show();
                }
                Log.d("vbvb", "BaseAty onLoadDataFail currentThread: "+Thread.currentThread().getId());
                mDialog.setContent((String)pO);
            }
        });*/
    }

    @Override
    public void onLoading(final Object pO, long total, long current, boolean isUploading) {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog==null) {
                    mDialog = new MaterialDialog.Builder(BaseAty.this)
                            .content("加载中")
                            .progress(true, 0)
                            .show();
                }
                Log.d("vbvb", "BaseAty onLoading currentThread: "+Thread.currentThread().getId());
                mDialog.setContent((String)pO);
            }
        });

    }

    @Override
    public void onCancelled(Object pO) {
        if (mDialog==null) {
            return;
        }
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("vbvb", "BaseAty onCancelled currentThread: "+Thread.currentThread().getId());
                mDialog.dismiss();
            }
        });
    }
}
