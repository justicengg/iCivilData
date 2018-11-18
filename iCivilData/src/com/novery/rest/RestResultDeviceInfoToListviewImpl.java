package com.novery.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.novery.base.App;
import com.novery.stack.NovListviewAdapter;
import com.novery.stack.NovRestDataItem;
import com.novery.stack.NovRestObjectInfo;
import com.novery.stack.NovRestResponseObjectInfo;
import com.novery.stack.NovListviewAdapter.MyListener;

public class RestResultDeviceInfoToListviewImpl implements IRestResultToListviewInterface {
	NovListviewAdapter adapter ;
	private Stack<NovRestObjectInfo> stackObject ;
	public RestResultDeviceInfoToListviewImpl() {
		
	}

	@Override
	public void setAdapter(NovListviewAdapter adapter) {
		this.adapter = adapter ;

	}

	@Override
	public void loadObjectInfo(NovRestResponseObjectInfo objinfo) {
		if ( null == adapter )
			return ;
		
		adapter.getLstRowData().clear();
		adapter.getLstListener().clear();
		for (int i = 0; i < objinfo.getData().getLstData().size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			NovRestDataItem item = (NovRestDataItem) objinfo.getData().getLstData().get( i );
			if( objinfo.getData().getObjType().equalsIgnoreCase( String.valueOf( NovDeviceType.DEVTYPE_CHANNEL))){
				String strCreated = item.getItemDate();
				String strText = String.valueOf(item.getItemValue()) + " " + item.getItemUnit();
				map.put("title",strCreated );
				map.put("info", strText );
			}
			else{
				String strCreated = item.getItemName();
				String strText = "";
				map.put("title",strCreated );
				map.put("info", strText );
			}
			adapter.getLstRowData().add(map);
			Map<String, MyListener> mapListen = new HashMap<String,MyListener>();
			adapter.getLstListener().add(mapListen);
			
		}

	}

	@Override
	public void updatePager( int rowCount,int rowStart, int rowReturned) {
		adapter.getRestPager().update(rowCount, rowStart, rowReturned);
		
	}

	@Override
	public
	Stack<NovRestObjectInfo> getStackObject() {
		return stackObject;
	}

	@Override
	public
	void setStackObject(Stack<NovRestObjectInfo> stackObject) {
		this.stackObject = stackObject;
	}

}
