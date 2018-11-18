package com.novery.alfa.fragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.novery.alfa.ActivityTorch;
import com.novery.alfa.HelpActivity;
import com.novery.alfa.view.TitleView;
import com.novery.icivilphone.R;
import com.novery.rest.NovMessageName;
import com.novery.rest.RestApiLoginRunnable;
import com.novery.rest.RestApiWarnPointRunnable;
import com.novery.stack.NovListviewAdapter;
import com.novery.stack.NovUserInfo;

/**
 * @author yangyu
 *	功能描述：首页fragment页面
 */
public class FragmentAlfaHome extends Fragment {

	private View mParent;
	
	private FragmentActivity mActivity;
	private Context  context ;
	private TitleView mTitle;
	
	//private TextView mText;
	private View currentView;

	private NotifyReceiver receiverNotify;

	private Object intentNotify; 
	
	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static FragmentAlfaHome newInstance(int index) {
		FragmentAlfaHome f = new FragmentAlfaHome();

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
		View view = inflater.inflate(R.layout.fragment_alfa_home, container, false);

		currentView = view;
		context = getActivity();
		
		receiverNotify = new NotifyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction( NovMessageName.MSG_ACTION_WARN_INFO);
		intentNotify = context.registerReceiver(receiverNotify, filter);
		
		
		setButtonOnClick();
		bindSystemToosOnClick();
		return view;
	}
	void setButtonOnClick(){
		
		int buttonID = R.id.imageButton_home_remote ;
		//
		//int viewID = 10000 + R.drawable.alfa_remote_normal; 
		//参见FragmentIndicator。FragmentIndicator中的view.setId(10000+iconResID);
		int viewID = 10000 + R.drawable.alfa_remote_normal;		
		bindButtonOnClickListener(buttonID, viewID);
		
		buttonID = R.id.imageButton_home_device ;
		viewID = 10000 + R.drawable.alfa_local_normal;		
		bindButtonOnClickListener(buttonID, viewID);		

		/*buttonID = R.id.imageButton_home_debug ;
		viewID = 10000 + R.drawable.alfa_tools_normal;		
		bindButtonOnClickListener(buttonID, viewID);*/
		
		buttonID = R.id.imageButton_home_myinfo ;
		viewID = 10000 + R.drawable.alfa_myhome_normal;		
		bindButtonOnClickListener(buttonID, viewID);
		

		buttonID = R.id.imageButton_home_warn ;
		viewID = 10000 + R.drawable.alfa_home_ic_warn;		
		bindButtonOnClickListener(buttonID, viewID);
		
	}

	void bindSystemToosOnClick(){
		ImageView btn = (ImageView) currentView.findViewById(R.id.imageView_comp);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startCalculator();
				/*
				Intent mIntent = new Intent();
				mIntent.setClassName("com.android.calculator2",
						"com.android.calculator2.Calculator");
				try{
				startActivity(mIntent);
				}
				catch( Exception e){
					System.out.println( e.getMessage());
				}
				*/
			}
			  
			public  PackageInfo searchForCalculator(Context context,String appNameKey) {  
			    PackageManager pManager = context.getPackageManager();  
			    // 获取手机内所有应用  
			    List<PackageInfo> packlist = pManager.getInstalledPackages(0);  
			    for (int i = 0; i < packlist.size(); i++) {  
			        PackageInfo pak = (PackageInfo) packlist.get(i);  
			         if(pak.packageName.toString().toLowerCase().contains(appNameKey)){  
			             return pak;  
			         }
			    }  
			    return null;  
			}  

			public void startCalculator(){  
			    PackageInfo pak = this.searchForCalculator(mActivity, "calculator"); //大小写  
			    if(pak != null){  
			       Intent intent = new Intent();    
			          intent = mActivity.getPackageManager().getLaunchIntentForPackage(pak.packageName);    
			          startActivity(intent);  
			          }else{  
			           Toast.makeText(mActivity, "未找到计算器", Toast.LENGTH_SHORT).show();  
			          }    
			}  
			
		});
		
		btn = (ImageView) currentView.findViewById(R.id.imageView_torch);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent mIntent = new Intent( mActivity,ActivityTorch.class);
				startActivity(mIntent);
			}

		});

		btn = (ImageView) currentView.findViewById(R.id.imageView_camera);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(); //调用照相机
				intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
				startActivity(intent);
			}

		});
		
		btn = (ImageView) currentView.findViewById(R.id.imageView_gps);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent mIntent = new Intent( mActivity,com.novery.gps.ActivityGPS.class);
				startActivity(mIntent);
			}

		});
	
		
	}
	private void bindButtonOnClickListener(int buttonID, final int viewID) {
		Button bt = (Button) currentView.findViewById( buttonID) ;	
		if( bt != null ){
			bt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					View img = (View)mActivity.findViewById( viewID );
					if( null != img){
						img.callOnClick();
					}
					else {
						RestApiWarnPointRunnable runRest = new RestApiWarnPointRunnable( mActivity,NovUserInfo.getInstance().getLogin().getClientID() );
						Thread thdService = new Thread( runRest );
						thdService.start();
					}
					
				}
			
			});
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mParent = getView();

		mTitle = (TitleView) mParent.findViewById(R.id.title);
		mTitle.setTitle(R.string.title_alfa_home_text);
		/*	mTitle.setLeftButton(R.string.exit, new OnLeftButtonClickListener(){

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
		
		//mText = (TextView) mParent.findViewById(R.id.fragment_home_text);

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
			String strmsg = bundle.getString( NovMessageName.MSG_WARN_INFO);
			if( strmsg != null ) {
				Toast.makeText( context, strmsg, Toast.LENGTH_SHORT).show();  
				return;
			}

		}
	}

}
