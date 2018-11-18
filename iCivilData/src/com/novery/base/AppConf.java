package com.novery.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.annotation.TargetApi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Xml;

public class AppConf {
	public static final String mStrDBStoreFullFilepath = "/conf/newfiles.txt";
	public static final String mStrConfigFullFilepath = "/conf/config.xml";
	public static final String mStrGPSLogFile = "/logs/gpslog.txt";
	public static String mVersion = "1.0";

	public static String strSSID;
	public static String strFlashairPwd;
	public static String strFtpServerIP;
	public static String strFtpServerPort;
	public static String strFtpUsername;
	public static String strFtpUserpassword;
	public static String strSyncFileExtraName;
	public static String strSyncRootPath;
	public static int n3GStrengthUpload;
	public static String strVer;
	public static String strDataBaseUri ="http://182.92.232.25:9090/rest";
	public static String strRestUri ="http://182.92.232.25:9090/rest";;
	public static int nDefaultStation;
	
	public AppConf(){
		strDataBaseUri="http://182.92.232.25:9090/rest";
		strRestUri="http://182.92.232.25:9090/rest";
		nDefaultStation=50;
	}
	public int LoadConf() {
		
		return 0;
	}
	   
	public static void setValue(String strName, String strValue) {
		if (strName.equalsIgnoreCase("ssid"))
			strSSID = strValue;
		if (strName.equalsIgnoreCase("version"))
			strVer = strValue;
		else if (strName.equalsIgnoreCase("flashairpwd"))
			strFlashairPwd = strValue;
		else if (strName.equalsIgnoreCase("ftpserverip"))
			strFtpServerIP = strValue;
		else if (strName.equalsIgnoreCase("ftpserverport"))
			strFtpServerPort = strValue;
		else if (strName.equalsIgnoreCase("ftpusername"))
			strFtpUsername = strValue;
		else if (strName.equalsIgnoreCase("ftpuserpwd"))
			strFtpUserpassword = strValue;
		else if (strName.equalsIgnoreCase("SyncFileExtraName"))
			strSyncFileExtraName = strValue;
		else if (strName.equalsIgnoreCase("SyncRootPath"))
			strSyncRootPath = strValue;
		else if (strName.equalsIgnoreCase("GprsStrengthUpload"))
			n3GStrengthUpload = Integer.parseInt(strValue);
		else if (strName.equalsIgnoreCase("DataBaseUri"))
			strDataBaseUri = strValue;
		else if (strName.equalsIgnoreCase("DefaultStation"))
			nDefaultStation = Integer.parseInt(strValue);
	}

