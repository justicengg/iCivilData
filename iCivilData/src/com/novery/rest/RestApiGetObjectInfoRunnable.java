package com.novery.rest;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.novery.stack.NovRestObjectInfo;
import com.novery.stack.NovRestResponseObjectInfo;

public class RestApiGetObjectInfoRunnable implements RestApiInterface {

	private final String RESAPI_NAME = "objectinfo";
	public RestApiGetObjectInfoRunnable() {
	}

	Context mContext;
	EditText editTextStation ;
	Integer nObjectType ;
	String strObjectID ;
	Integer nRowStart;
	Integer nRowEnd;
	private IRestResultToListviewInterface restToListview;
	
	public RestApiGetObjectInfoRunnable(Context mContext, 
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
	/* (non-Javadoc)
	 * @see com.novery.rest.RestApiInterface#run()
	 */
	@Override
	public void run() {		
		
		NovRestResponseObjectInfo res = getObjectInfo( nObjectType , strObjectID, nRowStart, nRowEnd ) ;		
		if( null == res ||  res.getCode().equalsIgnoreCase("201") ){
			if( null != res ){
				NotifyResult( false,res.getMsg());
			}
			return ;
		}
		//NovRestObjectInfo info =  res.getData();// gsonInfo.fromJson(strinfo,NovRestObjectInfo.class);
		restToListview.loadObjectInfo( res );
		NovRestObjectInfo info = res.getData();
		
		
		if( null != info  ){			
			NotifyObjectInfo( info.getObjName() );
			restToListview.updatePager( info.getDataTotal(), nRowStart, info.getLstData().size());
			if(restToListview.getStackObject().isEmpty() ){
				restToListview.getStackObject().push( info );
			}
			else{
				NovRestObjectInfo infotop = restToListview.getStackObject().peek();
				if(null ==  infotop ){
					System.out.println("same object");					
				}
				else if( infotop.getObjID().equalsIgnoreCase( info.getObjID() )){
					restToListview.getStackObject().pop();
					restToListview.getStackObject().push( info );
					System.out.println("same object");
				}
				else
				{
					restToListview.getStackObject().push( info );
				}
			}
			
		}		
        NotifyResult( true,res.getMsg());
		
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

	
	private void NotifyResult( Boolean bResult,String msg ){
		// 发送广播
		Intent intent = new Intent();
		if( bResult )
			intent.putExtra(NovMessageName.MSG_OBJECTINFO_RESULT,NovMessageName.MSG_RESULT_OK);
		else
			intent.putExtra( NovMessageName.MSG_OBJECTINFO_RESULT,NovMessageName.MSG_RESULT_ERROR);
		
		intent.putExtra(NovMessageName.MSG_OBJECTINFO,msg);
		intent.setAction( NovMessageName.MSG_ACTION_OBJECT_INFO);
		
		mContext.sendBroadcast(intent);	
	}
	private void NotifyObjectInfo( String strinfo ){
		// 发送广播
		Intent intent = new Intent();
		intent.putExtra(NovMessageName.MSG_OBJECTINFO,strinfo);

		intent.setAction( NovMessageName.MSG_ACTION_OBJECT_INFO);
		
		mContext.sendBroadcast(intent);	
	}
	/* (non-Javadoc)
	 * @see com.novery.rest.RestApiInterface#getRestToListview()
	 */
	@Override
	public IRestResultToListviewInterface getRestToListview() {
		return restToListview;
	}

	/* (non-Javadoc)
	 * @see com.novery.rest.RestApiInterface#setRestToListview(com.novery.rest.IRestResultToListviewInterface)
	 */
	@Override
	public void setRestToListview(IRestResultToListviewInterface restToListview) {
		this.restToListview = restToListview;
	}

	@Override
	public void init(Context mContext, 
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
