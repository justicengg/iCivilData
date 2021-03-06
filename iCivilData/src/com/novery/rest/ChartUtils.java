package com.novery.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.novery.alfa.fragment.MyMarkerView;
import com.novery.icivilphone.R;
import com.novery.stack.NovListviewAdapter;
import com.novery.stack.NovRestDataItem;
import com.novery.stack.NovRestObjectInfo;
import com.novery.stack.NovRestResponseObjectInfo;

public class ChartUtils implements IChartUtilsInterface {
	NovListviewAdapter adapter;
	private Stack<NovRestObjectInfo> stackObject;
	private LineChart mChart;

	private View rootView;
	private Context mContext;
	private IRestResultToListviewInterface restResultTools;

	public ChartUtils(Context mContext, View rootView,
			IRestResultToListviewInterface restResultTools) {
		this.rootView = rootView;
		this.mContext = mContext;
		this.restResultTools = restResultTools;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.novery.rest.IChartUtilsInterface#drawChart(com.novery.stack.
	 * NovRestResponseObjectInfo)
	 */
	@Override
	public int drawChart(NovRestObjectInfo info) {

		// NovRestObjectInfo info = objinfo.getData();
		if (!info.getObjType().equalsIgnoreCase(
				String.valueOf(NovObjectType.TYPE_POINT))) {
			mChart = (LineChart) rootView.findViewById(R.id.chart_left_remote);
			mChart.setVisibility(View.GONE);
			return -1;
		}
		mChart = (LineChart) rootView.findViewById(R.id.chart_left_remote);
		mChart.setVisibility(View.VISIBLE);

		mChart = (LineChart) rootView.findViewById(R.id.chart_left_remote);

		// 设置在Y轴上是否是从0开始显示
		mChart.setStartAtZero(true);
		// 是否在Y轴显示数据，就是曲线上的数据
		mChart.setDrawYValues(true);
		// 设置网格
		mChart.setDrawBorder(true);
		mChart.setBorderPositions(new BorderPosition[] { BorderPosition.BOTTOM });
		// 在chart上的右下角加描述
		// mChart.setDescription("曲线图");
		// 设置Y轴上的单位
		// mChart.setUnit("￥");
		// 设置透明度
		mChart.setAlpha(0.8f);
		
		// 设置网格底下的那条线的颜色
		mChart.setBorderColor(Color.rgb(213, 216, 214));
		// 设置Y轴前后倒置
		mChart.setInvertYAxisEnabled(false);
		// 设置高亮显示
		mChart.setHighlightEnabled(true);
		// 设置是否可以触摸，如为false，则不能拖动，缩放等
		mChart.setTouchEnabled(true);
		// 设置是否可以拖拽，缩放
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		// 设置是否能扩大扩小
		mChart.setPinchZoom(true);
		// 设置背景颜色
		// mChart.setBackgroundColor(Color.GRAY);
		// 设置点击chart图对应的数据弹出标注
		MyMarkerView mv = new MyMarkerView(mContext,
				R.layout.custom_marker_view);
		// define an offset to change the original position of the marker
		// (optional)
		mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());
		// set the marker to the chart
		mChart.setMarkerView(mv);
		// enable/disable highlight indicators (the lines that indicate the
		// highlighted Entry)
		mChart.setHighlightIndicatorEnabled(false);
		// 设置字体格式，如正楷
		// Typeface tf = Typeface.createFromAsset(getAssets(),
		// "OpenSans-Regular.ttf");
		// mChart.setValueTypeface(tf);

		XLabels xl = mChart.getXLabels();
		// xl.setAvoidFirstLastClipping(true);
		// xl.setAdjustXLabels(true);
		xl.setPosition(XLabelPosition.BOTTOM); // 设置X轴的数据在底部显示
		// xl.setTypeface(tf); // 设置字体
		xl.setTextSize(10f); // 设置字体大小
		xl.setSpaceBetweenLabels(3); // 设置数据之间的间距

		YLabels yl = mChart.getYLabels();
		// yl.setPosition(YLabelPosition.LEFT_INSIDE); // set the position
		// yl.setTypeface(tf); // 设置字体
		yl.setTextSize(10f); // s设置字体大小
		yl.setLabelCount(5); // 设置Y轴最多显示的数据个数
		// 加载数据
		setData();
		// 从X轴进入的动画
		mChart.setAnimation(null);
		//mChart.animateX(4000);
		//mChart.animateY(3000); // 从Y轴进入的动画
		//mChart.animateXY(3000, 3000); // 从XY轴一起进入的动画

		// 设置最小的缩放
		mChart.setScaleMinima(0.5f, 1f);
		// 设置视口
		// mChart.centerViewPort(10, 50);

		// get the legend (only possible after setting data)
		Legend l = mChart.getLegend();
		l.setForm(LegendForm.LINE); // 设置图最下面显示的类型
		// l.setTypeface(tf);
		l.setTextSize(15);
		l.setTextColor(Color.rgb(50, 100, 50));
		l.setFormSize(30f); // set the size of the legend forms/shapes

		// 刷新图表
		mChart.invalidate();
		return 0;
	}

	private void setData() {

		List<String> lstAA = new ArrayList<String>();
		List<String> lstBB = new ArrayList<String>();
		// String[] aa = {"12","14","15","17","18","19","20"};
		// String[] bb =
		// {"122.00","234.34","85.67","117.90","332.33","113.33","120.78"};
		Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();
		NovRestObjectInfo objinfo = (NovRestObjectInfo) stck.peek();

		if (objinfo.getLstData().size() <= 0){
			ArrayList<String> xVals = new ArrayList<String>();
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			LineDataSet set1 = new LineDataSet(yVals, "数据曲线");
			LineData data = new LineData(xVals, set1);
			mChart.setData( data);
			return;
		}
		for (int i = 0; i < objinfo.getLstData().size(); i++) {
			NovRestDataItem item = (NovRestDataItem) objinfo.getLstData()
					.get(i);
			lstAA.add(item.getItemDate());
			lstBB.add(String.valueOf(item.getItemValue()));

		}

		// 在chart上的右下角加描述
		mChart.setDescription(objinfo.getObjName());
		// 设置Y轴上的单位
		mChart.setUnit(objinfo.getLstData().get(0).getItemUnit());
		mChart.setDrawYValues(false);

		String[] aa = new String[lstAA.size()];
		String[] bb = new String[lstBB.size()];
		for (int i = 0; i < lstAA.size(); i++) {
			aa[i] = lstAA.get(i);
		}
		for (int i = 0; i < lstBB.size(); i++) {
			bb[i] = lstBB.get(i);
		}

		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < aa.length; i++) {
			xVals.add(aa[i]);
		}

		ArrayList<Entry> yVals = new ArrayList<Entry>();

		for (int i = 0; i < bb.length; i++) {
			yVals.add(new Entry(Float.parseFloat(bb[i]), i));
		}

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(yVals, "数据曲线");

		set1.setDrawCubic(true); // 设置曲线为圆滑的线
		set1.setCubicIntensity(0.2f);
		set1.setDrawFilled(false); // 设置包括的范围区域填充颜色
		set1.setDrawCircles(true); // 设置有圆点
		set1.setLineWidth(2f); // 设置线的宽度
		set1.setCircleSize(3f); // 设置小圆的大小
		set1.setHighLightColor(Color.rgb(244, 117, 117));
		set1.setColor(Color.rgb(104, 241, 175)); // 设置曲线的颜色

		// create a data object with the datasets
		LineData data = new LineData(xVals, set1);

		// set data
		mChart.setData(data);

	}
}
