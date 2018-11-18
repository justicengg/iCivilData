package com.novery.activity;


import com.novery.icivilphone.R;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FragmentLogin extends Fragment {

	public FragmentLogin() {
		// TODO Auto-generated constructor stub
	}
	Button login;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		findViews( rootView );
		return rootView;
	}
	private void findViews(View rootView) {
		if( rootView == null )
			return;
		/*
		login=(Button)rootView.findViewById(R.id.button_login);
		if( login == null )
			return ;
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent( FragmentLogin.this.getActivity(),ActivityHome.class );
				startActivity(intent);				
			}
			
		});*/
	}

}
