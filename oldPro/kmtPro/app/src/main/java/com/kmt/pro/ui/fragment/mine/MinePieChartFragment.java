package com.kmt.pro.ui.fragment.mine;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseFragment;
import com.kmt.pro.bean.mine.MineBean;
import com.kmt.pro.chart.mine.MinePieChart;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-05-21 15:32
 **/
public class MinePieChartFragment extends BaseFragment {
    @BindView(R.id.mine_zhong_exchange)
    TextView mineZhongExchange;
    @BindView(R.id.circle_v2)
    View circleV2;
    @BindView(R.id.mine_jing_exchange)
    TextView mineJingExchange;
    @BindView(R.id.mine_zong_tixian)
    TextView mineZongTixian;
    private MinePieChart pieChart;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine_piechart;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        pieChart = mRootView.findViewById(R.id.mine_piechart);
        initChart();
        if (null != getArguments()) {
            MineBean data = (MineBean) getArguments().getSerializable("data");
            setData(data);
        } else {
            setData(null);
        }
    }

    private float mTotal = 0.00f;//总和
    private float mRecharge = 0.00f;//总充值
    private float mWithDraw = 0.00f;//总提现
    private float mNet = 0.00f;//净充值

    public void setData(MineBean data) {
        if (data != null && pieChart != null && !isDetached()) {
            mWithDraw = Float.parseFloat((data.getTotalWithdrawMoney() != null && !"".equals(data.getTotalWithdrawMoney())) ? data.getTotalWithdrawMoney() : "0.00");
            mRecharge = Float.parseFloat((data.getTotalRechargeMoney() != null && !"".equals(data.getTotalRechargeMoney())) ? data.getTotalRechargeMoney() : "0.00");
            mNet = Float.parseFloat((data.getNetRecharge() != null && !"".equals(data.getNetRecharge())) ? data.getNetRecharge() : "0.00");
            mTotal = mRecharge + mWithDraw + Math.abs(mNet);

            if (!TextUtils.isEmpty(data.getTotalWithdrawMoney()))
                mineZongTixian.setText("总提现:" + data.getTotalWithdrawMoney() + "GBT");
            if (!TextUtils.isEmpty(data.getTotalRechargeMoney()))
                mineZhongExchange.setText("总充值:" + data.getTotalRechargeMoney() + "GBT");
            if (!TextUtils.isEmpty(data.getNetRecharge()))
                mineJingExchange.setText("净充值:" + data.getNetRecharge() + "GBT");

            ArrayList<PieEntry> valueList = new ArrayList<>();

            // 设置饼图各个区域颜色
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_pie_zongchongzhi));
            if (mNet >= 0) {
                colors.add(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_pie_jingchongzhi));
                circleV2.setBackground(ContextCompat.getDrawable(BaseApp.getInstance(), R.drawable.v3_mine_pie_circle_yue));
            } else {
                colors.add(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_start2));
                circleV2.setBackground(ContextCompat.getDrawable(BaseApp.getInstance(), R.drawable.v3_mine_pie_circle_tixian));
            }
            colors.add(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_pie_tixian));

            if (mTotal > 0) {
                //总充值
                if (mRecharge > 0) {
                    valueList.add(new PieEntry(mRecharge / mTotal * 100));
                } else {
                    valueList.add(new PieEntry(0));
                }
                //净充值
                if (Math.abs(mNet) > 0) {
                    valueList.add(new PieEntry(Math.abs(mNet) / mTotal * 100));
                } else {
                    valueList.add(new PieEntry(0));
                }
                //总提现
                if (mWithDraw > 0) {
                    valueList.add(new PieEntry(mWithDraw / mTotal * 100));
                } else {
                    valueList.add(new PieEntry(0));
                }
            } else {
                valueList.add(new PieEntry(10));
                valueList.add(new PieEntry(10));
                valueList.add(new PieEntry(10));
                colors.clear();
                colors.add(Color.parseColor("#F9F9F9"));
                colors.add(Color.parseColor("#F9F9F9"));
                colors.add(Color.parseColor("#F9F9F9"));
            }

            //显示在比例图上
            PieDataSet dataSet = new PieDataSet(valueList, " ");
            dataSet.setColors(colors);
            //设置个饼状图之间的距离
            dataSet.setSliceSpace(1.5f);
            // 部分区域被选中时多出的长度
            dataSet.setSelectionShift(1f);
            // 设置是否在图上描点
            dataSet.setDrawValues(false);
            PieData piedata = new PieData(dataSet);
            pieChart.setData(piedata);
        }
    }

    private void initChart() {
        pieChart.setNoDataText("暂无资产图");
        // 设置饼图是否接收点击事件，默认为true
        pieChart.setTouchEnabled(false);
        //设置饼图是否使用百分比
        pieChart.setUsePercentValues(true);
        //设置饼图右下角的文字描述
        pieChart.getDescription().setEnabled(false);
        //是否显示圆盘中间文字，默认显示
        pieChart.setDrawCenterText(true);
        //设置中间圆盘的颜色
        pieChart.setHoleColor(Color.WHITE);
        //设置中间圆盘的半径,值为所占饼图的百分比
        pieChart.setHoleRadius(40);

        //设置中间透明圈的半径,值为所占饼图的百分比
        pieChart.setTransparentCircleRadius(0);
        pieChart.setMinOffset(10f);

        //是否显示饼图中间空白区域，默认显示
        pieChart.setDrawHoleEnabled(true);
        //设置圆盘是否转动，默认转动
        pieChart.setRotationEnabled(false);
        //设置初始旋转角度
        pieChart.setRotationAngle(0);
        pieChart.setHighlightPerTapEnabled(false);

        pieChart.setDrawRoundedSlices(true);
        //底边描述数据不显示
        pieChart.getLegend().setEnabled(false);
        //设置是否打印日志
        pieChart.setLogEnabled(false);
    }

    public void isShow() {
        pieChart.animateX(500);
    }

}
