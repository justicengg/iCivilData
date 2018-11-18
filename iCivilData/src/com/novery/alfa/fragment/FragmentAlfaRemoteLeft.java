package com.novery.alfa.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels;
import com.novery.alfa.view.TitleView;
import com.novery.icivilphone.R;
import com.novery.rest.ChartUtils;
import com.novery.rest.IChartUtilsInterface;
import com.novery.rest.INovListviewRowButtonOnClickInterface;
import com.novery.rest.IRestResultToListviewInterface;
import com.novery.rest.NovListviewRowButtonOnClickImpl;
import com.novery.rest.NovMessageName;
import com.novery.rest.NovObjectType;
import com.novery.rest.RestApiGetObjectInfoRunnable;
import com.novery.rest.RestResultObjectInfoToListviewImpl;
import com.novery.stack.NovListviewAdapter;
import com.novery.stack.NovRestDataItem;
import com.novery.stack.NovRestObjectInfo;
import com.novery.stack.NovUserInfo;

/**
 * @author yangyu
 *	功能描述：首页fragment页面
 * @param <NotifyReceiver>
 */
public class FragmentAlfaRemoteLeft  extends Fragment {

	
	private View rootView;
	private Context  mContext;
	public ListView mlistView;
	//private RestApiRunnableEx runRest ;
	private Thread thdService;
	private NotifyReceiver receiverNotify = null;
	private Intent intentNotify = null;
	private IRestResultToListviewInterface restResultTools;
	private IChartUtilsInterface chartUtils;
	private NovListviewAdapter novLisviewAdapter;
	private RestApiGetObjectInfoRunnable rest;
	private INovListviewRowButtonOnClickInterface onRowButtonClick;
	private Stack<NovRestObjectInfo> stackObject ;
	private LineChart mChart;  
	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static FragmentAlfaRemoteLeft newInstance(int index) {
		FragmentAlfaRemoteLeft f = new FragmentAlfaRemoteLeft();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);

		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_alfa_remote_left, container, false);
		
		
		rootView = view;
		mContext = getActivity(); // container.getContext();
		
		stackObject = new Stack<NovRestObjectInfo>();
		
		receiverNotify = new NotifyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction( NovMessageName.MSG_ACTION_OBJECT_INFO);
		intentNotify = mContext.registerReceiver(receiverNotify, filter);

		ListView listView = (ListView) rootView.findViewById(R.id.lv_remote_left_data);
		mlistView = listView;

		restResultTools = new RestResultObjectInfoToListviewImpl( mContext, rootView);

		novLisviewAdapter = new NovListviewAdapter( mContext,rootView,mlistView);
		
		restResultTools.setAdapter(novLisviewAdapter);
		restResultTools.setStackObject(stackObject);

		chartUtils = new ChartUtils( mContext,rootView,restResultTools);
		
		
				
		rest = new RestApiGetObjectInfoRunnable();
		rest.init(
				rootView.getContext(),
				0,
				NovUserInfo.getInstance().getLogin().getClientID(),
				0,
				novLisviewAdapter.getRestPager().getPageSize() 
				);
		rest.setRestToListview(restResultTools);

		onRowButtonClick = new NovListviewRowButtonOnClickImpl(rootView,restResultTools,rest);
		novLisviewAdapter.setOnRowButtonClick(onRowButtonClick);
		setOnClickNavigator();
		CallRestApi(novLisviewAdapter);
		
		//drawChart();
		return view;
	}

	private void drawChart() {
		
		Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();		
		NovRestObjectInfo info = (NovRestObjectInfo) stck.peek();	
		if( !info.getObjType().equalsIgnoreCase( String.valueOf( NovObjectType.TYPE_POINT ))){
			 mChart = (LineChart) rootView.findViewById(R.id.chart_left_remote); 
			// mChart.setVisibility(View.GONE);	
			return ;
		}
		 mChart = (LineChart) rootView.findViewById(R.id.chart_left_remote); 
		 mChart.setVisibility(View.VISIBLE);
		
		 mChart = (LineChart) rootView.findViewById(R.id.chart_left_remote);  
		  
	        // 设置在Y轴上是否是从0开始显示  
	        mChart.setStartAtZero(true);  
	        //是否在Y轴显示数据，就是曲线上的数据  
	        mChart.setDrawYValues(true);  
	        //设置网格  
	        mChart.setDrawBorder(true);  
	        mChart.setBorderPositions(new BorderPosition[] {  
	            BorderPosition.BOTTOM});  
	        //在chart上的右下角加描述  
	       // mChart.setDescription("曲线图");  
	        //设置Y轴上的单位  
	       // mChart.setUnit("￥");   
	        //设置透明度  
	        mChart.setAlpha(0.8f);  
	        //设置网格底下的那条线的颜色  
	        mChart.setBorderColor(Color.rgb(213, 216, 214));  
	        //设置Y轴前后倒置  
	        mChart.setInvertYAxisEnabled(false);  
	        //设置高亮显示  
	        mChart.setHighlightEnabled(true);  
	        //设置是否可以触摸，如为false，则不能拖动，缩放等  
	        mChart.setTouchEnabled(true);  
	        //设置是否可以拖拽，缩放  
	        mChart.setDragEnabled(true);  
	        mChart.setScaleEnabled(true);  
	        //设置是否能扩大扩小  
	        mChart.setPinchZoom(true);  
	        // 设置背景颜色  
	        // mChart.setBackgroundColor(Color.GRAY);  
	        //设置点击chart图对应的数据弹出标注  
	        MyMarkerView mv = new MyMarkerView( mContext, R.layout.custom_marker_view);  
	        // define an offset to change the original position of the marker  
	        // (optional)  
	        mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());  
	        // set the marker to the chart  
	        mChart.setMarkerView(mv);  
	        // enable/disable highlight indicators (the lines that indicate the  
	        // highlighted Entry)  
	        mChart.setHighlightIndicatorEnabled(false);  
	        //设置字体格式，如正楷  
	      //  Typeface tf = Typeface.createFromAsset(getAssets(),  
	            //    "OpenSans-Regular.ttf");  
	     //   mChart.setValueTypeface(tf);  
	  
	        XLabels xl = mChart.getXLabels();  
