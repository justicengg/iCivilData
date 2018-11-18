package com.novery.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppOver {
	boolean boolOver ;
	static AppOver _app ;
	public static ArrayList<String> arrlstContacter ;
	AppOver()
	{
		boolOver = false ;		
	}
	public static AppOver getApp()
	{
		if( _app == null )
			_app = new AppOver();
		return _app ;
	}
	public boolean isOver()
	{
		return boolOver ;
	}
	public void SetOver()
	{
		boolOver = true; 
	}
	public String getShorttime( )
	{				
		Date dt = new Date() ;				
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);		
		String strdate =String.format(Locale.CHINESE,"%02d:%02d",cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
	
		return strdate;
	}
}
