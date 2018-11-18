package com.novery.util;

public class PathString {
	
	static public  void main( String[] args)
	{
	

	}
	static final String ROOT = "/data";
	public String mStrPath;
	public PathString( String str )
	{
		if( str==null)
			mStrPath="" ;
		else
			mStrPath =str ;
		mStrPath.replace('\\', '/');
		TrimBegin();
		TrimEnd();
	}
	public void TrimBegin()
	{		
		if( mStrPath.length()==0 )
			return ;
		if( mStrPath.equals("/"))
		{
			mStrPath="";
			return ;
		}
		while ( (mStrPath.length()>0) && mStrPath.startsWith("/"))
		{
			if( mStrPath.length()==1)
				mStrPath = "" ;
			else
				mStrPath = mStrPath.substring(1);
		}			
	}
	public void TrimEnd()
	{		
		if( mStrPath.length()==0 )
			return ;
		if( mStrPath.equals("/"))
		{
			mStrPath="";
			return ;
		}
		while ( (mStrPath.length()>0) && mStrPath.endsWith("/"))
		{
			if( mStrPath.length()==1)
				mStrPath = "" ;
			else
				mStrPath = mStrPath.substring(0, mStrPath.length()-1);
		}			
	}
	public String getPathName()
	{		
		return mStrPath ;
	}
	public String getPathHead()
	{
		if( mStrPath.length()==0)
			return "" ;
		return "/" + mStrPath ;
	}
	public String getPathTail()
	{		
		if( mStrPath.length()==0)
			return "" ;
		return mStrPath + "/" ;
	}
	public String getPathBoth()
	{
		if( mStrPath.length()==0)
			return "" ;
		return "/" + mStrPath + "/";
		
	}
	public boolean equalsdir(String str)
	{
		String strpath = new PathString( str).getPathName();		
		return mStrPath.equalsIgnoreCase(strpath) ;
	}
	public void catpath(String str)
	{
		String strpath = new PathString( str).getPathName();
		mStrPath+= "/" + strpath ;
		TrimBegin();
		TrimEnd();
	}
	public String trimFilename() {
		int idx = mStrPath.lastIndexOf("/");
		String strDir =mStrPath.substring(0,idx);
		return strDir ;
	}
	public String getFilename() {
		int idx = mStrPath.lastIndexOf("/");
		int nlen = mStrPath.length();
		if( nlen <= idx +1)
			return "";
		String strFilename =mStrPath.substring(idx+1,nlen);
		return strFilename ;
	}
	public String addRoot(  )
	{
		if( isRoot())
			return this.getPathHead() ;
		
		String strpath = mStrPath ;
		String path = new PathString( strpath).getPathHead();
		if(path.length()<6)
		{
			path= "/data" + path ;
			return path ;
		}		
		String prefix = path.substring(0,6);
		if( prefix.equalsIgnoreCase("/data/"))
		{
			return path ;
		}
		else
		{
			path= "/data" + path ;
		}
		return path ;
	}
	public String removeRoot(  )
	{
		if( isRoot () )
			return this.getPathHead() ;
		
		String strpath = mStrPath ;
		String path = new PathString( strpath).getPathHead();
		if( path.length() < 6 )
			return path ;
		String prefix = path.substring(0,6);
		if( prefix.equalsIgnoreCase("/data/"))
		{
			String str = path.substring(5, path.length()-1);
			return str ;
		}		
		return path ;
	}
	public boolean isRoot( )
	{
		String path = mStrPath ;
		if(path.equalsIgnoreCase("/data") || path.equalsIgnoreCase("data") || path.equalsIgnoreCase("/data/"))
			return true;
		else
			return false; 
	}
}
