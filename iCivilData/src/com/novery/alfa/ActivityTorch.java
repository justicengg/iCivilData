package com.novery.alfa;

import com.novery.icivilphone.R;
import com.novery.icivilphone.R.drawable;
import com.novery.icivilphone.R.id;
import com.novery.icivilphone.R.layout;
import com.novery.icivilphone.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ActivityTorch extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_torch);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_torch, menu);
		return true;
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
		private boolean isopent = false;
	    private Camera camera;
	    View rootView ;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_alfa_torch,
					container, false);
			

			TextView img_but = (TextView) rootView.findViewById(R.id.main_img);
			 
	        img_but.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	                if (!isopent) {
	                    Toast.makeText(rootView.getContext().getApplicationContext(), "您已经打开了手电筒", 0)
	                            .show();
	                    
	                    camera = Camera.open();
	                    Parameters params = camera.getParameters();
	                    params.setFlashMode(Parameters.FLASH_MODE_TORCH);
	                    camera.setParameters(params);
	                    camera.startPreview(); // 开始亮灯
	 
	                    isopent = true;
	                    v.setBackgroundResource(R.drawable.torch_open);
	                } else {
	                    Toast.makeText(rootView.getContext().getApplicationContext(), "关闭了手电筒",
	                            Toast.LENGTH_SHORT).show();
	                    camera.stopPreview(); // 关掉亮灯
	                    camera.release(); // 关掉照相机
	                    isopent = false;
	                    v.setBackgroundResource(R.drawable.torch_close);
	                }
	            }
	        });
			return rootView;
		}
	}

}
