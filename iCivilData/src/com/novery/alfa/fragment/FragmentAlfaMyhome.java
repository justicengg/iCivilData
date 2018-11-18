package com.novery.alfa.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novery.alfa.HelpActivity;
import com.novery.alfa.view.TitleView;
import com.novery.alfa.view.TitleView.OnLeftButtonClickListener;
import com.novery.alfa.view.TitleView.OnRightButtonClickListener;
import com.novery.icivilphone.R;
import com.novery.stack.NovUserInfo;

/**
 * @author yangyu
 *	功能描述：首页fragment页面
 */
public class FragmentAlfaMyhome extends Fragment {

	private View mParent;
	
	private FragmentActivity mActivity;
	
	private TitleView mTitle;
	
	private TextView mText;
	
	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static FragmentAlfaMyhome newInstance(int index) {
		FragmentAlfaMyhome f = new FragmentAlfaMyhome();

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
		View view = inflater.inflate(R.layout.fragment_alfa_myhome, container, false);
		
		TextView txObjectName = (TextView) view.findViewById(R.id.textUserName);	
		txObjectName.setText( "用户名："+NovUserInfo.getInstance().getLogin().getUserName());
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mParent = getView();

		mTitle = (TitleView) mParent.findViewById(R.id.title);
		mTitle.setTitle(R.string.title_alfa_myhome_text);
		
		
		
		
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

}
