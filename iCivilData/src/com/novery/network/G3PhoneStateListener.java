package com.novery.network;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
	
    public class G3PhoneStateListener extends PhoneStateListener{ 
    	
    	private final String TAG = "G3Phone";
    	public String mStrengthInfo ;
    	public int nLevel ;
    	public int nState ;
    	public Context myContext;
    	G3PhoneStateListener()
    	{
    		mStrengthInfo="";
    		nLevel = 0 ;
    		nState = 0 ;
    	}
    	
        public void onSignalStrengthsChanged(SignalStrength signalStrength) { 
            super.onSignalStrengthsChanged(signalStrength); 
            /*
            signalStrength.isGsm()           是否GSM信号 2G or 3G 
            signalStrength.getCdmaDbm();     联通3G 信号强度
            signalStrength.getCdmaEcio();    联通3G 载干比
            signalStrength.getEvdoDbm();     电信3G 信号强度
            signalStrength.getEvdoEcio();    电信3G 载干比
            signalStrength.getEvdoSnr();     电信3G 信噪比
            signalStrength.getGsmSignalStrength();  2G 信号强度
            signalStrength.getGsmBitErrorRate();    2G 误码率
 
            载干比 ，它是指空中模拟电波中的信号与噪声的比值
             */  
            mStrengthInfo = "IsGsm : " + signalStrength.isGsm() +  
                    "/CDMA Dbm : " + signalStrength.getCdmaDbm() + "Dbm" +  
                    "/CDMA Ecio : " + signalStrength.getCdmaEcio() +  "dB*10" + 
                    "/Evdo Dbm : " + signalStrength.getEvdoDbm() + "Dbm" +  
                    "/Evdo Ecio : " + signalStrength.getEvdoEcio() + "dB*10" +  
                    "/Gsm SignalStrength : " + signalStrength.getGsmSignalStrength() +  
                    "/Gsm BitErrorRate : " + signalStrength.getGsmBitErrorRate();
            
           
            //AppLog.log(mStrengthInfo);
            nLevel = Math.abs(signalStrength.getGsmSignalStrength()); 
            msg3gstrength(String.valueOf( nLevel)) ;
        } 
        private void msg3gstrength(String strmsg)
    	{
    		//发送广播
    	      Intent intent=new Intent();
    	      intent.putExtra("box", "3GStrength"); 
    	      intent.putExtra("msg", strmsg); 
    	      intent.setAction("com.filesync.SdFileSync"); 
    	      myContext.sendBroadcast(intent);
    	}
        private void msg3gstate(String strmsg)
    	{
    		//发送广播
    	      Intent intent=new Intent();
    	      intent.putExtra("box", "3GState"); 
    	      intent.putExtra("msg", strmsg); 
    	      intent.setAction("com.filesync.SdFileSync"); 
    	      myContext.sendBroadcast(intent);
    	}
        
        public void onServiceStateChanged(ServiceState serviceState){ 
            super.onServiceStateChanged(serviceState); 
 
            /*
             ServiceState.STATE_EMERGENCY_ONLY   仅限紧急呼叫
             ServiceState.STATE_IN_SERVICE       信号正常
             ServiceState.STATE_OUT_OF_SERVICE   不在服务区
             ServiceState.STATE_POWER_OFF        断电
             */ 
            nState = serviceState.getState();
            String strlog ="PHONESTATE ";


            switch(serviceState.getState()) 
            { 
            case ServiceState.STATE_EMERGENCY_ONLY: 
            	strlog= "STATE_EMERGENCY_ONLY" ;
                //Log.d(TAG, "3G STATUS : STATE_EMERGENCY_ONLY"); 
                break; 
            case ServiceState.STATE_IN_SERVICE: 
            	strlog = "STATE_IN_SERVICE" ;
                //Log.d(TAG, "3G STATUS : STATE_IN_SERVICE"); 
                break; 
            case ServiceState.STATE_OUT_OF_SERVICE: 
            	strlog = "STATE_OUT_OF_SERVICE" ;
               // Log.d(TAG, "3G STATUS : STATE_OUT_OF_SERVICE"); 
                break; 
            case ServiceState.STATE_POWER_OFF: 
            	strlog = "STATE_POWER_OFF" ;
               // Log.d(TAG, "3G STATUS : STATE_POWER_OFF"); 
                break; 
            default: 
                break; 
            } 
            msg3gstate( strlog);
          //  AppLog.log(strlog);
        } 
    } 

