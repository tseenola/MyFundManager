package com.tseenola.jijin.myjijing.base.presenter;

import com.tseenola.jijin.myjijing.utils.Constant;

/**
 * Created by lenovo on 2018/5/30.
 * 描述：
 */

public interface IBasePrt<T> {
    void loadDatas(T pT,@Constant.TaskName.NameList String pTaskName);
}
