package com.novery.alfa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novery.alfa.HelpActivity;
import com.novery.alfa.view.TitleView;
import com.novery.alfa.view.TitleView.OnLeftButtonClickListener;
import com.novery.alfa.view.TitleView.OnRightButtonClickListener;
import com.novery.icivilphone.R;

/**
 * @author yangyu
 *	������������ҳfragmentҳ��
 */
public class FragmentAlfaLocal extends Fragment {

	private View mParent;
	
	private FragmentActivity mActivity;
	
	private TitleView mTitle;
	
	private TextView mText;
	
	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static FragmentAlfaLocal newInstance(int index) {
		FragmentAlfaLocal f = new FragmentAlfaLocal();

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
		View view = inflater.inflate(R.layout.fragment_alfa_local, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mParent = getView();

		mTitle = (TitleView) mParent.findViewById(R.id.title);
		mTitle.setTitle(R.string.title_alfa_local_text);
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
