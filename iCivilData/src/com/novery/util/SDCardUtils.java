package com.novery.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.os.Environment;

public class SDCardUtils {

	public static String getFilename( String fullFileName)
	{
		String strPattern="\\w+\\.\\w+";
		Pattern p = Pattern.compile(strPattern);	
		
		Matcher m = p.matcher(fullFileName);
		String str ="" ;
		while( m.find())
		{
			str= str+ m.group();
		}
		return str ;
	}
	
	public static String getExtraName( String str )
	{
		String strPattern = "\\.[a-zA-Z]{3}";
		Pattern p = Pattern.compile(strPattern);	
		
		Matcher m = p.matcher(str);
		String strEx = "" ;
		if( m.find())
		{
			strEx = m.group();
		}
		return strEx ;
	}
	public static String getFilepath( String fullFileName)
	{
		String strPattern="^/.+/\\b";
		Pattern p = Pattern.compile(strPattern);			
		Matcher m = p.matcher(fullFileName);
		String str ="" ;
		if( m.find())
		{
			str= m.group();
		}
		return str ;
	}
	
	
	
	@SuppressLint("SimpleDateFormat")
	public static String attendDateTime( String strFilename )
	{
		
		Date dt = new Date() ;
		int idx = strFilename.lastIndexOf('.');
		String strleft ="" ;
		String strEx = "" ;
		if( idx>=0 )
		{
			strleft = strFilename.substring(0,idx );
			strEx = strFilename.substring(idx,strFilename.length());		
		}
		else
		{
			strleft = strFilename ;
			strEx = "" ;
		}	
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);		
		
		String strdate =String.format("%04d%02d%02d%02d%02d%02d",cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),cal.get(Calendar.SECOND));
		String str = strleft+"_"+strdate+ strEx;		
		return str;
	}
	public static String attendDateTime( String strFilename,Date dt)
	{ 
		int idx = strFilename.lastIndexOf('.');
		String strleft ="" ;
		String strEx = "" ;
		if( idx>=0 )
		{
			strleft = strFilename.substring(0,idx );
			strEx = strFilename.substring(idx,strFilename.length());		
		}
		else
		{
			strleft = strFilename ;
			strEx = "" ;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);		
		
		String strdate =String.format(Locale.getDefault(),"%04d%02d%02d%02d%02d%02d",cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),cal.get(Calendar.SECOND));
		String str = strleft+"_"+strdate+ strEx;		
		return str;
	}
	public static String prepairDir( String RootPath, String strPath)
	{
		if( strPath.length()==0)
			return "" ;
		PathString rootpath = new PathString( RootPath );
		PathString pathex = new PathString ( strPath );
		if( pathex.getPathName().length()==0 )
			return "" ;
		
		String strPattern="/[^/]+";
		Pattern p = Pattern.compile(strPattern);		
		Matcher m = p.matcher(pathex.getPathHead());

		String SDCard = Environment.getExternalStorageDirectory() + "";
		while( m.find())
		{
			rootpath.catpath(  m.group() );
			if( rootpath.getPathHead().length()<=SDCard.length())
			{
				continue ;
			}
			File fileDir = new File(rootpath.getPathHead());
			if (!fileDir.exists()) {
				fileDir.mkdir();
			}
		}
		return rootpath.getPathHead() ;
	}
	
	public static String prepairSDCardDir(String strPath)
	{
		if( strPath.length()==0)
			return "" ;
		

		PathString pathex = new PathString ( strPath );
		String SDCard = Environment.getExternalStorageDirectory() + "";
		PathString rootpath = new PathString(SDCard);
		if( pathex.getPathName().indexOf(rootpath.getPathName())<0)
		{
			return "";
		}
		
		String strPattern="/[^/]+";
		Pattern p = Pattern.compile(strPattern);		
		Matcher m = p.matcher(pathex.getPathHead());
		PathString path= new PathString("");
		while( m.find())
		{
			path.catpath(  m.group() );
			if( path.getPathHead().length()<=rootpath.getPathName().length())
			{
				continue ;
			}
			File fileDir = new File(path.getPathHead());
			if (!fileDir.exists()) {
				fileDir.mkdir();
			}
		}
		return path.getPathHead() ;
	}
	public static String getSDCardRootPath()
	{
		String SDCard = Environment.getExternalStorageDirectory() + ""+ "/flashair";
		return SDCard ;
	}
}
