package com.novery.activity;


import com.novery.icivilphone.R;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityCheckSystem extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_check_system);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_check_system, menu);
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
		
		View mRootView ;
		private BluetoothAdapter mBluetoothAdapter;
		TextView etBlueVer;
		TextView etBlueName;
		ImageView btEnter; 
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_activity_check_system, container, false);
			
			mRootView = rootView ;
			checkBluetooth();
			return rootView;
		}
		public boolean checkBluetooth(){

			etBlueVer = (TextView)mRootView.findViewById(R.id.textFieldBlueVer);
			String version;
			version = getVersionName();
			etBlueVer.setText( version);
			etBlueName = (TextView)mRootView.findViewById(R.id.textFieldBlueName);
			etBlueName.setText("");
			btEnter = (ImageView) mRootView.findViewById( R.id.imgEnter);
			btEnter.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent=new Intent(PlaceholderFragment.this.getActivity(),ActivityStart.class );
					startActivity(intent);				
				};
			});
	        // Use this check to determine whether BLE is supported on the device.  Then you can
	        // selectively disable BLE-related features.
	        if (!PlaceholderFragment.this.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
	            //Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
	        	//finish();
	        	etBlueVer.setText(  R.string.ble_not_supported);
	        }
	        else
	        {
	        	etBlueVer.setText(  R.string.ble_supported);
	        }

	        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
	        // BluetoothAdapter through BluetoothManager.
	        final BluetoothManager bluetoothManager =
	                (BluetoothManager) PlaceholderFragment.this.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
	        mBluetoothAdapter = bluetoothManager.getAdapter();
	        // Checks if Bluetooth is supported on the device.
	        if (mBluetoothAdapter == null) {
	           // Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
	        	//finish();
	            return false ; 
	        }
	       	        
	        etBlueName.setText(mBluetoothAdapter.getName());
	        mBluetoothAdapter.enable();
	       
	        return true;
	        
		}
		public  String getVersionName() 
		   {
		           // 获取packagemanager的实例
		           PackageManager packageManager =  PlaceholderFragment.this.getActivity().getPackageManager();
		           // getPackageName()是你当前类的包名，0代表是获取版本信息
		           PackageInfo packInfo = null;
				try {
					packInfo = packageManager.getPackageInfo( PlaceholderFragment.this.getActivity().getPackageName(),0);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		           String version = packInfo.versionName;
		           return version;
		   }
	}

}
