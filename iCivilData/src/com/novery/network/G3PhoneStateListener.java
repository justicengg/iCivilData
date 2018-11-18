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
            signalStrength.isGsm()           �Ƿ�GSM�ź� 2G or 3G 
            signalStrength.getCdmaDbm();     ��ͨ3G �ź�ǿ��
            signalStrength.getCdmaEcio();    ��ͨ3G �ظɱ�
            signalStrength.getEvdoDbm();     ����3G �ź�ǿ��
            signalStrength.getEvdoEcio();    ����3G �ظɱ�
            signalStrength.getEvdoSnr();     ����3G �����
            signalStrength.getGsmSignalStrength();  2G �ź�ǿ��
            signalStrength.getGsmBitErrorRate();    2G ������
 
            �ظɱ� ������ָ����ģ��粨�е��ź��������ı�ֵ
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
    		//���͹㲥
    	      Intent intent=new Intent();
    	      intent.putExtra("box", "3GStrength"); 
    	      intent.putExtra("msg", strmsg); 
    	      intent.setAction("com.filesync.SdFileSync"); 
    	      myContext.sendBroadcast(intent);
    	}
        private void msg3gstate(String strmsg)
    	{
    		//���͹㲥
    	      Intent intent=new Intent();
    	      intent.putExtra("box", "3GState"); 
    	      intent.putExtra("msg", strmsg); 
    	      intent.setAction("com.filesync.SdFileSync"); 
    	      myContext.sendBroadcast(intent);
    	}
        
        public void onServiceStateChanged(ServiceState serviceState){ 
            super.onServiceStateChanged(serviceState); 
 
            /*
             ServiceState.STATE_EMERGENCY_ONLY   ���޽�������
             ServiceState.STATE_IN_SERVICE       �ź�����
             ServiceState.STATE_OUT_OF_SERVICE   ���ڷ�����
             ServiceState.STATE_POWER_OFF        �ϵ�
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

