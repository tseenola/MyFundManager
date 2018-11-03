package com.tseenola.jijin.myjijing.biz.huobi.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.huobi.model.HistoryKLine;
import com.tseenola.jijin.myjijing.biz.huobi.model.MACDUtils;
import com.tseenola.jijin.myjijing.utils.ThreadUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * Created by lenovo on 2018/10/13.
 * 描述：
 */

public class MACDBackTestAty extends BaseAty {
    private static HistoryKLine mHistoryKLine;
    @Bind(R.id.line_chart)
    LineChartView mLineChart;
    @Bind(R.id.tv_Info)
    TextView mTvInfo;
    @Bind(R.id.bt_RunBackTest)
    Button mBtRunBackTest;

    @Bind(R.id.bt_RunBoll2BackTest)
    Button btRunBoll2BackTest;

    @Bind(R.id.line_chart_std)
    LineChartView mLineChartStd;
    private ArrayList<PointValue> mPointValues_Y;
    private ArrayList<PointValue> mPointValues_Y_MACD;
    private ArrayList<PointValue> mPointValues_Y_DIF;
    private ArrayList<PointValue> mPointValues_Y_DEA;
    private ArrayList<AxisValue> mAxisValues_X;
    private ArrayList<PointValue> mPointValues_Y_0;

    private static final int STATUS_NULL = 0;//非持有状态
    private static final int STATUS_HOLD = 1;//持有状态

    public static void launch(Context pContext, HistoryKLine pHistoryKLine) {
        pContext.startActivity(new Intent(pContext, MACDBackTestAty.class));
        mHistoryKLine = pHistoryKLine;
    }

    @Override
    public void initData() {
        mPointValues_Y = new ArrayList<PointValue>();//y轴值，实际价格
        mPointValues_Y_0 = new ArrayList<PointValue>();//代表0轴
        mPointValues_Y_MACD = new ArrayList<PointValue>();//y轴值，MACD
        mPointValues_Y_DIF = new ArrayList<PointValue>();//y轴值，DIF
        mPointValues_Y_DEA = new ArrayList<PointValue>();//y轴值，DEA
        mAxisValues_X = new ArrayList<AxisValue>();//x轴坐标

        getAxisXLables();//获取x轴的标注
        onStart("处理数据中");
        ThreadUtil.runCachedService(new Runnable() {
            @Override
            public void run() {
                getAxisPoints();//获取坐标点
                initLineChart();//初始化
                onLoadDatasSucc(null, null);
            }
        });
    }

    /**
     * 图表的每个点的显示 Y 轴的值
     */
    private void getAxisPoints() {
        List<HistoryKLine.DataBean> lDataBeans = mHistoryKLine.getData();
        LinkedList<Double> stdData = new LinkedList<>();

        for (int i = 0; i < lDataBeans.size(); i++) {
            // 收盘值
            double closeVal = lDataBeans.get(lDataBeans.size() - i - 1).getClose();
            mPointValues_Y.add(new PointValue(i, (float) closeVal));//净值
            stdData.add(closeVal);
            mPointValues_Y_0.add(new PointValue(i, 0));

            HashMap<String, Double> lHashMap = MACDUtils.getMACD(stdData, 12, 26, 9);
            double macd = lHashMap.get("MACD");
            mPointValues_Y_MACD.add(new PointValue(i, (float) macd));

            double dif = lHashMap.get("DIF");
            mPointValues_Y_DIF.add(new PointValue(i, (float) dif));

            double dea = lHashMap.get("DEA");
            mPointValues_Y_DEA.add(new PointValue(i, (float) dea));
        }

    }


    /**
     * X 轴的显示
     */
    private void getAxisXLables() {
        List<HistoryKLine.DataBean> lDataBeen = mHistoryKLine.getData();
        for (int i = 0; i < lDataBeen.size(); i++) {
            mAxisValues_X.add(new AxisValue(i).setLabel(i + ""));
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_boll_back_test);
        ButterKnife.bind(this);
    }

    @Override
    public void bindPresenter() {

    }

    @OnClick({R.id.bt_RunBackTest, R.id.bt_RunBoll2BackTest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_RunBackTest:
                macdStrategy();
                break;
            case R.id.bt_RunBoll2BackTest:
                macdStrategy2();
                break;
            default:
                break;
        }
    }



