package com.novery.rest;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.novery.base.AppConf;
import com.novery.stack.NovRestResponseObjectInfo;

public class RestUtil {

	public RestUtil() {
		// TODO Auto-generated constructor stub
	}
	public static NovRestResponseObjectInfo RestExecute(String strurlApi,
			NovRestResponseObjectInfo res) {
		
		String strUri = AppConf.strRestUri;
		String strurl = strUri + "/myService/Novery"+ strurlApi ;
		
		String strjson;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			//http://182.92.232.25:9090/rest/myService/Novery/objectinfo/0/6BB23D26-6B03-4E89-A000-B6E5FFD1FAB9/1/100
			
			HttpGet request = new HttpGet(strurl );
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			strjson = EntityUtils.toString(entity);	
			Gson gson = new Gson();
			res = gson.fromJson(strjson, NovRestResponseObjectInfo.class);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
