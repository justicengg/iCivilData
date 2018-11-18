package com.novery.activity;

import java.util.ArrayList;
import java.util.List;

import com.novery.icivilphone.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class ActivityDebug extends FragmentActivity {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	
	private ViewPager viewPager;
	private ImageView imageView;
	private TextView textRawcode,textParameters,textAutoTest, textData, textStatitics;
	private List<Fragment> fragments;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private int selectedColor, unSelectedColor;
	private Intent mIntent ;
	
	private static final int pageSize = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabwidget);

		this.setTitle(R.string.title_debug);
		/*setContentView(R.layout.activity_activity_debug);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		 mIntent = getIntent();
		initView();
	}
	private void initView() {
		selectedColor = getResources()
				.getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(
				R.color.tab_title_normal_color);

		InitImageView();
		InitTextView();
		InitViewPager();
	}

	private void InitViewPager() {
		
		viewPager = (ViewPager) findViewById(R.id.vPager);
		fragments = new ArrayList<Fragment>();
		fragments.add(new FragmentDebugRawcode( ));
		fragments.add(new FragmentDegugParameters());
		fragments.add(new FragmentDebugAutoTest());
		fragments.add(new FragmentDebugData());
		fragments.add(new FragmentDebugStatitics());
		viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
				fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	private void InitTextView() {
		textRawcode = (TextView) findViewById(R.id.tab_1);
		textParameters = (TextView) findViewById(R.id.tab_2);
		textAutoTest = (TextView) findViewById(R.id.tab_3);
		textData = (TextView) findViewById(R.id.tab_4);
		textStatitics = (TextView) findViewById(R.id.tab_5);

		textRawcode.setTextColor(selectedColor);
		textParameters.setTextColor(unSelectedColor);
		textAutoTest.setTextColor(unSelectedColor);
		textData.setTextColor(unSelectedColor);
		textStatitics.setTextColor(unSelectedColor);

		textRawcode.setText("原码");
		textParameters.setText("参数");
		textAutoTest.setText("自检");
		textData.setText("数据");
		textStatitics.setText("统计");

		textRawcode.setOnClickListener(new MyOnClickListener(0));
		textParameters.setOnClickListener(new MyOnClickListener(1));
		textAutoTest.setOnClickListener(new MyOnClickListener(2));
		textData.setOnClickListener(new MyOnClickListener(3));
		textStatitics.setOnClickListener(new MyOnClickListener(4));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_debug, menu);
		return true;
	}
	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_selected_tab).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / pageSize - bmpW) / 2;
		
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		Context mContext;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_activity_debug,
					container, false);
			return rootView;
		}
	}
	
	
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			updatePageSelectedState();
			viewPager.setCurrentItem(index);
		}

		private void updatePageSelectedState() {
			textRawcode.setTextColor(unSelectedColor);
			textParameters.setTextColor(unSelectedColor);
			textAutoTest.setTextColor(unSelectedColor);
			textData.setTextColor(unSelectedColor);
			textStatitics.setTextColor(unSelectedColor);
			switch (index) {
			case 0:
				textRawcode.setTextColor(selectedColor);
				break;
			case 1:
				textParameters.setTextColor(selectedColor);
				break;
			case 2:
				textAutoTest.setTextColor(selectedColor);
				break;
			case 3:
				textData.setTextColor(selectedColor);
				break;
			case 4:
				textStatitics.setTextColor(selectedColor);
				break;
			}
		}

	}
	class myPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragmentList;
		public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
			super(fm);
			this.fragmentList = fragmentList;
		}

		/**
		 * 寰楀埌姣忎釜椤甸�?
		 */
		@Override
		public Fragment getItem(int arg0) {
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);
		}

		/**
		 * 姣忎釜椤甸潰鐨則itle
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		/**
		 * 椤甸潰鐨勬�涓�?
		 */
		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}
	}
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 椤靛�? -> 椤靛�? 鍋忕Щ閲�
		int two = one * 2;// 椤靛�? -> 椤靛�? 鍋忕Щ閲�

		public void onPageScrollStateChanged(int index) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int index) {
			Animation animation = new TranslateAnimation(one * currIndex, one
					* index, 0, 0);
			currIndex = index;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);

			updatePageSelectedState( index );
			/*switch (index) {
			case 0:
				textRawcode.setTextColor(selectedColor);
				textData.setTextColor(unSelectedColor);
				textStatitics.setTextColor(unSelectedColor);
				break;
			case 1:
				textData.setTextColor(selectedColor);
				textRawcode.setTextColor(unSelectedColor);
				textStatitics.setTextColor(unSelectedColor);
				break;
			case 2:
				textStatitics.setTextColor(selectedColor);
				textRawcode.setTextColor(unSelectedColor);
				textData.setTextColor(unSelectedColor);
				break;
			}*/
		}
		private void updatePageSelectedState( int index) {
			textRawcode.setTextColor(unSelectedColor);
			textParameters.setTextColor(unSelectedColor);
			textAutoTest.setTextColor(unSelectedColor);
			textData.setTextColor(unSelectedColor);
			textStatitics.setTextColor(unSelectedColor);
			switch (index) {
			case 0:
				textRawcode.setTextColor(selectedColor);
				break;
			case 1:
				textParameters.setTextColor(selectedColor);
				break;
			case 2:
				textAutoTest.setTextColor(selectedColor);
				break;
			case 3:
				textData.setTextColor(selectedColor);
				break;
			case 4:
				textStatitics.setTextColor(selectedColor);
				break;
			}
		}
	}

	 /**监听对话框里面的button点击事件*/  
   DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
   {  
       public void onClick(DialogInterface dialog, int which)  
       {  
           switch (which)  
           {  
           case AlertDialog.BUTTON_POSITIVE:// "确认"按钮�?��程序  
                finish();
               break;  
           case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
               break;  
           default:  
               break;  
           }  
       }  
   };    
   
	@Override  
   public boolean onKeyDown(int keyCode, KeyEvent event)  
   {  
       if (keyCode == KeyEvent.KEYCODE_BACK )  
       {  
           // 创建�?��对话�? 
           AlertDialog isExit = new AlertDialog.Builder( this).create();  
           // 设置对话框标�? 
           isExit.setTitle("系统提示");  
           // 设置对话框消�? 
           isExit.setMessage("确定要�?出吗");  
           // 添加选择按钮并注册监�? 
           isExit.setButton("确定", listener);  
           isExit.setButton2("取消", listener);  
           // 显示对话�? 
           isExit.show();  
 
       }  
         
       return false;  
         
   }  
}
