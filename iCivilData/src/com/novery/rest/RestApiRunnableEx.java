package com.novery.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.widget.EditText;
import android.widget.ListView;

//import com.nover.alfa.fragment.FragmentAlfaRemoteLeft.MyListener;
import com.novery.base.App;
import com.novery.model.ListNovRawData;

public class RestApiRunnableEx implements Runnable {
	//private List<Map<String, MyListener>>  mlstListener ;
	//private ListView listView;
	private List<Map<String, Object>> mData;
	Context mContext;
	EditText editTextStation ;
	
	public RestApiRunnableEx(Context mContext, 
	//		List<Map<String, MyListener>>  mlstListener ,
			ListView listView , 
			List<Map<String, Object>> mData,
			EditText editTextStation 
			) {
		this.mContext = mContext;
	//	this.mlstListener = mlstListener;
		//this.listView = listView;
		this.mData = mData;
		this.editTextStation = editTextStation;
		
	}

	void sleepOnly(int nms) {
		try {
			Thread.sleep(nms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {		
			RefreshData();
	}
	private void NotifyRefresh(){
		// ·¢ËÍ¹ã²¥
		Intent intent = new Intent();
		intent.putExtra("msg","refresh_rawdata");
		intent.setAction("com.novery.rest");
		mContext.sendBroadcast(intent);	
	}
	private void RefreshData() {
		
		int nStation = 50 ;
		
		
		ListNovRawData lsRows = NovRawDataApi.getStationRows( nStation );
		if( null == lsRows ){
			return ;
		}
	//	mlstListener.clear();
		mData.clear();
		for (int i = 0; i < lsRows.getLstData().size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String strCreated = App.mysqlDateToFullString(lsRows.getLstData().get(i).getRawCreated());
			String strText = lsRows.getLstData().get(i).getRawContent();
			map.put("title",strCreated );
			map.put("info", strText );
			mData.add(map);		
		//	Map<String, MyListener> mapListen = new HashMap<String,MyListener>();
		//	mlstListener.add(mapListen);
			
		}
		NotifyRefresh();
	}

}
