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

		// ������Y�����Ƿ��Ǵ�0��ʼ��ʾ
		mChart.setStartAtZero(true);
		// �Ƿ���Y����ʾ���ݣ����������ϵ�����
		mChart.setDrawYValues(true);
		// ��������
		mChart.setDrawBorder(true);
		mChart.setBorderPositions(new BorderPosition[] { BorderPosition.BOTTOM });
		// ��chart�ϵ����½Ǽ�����
		// mChart.setDescription("����ͼ");
		// ����Y���ϵĵ�λ
		// mChart.setUnit("��");
		// ����͸����
		mChart.setAlpha(0.8f);
		
		// ����������µ������ߵ���ɫ
		mChart.setBorderColor(Color.rgb(213, 216, 214));
		// ����Y��ǰ����
		mChart.setInvertYAxisEnabled(false);
		// ���ø�����ʾ
		mChart.setHighlightEnabled(true);
		// �����Ƿ���Դ�������Ϊfalse�������϶������ŵ�
		mChart.setTouchEnabled(true);
		// �����Ƿ������ק������
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		// �����Ƿ���������С
		mChart.setPinchZoom(true);
		// ���ñ�����ɫ
		// mChart.setBackgroundColor(Color.GRAY);
		// ���õ��chartͼ��Ӧ�����ݵ�����ע
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
		// ���������ʽ��������
		// Typeface tf = Typeface.createFromAsset(getAssets(),
		// "OpenSans-Regular.ttf");
		// mChart.setValueTypeface(tf);

		XLabels xl = mChart.getXLabels();
		// xl.setAvoidFirstLastClipping(true);
		// xl.setAdjustXLabels(true);
		xl.setPosition(XLabelPosition.BOTTOM); // ����X��������ڵײ���ʾ
		// xl.setTypeface(tf); // ��������
		xl.setTextSize(10f); // ���������С
		xl.setSpaceBetweenLabels(3); // ��������֮��ļ��

		YLabels yl = mChart.getYLabels();
		// yl.setPosition(YLabelPosition.LEFT_INSIDE); // set the position
		// yl.setTypeface(tf); // ��������
		yl.setTextSize(10f); // s���������С
		yl.setLabelCount(5); // ����Y�������ʾ�����ݸ���
		// ��������
		setData();
		// ��X�����Ķ���
		mChart.setAnimation(null);
		//mChart.animateX(4000);
		//mChart.animateY(3000); // ��Y�����Ķ���
		//mChart.animateXY(3000, 3000); // ��XY��һ�����Ķ���

		// ������С������
		mChart.setScaleMinima(0.5f, 1f);
		// �����ӿ�
		// mChart.centerViewPort(10, 50);

		// get the legend (only possible after setting data)
		Legend l = mChart.getLegend();
		l.setForm(LegendForm.LINE); // ����ͼ��������ʾ������
		// l.setTypeface(tf);
		l.setTextSize(15);
		l.setTextColor(Color.rgb(50, 100, 50));
		l.setFormSize(30f); // set the size of the legend forms/shapes

		// ˢ��ͼ��
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
			LineDataSet set1 = new LineDataSet(yVals, "��������");
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

		// ��chart�ϵ����½Ǽ�����
		mChart.setDescription(objinfo.getObjName());
		// ����Y���ϵĵ�λ
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
		LineDataSet set1 = new LineDataSet(yVals, "��������");

		set1.setDrawCubic(true); // ��������ΪԲ������
		set1.setCubicIntensity(0.2f);
		set1.setDrawFilled(false); // ���ð����ķ�Χ���������ɫ
		set1.setDrawCircles(true); // ������Բ��
		set1.setLineWidth(2f); // �����ߵĿ��
		set1.setCircleSize(3f); // ����СԲ�Ĵ�С
		set1.setHighLightColor(Color.rgb(244, 117, 117));
		set1.setColor(Color.rgb(104, 241, 175)); // �������ߵ���ɫ

		// create a data object with the datasets
		LineData data = new LineData(xVals, set1);

		// set data
		mChart.setData(data);

	}
}
