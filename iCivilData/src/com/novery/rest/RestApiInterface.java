package com.novery.rest;

import android.content.Context;

public interface RestApiInterface extends Runnable {
	public IRestResultToListviewInterface getRestToListview();
	public void setRestToListview(
			IRestResultToListviewInterface restToListview);
	public void init(Context mContext, 
			Integer objectType,
			String objectID,
			Integer rowStart,
			Integer rowEnd);

}