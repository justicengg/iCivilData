package com.novery.network;


import java.lang.reflect.Method;
import java.util.List;

import com.novery.base.AppConf;
import com.novery.util.AppLog;
import com.novery.util.AppOver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;

public class NetworkService {
	WifiManager wifiManager;
	TelephonyManager teleManager;
	Context myContext;
	WifiBroadcastReceiver mWifiBroadcastReceiver;
	G3PhoneStateListener mG3PhoneState ;
	// 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
	public enum WifiCipherType {
		WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
	}

	// 构造函数
	public NetworkService(WifiManager wifiManager) {
		this.wifiManager = wifiManager;
	}

	public NetworkService(Context context) {
		myContext = context;
		
		String service = Context.WIFI_SERVICE;
		wifiManager = (WifiManager) myContext.getSystemService(service);
		mWifiBroadcastReceiver = new WifiBroadcastReceiver();
		mWifiBroadcastReceiver.wifiManager = wifiManager;

		teleManager = (TelephonyManager) myContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		mG3PhoneState = new G3PhoneStateListener();
		mG3PhoneState.myContext = myContext ;
		teleManager.listen(
				mG3PhoneState,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
				| PhoneStateListener.LISTEN_SERVICE_STATE
				); 

	}

	public String getConnectedWifiSSID(String wifiinfo) {
		String strWifi = "";
		WifiInfo info = wifiManager.getConnectionInfo();
		if (info.getBSSID() != null) {
			int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
			int speed = info.getLinkSpeed();
			String units = WifiInfo.LINK_SPEED_UNITS;
			String ssid = info.getSSID();
			wifiinfo = String.format("Connected to %s at %s%s.Strength %s/5",
					ssid, speed, units, strength);
			strWifi = info.getSSID();
		}
		return strWifi;
	}

