package com.novery.network;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiBroadcastReceiver extends BroadcastReceiver {
	WifiManager wifiManager;  
	String[] strssids = null;
	WifiBroadcastReceiver()
	{
		strssids = new String[0];
	}
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		List<ScanResult> results = wifiManager.getScanResults();
		strssids = new String[results.size()];
		
		
		ScanResult bestSignal = null;
		int i=0;
		for (ScanResult result : results) {
			if (bestSignal == null
					|| WifiManager.compareSignalLevel(bestSignal.level,
							result.level) < 0)
				bestSignal = result;
			strssids[i]=result.SSID;
			i++;
		}
		String connSummary = String.format(
				"%s networks found. %s is 	the strongest",
				results.size(), bestSignal.SSID);
		// Toast.makeText(MyActivity.this, connSummary,
		// Toast.LENGTH_LONG).show();
		
	}

}
