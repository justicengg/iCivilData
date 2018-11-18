package com.novery.util;

import android.telephony.TelephonyManager;


public class PhboxPhoneStateListener extends android.telephony.PhoneStateListener  {

	@Override
	public void onCallStateChanged(int state, String incomingNumber) { 
		
        switch (state) { 
        case TelephonyManager.CALL_STATE_IDLE: 
            break; 

        case TelephonyManager.CALL_STATE_RINGING:
        	try {
        		//this.wait(2000);
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            break; 
        case TelephonyManager.CALL_STATE_OFFHOOK: 
        default: 
            break; 

        } 
        super.onCallStateChanged(state, incomingNumber); 

    } 

	

}
