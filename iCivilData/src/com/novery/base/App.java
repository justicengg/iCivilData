package com.novery.base;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class App {

	public static void main2(String[] args) {

		// byte[] bytes = new byte[] { 50, 0, -1, 28, -24 };
		// String sendString = null;
		// try {
		// sendString = new String( bytes ,"UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// byte[] sendBytes= sendString .getBytes("UTF8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		//

		//String str = "f104";
		//Byte[] bytes2 = stringToBytes(str);

		//System.out.println(bytes2);
		//String str2 = App.BytesToString(bytes2);
		//System.out.println(str2);
		//Map<String, Object> params = new HashMap<String, Object>();
		//params.put("hello", "123456");
		//System.out.println( params.get("hello").toString());
		
		
		//Date dt =  new Date() ;
		//String str = mysqlDateToFullString( dt );
		//Date date = fullStringToMysqlDate( str );
		//System.out.println(date);
		//System.out.println( mysqlDateToFullString ( date ) );
		
		
	}
	public static void sleep( int ms )
	{
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{

		int n =19 ;
		System.out.println(  App.intToHexString( n ));
		
	}
	public static void println(Object obj) {
		System.out.println(obj);
		com.novery.util.AppLog.log(obj.toString());
		
	}

	public static String BytesToString(Byte[] bytes) {
		String strText = "";
		String strItem ;
		if ( null == bytes )
			return "" ;
		for (int i = 0; i < bytes.length; i++) {
			strItem = Integer.toHexString( bytes[i] & 0xFF);
			if( strItem.length() == 1 )
				strItem = "0"+strItem;
			strText +=  strItem;
		}
		return strText.toUpperCase();
	}
	

	public static String bytesToString(byte[] bytes) {
		String strText = "";
		String strItem ;
		if ( null == bytes )
			return "" ;
		for (int i = 0; i < bytes.length; i++) {
			strItem = Integer.toHexString( bytes[i] & 0xFF);
			if( strItem.length() == 1 )
				strItem = "0"+strItem;
			strText +=  strItem;
		}
		return strText.toUpperCase();
	}
	public static String byteToHex( byte bt )
	{
		return Integer.toHexString( bt & 0xFF );
	}
	public static String intToHexString( int n ){
		String str  = Integer.toHexString( n & 0xFF );
		str = String.format("%02x", n);
		return str;
	}
	public static String bytesToStringInt(byte[] bytes) {			
		String strText = "";
		String strItem ;
		if ( null == bytes )
			return "" ;
		for (int i = 0; i < bytes.length; i++) {
			strItem = Integer.toHexString( bytes[i] & 0xFF);
			if( strItem.length() == 1 )
				strItem = "0"+strItem;
			strText +=  strItem;
		}
		return strText.toUpperCase();
	}
	public static Byte[] stringToBytes(String str) {
		Byte[] bytes = null;

		String stritem = str.trim();

		if (stritem.length() < 0)
			return null;
		int n = stritem.length() / 2;

		if (n == 0) {
			bytes = new Byte[1];

			if( stritem.isEmpty() )
				bytes[0] = '0' ;
			else
			bytes[0] = Byte.parseByte(stritem);

			if( stritem.isEmpty() )
				return null ;
			bytes[0] = Byte.parseByte(stritem);

			return bytes;
		}
		bytes = new Byte[n];

		for (int i = 0; i < n; i++) {
			// item = String.format("%c%c",
			// stritem.charAt(i),stritem.charAt(i+1));
			int nByte = charValue(stritem.charAt(2 * i)) * 16
					+ charValue(stritem.charAt(2 * i + 1));
			try {

				bytes[i] = (byte) nByte;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}

		return bytes;
	}
	public static byte[] stringTobytes(String str) {
		byte[] bytes = null;

		String stritem = str.trim();

		if (stritem.length() < 0)
			return null;
		int n = stritem.length() / 2;

		if (n == 0) {
			
			bytes = new byte[1];
			if( stritem.isEmpty() )
				bytes[0] = '0' ;
			else
			bytes[0] = Byte.parseByte(stritem);

			return bytes;
		}
		bytes = new byte[n];

		for (int i = 0; i < n; i++) {
			// item = String.format("%c%c",
			// stritem.charAt(i),stritem.charAt(i+1));
			int nByte = charValue(stritem.charAt(2 * i)) * 16
					+ charValue(stritem.charAt(2 * i + 1));
			try {

				bytes[i] = (byte) nByte;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}

		return bytes;
	}
	public static int SubBytes(byte[] byteArr, int j, int n, byte[] arrItem) {
		for( int i=0;i<n;i++)
			arrItem[i] = byteArr[j++];
		return j;
	}
	
	public static int charValue(char ch) {
		if (ch >= '0' && ch <= '9') {
			return ch - '0';
		} else if (ch >= 'a' && ch <= 'f') {
			return ch - 'a' + 10;
		}

		else if (ch >= 'A' && ch <= 'F') {
			return ch - 'A' + 10;
		}
		return -1;

	}


	public static int byte2int(byte b) {
		/*
		int n = b;
		if (n >= 0)
			return n;
		else
			return n + 256;
			*/
		return b&0xFF;
	}
	public static int byte2unsignedint(byte b) {
		return b&0xFF;
	}
	public static void appendBytes(byte[] bufSocket, int nBuffer, byte[] bufItem, int nItem) {
		for( int i=0;i<nItem; i++ )
			bufSocket[nBuffer+i] = bufItem[i];
	}
	public static double byte2double(byte b) {
		double db = b ;
		if ( db >= 0)
			return b;
		else
			return db + 256;
	}
	public static long byte2long(byte b) {
		long db = b ;
		if ( db >= 0)
			return b;
		else
			return db + 256;
	}
	public static int doubleByteValue(byte b[]) {
		if (b.length != 2)
			return -999;
		int n = byte2int(b[0]) * 256 + byte2int(b[1]);
		return n;

	}

	public static int doubleByteValue(byte b0, byte b1) {
		int n = byte2int(b0) * 256 + byte2int(b1);
		return n;

	}
	public static double bcdValue( int lenInt , int lenDec, String strbytes) {	
		if( null == strbytes || strbytes.isEmpty() || lenDec > strbytes.length() )
			return -1;
		
		int len = strbytes.length();
		String stritem =strbytes;
		if( 0 < lenDec )
		{
			stritem = strbytes.substring(0,len-lenDec) + "." + strbytes.substring( len-lenDec,len );
		}
		
	
		double db = Double.parseDouble(stritem);	
		return db;
	}
	public static String bcd2String( byte[] bytes )
	{
		return App.bytesToString(bytes);
	}
	public static byte[] string2bcd( String str )
	{
		
		if ( null == str ||  str.isEmpty() )
			return null ;
		
		int nSize = str.length() ;
		if( 1 ==  nSize %2 ) 
			str = "0" + str ;
		
		byte[] byteResult = App.stringTobytes(str);		
		return byteResult ;
		
		
	}
	public static byte strint2byte( char[] str)
	{
		byte result = 0 ;
		if( null == str ||  str.length != 2 )
			return 0 ;
		int n = charValue( str[0]) <<8 + charValue(str[1]) ;
		result = (byte) (n & 0xFF) ;
		return result;
	}
	public static byte[] trimHeadBytes(byte[] bytes , int n )
	{
		int nStart = n;
		int nDataItem = bytes.length - nStart;
		byte[] bytesItem = new byte[nDataItem];
		for (int i = 0; i < nDataItem; i++) {
			bytesItem[i] = bytes[i + nStart];
		}
		return bytesItem ;
	}

	public static String getDatetime() {

		Date dt = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		String strdate = String.format(Locale.CHINESE,
				"%04d-%02d-%02d %02d:%02d:%02d:%03d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));

		return strdate;
	}
	
	public static String getNowDateTime( Date dt ) {	
		Calendar cal = Calendar.getInstance();		
		cal.setTime(dt);
		String strdate = String.format(Locale.CHINESE,
				"%04d-%02d-%02d %02d:%02d:%02d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND));

		return strdate;
	}
	public static String getLast24DateTime( Date dt ) {	
		Calendar cal = Calendar.getInstance();		
		cal.setTime(dt);
		cal.add(Calendar.DATE,-1 );
		String strdate = String.format(Locale.CHINESE,
				"%04d-%02d-%02d %02d:%02d:%02d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND));

		return strdate;
	}

	public static int getDateTimeSerioNo() {

		Date dt = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		String strdate = String.format(Locale.CHINESE,
				"%02d%02d%02d%02d%02d",
				cal.get(Calendar.MONTH) + 1+10,
				cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND));
		int nSerioNo = Integer.parseInt(strdate);
		return nSerioNo ;
	}
	public static Date timestamp2Date( String strTimeStamp ){
		Timestamp ts = new Timestamp( Long.parseLong( strTimeStamp ));
        Date date = new Date();   
        try {   
            date = ts;   
            System.out.println(date);   
        } catch (Exception e) {   
            e.printStackTrace();   
        }  
        return date;
	}
	public static String getSQLDatetime() {

		Date dt = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		String strdate = String.format(Locale.CHINESE,
				"%04d-%02d-%02d %02d:%02d:%02d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND));

		return strdate;
	}
	public static String dateToString( Date dt )
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		String strdate = String.format(Locale.CHINESE,
				"%04d-%02d-%02d %02d:%02d:%02d:%03d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
		return strdate;
				
	}
	/*
	public static Date fullStringToDate( String strdate )
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");  
        Date date = null;  
        try {  
            // Fri Feb 24 00:00:00 CST 2012  
            date = format.parse(strdate);   
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return date ;
        
	}
	*/
	public static String mysqlDateToFullString( Date date )
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String strdate = "" ;
        strdate = format.format(date);  
        return strdate ;
        
	}
	public static Date fullStringToMysqlDate( String strdate )
	{
		String str = strdate.substring(0,19);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date date = null;  
        try {  
            // Fri Feb 24 00:00:00 CST 2012  
            date = format.parse(str);   
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return date ;
        
	}
	public static  byte[] String2ASCIIBytes(String strbytes) {
		int nSize = strbytes.length();
		byte[] byteCommand = new byte[ nSize ] ;
		for( int i=0;i<nSize;i++)
		{
			byteCommand[i] = (byte) (strbytes.charAt(i) & 0xFF );
		}
		return byteCommand;
	}
	public static   String ASCIIBytes2String(byte[] bytes) {
		String strbytes = "";
		int nSize = bytes.length;
		for( int i=0;i<nSize;i++)
		{
			char ch = (char)( bytes[i] );
			strbytes += ch;
		}
		return strbytes;
	}
	/*
	public static java.sql.Date fullStringToSqlDate( String strdate )
	{
		//DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		java.sql.Date date = null;  
        try {  
            // Fri Feb 24 00:00:00 CST 2012  
        	date = java.sql.Date.valueOf(strdate );
            //date =(java.sql.Date) format.parse(strdate);   
        } catch (IllegalArgumentException  e) {  
            e.printStackTrace();  
        }  
        return date ;
        
	}
	*/
	/*
	public static String dateToFullString( Date date )
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");  
		String strdate = "" ;
        strdate = format.format(date);  
        return strdate ;
        
	}
	*/
	
	/*
	public static String sqlDateToFullString( java.sql.Date date )
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		String strdate = "" ;
        strdate = format.format(date);  
        return strdate ;        
	}
	*/
}
