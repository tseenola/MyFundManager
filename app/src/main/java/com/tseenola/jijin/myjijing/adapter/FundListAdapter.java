package com.tseenola.jijin.myjijing.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.biz.fundlist.model.FundListInfo;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2018/5/30.
 * 描述：
 */

public class FundListAdapter extends CommonAdapter<FundListInfo>{
    private Map<Integer,Boolean> mCbSelectedStatus;
    public Map<Integer,Boolean> getCbSelectedMap(){
        return mCbSelectedStatus;
    }

    public FundListAdapter(Context context, List<FundListInfo> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        mCbSelectedStatus = new HashMap<>();// 存放已被选中的CheckBox
    }

    @Override
    public void convert(CommonViewHolder pCommonViewHolder, final FundListInfo pFundInfo, final int pPosition) {
        pCommonViewHolder.setText(R.id.tv_FundCode,pFundInfo.getFundCode());
        pCommonViewHolder.setText(R.id.tv_FundAbbr,pFundInfo.getFundAbbr());
        pCommonViewHolder.setText(R.id.tv_FundName,pFundInfo.getFundName());
        pCommonViewHolder.setText(R.id.tv_FundType,pFundInfo.getFundType());
        CheckBox lCbSelected = pCommonViewHolder.getView(R.id.cb_Selected);
        lCbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton pCompoundButton, boolean pB) {
                if(pB){
                    mCbSelectedStatus.put(pPosition,true);
                }else {
                    mCbSelectedStatus.remove(pPosition);
                }
                upDateDB(pFundInfo,pB);

            }
        });
        if(mCbSelectedStatus.containsKey(pPosition)){
            lCbSelected.setChecked(true);
        }else {
            lCbSelected.setChecked(false);
        }
    }

    protected void upDateDB(FundListInfo pFundInfo,boolean pB) {
        pFundInfo.setSelected(pB);
        boolean succ = pFundInfo.save();
        Log.d("vbvb", "upDateDB: "+succ);
    }
}
