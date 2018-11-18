package com.novery.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.novery.base.AppConf;
import com.novery.stack.NovRestLoginInfo;
import com.novery.stack.NovRestResponseLogin;
import com.novery.stack.NovUserInfo;



public class RestApiLoginRunnable implements Runnable {

	//private List<Map<String, MyListener>>  mlstListener ;
	//private ListView listView;
	//private List<Map<String, Object>> mData;
	Context mContext;
	EditText editTextStation ;
	String strUserName ;
	String strUserPwd ;
	
	public RestApiLoginRunnable(Context mContext, 
			String userName,
			String userPwd
			) {
		this.mContext = mContext;
		this.strUserName = userName;
		this.strUserPwd = userPwd ;
		
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
		NovRestResponseLogin res = null;
		try{
			res  = restLoginPost( strUserName , strUserPwd ) ;		
			
		} catch (Exception e) {
			NotifyLoginResult( false,res.getMsg());
			e.printStackTrace();
		}
		if( null == res){
			NotifyLoginResult( false,"error");
			return;
		}
		else if(res.getCode().equalsIgnoreCase("201") ){
			
			NotifyLoginResult( false,res.getMsg());
			return ;
		}
		
		NovRestLoginInfo logininfo = res.getData();
		NovUserInfo.getInstance().setLogin( logininfo );
		
		/*NovRestLoginInfo info =  res.getData();// gsonInfo.fromJson(strinfo,NovRestObjectInfo.class);
		NovUserInfo.getInstance().setLogin( info );
		final Intent intent = new Intent(this.mContext, ActivityAlfa.class);
		intent.putExtra("USER_INFO_USERID",info.getUserID());
		intent.putExtra("USER_INFO_USERID",info.getUserID());
		intent.putExtra("USER_INFO_CLIENTID",info.getClientID());
		intent.putExtra("USER_INFO_CLIENTNAME",info.getClientName());
        mContext.startActivity(intent);*/
        NotifyLoginResult( true,res.getMsg());
		
	}
	public static void main( String args[]){
		/*String strjson="{\"itemName\":\"li\",\"itemSex\":\"man\"}";
		JSONObject jobj = JSONObject.fromObject(strjson);
		JsonItem item = (JsonItem) JSONObject.toBean(jobj,JsonItem.class);
		System.out.print( item );*/
	}
	private void NotifyLoginResult( Boolean bResult,String msg ){
		// 发送广播
		Intent intent = new Intent();
		if( bResult )
			intent.putExtra("MSG_LOGIN_RESULT","OK");
		else
			intent.putExtra("MSG_LOGIN_RESULT","ERROR");
		
		intent.putExtra("MSG_LOGIN_MESSAGE",msg);
		intent.setAction("com.novery.rest.login");
		mContext.sendBroadcast(intent);	
	}
	public  NovRestResponseLogin restLogin2( String strUserName, String strUserPwd ) {
		String strUri = AppConf.strRestUri;
		String strurl = strUri + "/myService/Novery/login/"+ strUserName +"/"+ strUserPwd;
		NovRestResponseLogin res = null ;
		 HttpURLConnection connection = null;  
	        try {  
	            URL url = new URL(  strurl);  
	            connection = (HttpURLConnection) url.openConnection();  	  
	            connection.setRequestMethod("GET");  	  
	            connection.setConnectTimeout(8000);  
	            connection.setReadTimeout(8000); 
	            InputStream in = connection.getInputStream();  
	            // 下面对获取到的输入流进行读取  
	            BufferedReader reader = new BufferedReader(new  InputStreamReader(in));  
	            StringBuilder response = new StringBuilder();  
	            String line;  
	            while ((line = reader.readLine()) != null) {  
	                response.append(line);  
	            }  
	            String strjson = response.toString(); 				
				Gson gson = new Gson();
				res = gson.fromJson(strjson, NovRestResponseLogin.class);
				
				return res;
	            
	        } catch (Exception e) {  
	  
	            e.printStackTrace();  
	            
	        } finally {  
	                if (connection != null) {  
	                    connection.disconnect();  
	                } 
	                
	        }
	        return res;  
	    }  
	
	
	public  NovRestResponseLogin restLogin( String strUserName, String strUserPwd ) {
		try {
			String strUri = AppConf.strRestUri;
			HttpClient httpclient = new DefaultHttpClient();
			String strurl = strUri + "/myService/Novery/login/"+ strUserName +"/"+ strUserPwd;
			NovRestResponseLogin res = null ;
			HttpGet request = new HttpGet(strurl );
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String strjson = EntityUtils.toString(entity); 
			
			Gson gson = new Gson();
			res = gson.fromJson(strjson, NovRestResponseLogin.class);
			
			return res;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public  NovRestResponseLogin restLoginPost( String strUserName, String strUserPwd ) {
		try {
			String strUri = AppConf.strRestUri;
			HttpClient httpclient = new DefaultHttpClient();
			String strurl = strUri + "/myService/Novery/lg";
			NovRestResponseLogin res = null ;
			HttpPost request = new HttpPost(strurl );
			/*LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
			params.add(new BasicNameValuePair("usrName",strUserName));  
			params.add(new BasicNameValuePair("usrPwd", strUserPwd));  */
			
			JsonObject jsnParam = new JsonObject();
			jsnParam.addProperty("usrName",strUserName);  
			jsnParam.addProperty("usrPwd", strUserPwd); 
			String strjsonParam = jsnParam.toString();
			
			
			StringEntity s = new StringEntity(strjsonParam,"utf-8");    
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));  
            //s.setContentType("application/json");    
            //s.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));   
            request.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            request.setEntity(s);
		  
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String strjson = EntityUtils.toString(entity); 
			
			Gson gson = new Gson();
			res = gson.fromJson(strjson, NovRestResponseLogin.class);
			
			return res;
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