    /**
     * 新策略：
     * 当MACD DIF 和 DIA 在0轴以下，并且DIF 上穿过 DIA时候买入。
     * 当DIF 上穿0轴时候卖出  或者  当DIF 下穿 DEA 卖出
     *
     *
     */
    private void macdStrategy() {
        mTvInfo.setText("");
        double preDIF = 0d;
        double preDEA = 0d;
        int curStatus = STATUS_NULL;
        double shouYiRateSum = 0;//收益率
        double curHoldVal = 0d;//当前持有价格
        for (int lI = 0; lI < mPointValues_Y_MACD.size(); lI++) {
            double closeVal = mPointValues_Y.get(lI).getY();
            double dif = mPointValues_Y_DIF.get(lI).getY();
            double dea = mPointValues_Y_DEA.get(lI).getY();

            if (lI == 0) {
                preDIF = dif;
                preDEA = dea;
            }
            if (dif < 0 && dea < 0) {
                if (dif > dea && preDIF <= preDEA) {//上穿买入
                    if (curStatus == STATUS_NULL) {
                        mTvInfo.append("DEF < 0 买入：" + lI + " close:" + closeVal + "\n");
                        curStatus = STATUS_HOLD;
                        curHoldVal = closeVal;
                    }
                }else if (dif < dea && preDIF >= preDEA){//下穿卖出
                    if (curStatus == STATUS_HOLD){
                        double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                        shouYiRateSum += curShouYiRate;
                        mTvInfo.append("DIF < 0卖出：" + lI + " close:" + closeVal + "收益率：" + curShouYiRate * 100 + "%\n");
                        curStatus = STATUS_NULL;
                    }
                }
            } else if (dif >= 0) {
                if (dif < dea && preDIF >= preDEA){//下穿卖出
                    if (curStatus == STATUS_HOLD){
                        double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                        shouYiRateSum += curShouYiRate;
                        mTvInfo.append("DIF > 0 卖出：" + lI + " close:" + closeVal + "收益率：" + curShouYiRate * 100 + "%\n");
                        curStatus = STATUS_NULL;
                    }
                }
                /*if (curStatus == STATUS_HOLD) {
                    double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                    shouYiRateSum += curShouYiRate;
                    mTvInfo.append("卖出：" + lI + " close:" + closeVal + "收益率：" + curShouYiRate * 100 + "%\n");
                    curStatus = STATUS_NULL;
                }*/
            }
            preDIF = dif;
            preDEA = dea;
        }
        mTvInfo.append("总收益率：" + shouYiRateSum * 100 + "%\n");
    }


