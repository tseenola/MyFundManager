package com.tseenola.jijin.myjijing.biz.fundstrategy.presenter;

import com.tseenola.jijin.myjijing.biz.fundstrategy.view.IFundStrategyAty;
import com.tseenola.jijin.myjijing.utils.Constant;

import org.litepal.crud.DataSupport;

/**
 * Created by lenovo on 2018/6/7.
 * 描述：
 */

public class FundStrategyPrt implements IFundStrategyPrt{

    private IFundStrategyAty mContext;

    public FundStrategyPrt(IFundStrategyAty pIFundStrategyAty){
        mContext = pIFundStrategyAty;
    }

    @Override
    public void loadDatas(Object pO, @Constant.TaskName.NameList String pTaskName) {
        //1.找到基金列表。
        //DataSupport.findAll

        //2.获取每个基金的histrory,并且保存

    }
}
