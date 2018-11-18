package com.novery.alfa;

import com.novery.alfa.fragment.FragmentIndicator;
import com.novery.alfa.fragment.FragmentIndicator.OnIndicateListener;
import com.novery.base.AppConf;
import com.novery.icivilphone.R;

//import android.app.Fragment;





import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ActivityAlfa extends FragmentActivity {
	public static Fragment[] mFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alfa);

		setFragmentIndicator(0);
		
		
		resizeImages();
		resizeButtons();
		
		AppConf.init();
		AppConf.load();
	}

	private void resizeButtons(){
		Resources resources = this.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		//float density1 = dm.density;
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		
		resizePannelButton(screenWidth, R.id.imageButton_home_remote);	
		resizePannelButton(screenWidth, R.id.imageButton_home_device);
		//resizePannelButton(screenWidth, R.id.imageButton_home_debug);		
		resizePannelButton(screenWidth, R.id.imageButton_home_myinfo);	
		resizePannelVerticalLine(screenWidth,R.id.imageView_vertical_line_00);	
		resizePannelVerticalLine(screenWidth,R.id.imageView_vertical_line_01);	
	//	resizePannelVerticalLine(screenWidth,R.id.imageView_vertical_line_02);	
		
		
		
	}

	private void resizePannelButton(int screenWidth, int imgbtID) {
		Button imgButtonDevice = (Button)this.findViewById(imgbtID);
		LayoutParams param = imgButtonDevice.getLayoutParams( );
		param.width = (screenWidth - 6 )/4;
		//param.height = 80;		
		imgButtonDevice.setLayoutParams(param);
	}
	
	private void resizePannelVerticalLine(int screenWidth, int imgbtID) {
		ImageView imgview = (ImageView)this.findViewById(imgbtID);
		LayoutParams param = imgview.getLayoutParams( );
		param.width =  2 ;
		param.height = 80;
		imgview.setLayoutParams(param);
	}
	
	private void resizeImages() {
		Resources resources = this.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		//float density1 = dm.density;
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		
		LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.LinearLayout_home_tools);
		
		 LayoutParams paramLinear = mLinearLayout.getLayoutParams();
		 paramLinear.height = (int) (screenHeight * 0.1) ;
		 mLinearLayout.setLayoutParams(  paramLinear );  
		
        ImageView imgGps = (ImageView)this.findViewById(R.id.imageView_gps);
        ViewGroup.LayoutParams para = imgGps.getLayoutParams();
        para.width = (int) (screenWidth *( 0.21));
        para.height = paramLinear.height;
        imgGps.setLayoutParams(para);
        

        ImageView imgTorch = (ImageView)this.findViewById(R.id.imageView_torch);
        ViewGroup.LayoutParams paraTorch = imgTorch.getLayoutParams();
        paraTorch.width = (int) (screenWidth *( 0.21));
        paraTorch.height =  paramLinear.height;
        imgTorch.setLayoutParams(para);
        

        ImageView imgInterval_01 = (ImageView)this.findViewById(R.id.imageView_interval_01);
        ViewGroup.LayoutParams paraInterval01 = imgInterval_01.getLayoutParams();
        paraInterval01.width = (int) (screenWidth *( 0.06));
        paraInterval01.height = paramLinear.height;
        imgInterval_01.setLayoutParams(paraInterval01);
        

        ImageView imgComputer = (ImageView)this.findViewById(R.id.imageView_comp);
        ViewGroup.LayoutParams paraComputer = imgComputer.getLayoutParams();
        paraComputer.width = (int) (screenWidth *( 0.21));
        paraComputer.height = paramLinear.height;
        imgComputer.setLayoutParams(paraComputer);
        
        ImageView imgInterval_02 = (ImageView)this.findViewById(R.id.imageView_interval_02);
        ViewGroup.LayoutParams paraInterval02 = imgInterval_02.getLayoutParams();
        paraInterval02.width = (int) (screenWidth *( 0.06));
        paraInterval02.height = paramLinear.height;
        imgInterval_02.setLayoutParams(paraInterval02);
        
        ImageView imgCamera = (ImageView)this.findViewById(R.id.imageView_camera);
        ViewGroup.LayoutParams paraCamera = imgCamera.getLayoutParams();
        paraCamera.width = (int) (screenWidth *( 0.25));
        paraCamera.height = paramLinear.height;
        imgCamera.setLayoutParams(paraCamera);
	}

	/**
	 * 初始化fragment
	 */
	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new Fragment[4];
		mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_alfa_home);
		mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_alfa_device);
		mFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_alfa_remote);
		//mFragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_alfa_debug);
		mFragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_alfa_myhome);
		getSupportFragmentManager().beginTransaction()
				.hide(mFragments[0])
				.hide(mFragments[1])
				.hide(mFragments[2])
				//.hide(mFragments[3])
				.hide(mFragments[3])
				.show(mFragments[whichIsDefault])
				.commit();

		FragmentIndicator mIndicator = (FragmentIndicator) findViewById(R.id.indicator);
		FragmentIndicator.setIndicator(whichIsDefault);
		mIndicator.setOnIndicateListener(new OnIndicateListener() {
			@Override
			public void onIndicate(View v, int which) {
				getSupportFragmentManager().beginTransaction()
						.hide(mFragments[0])
						.hide(mFragments[1])
						.hide(mFragments[2])
						//.hide(mFragments[3])
						.hide(mFragments[3])
						.show(mFragments[which])
						.commit();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {			
			new AlertDialog.Builder( this )
			.setTitle("退出确认")
			.setMessage("确定退出吗？")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {					
					finish();
					onDestroy();  
					System.exit(0);
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			})
			.show();
		}
		return false;
	}
}
