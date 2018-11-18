package com.novery.alfa.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novery.alfa.HelpActivity;
import com.novery.icivilphone.R;
import com.novery.rest.NovMessageName;
import com.novery.stack.NovUserInfo;

/**
 * @author yangyu
 *	功能描述：首页fragment页面
 */
public class FragmentAlfaDeviceRight extends Fragment {

	private View mParent;

	private View rootView;
	private FragmentActivity mActivity;
	
	//private TitleView mTitle;

	private NotifyReceiver receiverNotify = null;
	private TextView mText;

	private FragmentActivity mContext;

	private Intent intentNotify;
	
	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static FragmentAlfaDeviceRight newInstance(int index) {
		FragmentAlfaDeviceRight f = new FragmentAlfaDeviceRight();

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
		View view = inflater.inflate(R.layout.fragment_alfa_device_right, container, false);
		rootView = view;
		mContext = getActivity(); // container.getContext();
		
		receiverNotify = new NotifyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction( NovMessageName.MSG_ACTION_DEVICE_INFO);
		intentNotify = mContext.registerReceiver(receiverNotify, filter);

		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mParent = getView();

		//mTitle = (TitleView) mParent.findViewById(R.id.title);
		//mTitle.setTitle(R.string.title_alfa_remote_text);
		/*mTitle.setLeftButton(R.string.exit, new OnLeftButtonClickListener(){

			@Override
			public void onClick(View button) {
				mActivity.finish();
			}
			
		});
		mTitle.setRightButton(R.string.help, new OnRightButtonClickListener() {

			@Override
			public void onClick(View button) {
				goHelpActivity();
			}
		});*/
		
		mText = (TextView) mParent.findViewById(R.id.fragment_home_text);

	}
	
	private void goHelpActivity() {
		Intent intent = new Intent(mActivity, HelpActivity.class);
		startActivity(intent);
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
			
			String strmsg = bundle.getString( NovMessageName.MSG_DEVICEINFO_RESULT);
			if( strmsg != null && strmsg.equalsIgnoreCase( NovMessageName.MSG_RESULT_OK)) {
				//TextView txObjectName = (TextView) rootView.findViewById(R.id.textDeviceName);	
				//String strObjName = bundle.getString("MSG_OBJECTINFO");
				//txObjectName.setText( strObjName );
				//return;
			}
			strmsg = bundle.getString( NovMessageName.MSG_DEVICEINFO);
			if( strmsg != null ) {
				TextView txObjectName = (TextView) rootView.findViewById(R.id.textDeviceName);					
				txObjectName.setText( strmsg );
				return;
			}
		}
	}

}
