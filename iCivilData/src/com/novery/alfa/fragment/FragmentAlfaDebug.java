package com.novery.alfa.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.novery.alfa.HelpActivity;
import com.novery.alfa.view.TitleView;
import com.novery.icivilphone.R;

/**
 * @author yangyu
 *	功能描述：首页fragment页面
 */
public class FragmentAlfaDebug extends Fragment {

	private View mParent;
	
	private FragmentActivity mActivity;
	
	private TitleView mTitle;
	
	//private TextView mText;
	
	
	private ViewPager viewPager;
	private ImageView imageView;
	private TextView textTabLeft,textTabRight;
	private List<Fragment> fragments;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private int selectedColor, unSelectedColor;
	private Intent mIntent ;
	
	private static final int pageSize = 2;
	
	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static FragmentAlfaDebug newInstance(int index) {
		FragmentAlfaDebug f = new FragmentAlfaDebug();

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
		View view = inflater.inflate(R.layout.fragment_alfa_debug, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mParent = getView();

		mTitle = (TitleView) mParent.findViewById(R.id.title);
		mTitle.setTitle(R.string.title_alfa_debug_text);
				
		InitImageView();
		InitTextView();
		InitViewPager();

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
	
private void InitViewPager() {
		
		viewPager = (ViewPager) this.getActivity().findViewById(R.id.vPager_alfa_debug);
		fragments = new ArrayList<Fragment>();
		fragments.add(new FragmentAlfaDebugSearch());
		fragments.add(new FragmentAlfaDebugCommand());
		viewPager.setAdapter(new myPagerAdapter(this.getActivity().getSupportFragmentManager(),
				fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	private void InitTextView() {
		selectedColor = getResources()
				.getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(
				R.color.tab_title_normal_color);
		textTabLeft = (TextView) this.getActivity().findViewById(R.id.tab_alfa_debug_search);
		textTabRight = (TextView) this.getActivity().findViewById(R.id.tab_alfa_debug_command);


		textTabLeft.setText("搜索");
		textTabRight.setText("指令");
		
		textTabLeft.setOnClickListener(new MyOnClickListener(0));
		textTabRight.setOnClickListener(new MyOnClickListener(1));
	}
	private void InitImageView() {
		imageView = (ImageView) this.getActivity().findViewById(R.id.cursor_debug);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_selected_tab).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / pageSize - bmpW) / 2;
		
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);
	}
	
	class myPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragmentList;
		public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
			super(fm);
			this.fragmentList = fragmentList;
		}

		@Override
		public Fragment getItem(int arg0) {
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}
	}
	public class MyOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + bmpW;
		int two = one * 2;
	
		public void onPageScrollStateChanged(int index) {
			
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int index) {
			Animation animation = new TranslateAnimation(one * currIndex, one * index, 0, 0);
			currIndex = index;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);

			updatePageSelectedState( index );
		}
		private void updatePageSelectedState( int index) {
			textTabLeft.setTextColor(unSelectedColor);
			textTabRight.setTextColor(unSelectedColor);
			switch (index) {
			case 0:
				textTabLeft.setTextColor(selectedColor);
				break;
			case 1:
				textTabRight.setTextColor(selectedColor);
				break;
			}
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
			textTabLeft.setTextColor(unSelectedColor);
			textTabRight.setTextColor(unSelectedColor);
			switch (index) {
			case 0:
				textTabLeft.setTextColor(selectedColor);
				break;
			case 1:
				textTabRight.setTextColor(selectedColor);
				break;
			case 2:
			}
		}

	}

}
