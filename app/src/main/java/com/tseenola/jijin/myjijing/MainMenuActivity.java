package com.tseenola.jijin.myjijing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tseenola.jijin.myjijing.biz.fundhistory.view.FundHistoryAty;
import com.tseenola.jijin.myjijing.biz.fundlist.view.FundListAty;
import com.tseenola.jijin.myjijing.biz.fundstrategy.view.FundStrategyAty;
import com.tseenola.jijin.myjijing.biz.mail.SendMailUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.x;

/**
 * Created by lenovo on 2018/5/31.
 * 描述：
 */

public class MainMenuActivity extends Activity {
    @Bind(R.id.bt_FundList)
    Button mBtFundList;
    @Bind(R.id.bt_FundHistory)
    Button mBtFundHistory;
    @Bind(R.id.bt_Setting)
    Button mBtSetting;
    @Bind(R.id.bt_FundStrategy)
    Button mBtFundStrategy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.bt_FundList, R.id.bt_FundHistory, R.id.bt_Setting,R.id.bt_FundStrategy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_FundList:
                startActivity(new Intent(this, FundListAty.class));
                break;
            case R.id.bt_FundHistory:
                startActivity(new Intent(this, FundHistoryAty.class));
                break;
            case R.id.bt_Setting:
                try{
                    SendMailUtil.send("641380205@qq.com");
                }catch(Exception pE){
                    Log.d("vbvb", "onClick: 发送邮件失败");
                    pE.printStackTrace();
                }
                break;
            case R.id.bt_FundStrategy:
                startActivity(new Intent(this, FundStrategyAty.class));
                break;
            default:
                break;
        }
    }


}
