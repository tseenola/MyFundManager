package com.tseenola.jijin.myjijing.utils;

import java.text.SimpleDateFormat;

/**
 * Created by lenovo on 2018/6/1.
 * 描述：
 */

public class DateUtils {

    public static String getFormateTimeByStamp(long pTimeStamp,String pPattern){
        SimpleDateFormat format =  new SimpleDateFormat(pPattern);
        return format.format(pTimeStamp);
    }

}
