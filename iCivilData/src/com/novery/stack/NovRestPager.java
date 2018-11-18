package com.novery.stack;

public class NovRestPager implements IRestPager {

	public NovRestPager() {
		pageNo = 0 ;
		rowCount = 0 ;
		pageSize = 25;
	}
	public int pageNo;
	public int rowCount;
	public int pageSize;
	public int rowLastNo;
	
	/* (non-Javadoc)
	 * @see com.novery.stack.IRestPager#reset()
	 */
	@Override
	public void reset(){
		pageNo = 0 ;
		rowCount = 0 ;
		pageSize = 25;
	}
	/* (non-Javadoc)
	 * @see com.novery.stack.IRestPager#nextPageStartRow()
	 */
	@Override
	public int nextPageStartRow(){
		this.pageNo =  rowLastNo/pageSize;
	
		if( ( pageNo + 1) >= rowCount/pageSize)
			return pageNo * pageSize;
		else
			return ( pageNo + 1)* pageSize;
	}
	/* (non-Javadoc)
	 * @see com.novery.stack.IRestPager#proviousPageStartRow()
	 */
	@Override
	public int proviousPageStartRow(){
		this.pageNo =  rowLastNo/pageSize;	
		if( pageNo == 0)
			return 0 ;
		return ( pageNo -1)* pageSize;
	}
	/* (non-Javadoc)
	 * @see com.novery.stack.IRestPager#update(int, int, int)
	 */
	@Override
	public void update( int rowCount ,int rowStart, int rowReturned ){
		this.rowCount = rowCount;
		this.rowLastNo = rowStart + rowReturned -1 ;
		this.pageNo =  rowLastNo/pageSize;		
	}
	@Override
	public int getPageSize() {		
		return pageSize;
	}
	@Override
	public int getRowCount() {
		return rowCount;
	}
	@Override
	public int getLastRowNo() {
		return rowLastNo;
	}
	@Override
	public int getPageNo() {
		return pageNo;
	}
	

}
