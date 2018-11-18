package com.novery.activity;

import java.util.ArrayList;
import java.util.List;

import com.novery.icivilphone.R;

import android.app.Activity;
import android.app.ActionBar;
import android.content.Context;
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

public class ActivityStart extends FragmentActivity {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	
	private ViewPager viewPager;
	private ImageView imageView;
	private TextView textRawcode, textData, textStatitics;
	private List<Fragment> fragments;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private int selectedColor, unSelectedColor;
	
	private static final int pageSize = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabwidget_start);
		this.setTitle(R.string.title_activity_start);
		/*setContentView(R.layout.activity_activity_debug);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
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
		viewPager = (ViewPager) findViewById(R.id.vPagerStart);
		fragments = new ArrayList<Fragment>();
		fragments.add(new FragmentBlueDevices());
		fragments.add(new FragmentDebugData());
		fragments.add(new FragmentDataTrace());
		viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
				fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	private void InitTextView() {
		textRawcode = (TextView) findViewById(R.id.tab_first);
		textData = (TextView) findViewById(R.id.tab_second);
		textStatitics = (TextView) findViewById(R.id.tab_third);

		textRawcode.setTextColor(selectedColor);
		textData.setTextColor(unSelectedColor);
		textStatitics.setTextColor(unSelectedColor);

		textRawcode.setText("设备");
		textData.setText("配置");
		textStatitics.setText("跟踪");

		textRawcode.setOnClickListener(new MyOnClickListener(0));
		textData.setOnClickListener(new MyOnClickListener(1));
		textStatitics.setOnClickListener(new MyOnClickListener(2));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_activity_start, menu);
		return true;
	}
	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.tab_selected_bg).getWidth();
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
		if (id == R.id.action_start_configure) {
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
			View rootView = inflater.inflate(R.layout.fragment_activity_start,
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

			switch (index) {
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
			}
			viewPager.setCurrentItem(index);
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
					* index, 0, 0);// 鏄剧劧杩欎釜姣旇緝绠�磥锛屽彧鏈変竴琛屼唬鐮併�
			currIndex = index;
			animation.setFillAfter(true);// True:鍥剧墖鍋滃湪鍔ㄧ敾缁撴潫浣嶇�?
			animation.setDuration(300);
			imageView.startAnimation(animation);

			switch (index) {
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
			}
		}
	}
}
