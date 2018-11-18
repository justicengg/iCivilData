package com.novery.rest;

import java.util.Stack;

import android.view.View;

import com.novery.stack.NovRestObjectInfo;
import com.novery.stack.NovUserInfo;

public class NovListviewRowButtonOnClickImpl implements
		INovListviewRowButtonOnClickInterface {

	private View rootView;
	private IRestResultToListviewInterface restResultTools;
	private Integer pageSize = 25;
	private RestApiInterface resapi ;

	public NovListviewRowButtonOnClickImpl(  
			View rootView , 
			IRestResultToListviewInterface restResultTools,
			RestApiInterface resapi) {
		this.rootView = rootView ;
		this.restResultTools = restResultTools;
		this.resapi = resapi;
	}

	@Override
	public void OnClick(int pos) {
		
		Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();
		
		NovRestObjectInfo info = (NovRestObjectInfo) stck.peek();
	
		if( info.getObjType().equalsIgnoreCase("4")){
			return ;
		}
		
		int nType = Integer.parseInt( info.getLstData().get(pos).getItemType());
		String objID = info.getLstData().get( pos).getItemID() ;
		//RestApiGetObjectInfoRunnable rest = new RestApiGetObjectInfoRunnable( 
		//		rootView.getContext(), nType,objID,0,pageSize  );
		resapi.init(rootView.getContext(), nType,objID,0,pageSize );
		resapi.setRestToListview(restResultTools);
		Thread thdService = new Thread( resapi );
		thdService.start();
	}
	@Override
	public
	void setRestApi(RestApiInterface resapi){
		this.resapi = resapi;
	}

}