//	      xl.setAvoidFirstLastClipping(true);  
//	      xl.setAdjustXLabels(true);  
	        xl.setPosition(XLabelPosition.BOTTOM); // 设置X轴的数据在底部显示  
	    //    xl.setTypeface(tf); // 设置字体  
	        xl.setTextSize(10f); // 设置字体大小  
	        xl.setSpaceBetweenLabels(3); // 设置数据之间的间距  
	  
	        YLabels yl = mChart.getYLabels();  
	        // yl.setPosition(YLabelPosition.LEFT_INSIDE); // set the position  
	     //   yl.setTypeface(tf); // 设置字体  
	        yl.setTextSize(10f); // s设置字体大小  
	        yl.setLabelCount(5); // 设置Y轴最多显示的数据个数  
	        // 加载数据  
	        setData();  
	        //从X轴进入的动画  
	        mChart.animateX(4000);  
	        mChart.animateY(3000);   //从Y轴进入的动画  
	        mChart.animateXY(3000, 3000);    //从XY轴一起进入的动画  
	          
	        //设置最小的缩放  
	         mChart.setScaleMinima(0.5f, 1f);  
	        //设置视口  
	        // mChart.centerViewPort(10, 50);  
	  
	        // get the legend (only possible after setting data)  
	        Legend l = mChart.getLegend();  
	        l.setForm(LegendForm.LINE);  //设置图最下面显示的类型  
	   //     l.setTypeface(tf);    
	        l.setTextSize(15);  
	        l.setTextColor(Color.rgb(104, 241, 175));  
	        l.setFormSize(30f); // set the size of the legend forms/shapes  
	  
	        // 刷新图表  
	        mChart.invalidate();  
	    }  
	
		
	private void setData() {

		String[] aa = {};  
        String[] bb = {}; 
        List<String> lstAA = new ArrayList<String>();
        List<String> lstBB = new ArrayList<String>();
		//String[] aa = {"12","14","15","17","18","19","20"};  
        //String[] bb = {"122.00","234.34","85.67","117.90","332.33","113.33","120.78"};  
		Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();		
		NovRestObjectInfo objinfo = (NovRestObjectInfo) stck.peek();	
		
		 for (int i = 0; i < objinfo.getLstData().size(); i++) {
				NovRestDataItem item = (NovRestDataItem) objinfo.getLstData().get( i );
				lstAA.add(item.getItemDate());
				lstBB.add(String.valueOf(item.getItemValue()));
						
			}
		 
		//在chart上的右下角加描述  
	    mChart.setDescription(objinfo.getObjName());  
	    //设置Y轴上的单位  
	    mChart.setUnit( objinfo.getLstData().get( 0).getItemUnit());   
		 
		 
		 aa = (String[]) lstAA.toArray();
		 bb = (String[]) lstBB.toArray();
   
         ArrayList<String> xVals = new ArrayList<String>();  
         for (int i = 0; i < aa.length; i++) {  
             xVals.add(aa[i]);  
         }  
   
         ArrayList<Entry> yVals = new ArrayList<Entry>();  
   
         for (int i = 0; i < bb.length; i++) {  
             yVals.add(new Entry(Float.parseFloat(bb[i]), i));  
         }  
   
         // create a dataset and give it a type  
         LineDataSet set1 = new LineDataSet(yVals, "DataSet Line");  
   
         set1.setDrawCubic(true);  //设置曲线为圆滑的线  
         set1.setCubicIntensity(0.2f);  
         set1.setDrawFilled(false);  //设置包括的范围区域填充颜色  
         set1.setDrawCircles(true);  //设置有圆点  
         set1.setLineWidth(2f);    //设置线的宽度  
         set1.setCircleSize(5f);   //设置小圆的大小  
         set1.setHighLightColor(Color.rgb(244, 117, 117));  
         set1.setColor(Color.rgb(104, 241, 175));    //设置曲线的颜色  
   
         // create a data object with the datasets  
         LineData data = new LineData(xVals, set1);  
   
         // set data  
         mChart.setData(data);  
		
	}


	private void CallRestApi(NovListviewAdapter novLisviewAdapter) {
		//runRest = new RestApiRunnable( mContext, this.mlstListener,this.mlistView, novLisviewAdapter.getmData(),null );
		//runRest = new RestApiRunnableEx( mContext, this.mlistView, novLisviewAdapter.getmData(),null );
		
		thdService = new Thread( rest );
		thdService.start();
	}
	
	private void setOnClickNavigator(){
		ImageButton btProvious = (ImageButton) rootView.findViewById(R.id.imgBtProvious);
		ImageButton btNext = (ImageButton) rootView.findViewById(R.id.imgBtNext);
		ImageButton btReturnUp = (ImageButton) rootView.findViewById(R.id.imgBtReturnUp);
		
		btReturnUp.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
				Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();
				NovRestObjectInfo top = stck.pop();
				if( stck.isEmpty()){
					stck.push( top );
					return ;
				}
				NovRestObjectInfo info = (NovRestObjectInfo) stck.peek();
			
				if( info.getObjType().equalsIgnoreCase("4")){
					stck.push( top );
					return ;
				}
				int nType = Integer.parseInt( info.getObjType());
				String objID = info.getObjID();
				
				novLisviewAdapter.getRestPager().reset();
				Integer rowStart  = 0;
				Integer pageSize = novLisviewAdapter.getRestPager().getPageSize();
				
				RestApiGetObjectInfoRunnable rest = new RestApiGetObjectInfoRunnable( 
						rootView.getContext(), nType,objID,rowStart,pageSize );
				rest.setRestToListview(restResultTools);
				Thread thdService = new Thread( rest );
				thdService.start();
				
			}
			
		});
		btProvious.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();
				
				NovRestObjectInfo info = (NovRestObjectInfo) stck.peek();
			
				
				int nType = Integer.parseInt( info.getObjType());
				String objID = info.getObjID();	
				
				Integer rowStart = novLisviewAdapter.getRestPager().proviousPageStartRow();
				Integer pageSize = novLisviewAdapter.getRestPager().getPageSize();
				RestApiGetObjectInfoRunnable rest = new RestApiGetObjectInfoRunnable( 
						rootView.getContext(), nType,objID,rowStart,pageSize );
				rest.setRestToListview(restResultTools);
				Thread thdService = new Thread( rest );
				thdService.start();
				
			}
			
		});
		
		btNext.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();
				
				NovRestObjectInfo info = (NovRestObjectInfo) stck.peek();
			
				
				int nType = Integer.parseInt( info.getObjType());
				String objID = info.getObjID();
				
				
				Integer rowStart = novLisviewAdapter.getRestPager().nextPageStartRow();
				Integer pageSize = novLisviewAdapter.getRestPager().getPageSize();
				RestApiGetObjectInfoRunnable rest = new RestApiGetObjectInfoRunnable( 
						rootView.getContext(), nType,objID,rowStart,pageSize );
				rest.setRestToListview(restResultTools);
				Thread thdService = new Thread( rest );
				thdService.start();
				
			}
			
		});
		
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class NotifyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();			
			String strmsg = bundle.getString("msg");
			if( strmsg != null && strmsg.equalsIgnoreCase("refresh_rawdata")) {
				ListView listView = (ListView) rootView.findViewById(R.id.lv_remote_left_data);	
				NovListviewAdapter adapterOld = (NovListviewAdapter) listView.getAdapter();
				adapterOld.notifyDataSetChanged();
				adapterOld.resetListener();
				return;
			}
			strmsg = bundle.getString( NovMessageName.MSG_OBJECTINFO_RESULT);
			if( strmsg != null && strmsg.equalsIgnoreCase( NovMessageName.MSG_RESULT_OK )) {
				ListView listView = (ListView) rootView.findViewById(R.id.lv_remote_left_data);	
				NovListviewAdapter adapterOld = (NovListviewAdapter) listView.getAdapter();
				adapterOld.notifyDataSetChanged();
				adapterOld.resetListener();
				
				
				Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();		
				NovRestObjectInfo info = (NovRestObjectInfo) stck.peek();	
				if( !info.getObjType().equalsIgnoreCase( String.valueOf( NovObjectType.TYPE_POINT ))){
					 mChart = (LineChart) rootView.findViewById(R.id.chart_left_remote); 				
				}
				
				TitleView mTitle = (TitleView) getActivity().findViewById(R.id.title_frgm_remote);
				if( mTitle != null )
				{
					mTitle.setTitle( info.getObjName());
				}
				if (!info.getObjType().equalsIgnoreCase(
						String.valueOf(NovObjectType.TYPE_POINT))) {
					/*LayoutParams lvlp = listView.getLayoutParams();	
					RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) lvlp;
					lp.topMargin = 45;
					listView.setLayoutParams( lp);*/

					chartUtils.drawChart(info);
					return ;
				}
				else{
				/*	LayoutParams lvlp = listView.getLayoutParams();	
					RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) lvlp;
					lp.topMargin = 195;
					listView.setLayoutParams( lp);*/
					chartUtils.drawChart(info);
				}				
				return;
			}

		}
	}
	

}
