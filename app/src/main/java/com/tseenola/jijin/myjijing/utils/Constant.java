package com.tseenola.jijin.myjijing.utils;

        import android.support.annotation.StringDef;

/**
 * Created by lenovo on 2018/5/30.
 * 描述：
 */

public class Constant {
    public static class Url{
        public static String FUND_LIST = "http://fund.eastmoney.com/js/fundcode_search.js";
        public static String FUND_DETAIL = "http://fundgz.1234567.com.cn/js/%s.js?rt=1463558676006";
        public static String FUND_HISTORY = "http://fund.eastmoney.com/pingzhongdata/%s.js?v=20160518155842";
    }

    public static class TaskName{
        public static final String DOWN_FUND_LIST = "DOWN_FUND_LIST";
        public static final String DOWN_FUND_HISTORY = "DOWN_FUND_HISTORY";
        public static final String DOWN_FUND_BASE = "DOWN_FUND_BASE";
        public static final String DOWN_All_FUNDS_HISTORY = "DOWN_All_FUNDS_HISTORY";

        @StringDef({DOWN_FUND_LIST,DOWN_FUND_HISTORY,DOWN_FUND_BASE,DOWN_All_FUNDS_HISTORY})
        public @interface NameList {}
    }

    public static class DATA_SOURCE{
        public static final String FROM_NET = "FROM_NET";
        public static final String FROM_DB = "FROM_DB";

        @StringDef({FROM_NET,FROM_DB})
        public @interface SourceList {}
    }
}
