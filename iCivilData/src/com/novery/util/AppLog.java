package com.novery.util;

import android.annotation.SuppressLint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public  class AppLog {
	public static  String strlogpath =  "/updatelog/log.txt"; ;
	public static File dbFile ;
	
	public AppLog()
	{
		PathString path = new PathString( SDCardUtils.getSDCardRootPath() ) ;
		path.catpath(strlogpath );
		strlogpath = path.getPathHead();
	}
	@SuppressWarnings("resource")
	public static int createLogFile()
	{
		PathString path = new PathString( SDCardUtils.getSDCardRootPath() ) ;
		path.catpath(strlogpath );
		strlogpath = path.getPathHead();
		
		int nRet = -1 ;
			//
			//write to file
			File fileSource = new File ( strlogpath ) ;
			if( fileSource.exists())
				return 0 ;
			
			int idx = strlogpath.lastIndexOf('/');
			String strpath = strlogpath.substring(0,idx);			
			SDCardUtils.prepairDir("", strpath);
			if( !fileSource.exists())
			{
				try {
					fileSource.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			FileOutputStream fos = null ;
			try {
				fos=new FileOutputStream(new File(strlogpath));
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
	

	public static String getDatetime( )
	{
		
		Date dt = new Date() ;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);		
		String strdate =String.format(Locale.CHINESE,"%04d-%02d-%02d %02d:%02d:%02d:%03d",cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),cal.get(Calendar.SECOND),cal.get(Calendar.MILLISECOND));
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		//String strdate  = df.format(dt);
		
		return strdate;
	}
	public static int log(String strlog)
	{
	
		int idx = strlogpath.indexOf("log.txt");
		if( idx >40)
		{
			System.out.println(strlogpath);
		}
		String str = getDatetime() + " " + strlog +"\n";
		writelog( strlogpath, str);
        return 0 ;
        
	}
	
	public static void clearlog()
	{
		PathString path = new PathString( strlogpath ) ;
		String logPath = path.getPathHead();	
		File fileSource = new File ( logPath ) ;
		if( fileSource.exists())
		{
			fileSource.delete();
		}
		createLogFile();
	}
    public static void method1(String file, String conent) {     
        BufferedWriter out = null;     
        try {      
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));     
            out.write(conent);     
        } catch (Exception e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(out != null){  
                    out.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }     
    }     
    
    /**   
     * 追加文件：使用FileWriter   
     *    
     * @param fileName   
     * @param content   
     */    
    public static void writelog(String fileName, String content) {   
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
    
    /**   
     * 追加文件：使用RandomAccessFile   
     *    
     * @param fileName 文件名   
     * @param content 追加的内容   
     */    
    public static void method3(String fileName, String content) {   
        RandomAccessFile randomFile = null;  
        try {     
            // 打开一个随机访问文件流，按读写方式      
            randomFile = new RandomAccessFile(fileName, "rw");     
            // 文件长度，字节数      
            long fileLength = randomFile.length();     
            // 将写文件指针移到文件尾。      
            randomFile.seek(fileLength);     
            randomFile.writeBytes(content);      
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally{  
            if(randomFile != null){  
                try {  
                    randomFile.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }    

}
