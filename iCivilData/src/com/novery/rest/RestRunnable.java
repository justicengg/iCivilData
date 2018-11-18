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

import com.novery.activity.ActivityDeviceDebug.PlaceholderFragment.MyListener;
import com.novery.base.App;
import com.novery.model.ListNovRawData;

public class RestRunnable implements Runnable {
	private List<Map<String, MyListener>>  mlstListener ;
	private ListView listView;
	private List<Map<String, Object>> mData;
	private boolean boolStop;
	Context mContext;
	EditText editTextStation ;
	
	public RestRunnable(Context mContext, 
			List<Map<String, MyListener>>  mlstListener ,
			ListView listView , 
			List<Map<String, Object>> mData,
			EditText editTextStation 
			) {
		this.mContext = mContext;
		this.mlstListener = mlstListener;
		this.listView = listView;
		this.mData = mData;
		this.editTextStation = editTextStation;
		boolStop = false;
		
	}
	public void stopAction() {
		this.boolStop = true;
	}
	public boolean isBoolStop() {
		return boolStop;
	}

	void sleepOnly(int nms) {
		try {
			Thread.sleep(nms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {		
		while (!isBoolStop()) {
			RefreshData();
			sleepOnly(10000);
		}		
	}
	private void NotifyRefresh(){
		// ·¢ËÍ¹ã²¥
		Intent intent = new Intent();
		intent.putExtra("msg","refresh_rawdata");
		intent.setAction("com.novery.rest");
		mContext.sendBroadcast(intent);	
	}
	private void RefreshData() {
		 Editable et = editTextStation.getText();
		 if( null == et )
			 return ;
		 String strTextStation = et.toString();
		if( null == strTextStation || strTextStation.isEmpty())
			return ;
		int nStation = 0 ;
		
		try{
			nStation = Integer.parseInt( strTextStation );
		}
		catch( Exception e){
			return ;
		}
		
		
		ListNovRawData lsRows = NovRawDataApi.getStationRows( nStation );
		if( null == lsRows ){
			return ;
		}
		mlstListener.clear();
		mData.clear();
		for (int i = 0; i < lsRows.getLstData().size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String strCreated = App.mysqlDateToFullString(lsRows.getLstData().get(i).getRawCreated());
			String strText = lsRows.getLstData().get(i).getRawContent();
			map.put("title",strCreated );
			map.put("info", strText );
			mData.add(map);		
			Map<String, MyListener> mapListen = new HashMap<String,MyListener>();
			mlstListener.add(mapListen);
			
		}
		NotifyRefresh();
		//MyAdapter adapter = (MyAdapter) listView.getAdapter();
	//	adapter.notifyDataSetChanged();
	}

}
