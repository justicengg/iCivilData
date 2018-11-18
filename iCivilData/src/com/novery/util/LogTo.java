package com.novery.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LogTo {
	public  String mFilePath ;
	public  File dbFile ;
	
	LogTo( String strFilePath )
	{
		PathString strpath = new PathString( strFilePath );
		
		mFilePath = strpath.getPathHead() ;
		createLogFile();
	}
	public int createLogFile()
	{
		PathString path = new PathString( SDCardUtils.getSDCardRootPath() ) ;
		path.catpath(mFilePath );
		mFilePath = path.getPathHead();
		int nRet = -1 ;
			//
			//write to file
			File fileSource = new File ( mFilePath ) ;
			if( fileSource.exists())
				return 0 ;
			
			int idx = mFilePath.lastIndexOf('/');
			String strpath = mFilePath.substring(0,idx);			
			SDCardUtils.prepairDir("", strpath);
			try {
				fileSource.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			FileOutputStream fos = null ;
			try {
				fos=new FileOutputStream(new File(mFilePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
				return 3;
			}
			OutputStreamWriter osw = null;
			try {
				osw = new OutputStreamWriter(fos, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					fos.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return 4 ;
			}
	        BufferedWriter  bw=new BufferedWriter(osw);
	        //
	        //write table head
	        String strout = getDatetime() + " " + "START TO LOG" ;
	    	
	        try {
				bw.write(strout+"\r\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				nRet = 5  ;
			}
	        
	        
	        //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
	        try {
				bw.close();
				osw.close();
				fos.close();
				nRet = 0 ;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 7;
			}
	       
	        return nRet ;
	       
	}
	

	public String getDatetime( )
	{
		
		Date dt = new Date() ;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);		
		String strdate =String.format(Locale.CHINESE,"%04d-%02d-%02d %02d:%02d:%02d:%03d",cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),cal.get(Calendar.SECOND),cal.get(Calendar.MILLISECOND));
	
		return strdate;
	}
	public int log(String strlog)
	{
		int idx = mFilePath.indexOf("log.txt");
		if( idx >40)
		{
			System.out.println(mFilePath);
		}
		String str = getDatetime() + " " + strlog +"\n";
		logData( mFilePath, str);
        return 0 ;
        
	}
	

    /**   
     * 追加文件：使用FileWriter   
     *    
     * @param fileName   
     * @param content   
     */    
    public static void logData(String fileName, String content) {   
        FileWriter writer = null;  
        try {     
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件      
            writer = new FileWriter(fileName, true);     
            writer.write(content);       
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }   
    }   
}
