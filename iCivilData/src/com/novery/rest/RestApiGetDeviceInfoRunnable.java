package com.novery.rest;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.novery.stack.NovRestObjectInfo;
import com.novery.stack.NovRestResponseObjectInfo;
import com.novery.stack.NovUserInfo;

public class RestApiGetDeviceInfoRunnable implements RestApiInterface {

	private final String RESAPI_NAME = "deviceinfo";
	public RestApiGetDeviceInfoRunnable() {
		//restApiName = "deviceinfo";
	}

	Context mContext;
	EditText editTextStation ;
	Integer nObjectType ;
	String strObjectID ;
	Integer nRowStart;
	Integer nRowEnd;
	//String restApiName ;
	private IRestResultToListviewInterface restToListview;
	
	public RestApiGetDeviceInfoRunnable(Context mContext, 
			Integer objectType,
			String objectID,
			Integer rowStart,
			Integer rowEnd
			) {
		this.mContext = mContext;
		this.nObjectType = objectType;
		this.strObjectID = objectID;
		this.nRowStart = rowStart;
		this.nRowEnd = rowEnd;
		
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
		
		NovRestResponseObjectInfo res = getObjectInfo( nObjectType , strObjectID, nRowStart, nRowEnd ) ;		
		if( null == res  ){
			return ;
		}
		if( null != res &&  res.getCode().equalsIgnoreCase("201") ){
			NotifyLoginResult( false,res.getMsg());
			return ;
		}
		//NovRestObjectInfo info =  res.getData();// gsonInfo.fromJson(strinfo,NovRestObjectInfo.class);
		restToListview.loadObjectInfo( res );
		NovRestObjectInfo info = res.getData();
		
		
		if( null != info  ){		
			NotifyDeviceInfo( info.getObjName());
			restToListview.updatePager( info.getDataTotal(), nRowStart, info.getLstData().size());
			if(restToListview.getStackObject().isEmpty() ){
				restToListview.getStackObject().push( info );
			}
			else{
				NovRestObjectInfo infotop = restToListview.getStackObject().peek();
				if(null ==  infotop  ||  !infotop.getObjID().equalsIgnoreCase( info.getObjID() )){
					restToListview.getStackObject().push( info );
				}
				else
				{
					System.out.println("same object");
				}
			}
			
		}		
        NotifyLoginResult( true,res.getMsg());
		
	}
	private NovRestResponseObjectInfo getObjectInfo(Integer nObjectType,
			String strObjectID, Integer nRowStart, Integer nRowEnd) {		
		String strurl ="/"+RESAPI_NAME+"/"+ nObjectType +"/"+ strObjectID+"/" + nRowStart +"/" + nRowEnd;
		NovRestResponseObjectInfo res = null ;		
		res = RestUtil.RestExecute(strurl, res);
		if( null == res )
			return null;
		else{
			return res;
		}
	}

	
	private void NotifyLoginResult( Boolean bResult,String msg ){
		// 发送广播
		Intent intent = new Intent();
		if( bResult )
			intent.putExtra(NovMessageName.MSG_DEVICEINFO_RESULT,NovMessageName.MSG_RESULT_OK);
		else
			intent.putExtra( NovMessageName.MSG_DEVICEINFO_RESULT,NovMessageName.MSG_RESULT_ERROR);
		
		intent.putExtra(NovMessageName.MSG_DEVICEINFO,msg);
		intent.setAction( NovMessageName.MSG_ACTION_DEVICE_INFO);
		
		mContext.sendBroadcast(intent);	
	}
	private void NotifyDeviceInfo( String strinfo ){
		// 发送广播
		Intent intent = new Intent();
		intent.putExtra(NovMessageName.MSG_DEVICEINFO,strinfo);

		intent.setAction( NovMessageName.MSG_ACTION_DEVICE_INFO);
		
		mContext.sendBroadcast(intent);	
	}
	public IRestResultToListviewInterface getRestToListview() {
		return restToListview;
	}

	public void setRestToListview(IRestResultToListviewInterface restToListview) {
		this.restToListview = restToListview;
	}

	@Override
	public void init
		(Context mContext, 
				Integer objectType,
				String objectID,
				Integer rowStart,
				Integer rowEnd
				) {
			this.mContext = mContext;
			this.nObjectType = objectType;
			this.strObjectID = objectID;
			this.nRowStart = rowStart;
			this.nRowEnd = rowEnd;
	}

}