    /**
     * 新策略：
     * 当MACD DIF 和 DIA 在0轴以下，并且DIF 上穿过 DIA时候买入(MACD 大于0)。
     * 当MACD 小于买入后macd 最大值卖出。
     */
    private void macdStrategy2() {
        mTvInfo.setText("");
        int curStatus = STATUS_NULL;
        double shouYiRateSum = 0;//收益率
        double curHoldVal = 0d;//当前持有价格
        double curMaxMACD = 0d;//买入后最大macd值
        for (int lI = 0; lI < mPointValues_Y_MACD.size(); lI++) {
            double closeVal = mPointValues_Y.get(lI).getY();
            double macd = mPointValues_Y_MACD.get(lI).getY();

            if (macd > 0) {//上穿买入
                if (curStatus == STATUS_NULL) {
                    Log.d("vbvb", lI+" :macdStrategy2: macd 大于0 且 未买入 ：closeVal:"+ closeVal+"==》买入");
                    mTvInfo.append("DEF < 0 买入：" + lI + " close:" + closeVal + "\n");
                    curStatus = STATUS_HOLD;
                    curHoldVal = closeVal;
                    curMaxMACD = macd;
                }else if (curStatus == STATUS_HOLD){//
                    if (curMaxMACD<=macd){
                        curMaxMACD = macd;
                        Log.d("vbvb", lI+" :macdStrategy2: 买入后macd 还在继续增加，继续持有");
                    }else {//macd小于买入后macd最大值。卖出
                        double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                        shouYiRateSum += curShouYiRate;
                        mTvInfo.append("DIF < 0卖出：" + lI + " close:" + closeVal + "收益率：" + curShouYiRate * 100 + "%\n");
                        curStatus = STATUS_NULL;
                        Log.d("vbvb", lI+" :macdStrategy2: 买入后macd 缩小，closeVal"+ closeVal +"==》卖出");
                    }
                }
            }else{//下穿卖出
                if (curStatus == STATUS_HOLD){
                    double curShouYiRate = (closeVal - curHoldVal) / curHoldVal;
                    shouYiRateSum += curShouYiRate;
                    mTvInfo.append("DIF < 0卖出：" + lI + " close:" + closeVal + "收益率：" + curShouYiRate * 100 + "%\n");
                    curStatus = STATUS_NULL;
                    Log.d("vbvb", lI+" :macdStrategy2: 买入后macd 小于0，==》卖出");
                }
            }
        }
        mTvInfo.append("总收益率：" + shouYiRateSum * 100 + "%\n");
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart() {
        List<Line> lines = new ArrayList<Line>();
        List<Line> lines_Std = new ArrayList<Line>();
        //收盘价
        Line line = new Line(mPointValues_Y).setColor(Color.parseColor("#0000FF"));  //折线的颜色
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
        line.setStrokeWidth(2);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
        lines_Std.add(line);

        //0轴
        Line zeroline = new Line(mPointValues_Y_0).setColor(Color.parseColor("#000000"));  //折线的颜色
        zeroline.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        zeroline.setCubic(false);//曲线是否平滑
        zeroline.setStrokeWidth(1);//线条的粗细，默认是3
        zeroline.setFilled(false);//是否填充曲线的面积
        zeroline.setHasLabels(false);//曲线的数据坐标是否加上备注
        zeroline.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        zeroline.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(zeroline);

        //MACD
        Line avgline = new Line(mPointValues_Y_MACD).setColor(Color.parseColor("#CE0000"));  //折线的颜色
        avgline.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        avgline.setCubic(false);//曲线是否平滑
        avgline.setStrokeWidth(2);//线条的粗细，默认是3
        avgline.setFilled(true);//是否填充曲线的面积
        avgline.setHasLabels(false);//曲线的数据坐标是否加上备注
        avgline.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        avgline.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(avgline);

        //DIF
        Line bollUpLine = new Line(mPointValues_Y_DIF).setColor(Color.parseColor("#F9F900"));  //折线的颜色
        bollUpLine.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        bollUpLine.setCubic(false);//曲线是否平滑
        bollUpLine.setStrokeWidth(2);//线条的粗细，默认是3
        bollUpLine.setFilled(false);//是否填充曲线的面积
        bollUpLine.setHasLabels(false);//曲线的数据坐标是否加上备注
        bollUpLine.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        bollUpLine.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(bollUpLine);


        //DEA
        Line bollLowLine = new Line(mPointValues_Y_DEA).setColor(Color.parseColor("#00BB00"));  //折线的颜色
        bollLowLine.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        bollLowLine.setCubic(false);//曲线是否平滑
        bollLowLine.setStrokeWidth(2);//线条的粗细，默认是3
        bollLowLine.setFilled(false);//是否填充曲线的面积
        bollLowLine.setHasLabels(false);//曲线的数据坐标是否加上备注
        bollLowLine.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        bollLowLine.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(bollLowLine);


        LineChartData data = new LineChartData();
        data.setLines(lines);

        LineChartData data_Std = new LineChartData();
        data_Std.setLines(lines_Std);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.parseColor("#212121"));//黑色

        axisX.setName("基金历史净值" + mHistoryKLine.getCh());  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisValues_X);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线

        data_Std.setAxisXBottom(axisX); //x 轴在底部
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        Axis axisY = new Axis();  //Y轴
        axisY.setName("基金净值");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        axisY.setTextColor(Color.parseColor("#212121"));
        axisY.setHasLines(true);
        data.setAxisYLeft(axisY);  //Y轴设置在左边

        data_Std.setAxisYLeft(axisY);  //Y轴设置在左边
        ///////////////////////////////////////////////////////////////////////////////////////////////
        //设置行为属性，支持缩放、滑动以及平移
        mLineChart.setInteractive(true);
        mLineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        mLineChart.setMaxZoom((float) mAxisValues_X.size() / 7);//缩放比例
        mLineChart.setLineChartData(data);
        mLineChart.setVisibility(View.VISIBLE);


        mLineChartStd.setInteractive(true);
        mLineChartStd.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        mLineChartStd.setMaxZoom((float) mAxisValues_X.size() / 7);//缩放比例
        mLineChartStd.setLineChartData(data_Std);
        mLineChartStd.setVisibility(View.VISIBLE);


        /**注：下面的7，10只是代表一个数字去类比而已
         * 尼玛搞的老子好辛苦！！！见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据），当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。当数据点个数大于（29）的时候，
         * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
         * 若设置axisX.setMaxLabelChars(int count)这句话,
         * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，则
         刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
         若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
         * 并且Y轴是根据数据的大小自动设置Y轴上限
         * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
         */
        Viewport v = new Viewport(mLineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        mLineChart.setCurrentViewport(v);


        Viewport v2 = new Viewport(mLineChartStd.getMaximumViewport());
        v2.left = 0;
        v2.right = 7;
        mLineChartStd.setCurrentViewport(v2);
    }
}
