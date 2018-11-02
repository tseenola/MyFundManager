package com.tseenola.jijin.myjijing;

import android.util.Log;

import com.google.gson.Gson;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundVals;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String data = "*\n" +
                " * 测试数据\n" +
                " * @type {arry}\n" +
                " \n" +
                "2018-06-01 16:22:49\n" +
                "var ishb=false;\n" +
                "基金或股票信息\n" +
                "var fS_name = \"富国文体健康股票\";\n" +
                "var fS_code = \"001186\";\n" +
                "原费率\n" +
                "var fund_sourceRate=\"1.50\";\n" +
                "现费率\n" +
                "var fund_Rate=\"0.15\";\n" +
                "//最小申购金额\n" +
                "var fund_minsg=\"100\";\n" +
                "\n" +
                "基金持仓股票代码\n" +
                "var stockCodes=[\"0024192\",\"6003371\",\"3001882\",\"6005211\",\"6000851\",\"0020272\",\"0024542\",\"0020442\",\"0021852\",\"0020012\"];\n" +
                "基金持仓债券代码\n" +
                "var zqCodes=\"0180051,0103031,1230082\";\n" +
                "收益率\n" +
                "近一年收益率\n" +
                "var syl_1n=\"41.0191\";\n" +
                "近6月收益率\n" +
                "var syl_6y=\"13.0746\";\n" +
                "近三月收益率\n" +
                "var syl_3y=\"6.0345\";\n" +
                "近一月收益率\n" +
                "var syl_1y=\"5.3283\";\n" +
                "股票仓位测算图\n" +
                "var Data_fundSharesPositions = [[1525190400000,95.00],[1525276800000,95.00],[1525363200000,96.8600],[1525622400000,99.3100],[1525708800000,95.00],[1525795200000,99.8900],[1525881600000,95.00],[1525968000000,95.00],[1526227200000,95.00],[1526313600000,95.00],[1526400000000,95.00],[1526486400000,95.00],[1526572800000,95.00]];\n" +
                "单位净值走势 equityReturn-净值回报 unitMoney-每份派送金\n" +
                "var Data_netWorthTrend = [{\"x\":1430841600000,\"y\":1.0,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1431014400000,\"y\":1.004,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1431619200000,\"y\":1.021,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1432224000000,\"y\":1.078,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1432828800000,\"y\":1.112,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1433433600000,\"y\":1.203,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1433952000000,\"y\":1.188,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1434038400000,\"y\":1.204,\"equityReturn\":1.3468,\"unitMoney\":\"\"},{\"x\":1434297600000,\"y\":1.204,\"equityReturn\":0.0,\"unitMoney\":\"\"}];\n" +
                "累计净值走势\n" +
                "var Data_ACWorthTrend = [[1430841600000,1.0],[1431014400000,1.004],[1431619200000,1.021],[1432224000000,1.078],[1432828800000,1.112],[1433433600000,1.203],[1433952000000,1.188],[1434038400000,1.204],[1434297600000,1.204],[1434384000000,1.162],[1434470400000,1.175],[1434556800000,1.144],[1434643200000,1.087],[1434988800000,1.091],[1435075200000,1.118],[1435161600000,1.073],[1435248000000,1.005],[1435507200000,0.932],[1435593600000,0.96],[1435680000000,0.911],[1435766400000,0.846],[1435852800000,0.795],[1436112000000,0.757],[1436198400000,0.715],[1436284800000,0.711],[1436371200000,0.706],[1436457600000,0.742],[1436716800000,0.789],[1436803200000,0.809]];\n" +
                "累计收益率走势\n" +
                "var Data_grandTotal = [{\"name\":\"富国文体健康股票\",\"data\":[[1511971200000,0],[1512057600000,1.43],[1512316800000,1.12],[1512403200000,-0.31],[1512489600000,0.82],[1512576000000,0],[1512662400000,2.04],[1512921600000,4.90],[1513008000000,4.39],[1513094400000,5.11],[1513180800000,5.82]},{\"name\":\"同类平均\",\"data\":[[1511971200000,0],[1512057600000,0.39],[1512316800000,0.48],[1512403200000,-0.31],[1512489600000,-0.30],[1512576000000,-1.11],[1512662400000,-0.16],[1512921600000,1.35],[1513008000000,0.35]]}];\n" +
                "同类排名走势\n" +
                "var Data_rateInSimilarType = [{\"x\":1438790400000,\"y\":383,\"sc\":\"410\"},{\"x\":1438876800000,\"y\":374,\"sc\":\"410\"},{\"x\":1439136000000,\"y\":353,\"sc\":\"410\"},{\"x\":1439222400000,\"y\":297,\"sc\":\"410\"},{\"x\":1439308800000,\"y\":292,\"sc\":\"411\"},{\"x\":1439395200000,\"y\":295,\"sc\":\"413\"},{\"x\":1439481600000,\"y\":303,\"sc\":\"414\"},{\"x\":1439740800000,\"y\":330,\"sc\":\"415\"},{\"x\":1439827200000,\"y\":347,\"sc\":\"417\"},{\"x\":1439913600000,\"y\":307,\"sc\":\"418\"},{\"x\":1440000000000,\"y\":306,\"sc\":\"421\"},{\"x\":1440086400000,\"y\":269,\"sc\":\"423\"},{\"x\":1440345600000,\"y\":341,\"sc\":\"424\"},{\"x\":1440432000000,\"y\":319,\"sc\":\"425\"},{\"x\":1440518400000,\"y\":304,\"sc\":\"426\"},{\"x\":1440604800000,\"y\":308,\"sc\":\"430\"}];\n" +
                "同类排名百分比\n" +
                "var Data_rateInSimilarPersent=[[1438790400000,6.5900],[1438876800000,8.7800],[1439136000000,13.9000],[1439222400000,27.5600],[1439308800000,28.9500],[1439395200000,28.5700],[1439481600000,26.8100],[1439740800000,20.4800],[1439827200000,16.7900],[1439913600000,26.5600],[1440000000000,27.3200],[1440086400000,36.4100],[1440345600000,19.5800],[1440432000000,24.9400],[1440518400000,28.6400],[1440604800000,28.3700],[1440691200000,19.3000],[1440950400000,13.3300],[1441036800000,14.4800],[1441123200000,18.9500],[1441555200000,13.6800],[1441641600000,11.4300],[1441728000000,10.3800],[1441814400000,14.7600],[1441900800000,17.4000],[1442160000000,15.8600]];\n" +
                "规模变动 mom-较上期环比\n" +
                "var Data_fluctuationScale = {\"categories\":[\"2017-03-31\",\"2017-06-30\",\"2017-09-30\",\"2017-12-31\",\"2018-03-31\"],\"series\":[{\"y\":20.72,\"mom\":\"-1.66%\"},{\"y\":20.50,\"mom\":\"-1.08%\"},{\"y\":21.35,\"mom\":\"4.16%\"},{\"y\":20.07,\"mom\":\"-5.99%\"},{\"y\":15.80,\"mom\":\"-21.27%\"}]};\n" +
                "持有人结构\n" +
                "var Data_holderStructure ={\"series\":[{\"name\":\"机构持有比例\",\"data\":[0,0.0,0.0,0]},{\"name\":\"个人持有比例\",\"data\":[100.0,100.0,100.0,100.0]},{\"name\":\"内部持有比例\",\"data\":[0.0042,0.004,0.0028,0.0041]}],\"categories\":[\"2016-06-30\",\"2016-12-31\",\"2017-06-30\",\"2017-12-31\"]};\n" +
                "资产配置\n" +
                "var Data_assetAllocation = {\"series\":[{\"name\":\"股票占净比\",\"type\":null,\"data\":[84.78,87.88,84.51,86.15],\"yAxis\":0},{\"name\":\"债券占净比\",\"type\":null,\"data\":[0,0,0,2.18],\"yAxis\":0},{\"name\":\"现金占净比\",\"type\":null,\"data\":[15.71,7.06,11.79,8.07],\"yAxis\":0},{\"name\":\"净资产\",\"type\":\"line\",\"data\":[20.4964753577,21.34862788,20.0697573689,15.8017328892],\"yAxis\":1}],\"categories\":[\"2017-06-30\",\"2017-09-30\",\"2017-12-31\",\"2018-03-31\"]};\n" +
                "业绩评价 ['选股能力', '收益率', '抗风险', '稳定性','择时能力']\n" +
                "var Data_performanceEvaluation = {\"avr\":\"84.00\",\"categories\":[\"选证能力\",\"收益率\",\"抗风险\",\"稳定性\",\"择时能力\"],\"dsc\":[\"反映基金挑选证券而实现风险\\u003cbr\\u003e调整后获得超额收益的能力\",\"根据阶段收益评分，反映基金的盈利能力\",\"反映基金投资收益的回撤情况\",\"反映基金投资收益的波动性\",\"反映基金根据对市场走势的判断，\\u003cbr\\u003e通过调整仓位及配置而跑赢基金业\\u003cbr\\u003e绩基准的能力\"],\"data\":[70.0,90.0,100.0,70.0,80.0]};\n" +
                "现任基金经理\n" +
                "var Data_currentFundManager =[{\"id\":\"30339268\",\"pic\":\"https://pdf.dfcfw.com/pdf/H8_GIF30339268_1.jpg\",\"name\":\"林庆\",\"star\":5,\"workTime\":\"3年又27天\",\"fundSize\":\"15.80亿(1只基金)\",\"power\":{\"avr\":\"86.95\",\"categories\":[\"经验值\",\"收益率\",\"抗风险\",\"稳定性\",\"择时能力\"],\"dsc\":[\"反映基金经理从业年限和管理基金的经验\",\"根据基金经理投资的阶段收益评分，反映\\u003cbr\\u003e基金经理投资的盈利能力\",\"反映基金经理投资的回撤控制能力\",\"反映基金经理投资收益的波动\",\"反映基金经理根据对市场的判断，通过\\u003cbr\\u003e调整仓位及配置而跑赢业绩的基准能力\"],\"data\":[59.70,98.50,92.10,60.30,93.60],\"jzrq\":\"2018-05-31\"},\"profit\":{\"categories\":[\"任期收益\",\"同类平均\",\"沪深300\"],\"series\":[{\"data\":[{\"name\":null,\"color\":\"#7cb5ec\",\"y\":10.7},{\"name\":null,\"color\":\"#414c7b\",\"y\":-10.22},{\"name\":null,\"color\":\"#f7a35c\",\"y\":-16.49}]}],\"jzrq\":\"2018-05-31\"}}] ;\n" +
                "申购赎回\n" +
                "var Data_buySedemption ={\"series\":[{\"name\":\"期间申购\",\"data\":[0.12,0.16,1.37,0.66]},{\"name\":\"期间赎回\",\"data\":[1.22,1.91,4.76,5.23]},{\"name\":\"总份额\",\"data\":[24.50,22.75,19.36,14.79]}],\"categories\":[\"2017-06-30\",\"2017-09-30\",\"2017-12-31\",\"2018-03-31\"]};\n" +
                "同类型基金涨幅榜（页面底部通栏）\n" +
                "var swithSameType = [['161725_招商中证白酒指数分级_20.97','005235_银华食品饮料量化股票_17.47','005236_银华食品饮料量化股票_17.45','001631_天弘中证食品饮料指数_17.35','001632_天弘中证食品饮料指数_17.33'],['005176_富国精准医疗混合_33.40','161616_融通医疗保健行业混合_32.75','000220_富国医疗保健行业混合_32.60','003096_中欧医疗健康混合C_30.58','003095_中欧医疗健康混合A_30.28'],['003096_中欧医疗健康混合C_38.32','005176_富国精准医疗混合_38.22','003095_中欧医疗健康混合A_37.97','005112_银华中证全指医药卫生_32.45','001417_汇添富医疗服务混合_32.07']];\n";

        String key = "fS_name;\n" +
                "fS_code;\n" +
                "fund_sourceRate;\n" +
                "fund_Rate;\n" +
                "fund_minsg;\n" +
                "stockCodes;\n" +
                "zqCodes;\n" +
                "syl_1n;\n" +
                "syl_6y;\n" +
                "syl_3y;\n" +
                "syl_1y;\n" +
                "Data_fundSharesPositions;\n" +
                "Data_netWorthTrend;\n" +
                "Data_ACWorthTrend;\n" +
                "Data_grandTotal;\n" +
                "Data_rateInSimilarType;\n" +
                "Data_rateInSimilarPersent;\n" +
                "Data_fluctuationScale;\n" +
                "Data_holderStructure;\n" +
                "Data_assetAllocation;\n" +
                "Data_performanceEvaluation;\n" +
                "Data_currentFundManager;\n" +
                "Data_buySedemption;\n" +
                "swithSameType";


        String results [] = data.split(";");
        StringBuilder lStringBuilder = new StringBuilder("{");
        FundVals lFundInfo = null;
        for(int i = 0;i<results.length;i++){
            if(results[i]!=null){
                String attr = "";
                if (results[i].contains("Data_netWorthTrend")) {
                    lStringBuilder.append("\"Vals\":");
                    attr = getFundList(results[i]);
                    lStringBuilder.append(attr);
                    lStringBuilder.append("}");
                    Gson lGson = new Gson();
                    lFundInfo = lGson.fromJson(lStringBuilder.toString(), FundVals.class);
                    Log.d("vbvb", "addition_isCorrect: ");
                }
            }
        }
        Log.d("vbvb", "addition_isCorrect: ");
    }

    private String getFundList(String pS) {
        int startIndex = pS.indexOf("[{");
        int endIndex = pS.lastIndexOf("}]");
        return pS.substring(startIndex,endIndex+2);
    }

    private String getFundAttr(String pS) {
        int startIndex = pS.indexOf("\"");
        int endIndex = pS.lastIndexOf("\"");
        return pS.substring(startIndex,endIndex+1);
    }

    @Test
    public void t2() throws Exception {
        String data = "基金或股票信息,fS_name;\n" +
                "基金代码,fS_code;\n" +
                "原费率,fund_sourceRate;\n" +
                "现费率,fund_Rate;\n" +
                "最小申购金额,fund_minsg;\n" +
                "基金持仓股票代码,stockCodes;\n" +
                "基金持仓债券代码,zqCodes;\n" +
                "收益率近一年收益率,syl_1n;\n" +
                "近6月收益率,syl_6y;\n" +
                "近三月收益率,syl_3y;\n" +
                "近一月收益率,syl_1y;\n" +
                "股票仓位测算图,Data_fundSharesPositions;\n" +
                "单位净值走势,Data_netWorthTrend;\n" +
                "累计净值走势,Data_ACWorthTrend;\n" +
                "累计收益率走势,Data_grandTotal;\n" +
                "同类排名走势,Data_rateInSimilarType;\n" +
                "同类排名百分比,Data_rateInSimilarPersent;\n" +
                "规模变动mom-较上期环比,Data_fluctuationScale;\n" +
                "持有人结构,Data_holderStructure;\n" +
                "资产配置,Data_assetAllocation;\n" +
                "业绩评价,Data_performanceEvaluation;\n" +
                "现任基金经理,Data_currentFundManager;\n" +
                "申购赎回,Data_buySedemption;\n" +
                "同类型基金涨幅榜（页面底部通栏）,swithSameType";

        String ss[] = data.split(";");
        System.out.print("");
    }

    @Test
    public void test2(){
        int i = 0;
        i++;
        System.out.print("vbvb i:"+i);

        int z = 0;
        ++z;
        System.out.print("vbvb z:"+z);
    }

    @Test
    public void getBiaoZhunCha(){
        double [] testData=new double[]{1,1.1};
        System.out.println("vbvb：标准差："+getStandardDiviation(testData));
    }

    public double getStandardDiviation(double[] inputData) {
        double result;
        //绝对值化很重要
        result = Math.sqrt(Math.abs(getVariance(inputData)));

        return result;

    }


    /**
     * 求给定双精度数组中值的方差
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public double getVariance(double[] inputData) {
        int count = getCount(inputData);
        double sqrsum = getSquareSum(inputData);
        double average = getAverage(inputData);
        double result;
        result = (sqrsum - count * average * average) / count;

        return result;
    }

    /**
     * 求给定双精度数组中值的数目
     *
     * @param inputData
     *            Data 输入数据数组
     * @return 运算结果
     */
    public int getCount(double[] inputData) {
        if (inputData == null)
            return -1;

        return inputData.length;
    }

    /**
     * 求给定双精度数组中值的平方和
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getSquareSum(double[] inputData) {
        if(inputData==null||inputData.length==0)
            return -1;
        int len=inputData.length;
        double sqrsum = 0.0;
        for (int i = 0; i <len; i++) {
            sqrsum = sqrsum + inputData[i] * inputData[i];
        }
        return sqrsum;
    }

    /**
     * 求给定双精度数组中值的平均值
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public double getAverage(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double result;
        result = getSum(inputData) / len;

        return result;
    }

    /**
     * 求给定双精度数组中值的和
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public double getSum(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum = sum + inputData[i];
        }

        return sum;

    }

    @Test
    public void testStd(){
        LinkedList<Double> lList = new LinkedList<>();
        lList.add(1d);
        lList.add(3d);
        lList.add(2d);

        int i = 0;
    }

    @Test
    public void testMACD(){

    }

    /**
     * Calculate EMA,
     *
     * @param list
     *            :Price list to calculate，the first at head, the last at tail.
     * @return
     */
    public static final Double getEXPMA(final List<Double> list, final int number) {
        // 开始计算EMA值，
        Double k = 2.0 / (number + 1.0);// 计算出序数
        Double ema = list.get(0);// 第一天ema等于当天收盘价
        for (int i = 1; i < list.size(); i++) {
            // 第二天以后，当天收盘 收盘价乘以系数再加上昨天EMA乘以系数-1
            ema = list.get(i) * k + ema * (1 - k);
        }
        return ema;
    }


}