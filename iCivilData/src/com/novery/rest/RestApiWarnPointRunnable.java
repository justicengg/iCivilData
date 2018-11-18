package com.novery.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.novery.base.App;
import com.novery.base.AppConf;
import com.novery.model.NovWarnPoint;
import com.novery.stack.NovRestLoginInfo;
import com.novery.stack.NovRestResponse;
import com.novery.stack.NovRestResponseLogin;
import com.novery.stack.NovUserInfo;



public class RestApiWarnPointRunnable implements Runnable {

	//private List<Map<String, MyListener>>  mlstListener ;
	//private ListView listView;
	//private List<Map<String, Object>> mData;
	Context mContext;
	EditText editTextStation ;
	String strClientID;
	public RestApiWarnPointRunnable(Context mContext, 
			String clientID
			) {
		this.mContext = mContext;
		this.strClientID = clientID;
		
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
		
		//String strjson ="{\"code\":\"200\",\"msg\":null,\"data\":{\"cliID\":\"6BB23D26-6B03-4E89-A000-B6E5FFD1FAB9\",\"cliName\":\"6BB23D26-6B03-4E89-A000-B6E5FFD1FAB9\",\"objID\":null,\"objName\":null,\"objType\":null,\"dataTotal\":0,\"objData\":[{\"itemID\":\"BFC7678F-3BA1-44A4-A848-4D6FD5F97F5A\",\"itemName\":\"wtsfgs\",\"itemType\":\"0\",\"itemValue\":0.0,\"itemUnit\":\"\",\"itemDate\":\"2016-01-01\"},{\"itemID\":\"D8545DDE-B0E2-4E0E-BC20-FD003E65922D\",\"itemName\":\"黑龙江科技大学\",\"itemType\":\"0\",\"itemValue\":0.0,\"itemUnit\":\"\",\"itemDate\":\"2016-01-01\"}]}}";
		//Gson gson = new Gson();
		//NovRestResponse rest = gson.fromJson(strjson, NovRestResponse.class);
		//String strjson="{\"itemName\":\"li\",\"itemSex\":\"man\"}";
		//JSONObject jobj = JSONObject.fromObject(strjson);
		//JsonItem item = (JsonItem) JSONObject.toBean(jobj,JsonItem.class);
		//System.out.print( rest );
		String res = null;
		try{
			res  = restGetWarnPoint() ;		
			if( res.isEmpty() ){
				NotifyLoginResult( "没有警报"); 
			}
			else{
				NotifyLoginResult( "发现警报：\n" + res); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if( null == res){
			return;
		}
		
	}
	
	private void NotifyLoginResult( String msg ){
		// 发送广播
		Intent intent = new Intent();
		intent.putExtra(NovMessageName.MSG_WARN_INFO,msg);
		intent.setAction( NovMessageName.MSG_ACTION_WARN_INFO);		
		mContext.sendBroadcast(intent);	
	}
	
	public  String restGetWarnPoint() {
		try {
			String strUri = AppConf.strRestUri;
			HttpClient httpclient = new DefaultHttpClient();
			//http://localhost:8080/ICivilApi/myService/Novery/warnpnt?clientID=23DC429F-7FD1-455F-BAD9-198A87365894&dateStart=2017-08-01 00:00:00&dateEnd=2017-08-02 00:00:00
			
			String strurl = strUri + "/myService/Novery/warnpnt" + "?" ;
			strurl += "clientID=" + strClientID ;
			Date now = new Date();
			strurl += "&dateStart=" + App.getLast24DateTime( now );
			strurl += "&dateEnd=" + App.getNowDateTime( now );
			
			NovRestResponse res = null ;
			String strencode = strurl.replace(" ", "%20");
				
			HttpGet request = new HttpGet(strencode );
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String strjson = EntityUtils.toString(entity); 
			
			Gson gson = new Gson();
			res = gson.fromJson(strjson, NovRestResponse.class);
			
			String strResult="";
			if( null != res.getData() ){
				try{
					String strjson2 =  gson.toJson( res.getData() );
					JSONArray arr = new JSONArray( strjson2 );
				
					for( int i=0;i<arr.length();i++){
						NovWarnPoint item = gson.fromJson( arr.get(i).toString(),NovWarnPoint.class );
						strResult += item.getWarnObjectName() +"\n";
					}
				}
				catch(Exception e) {
					
				}
				finally{
					
				}
			}
			
			return strResult;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
