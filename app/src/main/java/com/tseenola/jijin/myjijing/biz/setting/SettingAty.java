package com.tseenola.jijin.myjijing.biz.setting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.tseenola.jijin.myjijing.R;

/**
 * Created by lenovo on 2018/6/1.
 * 描述：
 */

public class SettingAty extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("vbvb", "onCreate: ");
        setContentView(R.layout.activity_setting);
    }
}
