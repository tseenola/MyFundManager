package com.tseenola.jijin.myjijing.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by lenovo on 2018/11/6.
 * 描述：
 */
public class TimeUtils {
    public static String getUtcTime(){
        DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        TimeZone pst = TimeZone.getTimeZone("Etc/GMT+0");

        Date curDate = new Date();
        dateFormatter.setTimeZone(pst);
        String str=dateFormatter.format(curDate);//这就是我们想要获取的值
        return str;
    }
}
