package com.tseenola.jijin.myjijing.base.view;

import com.tseenola.jijin.myjijing.utils.Constant;

/**
 * Created by lenovo on 2018/5/30.
 * 描述：
 */

public interface IBaseAty {
    //初始化presenter
    void bindPresenter();

    void onStart(Object pO);

    void onLoading(Object pO,long total, long current, boolean isUploading);

    void onLoadDatasSucc(Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource);

    void onLoadDataFail(Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource);

    void onCancelled(Object pO);
}
