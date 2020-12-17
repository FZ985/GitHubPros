package com.kmt.pro.ui.fragment.mine;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.Fill;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseFragment;
import com.kmt.pro.bean.mine.MineBean;
import com.kmt.pro.chart.mine.MineHorBarChart;
import com.kmt.pro.chart.render.MineFill;
import com.lxbuytimes.kmtapp.span.Span;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-05-21 9:46
 **/
public class MineHorBarChartFragment extends BaseFragment {
    @BindView(R.id.shizhi_tv)
    TextView shizhiTv;
    @BindView(R.id.yue_tv)
    TextView yueTv;
    @BindView(R.id.keyong_tv)
    TextView keyongTv;
    @BindView(R.id.yingkui_v)
    View yingkuiV;
    @BindView(R.id.yingkui_tv)
    TextView yingkuiTv;
    @BindView(R.id.today_yingkui_v)
    View todayYingkuiV;
    @BindView(R.id.view_zhezhao)
    TextView view_zhezhao;
    @BindView(R.id.todayyingkui_tv)
    TextView todayyingkuiTv;
    @BindView(R.id.mine_horchart)
    MineHorBarChart chart;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_minehor_barchart;
    }

    @Override
    public void initView() {
        initChart();
        if (null != getArguments()) {
            MineBean data = (MineBean) getArguments().getSerializable("data");
            setData(data);
        } else {
            setData(null);
        }
    }

    @Override
    public void initData() {

    }

    private float mTotal = 0.0f;//总市值
    private float mYinKui = 0.0f;//总盈亏
    private float mTodayYingKui = 0.0f;//日盈亏
    private float mYuE = 0.0f;//总余额
    private float mCanUse = 0.0f;//可用
    private float maxValue = 0;

    public void setData(MineBean data) {
        if (data != null && chart != null && !isDetached()) {
            Span.impl()
                    .append(Span.builder("总市值:").textColor(Color.parseColor("#8E8EA3")))
                    .append(Span.builder(data.getPositionPrice() + "GBT").textColor(Color.parseColor("#351292")))
                    .into(shizhiTv);
            Span.impl()
                    .append(Span.builder("余  额:").textColor(Color.parseColor("#8E8EA3")))
                    .append(Span.builder(data.getUserPurse() + "GBT").textColor(Color.parseColor("#351292")))
                    .into(yueTv);
            Span.impl()
                    .append(Span.builder("可  用:").textColor(Color.parseColor("#8E8EA3")))
                    .append(Span.builder(data.getWithDrawMoney() + "GBT").textColor(Color.parseColor("#351292")))
                    .into(keyongTv);
            Span.impl()
                    .append(Span.builder("总盈亏:").textColor(Color.parseColor("#8E8EA3")))
                    .append(Span.builder(data.getTotalWorth() + "GBT").textColor(Color.parseColor("#351292")))
                    .into(yingkuiTv);
            Span.impl()
                    .append(Span.builder("日盈亏:").textColor(Color.parseColor("#8E8EA3")))
                    .append(Span.builder(data.getTodayEarnings() + "GBT").textColor(Color.parseColor("#351292")))
                    .into(todayyingkuiTv);

            if (view_zhezhao != null) {
                view_zhezhao.setVisibility(data.isShowTodayEarnings() ? View.GONE : View.VISIBLE);
            }
            //设置数据
            List<Float> vals = getVals(data);

            float spaceForBar = 9f;
            ArrayList<BarEntry> values = new ArrayList<>();
            for (int i = 0; i < vals.size(); i++) {
                values.add(new BarEntry(i * spaceForBar, vals.get(i)));
            }

            BarDataSet set1 = new BarDataSet(values, "");
            set1.setDrawValues(false);
            set1.setDrawIcons(false);
            set1.setHighlightEnabled(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            List<Fill> gradientFills = getFillList();

            if (mYinKui >= 0) {
                gradientFills.add(0, new MineFill(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_end)
                        , ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_start)));
                yingkuiV.setBackground(ContextCompat.getDrawable(BaseApp.getInstance(), R.drawable.v3_mine_circle_yingkui1));
            } else {
                gradientFills.add(0, new MineFill(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_end2)
                        , ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_start2)));
                yingkuiV.setBackground(ContextCompat.getDrawable(BaseApp.getInstance(), R.drawable.v3_mine_circle_yingkui2));
            }
            if (data.isShowTodayEarnings()) {
                if (mTodayYingKui >= 0) {
                    gradientFills.add(0, new MineFill(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_end)
                            , ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_start)));
                    todayYingkuiV.setBackground(ContextCompat.getDrawable(BaseApp.getInstance(), R.drawable.v3_mine_circle_yingkui1));
                } else {
                    gradientFills.add(0, new MineFill(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_end2)
                            , ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_start2)));
                    todayYingkuiV.setBackground(ContextCompat.getDrawable(BaseApp.getInstance(), R.drawable.v3_mine_circle_yingkui2));
                }
            }

            set1.setFills(gradientFills);
            dataSets.add(set1);

            BarData chartData = new BarData(dataSets);
            chartData.setValueTextSize(10f);
            chartData.setBarWidth(3.5f);
            chart.setData(chartData);
            chart.setFitBars(true);
            chart.animateY(800);
        } else {
            setDefData();
        }
    }

    private List<Float> getVals(MineBean data) {
        mCanUse = Float.parseFloat((data.getWithDrawMoney() != null && !"".equals(data.getWithDrawMoney())) ? data.getWithDrawMoney() : "0");
        mYuE = Float.parseFloat((data.getUserPurse() != null && !"".equals(data.getUserPurse())) ? data.getUserPurse() : "0");
        mYinKui = Float.parseFloat((data.getTotalWorth() != null && !"".equals(data.getTotalWorth())) ? data.getTotalWorth() : "0");
        mTotal = Float.parseFloat((data.getPositionPrice() != null && !"".equals(data.getPositionPrice())) ? data.getPositionPrice() : "0");
        mTodayYingKui = Float.parseFloat((data.getTodayEarnings() != null && !"".equals(data.getTodayEarnings())) ? data.getTodayEarnings() : "0");

        if (mTotal == 0 && mCanUse == 0 && mYuE == 0 && mYinKui == 0 && mTodayYingKui == 0) {
            List<Float> defVal = new ArrayList<>();
            defVal.add(0f);
            defVal.add(0f);
            defVal.add(0f);
            defVal.add(0f);
            if (data.isShowTodayEarnings()) {
                defVal.add(0f);
            }
            return defVal;
        }
        float n = 0, m = 0;
        if (mCanUse > mYuE)
            m = mCanUse;
        else m = mYuE;
        if (Math.abs(mYinKui) > mTotal)
            n = Math.abs(mYinKui);
        else n = mTotal;
        if (m > n)
            maxValue = m;
        else maxValue = n;
        List<Float> vals = new ArrayList<>();

        //总市值
        if (mTotal > 0) {
            vals.add(0, mTotal + maxValue * 0.01f);
        } else {
            vals.add(0, maxValue * 0.01f);
        }

        //余额
        if (mYuE > 0) {
            vals.add(0, mYuE + maxValue * 0.01f);
        } else {
            vals.add(0, maxValue * 0.01f);
        }

        //可用
        if (mCanUse > 0) {
            vals.add(0, mCanUse + maxValue * 0.01f);
        } else {
            vals.add(0, maxValue * 0.01f);
        }

        //总盈亏
        if (Math.abs(mYinKui) > 0) {
            vals.add(0, Math.abs(mYinKui) + maxValue * 0.01f);
        } else {
            vals.add(0, maxValue * 0.01f);
        }

        //日盈亏
        if (data.isShowTodayEarnings()) {
            if (Math.abs(mTodayYingKui) > 0) {
                vals.add(0, Math.abs(mTodayYingKui) + maxValue * 0.01f);
            } else {
                vals.add(0, maxValue * 0.01f);
            }
        }
        return vals;
    }

    private void setDefData() {
        if (isDetached()) return;
        float spaceForBar = 9f;
        if (view_zhezhao != null) {
            view_zhezhao.setVisibility(View.VISIBLE);
        }
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            values.add(new BarEntry(i * spaceForBar, 20));
        }
        BarDataSet set1 = new BarDataSet(values, "");
        set1.setDrawValues(false);
        set1.setDrawIcons(false);
        set1.setHighlightEnabled(false);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        List<Fill> gradientFills = getFillList();
        gradientFills.add(0, new MineFill(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_end)
                , ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yingkui_start)));
        set1.setFills(gradientFills);
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(5f);
        chart.setData(data);
        chart.setFitBars(true);
        chart.animateY(800);
    }

    private List<Fill> getFillList() {
        List<Fill> gradientFills = new ArrayList<>();
        gradientFills.add(0, new MineFill(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_all_end)
                , ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_all_start)));

        gradientFills.add(0, new MineFill(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yue_end)
                , ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_yue_start)));

        gradientFills.add(0, new MineFill(ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_canuse_end)
                , ContextCompat.getColor(BaseApp.getInstance(), R.color.v3_mine_line_canuse_start)));

        return gradientFills;
    }

    public void isShow() {
        chart.animateY(800);
    }

    private void initChart() {
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.getDescription().setEnabled(false);
        chart.setNoDataText("暂无资产图");
        chart.setPinchZoom(false);
        //底边描述数据不显示
        chart.getLegend().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);

        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(false);
        xl.setDrawLabels(false);
        xl.setDrawGridLines(false);
        xl.setTextSize(1);

        YAxis yl = chart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
        yl.setDrawLabels(true);
        yl.setAxisMinimum(0f);
        yl.setLabelXOffset(10f);
        yl.setTextSize(1);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0);
        rightAxis.setTextSize(1);
    }

}