	public String[] scanwifi() {

		AppLog.log("scanwifi");
		// Register a broadcast receiver that listens for scan results.
		myContext.registerReceiver(mWifiBroadcastReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		// Initiate a scan.
		try {
			wifiManager.startScan();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			for ( int i=0 ;i <5 ;i++ )
			{
				if( AppOver.getApp().isOver())
					break ;
				Thread.sleep(100);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int n = 10 ;
		while (mWifiBroadcastReceiver.strssids.length == 0 && n-- > 0) {
			
			try {
				if( AppOver.getApp().isOver())
					break ;
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return mWifiBroadcastReceiver.strssids;
	}

	// 打开wifi功能
	public boolean OpenWifi() {
		boolean bRet = true;
		if (!wifiManager.isWifiEnabled()) {
			bRet = wifiManager.setWifiEnabled(true);
		}
		return bRet;
	}

	public boolean open3G() {
		ConnectivityManager conMan = (ConnectivityManager) myContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();

		if( mobile == State.CONNECTED || mobile == State.CONNECTING )
		{
			return true ;
		}
		ConnectivityManager mConnectivityManager = (ConnectivityManager) myContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		setMobileNetEnable();
		return false;
	}

	public final void setMobileNetEnable() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) myContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		Object[] arg = null;
		try {
			boolean isMobileDataEnable = invokeMethod("getMobileDataEnabled",
					arg);
			if (!isMobileDataEnable) {
				invokeBooleanArgMethod("setMobileDataEnabled", true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean invokeMethod(String methodName, Object[] arg)
			throws Exception {

		ConnectivityManager mConnectivityManager = (ConnectivityManager) myContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		Class ownerClass = mConnectivityManager.getClass();

		Class[] argsClass = null;
		if (arg != null) {
			argsClass = new Class[1];
			argsClass[0] = arg.getClass();
		}

		Method method = ownerClass.getMethod(methodName, argsClass);

		Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);

		return isOpen;
	}

	public Object invokeBooleanArgMethod(String methodName, boolean value)
			throws Exception {

		ConnectivityManager mConnectivityManager = (ConnectivityManager) myContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		Class ownerClass = mConnectivityManager.getClass();

		Class[] argsClass = new Class[1];
		argsClass[0] = boolean.class;

		Method method = ownerClass.getMethod(methodName, argsClass);

		return method.invoke(mConnectivityManager, value);
	}

	public boolean CloseWifi() {
		boolean bRet = true;
		if (wifiManager.isWifiEnabled()) {
			bRet = wifiManager.setWifiEnabled(false);
		}
		return bRet;
	}

	public boolean ConnectFlahair23(String SSID, String Password) {
		
		//access2Wifi("WPA2-PSK") ;
		return true ;
		
	}
	public boolean ConnectFlahair(String SSID, String Password) {
		if (!wifiManager.isWifiEnabled()) {
			if (!wifiManager.setWifiEnabled(true)){
				//AppLog.log("打开wifi失败");
				return false;
			}
		}

		String strWifiInfo ="" ;
		String ssidConnected = getConnectedWifiSSID( strWifiInfo );
		if( ssidConnected.indexOf("flashair")>=0)
		{
			return true ;
		}
		
		
		int n = 1;
		while (n-- >= 0) {
			
			String[] strssids = scanwifi();
			if (strssids.length > 0) {
				for (String ssid : strssids) {
					if (ssid.indexOf("flashair") >= 0) {
						String strpwd = AppConf.strFlashairPwd ;
						String strssid =  AppConf.strSSID  ;
						try{
						 boolean bRet = Connect(strssid, strpwd,
								WifiCipherType.WIFICIPHER_WPA);
						 if( bRet )
						 {
							 AppLog.log("Flashair connected");
							 return true ; 
						 }
						}
						catch ( Exception e )
						{
							e.printStackTrace();
						}
					}
				}
				
			}
			else
			{
				try {
					if( AppOver.getApp().isOver())
						break ;
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		AppLog.log("不能边接　Flashair");
		return false;
	}

	// 提供一个外部接口，传入要连接的无线网
	public boolean Connect(String SSID, String Password, WifiCipherType Type) {
		if (!this.OpenWifi()) {
			return false;
		}
		// 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
		// 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
		int n = 10 ;
		while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING && n--> 0) {
			try {

				// 为了避免程序一直while循环，让它睡个100毫秒在检测……
				Thread.currentThread();
				if( AppOver.getApp().isOver())
					break ;
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
			}
		}
		if( AppOver.getApp().isOver())
			return false ;

		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
			boolean bRet = wifiManager.enableNetwork(tempConfig.networkId, true);
			tempConfig.preSharedKey = "\""+ Password +"\"" ;
			wifiManager.updateNetwork(tempConfig) ;
			return bRet;
		}

		WifiConfiguration wifiConfig = this
				.CreateWifiInfo(SSID, Password, Type);
		//
		if (wifiConfig == null) {
			return false;
		}	

		int netID = wifiManager.addNetwork(wifiConfig);
		boolean bRet = wifiManager.enableNetwork(netID, true);
		bRet = wifiManager.saveConfiguration();
		
		//
		//等待自动连接wifi
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bRet;
	}

	// 查看以前是否也配置过这个网络
	private WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = wifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	private WifiConfiguration CreateWifiInfo(String SSID, String Password,
			WifiCipherType Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == WifiCipherType.WIFICIPHER_WEP) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == WifiCipherType.WIFICIPHER_WPA) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.status = WifiConfiguration.Status.ENABLED;
			
			///
			///
			config.priority = 30;  
            config.status = WifiConfiguration.Status.ENABLED;  
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);  
              
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);  
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);  
              
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);  
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);  
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);  
              
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);  
            // 必须添加，否则无线路由无法连接   
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			
		} else {
			return null;
		}
		return config;
	}

	private String checkNetworkInfo()

	{

		ConnectivityManager conMan = (ConnectivityManager) myContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// mobile 3G Data Network

		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();

		// txt3G.setText(mobile.toString()); //显示3G网络连接状态(mobile.toString()
		return mobile.toString();

		// wifi

		// State wifi =
		// conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

		// txtWifi.setText(wifi.toString()); //显示wifi连接状态

	}
	
	/** 
     * 切换网络 
     * @param type 网络类型(1为中心网络 2 为车载网络) 
     * @return 结果码 -1-本地设置为空；0-连接成功；1-已经为当前连接 
     * @see [类、类#方法、类#成员] 
     */  
    public int access2Wifi(String SSID, String Password,String type)  
    {  
    	
        // 无线未打开时，开启无线   
        if (!wifiManager.isWifiEnabled() && WifiManager.WIFI_STATE_ENABLING != wifiManager.getWifiState())  
        {  
        	wifiManager.setWifiEnabled(true);  
        }  
        // 获取本地的配置信息   
        String sSSID = "\"" + SSID + "\"";  
       String sKey = "\"" + Password + "\"";  
     //   String sSSID = "\"flashair\"";
    //   String sKey = "\"12345678\"";
        int encryptionType = getKeyMgmtType("WPA2-PSK");  
          
        List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();  
        WifiConfiguration config = null;  
        boolean isExisted = false;  
        int networkId = -1;  
        for (int i = configurations.size() - 1; i >= 0; i--)  
        {  
            config = configurations.get(i);  
            if (config.SSID.lastIndexOf(sSSID)>=0)  
            {  
                networkId = config.networkId;  
                isExisted = true;  
                break;  
            }  
        }  
        if (!isExisted)  
        {  
            // 安全类型 无、WEP(128)、WPA(TKIP)、WPA2(AES)   
            config = new WifiConfiguration();  
            // 名称   
            config.SSID = sSSID;  
            config.allowedKeyManagement.set(encryptionType);  
            if (encryptionType != 0)  
            {  
                // 密码   
                config.preSharedKey = sKey;  
            }  
            config.hiddenSSID = false;  
              
            config.priority = 30;  
            config.status = WifiConfiguration.Status.ENABLED;  
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);  
              
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);  
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);  
              
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);  
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);  
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);  
              
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);  
            // 必须添加，否则无线路由无法连接   
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);  
              
            networkId = wifiManager.addNetwork(config);  
            if (networkId != -1)  
            {  
            	wifiManager.saveConfiguration();  
            }  
        }  
        else  
        {  
            // 获取当前的wifi连接   
            WifiInfo curConnection = wifiManager.getConnectionInfo();  
            if (curConnection != null && sSSID.equals(curConnection.getSSID()))  
            {  
                // 已经是当前连接   
                return 1;  
            }  
              
            config.allowedKeyManagement.set(encryptionType);  
            if (encryptionType != 0)  
            {  
                // 密码   
                config.preSharedKey = sKey;  
            }  
            wifiManager.updateNetwork(config);  
        }  
          
        if (networkId != -1)  
        {  
        	wifiManager.disconnect();  
        	wifiManager.enableNetwork(networkId, true);  
        }  
        return 0;  
    }  

	 /** 
     * 获取加密类型 
     * @param type 加密类型 
     * @return 加密类型 
     * @see [类、类#方法、类#成员] 
     */  
    private int getKeyMgmtType(String type)  
    {  
        if (type == null)  
        {  
            return WifiConfiguration.KeyMgmt.NONE;  
        }  
          
        if ("WEP".equals(type))  
        {  
            return WifiConfiguration.KeyMgmt.IEEE8021X;  
        }  
        else if ("WPA-PSK".equals(type))  
        {  
            return WifiConfiguration.KeyMgmt.WPA_PSK;  
        }  
        else if ("WPA2-PSK".equals(type))  
        {  
            return WifiConfiguration.KeyMgmt.WPA_PSK;  
        }  
        return WifiConfiguration.KeyMgmt.NONE;  
    }  

    
    /*
     *
     onSignalStrengthsChanged   手机信号变动 
	 onServiceStateChanged      手机服务状态变动 
                其中onSignalStrengthsChanged可以获取到的有用信息如下：
    signalStrength.isGsm()           是否GSM信号 2G or 3G 
    signalStrength.getCdmaDbm();     联通3G 信号强度
    signalStrength.getCdmaEcio();    联通3G 载干比
    signalStrength.getEvdoDbm();     电信3G 信号强度
    signalStrength.getEvdoEcio();    电信3G 载干比
    signalStrength.getEvdoSnr();     电信3G 信噪比
    signalStrength.getGsmSignalStrength();  2G 信号强度
    signalStrength.getGsmBitErrorRate();    2G 误码率
   
    载干比 ，它是指空中模拟电波中的信号与噪声的比值
    
    onServiceStateChanged 提供的状态变动如下
    ServiceState.STATE_EMERGENCY_ONLY   仅限紧急呼叫
  ServiceState.STATE_IN_SERVICE       信号正常
  ServiceState.STATE_OUT_OF_SERVICE   不在服务区
  ServiceState.STATE_POWER_OFF        断电

  */  


}
