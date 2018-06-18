package com.tseenola.jijin.myjijing.biz.fundhistory.view;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tseenola.jijin.myjijing.R;
import com.tseenola.jijin.myjijing.base.view.BaseAty;
import com.tseenola.jijin.myjijing.biz.fundhistory.model.FundInfo;
import com.tseenola.jijin.myjijing.biz.fundhistory.presenter.FundHistoryPrt;
import com.tseenola.jijin.myjijing.biz.fundstrategy.model.DataNetWorthTrend;
import com.tseenola.jijin.myjijing.utils.Constant;
import com.tseenola.jijin.myjijing.utils.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
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
 * Created by lenovo on 2018/6/1.
 * 描述：
 */

public class FundHistoryAty extends BaseAty implements IFundHistoryAty {
    @Bind(R.id.et_FundCode)
    EditText mEtFundCode;
    @Bind(R.id.bt_HistoryQuery)
    Button mBtHistoryQuery;
    @Bind(R.id.line_chart)
    LineChartView mLineChart;
    private FundHistoryPrt mFundHistoryPrt;
    private FundInfo mFundInfo;
    private ArrayList<PointValue> mPointValues;
    private ArrayList<AxisValue> mAxisXValues;

    @Override
    public void initData() {
        mPointValues = new ArrayList<PointValue>();
        mAxisXValues = new ArrayList<AxisValue>();
        Serializable lFundListInfo  = getIntent().getSerializableExtra("FundInfo");
        if (lFundListInfo!=null){
            mFundInfo = (FundInfo) lFundListInfo;
            Log.d("vbvb", "onLoadDatasSucc: ");
            getAxisXLables();//获取x轴的标注
            getAxisPoints();//获取坐标点
            initLineChart();//初始化
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_fund_history);
        ButterKnife.bind(this);
    }

    @Override
    public void bindPresenter() {
        mFundHistoryPrt = new FundHistoryPrt(this);
    }

    @Override
    public void onLoadDatasSucc(Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        mFundInfo = (FundInfo) pO;
        Log.d("vbvb", "onLoadDatasSucc: ");
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
        mFundHistoryPrt.saveFundInfo(mFundInfo);
        super.onLoadDatasSucc(pO,pDataSource);
    }

    @Override
    public void onLoadDataFail(Object pO, @Constant.DATA_SOURCE.SourceList String pDataSource) {
        Log.d("vbvb", "onLoadDataFail: ");
        super.onLoadDataFail(pO,pDataSource);
    }

    @OnClick(R.id.bt_HistoryQuery)
    public void onClick() {
        onStart(null);
        mFundHistoryPrt.loadDatas(mEtFundCode.getText().toString(), Constant.TaskName.DOWN_FUND_HISTORY);
    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables(){
        Gson lGson = new Gson();
        DataNetWorthTrend lWorthTrends = lGson.fromJson(mFundInfo.getDataNetWorthTrend(),DataNetWorthTrend.class);
        List<DataNetWorthTrend.DataNetWorthTrendBean> lDataNetWorthTrendBeens = lWorthTrends.getDataNetWorthTrend();
        for (int i = 0; i < lDataNetWorthTrendBeens.size(); i++) {
            String time = DateUtils.getFormateTimeByStamp(lDataNetWorthTrendBeens.get(i).getX(),"yy/MM/dd");
            mAxisXValues.add(new AxisValue(i).setLabel(time));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(){
        Gson lGson = new Gson();
        DataNetWorthTrend lWorthTrends = lGson.fromJson(mFundInfo.getDataNetWorthTrend(),DataNetWorthTrend.class);
        List<DataNetWorthTrend.DataNetWorthTrendBean> lDataNetWorthTrendBeens = lWorthTrends.getDataNetWorthTrend();
        for (int i = 0; i < lDataNetWorthTrendBeens.size(); i++) {
            mPointValues.add(new PointValue(i, (float) (lDataNetWorthTrendBeens.get(i).getY())));
        }
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#0000FF"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
	    line.setStrokeWidth(1);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.parseColor("#212121"));//黑色

	    axisX.setName("基金历史净值"+mFundInfo.getfSCode());  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        Axis axisY = new Axis();  //Y轴
        axisY.setName("基金净值");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        axisY.setTextColor(Color.parseColor("#212121"));
        axisY.setHasLines(true);
        data.setAxisYLeft(axisY);  //Y轴设置在左边



        ///////////////////////////////////////////////////////////////////////////////////////////////
        //设置行为属性，支持缩放、滑动以及平移
        mLineChart.setInteractive(true);
        mLineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        mLineChart.setMaxZoom((float) 3);//缩放比例
        mLineChart.setLineChartData(data);
        mLineChart.setVisibility(View.VISIBLE);
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
        v.right= 7;
        mLineChart.setCurrentViewport(v);
    }
}
