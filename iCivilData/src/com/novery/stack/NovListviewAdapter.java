package com.novery.stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.novery.icivilphone.R;
import com.novery.rest.IChartUtilsInterface;
import com.novery.rest.INovListviewRowButtonOnClickInterface;

public class NovListviewAdapter extends BaseAdapter {
	private List<Map<String, Object>> lstRowData;
	private List<Map<String, MyListener>> lstListener;
	//private int flag;
	public  String title[] = new String[] { "-" };
	public  String info[] = new String[] { "-" };

	//private View rootView;
	private Context mContext;
	public ListView mlistView;

	private LayoutInflater mInflater;
	private INovListviewRowButtonOnClickInterface onRowButtonClick;
//	private int nLastClickedPos ;
	
	private IRestPager restPager;
	
	private IChartUtilsInterface chartUtils; 
	
	public NovListviewAdapter(Context context, View rootView, ListView mlistView
			) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		//this.rootView = rootView;
		this.mlistView = mlistView;
		this.chartUtils = chartUtils ;

		restPager = new NovRestPager();
	//	nLastClickedPos = 0;
		lstListener = new ArrayList<Map<String, MyListener>>();
		lstRowData = getInitiateData();
		try {
			mlistView.setAdapter(this);
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		
	}

	@Override
	public int getCount() {

		return lstRowData.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void onResume() {
		Toast.makeText(mContext, "onResume", Toast.LENGTH_SHORT).show();
	}

	public List<Map<String, Object>> getInitiateData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < title.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", title[i]);
			map.put("info", info[i]);
			list.add(map);

			Map<String, MyListener> mapListen = new HashMap<String, MyListener>();
			lstListener.add(mapListen);
		}
		return list;
	}

	public void onPause() {
		Toast.makeText(mContext, "onPause", Toast.LENGTH_SHORT).show();
	}

	// ****************************************第二种方法，高手一般都用此种方法,具体原因，我还不清楚,有待研究
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		MyListener myListener = null;

		if (position >= lstRowData.size())
			return convertView;

		if (convertView == null) {

			holder = new ViewHolder();

			// 可以理解为从vlist获取view 之后把view返回给ListView
			myListener = lstListener.get(position).get("listener");
			if (null == myListener) {
				myListener = new MyListener(position);
				lstListener.get(position).put("listener", myListener);
			}
			convertView = mInflater.inflate(R.layout.vlist, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.info = (TextView) convertView.findViewById(R.id.info);
			;
			holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			myListener = lstListener.get(position).get("listener");
			if (null == myListener) {
				myListener = new MyListener(position);
				lstListener.get(position).put("listener", myListener);
			}
		}

		if (null != lstRowData && (!lstRowData.isEmpty())) {
			holder.title.setText((String) lstRowData.get(position).get("title"));
			String strItem = (String) lstRowData.get(position).get("info");
			if (strItem.length() > 10) {
				strItem = strItem.substring(0, 10);
			}
			holder.info.setText(strItem);
		} else {
			holder.title.setText("none");
			holder.info.setText("none");
		}
		holder.viewBtn.setTag(position);
		// 给Button添加单击事件 添加Button之后ListView将失去焦点 需要的直接把Button的焦点去掉
		holder.viewBtn.setOnClickListener(myListener);
		
		MyRowListener myRowListener = new MyRowListener( position, mlistView );
		convertView.setOnClickListener(myRowListener);
		// holder.viewBtn.setOnClickListener(MyListener(position));

		return convertView;
	}

	public List<Map<String, Object>> getLstRowData() {
		return lstRowData;
	}

	public void setListRowData(List<Map<String, Object>> lstRowData) {
		this.lstRowData = lstRowData;
	}

	// 提取出来方便点
	public final class ViewHolder {
		public TextView title;
		public TextView info;
		public Button viewBtn;
	}

	public class MyListener implements OnClickListener {
		int mPosition;

		public MyListener(int inPosition) {
			mPosition = inPosition;
			
			/*
			ListView listView = (ListView) rootView
					.findViewById(R.id.lv_remote_left_data);
			listView.setSelected(true);
			listView.setSelection(mPosition);*/
		}

		@Override
		public void onClick(View v) {
			//nLastClickedPos= mPosition ;
			onRowButtonClick.OnClick(mPosition);
		}
	}
	public class MyRowListener implements OnClickListener {
		int mPosition;
		ListView listView;
		public MyRowListener(int inPosition, ListView lstView ) {
			mPosition = inPosition;
			listView = lstView;
			
		}

		@Override
		public void onClick(View v) {
			//nLastClickedPos= mPosition ;

			listView.setSelected(true);
			listView.setSelection(mPosition);
			onRowButtonClick.OnClick(mPosition);
		}
	}

	public void resetListener() {
		for (int i = 0; i < lstRowData.size(); i++) {
			Map<String, MyListener> mapListen = new HashMap<String, MyListener>();
			lstListener.add(mapListen);
		}
	}
/*
	public void setnLastClickedPos(int mPosition) {
		this.nLastClickedPos = mPosition;
		
	}
*/
	public List<Map<String, MyListener>> getLstListener() {
		return lstListener;
	}

	public void setLstListener(List<Map<String, MyListener>> lstListener) {
		this.lstListener = lstListener;
	}

	public INovListviewRowButtonOnClickInterface getOnRowButtonClick() {
		return onRowButtonClick;
	}

	public void setOnRowButtonClick(INovListviewRowButtonOnClickInterface onRowButtonClick) {
		this.onRowButtonClick = onRowButtonClick;
	}
/*
	public int getLastClickedPos() {
		return nLastClickedPos;
	}
*/
	public IRestPager getRestPager() {
		return restPager;
	}

	public void setRestPager(IRestPager restPager) {
		this.restPager = restPager;
	}

}
