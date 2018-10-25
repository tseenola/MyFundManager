package com.tseenola.jijin.myjijing.base.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.tseenola.jijin.myjijing.utils.Constant;
import com.tseenola.jijin.myjijing.utils.DialogUtils;

/**
 * Created by lenovo on 2018/5/30.
 * 描述：
 */

public abstract class BaseAty<T> extends Activity implements IBaseAty,AdapterView.OnItemClickListener{

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
        DialogUtils.showCirDailog((String) pO);

    }

    @Override
    public void onLoadDatasSucc(final Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        onCancelled(null);
    }

    @Override
    public void onLoadDataFail(final Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        onCancelled(null);
    }

    @Override
    public void onLoading(final Object pO, long total, long current, boolean isUploading) {
    }

    @Override
    public void onCancelled(Object pO) {
        DialogUtils.dissmisCirDialog();
    }
}