	public static int load() {
		PathString path = new PathString(SDCardUtils.getSDCardRootPath());
		path.catpath(mStrConfigFullFilepath);
		try {
			loadConfig(path.getPathHead());
		} catch (XmlPullParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	public static int loadConfig(String xmlPath) throws XmlPullParserException,
			IOException {
		File fileSource = new File(xmlPath);
		if (!fileSource.exists()) {
			return 1;
		}
		InputStream xml = new FileInputStream(new File(xmlPath));
		XmlPullParser pullParser = Xml.newPullParser();
		pullParser.setInput(xml, "UTF-8"); // 为Pull解释器设置要解析的XML数据

		int event = pullParser.getEventType();

		while (event != XmlPullParser.END_DOCUMENT) {

			switch (event) {

			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG: {
				String item = pullParser.getName();
				if (item.equalsIgnoreCase("root"))
					break;
				else {
					String text = pullParser.nextText();
					AppConf.setValue(item, text);
				}
			}
				break;
			case XmlPullParser.END_TAG:
				break;
			}

			event = pullParser.next();
		}
		return 0;
	}

	
	public static int update() throws IllegalArgumentException,
			IllegalStateException, IOException {

		PathString path = new PathString(SDCardUtils.getSDCardRootPath());
		path.catpath(mStrConfigFullFilepath);
		SDCardUtils.prepairSDCardDir(path.trimFilename());

		int nRet = -1;
		//
		// write to file
		File fileSource = new File(path.getPathHead());
		if (fileSource.exists())
			fileSource.delete();

		try {
			fileSource.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OutputStream fos = null;

		try {
			fos = new FileOutputStream(new File(path.getPathHead()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 3;
		}

		/*
		 * public static String strSSID; public static String strFlashairPwd;
		 * public static String strFtpServerIP; public static String
		 * strFtpServerPort; public static String strFtpUsername; public static
		 * String strFtpUserpassword;
		 */
		XmlSerializer serializer = Xml.newSerializer();

		serializer.setOutput(fos, "UTF-8");
		serializer.startDocument("UTF-8", true);
		serializer.startTag(null, "root");

		serializer.startTag(null, "version");
		serializer.text(mVersion);
		serializer.endTag(null, "version");

		serializer.startTag(null, "SSID");
		serializer.text(strSSID);
		serializer.endTag(null, "SSID");

		serializer.startTag(null, "FlashairPwd");
		serializer.text(strFlashairPwd);
		serializer.endTag(null, "FlashairPwd");

		serializer.startTag(null, "FTPServerIP");
		serializer.text(strFtpServerIP);
		serializer.endTag(null, "FTPServerIP");

		serializer.startTag(null, "FTPServerPort");
		serializer.text(strFtpServerPort);
		serializer.endTag(null, "FTPServerPort");

		serializer.startTag(null, "FTPUsername");
		serializer.text(strFtpUsername);
		serializer.endTag(null, "FTPUsername");

		serializer.startTag(null, "FTPUserpwd");
		serializer.text(strFtpUserpassword);
		serializer.endTag(null, "FTPUserpwd");

		serializer.startTag(null, "SyncFileExtraName");
		serializer.text(strSyncFileExtraName);
		serializer.endTag(null, "SyncFileExtraName");

		serializer.startTag(null, "SyncRootPath");
		serializer.text(strSyncRootPath);
		serializer.endTag(null, "SyncRootPath");

		serializer.startTag(null, "GprsStrengthUpload");
		serializer.text(String.valueOf(n3GStrengthUpload));
		serializer.endTag(null, "GprsStrengthUpload");
		
		
		serializer.startTag(null, "DataBaseUri");
		serializer.text(strDataBaseUri);
		serializer.endTag(null, "DataBaseUri");
		
		serializer.startTag(null, "DefaultStation");
		serializer.text(String.valueOf(nDefaultStation));
		serializer.endTag(null, "DefaultStation");
		
		serializer.endTag(null, "root");

		serializer.endDocument();
		// 注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
		try {
			fos.flush();
			fos.close();
			nRet = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 7;
		}

		return nRet;
	}

	public static int init() {
		try {
			create();
			load();
			if (!strVer.equalsIgnoreCase(mVersion)) {
				strVer = mVersion;
				//
				// delete old version
				PathString path = new PathString(
						SDCardUtils.getSDCardRootPath());
				path.catpath(mStrConfigFullFilepath);
				SDCardUtils.prepairSDCardDir(path.trimFilename());
				File fileSource = new File(path.getPathHead());
				if (fileSource.exists()) {
					fileSource.delete();
				}
				create();
				load();
			}
		} catch (IllegalArgumentException | IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public static int create() throws IllegalArgumentException,
			IllegalStateException, IOException {
		PathString path = new PathString(SDCardUtils.getSDCardRootPath());
		path.catpath(mStrConfigFullFilepath);
		SDCardUtils.prepairSDCardDir(path.trimFilename());

		int nRet = -1;
		//
		// write to file
		File fileSource = new File(path.getPathHead());
		if (fileSource.exists())
			return 0;

		try {
			fileSource.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OutputStream fos = null;

		try {
			fos = new FileOutputStream(new File(path.getPathHead()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 3;
		}

		XmlSerializer serializer = Xml.newSerializer();

		serializer.setOutput(fos, "UTF-8");
		serializer.startDocument("UTF-8", true);
		serializer.startTag(null, "root");

		serializer.startTag(null, "version");
		serializer.text(mVersion);
		serializer.endTag(null, "version");

		serializer.startTag(null, "SSID");
		serializer.text("flashair");
		serializer.endTag(null, "SSID");

		serializer.startTag(null, "flashairpwd");
		serializer.text("12345678");
		serializer.endTag(null, "flashairpwd");

		serializer.startTag(null, "FTPServerIP");
		serializer.text("61.4.82.80");
		serializer.endTag(null, "FTPServerIP");

		serializer.startTag(null, "FTPServerPort");
		serializer.text("2121");
		serializer.endTag(null, "FTPServerPort");

		serializer.startTag(null, "FTPUsername");
		serializer.text("");
		serializer.endTag(null, "FTPUsername");

		serializer.startTag(null, "FTPUserpwd");
		serializer.text("");
		serializer.endTag(null, "FTPUserpwd");

		serializer.startTag(null, "SyncFileExtraName");
		serializer.text(".PTE;.gps");
		serializer.endTag(null, "SyncFileExtraName");

		serializer.startTag(null, "GprsStrengthUpload");
		serializer.text("25");
		serializer.endTag(null, "GprsStrengthUpload");

		
		
		serializer.startTag(null, "SyncRootPath");
		serializer.text("/");
		serializer.endTag(null, "SyncRootPath");
		
		serializer.startTag(null, "DataBaseUri");
		serializer.text("http://");
		serializer.endTag(null, "DataBaseUri");
		
		serializer.startTag(null, "DefaultStation");
		serializer.text("0");
		serializer.endTag(null, "DefaultStation");
		/*
		 * else if (strName.equalsIgnoreCase("DataBaseUri"))
			strDataBaseUri = strValue;
		else if (strName.equalsIgnoreCase("DefaultStation"))
			nDefaultStation = Integer.parseInt(strValue);
		 * */
		
		
		serializer.endTag(null, "root");

		serializer.endDocument();
		// 注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
		try {
			fos.flush();
			fos.close();
			nRet = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 7;
		}

		return nRet;
	}
}
