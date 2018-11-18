package com.novery.rest;

import java.util.Stack;

import com.novery.stack.NovListviewAdapter;
import com.novery.stack.NovRestObjectInfo;
import com.novery.stack.NovRestResponseObjectInfo;

public interface IRestResultToListviewInterface {
	void setAdapter(NovListviewAdapter adapter);
	void loadObjectInfo( NovRestResponseObjectInfo objinfo );
	void updatePager(  int rowCount,int rowStart, int rowReturned );
	Stack<NovRestObjectInfo> getStackObject();
	void setStackObject(Stack<NovRestObjectInfo> stackObject);

}
