package com.novery.alfa.fragment;

import java.util.Stack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.novery.icivilphone.R;
import com.novery.rest.INovListviewRowButtonOnClickInterface;
import com.novery.rest.IRestResultToListviewInterface;
import com.novery.rest.NovListviewRowButtonOnClickImpl;
import com.novery.rest.NovMessageName;
import com.novery.rest.RestApiGetDeviceInfoRunnable;
import com.novery.rest.RestResultDeviceInfoToListviewImpl;
import com.novery.rest.RestResultObjectInfoToListviewImpl;
import com.novery.stack.NovListviewAdapter;
import com.novery.stack.NovRestObjectInfo;
import com.novery.stack.NovUserInfo;

/**
 * @author yangyu
 *	功能描述：首页fragment页面
 * @param <NotifyReceiver>
 */
public class FragmentAlfaDeviceLeft  extends Fragment {

	
	private View rootView;
	private Context  mContext;
	public ListView mlistView;
	//private RestApiRunnableEx runRest ;
	private Thread thdService;
	private NotifyReceiver receiverNotify = null;
	private Intent intentNotify = null;
	private IRestResultToListviewInterface restResultTools;
	private NovListviewAdapter novLisviewAdapter;
	private RestApiGetDeviceInfoRunnable rest;
	private INovListviewRowButtonOnClickInterface onRowButtonClick;
	private Stack<NovRestObjectInfo> stackObject ;
	
	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static FragmentAlfaDeviceLeft newInstance(int index) {
		FragmentAlfaDeviceLeft f = new FragmentAlfaDeviceLeft();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);

		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_alfa_device_left, container, false);
		
		
		rootView = view;
		mContext = getActivity(); // container.getContext();
		
		stackObject = new Stack<NovRestObjectInfo>();
		
		receiverNotify = new NotifyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction( NovMessageName.MSG_ACTION_DEVICE_INFO);
		intentNotify = mContext.registerReceiver(receiverNotify, filter);

		ListView listView = (ListView) rootView.findViewById(R.id.lv_device_left_data);
		mlistView = listView;
		novLisviewAdapter = new NovListviewAdapter( mContext,rootView,mlistView);

		restResultTools = new RestResultDeviceInfoToListviewImpl();
		restResultTools.setAdapter(novLisviewAdapter);
		restResultTools.setStackObject(stackObject);
	
		
		
		
		rest = new RestApiGetDeviceInfoRunnable();
		rest.init(rootView.getContext(),
				0,
				NovUserInfo.getInstance().getLogin().getClientID(),
				0,
				novLisviewAdapter.getRestPager().getPageSize() );
		rest.setRestToListview(restResultTools);
		
		onRowButtonClick = new NovListviewRowButtonOnClickImpl(rootView,restResultTools,rest);
		novLisviewAdapter.setOnRowButtonClick(onRowButtonClick);
		setOnClickNavigator();
		CallRestApi(novLisviewAdapter);
		return view;
	}

	private void CallRestApi(NovListviewAdapter novLisviewAdapter) {
		
		thdService = new Thread( rest );
		thdService.start();
	}
	
	private void setOnClickNavigator(){
		ImageButton btProvious = (ImageButton) rootView.findViewById(R.id.imgBtProvious);
		ImageButton btNext = (ImageButton) rootView.findViewById(R.id.imgBtNext);
		ImageButton btReturnUp = (ImageButton) rootView.findViewById(R.id.imgBtReturnUp);
		
		btReturnUp.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
				Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();
				NovRestObjectInfo top = stck.pop();
				if( stck.isEmpty()){
					stck.push( top );
					return ;
				}
				NovRestObjectInfo info = (NovRestObjectInfo) stck.peek();
			
				if( info.getObjType().equalsIgnoreCase("4")){
					stck.push( top );
					return ;
				}
				int nType = Integer.parseInt( info.getObjType());
				String objID = info.getObjID();
				
				novLisviewAdapter.getRestPager().reset();
				Integer rowStart  = 0;
				Integer pageSize = novLisviewAdapter.getRestPager().getPageSize();
				
				RestApiGetDeviceInfoRunnable rest = new RestApiGetDeviceInfoRunnable( 
						rootView.getContext(), nType,objID,rowStart,pageSize );
				rest.setRestToListview(restResultTools);
				Thread thdService = new Thread( rest );
				thdService.start();
				
			}
			
		});
		btProvious.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();
				NovRestObjectInfo info = (NovRestObjectInfo) stck.peek();
				int nType = Integer.parseInt( info.getObjType());
				String objID = info.getObjID();	
				
				Integer rowStart = novLisviewAdapter.getRestPager().proviousPageStartRow();
				Integer pageSize = novLisviewAdapter.getRestPager().getPageSize();
				RestApiGetDeviceInfoRunnable rest = new RestApiGetDeviceInfoRunnable( 
						rootView.getContext(), nType,objID,rowStart,pageSize );
				rest.setRestToListview(restResultTools);
				Thread thdService = new Thread( rest );
				thdService.start();
			}
			
		});
		
		btNext.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Stack<NovRestObjectInfo> stck = restResultTools.getStackObject();
				
				NovRestObjectInfo info = (NovRestObjectInfo) stck.peek();
			
				
				int nType = Integer.parseInt( info.getObjType());
				String objID = info.getObjID();
				
				
				Integer rowStart = novLisviewAdapter.getRestPager().nextPageStartRow();
				Integer pageSize = novLisviewAdapter.getRestPager().getPageSize();
				RestApiGetDeviceInfoRunnable rest = new RestApiGetDeviceInfoRunnable( 
						rootView.getContext(), nType,objID,rowStart,pageSize );
				rest.setRestToListview(restResultTools);
				Thread thdService = new Thread( rest );
				thdService.start();
				
			}
			
		});
		
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class NotifyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();			
			String strmsg = bundle.getString("msg");
			if( strmsg != null && strmsg.equalsIgnoreCase("refresh_rawdata")) {
				ListView listView = (ListView) rootView.findViewById(R.id.lv_device_left_data);	
				NovListviewAdapter adapterOld = (NovListviewAdapter) listView.getAdapter();
				adapterOld.notifyDataSetChanged();
				adapterOld.resetListener();
				return;
			}
			strmsg = bundle.getString( NovMessageName.MSG_DEVICEINFO_RESULT);
			if( strmsg != null && strmsg.equalsIgnoreCase( NovMessageName.MSG_RESULT_OK)) {
				ListView listView = (ListView) rootView.findViewById(R.id.lv_device_left_data);	
				NovListviewAdapter adapterOld = (NovListviewAdapter) listView.getAdapter();
				adapterOld.notifyDataSetChanged();
				adapterOld.resetListener();
				return;
			}

		}
	}
	

}
