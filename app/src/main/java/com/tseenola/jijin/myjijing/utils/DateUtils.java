package com.tseenola.jijin.myjijing.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenovo on 2018/6/1.
 * 描述：
 */

public class DateUtils {

    public static String getFormateTimeByStamp(long pTimeStamp,String pPattern){
        SimpleDateFormat format =  new SimpleDateFormat(pPattern);
        return format.format(pTimeStamp);
    }

    public static String getNextDay(Date date, int pNextDate,SimpleDateFormat pDateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, pNextDate);
        return pDateFormat.format(calendar.getTimeInMillis());
    }
}
