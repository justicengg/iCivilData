package com.novery.rest;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;









import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.novery.base.App;
import com.novery.base.AppConf;
import com.novery.model.ListNovRawData;
import com.novery.model.NovRawData;

public class NovRawDataApi {
	private static String strUri ;
	public NovRawDataApi() {
		strUri = AppConf.strRestUri;
	}
	public static ListNovRawData getStationRows( int stacode ) {
		try {
			strUri = AppConf.strRestUri;
			HttpClient httpclient = new DefaultHttpClient();
			//String strurl = "http://182.92.232.25:9090/ICivilApi/myService/Novery/rawdata/" + stacode ;
			String strurl = strUri + "/myService/Novery/rawdata/" + stacode ;
		
			HttpGet request = new HttpGet(strurl );
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String strjson = EntityUtils.toString(entity);  
			/*
			JSONObject jsonobject = new JSONObject( "{\"lstData\":"+strjson+"}");
			if( jsonobject.equals( null)){
				return null;
			}
			*/
			ListNovRawData lst = new  ListNovRawData();
			JSONArray jsonarr = new JSONArray( strjson );
			for( int i=0;i<jsonarr.length();i++){
				NovRawData item = new NovRawData();
				JSONObject obj = jsonarr.getJSONObject(i);
				String strItem = "rawCreated";
				String strValue = "" ;
				strValue = obj.getString( strItem );
				Date dtCreated = App.timestamp2Date(strValue);
				item.setRawCreated(dtCreated);
				
				strItem = "rawContent";
				strValue = obj.getString( strItem );
				item.setRawContent(strValue);
				
				
				lst.getLstData().add(item);
			}
           
            //(ListNovRawData) JSONObject.toBean(jsonobject,ListNovRawData.class);
            
			//System.out.println( lst.getLstData().toString());
			return lst;
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
