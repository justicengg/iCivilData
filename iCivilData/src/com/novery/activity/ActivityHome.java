package com.novery.activity;

import java.util.ArrayList;

import com.novery.base.App;
import com.novery.icivilphone.R;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ActivityHome extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_home);


		this.setTitle(R.string.title_search);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
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
	public static class PlaceholderFragment extends Fragment  
	implements IXListViewListener,AdapterView.OnItemClickListener, View.OnClickListener    {

		Context mContext;
		private XListView mListView;
		private ArrayAdapter<String> mAdapter;
		private ArrayList<String> items = new ArrayList<String>();
		private Handler mHandler;
		private int start = 0;
		private static int refreshCnt = 0;
		 private static final int REQUEST_ENABLE_BT = 1;
		    // Stops scanning after 10 seconds.
		    private static final long SCAN_PERIOD = 10000;
		
		//private LeDeviceListAdapter mLeDeviceListAdapter;
		 private BluetoothAdapter mBluetoothAdapter;
		 private ArrayList<BluetoothDevice> mLeDevices;
		 private boolean mScanning;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			mContext = container.getContext();
			View rootView = inflater.inflate(R.layout.fragment_activity_home,
					container, false);
			
			initList( rootView );

			checkBluetooth();
			return rootView;
		}
		private void initList( View rootView ){
			geneItems();
			mListView = (XListView) rootView.findViewById(R.id.xListView);
			mListView.setPullLoadEnable(true);
			mAdapter = new ArrayAdapter<String>( mContext , R.layout.list_item, items);
			mListView.setAdapter(mAdapter);
//			mListView.setPullLoadEnable(false);
//			mListView.setPullRefreshEnable(false);
			mListView.setXListViewListener(PlaceholderFragment.this);
			mListView.setOnItemClickListener(PlaceholderFragment.this);
			mHandler = new Handler();
		}

		private void geneItems() {
			//items.add("没有找到设备");
			
			/*for (int i = 0; i != 20; ++i) {
				items.add("refresh cnt " + (++start));
			}*/
		}

		private void onLoad() {
			mListView.stopRefresh();
			mListView.stopLoadMore();
			mListView.setRefreshTime(App.getSQLDatetime());
		}
		
		@Override
		public void onRefresh() {
			// mLeDeviceListAdapter.clear();//
			RefreshDevice();
		}

		private void RefreshDevice() {
			mLeDevices.clear();
            scanLeDevice(true);
            for( int i=0;i<5;i++){
	            mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
							items.clear();
							for( BluetoothDevice dev : mLeDevices){
								String itemName = dev.getName() +":"+dev.getAddress() ;
								items.add( itemName );
							}
							mAdapter = new ArrayAdapter<String>( mContext, R.layout.list_item, items);
							mListView.setAdapter(mAdapter);
					}
				},2000*i );
	        }
            mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
						items.clear();
						for( BluetoothDevice dev : mLeDevices){
							String itemName = dev.getName() +":"+dev.getAddress() ;
							items.add( itemName );
						}
						mAdapter = new ArrayAdapter<String>( mContext, R.layout.list_item, items);
						mListView.setAdapter(mAdapter);						
						onLoad();
				}
			},2000*6 );
		}
		
		@Override
		public void onLoadMore() {
			RefreshDevice();
			/*
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					//geneItems();
					mAdapter.notifyDataSetChanged();
					onLoad();
				}
			}, 2000);
			*/
		}


		public boolean checkBluetooth(){

	        // Use this check to determine whether BLE is supported on the device.  Then you can
	        // selectively disable BLE-related features.
	        if (!this.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
	            //Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
	        	this.getActivity(). finish();
	        }

	        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
	        // BluetoothAdapter through BluetoothManager.
	        final BluetoothManager bluetoothManager =
	                (BluetoothManager) this.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
	        mBluetoothAdapter = bluetoothManager.getAdapter();
	        
	        // Checks if Bluetooth is supported on the device.
	        if (mBluetoothAdapter == null) {
	           // Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
	        	this.getActivity().finish();
	            return false ; 
	        }
	        
	        //Initializes list view adapter.
	        //mLeDeviceListAdapter = new LeDeviceListAdapter(this.getActivity().getLayoutInflater());
	        mLeDevices = new   ArrayList<BluetoothDevice>();
	        //setListAdapter(mLeDeviceListAdapter);
	        return true;
	        
		}

	    // Device scan callback.
	    private BluetoothAdapter.LeScanCallback mLeScanCallback =
	            new BluetoothAdapter.LeScanCallback() {

	        @Override
	        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
	        	PlaceholderFragment.this.getActivity().runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                	//mLeDeviceListAdapter.addDevice(device);
	                	 if(!mLeDevices.contains(device)) {
	                         mLeDevices.add(device);
	                     }	                	 
	            		mHandler.sendEmptyMessage(1);
	                }
	            });
	        }
	    };

		 private void scanLeDevice(final boolean enable) {
		        if (enable) {
		            // Stops scanning after a pre-defined scan period.
		            mHandler.postDelayed(new Runnable() {
		                @Override
		                public void run() {
		                	if(mScanning)
		                	{
		                		mScanning = false;
		                		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		                		PlaceholderFragment.this.getActivity().invalidateOptionsMenu();
		                	}
		                }
		            }, SCAN_PERIOD);

		            mScanning = true;
		            //F000E0FF-0451-4000-B000-000000000000
		           // mLeDeviceListAdapter.clear();
		            mLeDevices.clear();
		            mHandler.sendEmptyMessage(1);
		            mBluetoothAdapter.startLeScan(mLeScanCallback);
		        } else {
		            mScanning = false;
		            mBluetoothAdapter.stopLeScan(mLeScanCallback);
		        }
		        PlaceholderFragment.this.getActivity().invalidateOptionsMenu();
		 }

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if( mLeDevices.size() > 0 )
				return;
		}

		@Override
		public void onItemClick( AdapterView<?> arg0, View v, int position, long id) {
			String strDevice  = (String) mListView.getItemAtPosition(position);
			System.out.println( strDevice );
			int idx = position - 1;
			BluetoothDevice device = mLeDevices.get( idx );
	        if (device == null) return;
	       // final Intent intent = new Intent(PlaceholderFragment.this.getActivity(),ActivityHome.class);
	        
	        Intent intent = new Intent(mContext, ActivityDebug.class);
	        intent.putExtra(ActivityDebug.EXTRAS_DEVICE_NAME, device.getName());
	        intent.putExtra(ActivityDebug.EXTRAS_DEVICE_ADDRESS, device.getAddress());
	        if (mScanning) {
	            mBluetoothAdapter.stopLeScan(mLeScanCallback);
	            mScanning = false;
	        }
			startActivity(intent);
	      
	       
	        
	       // startActivity(intent);
			
		}
		 
		
		 
	}//end of PlaceholderFragment
	
}


