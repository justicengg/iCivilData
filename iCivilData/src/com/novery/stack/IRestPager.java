package com.novery.stack;

public interface IRestPager {

	public int getPageSize();
	public int getRowCount();
	public int getLastRowNo();
	public int getPageNo();
	public void reset();

	public int nextPageStartRow();

	public int proviousPageStartRow();

	public void update(int rowCount, int rowStart, int rowReturned);

}