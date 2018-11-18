package com.novery.alfa.fragment;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.novery.activity.ActivityDebug;
import com.novery.activity.FragmentBlueDevices;
import com.novery.alfa.HelpActivity;
import com.novery.alfa.view.TitleView;
import com.novery.alfa.view.TitleView.OnLeftButtonClickListener;
import com.novery.alfa.view.TitleView.OnRightButtonClickListener;
import com.novery.base.App;
import com.novery.icivilphone.R;

/**
 * @author yangyu 功能描述：首页fragment页面
 */
public class FragmentAlfaDebugCommand extends Fragment implements IXListViewListener,
		AdapterView.OnItemClickListener, View.OnClickListener {

	private View mParent;
	private FragmentActivity mActivity;
	private TitleView mTitle;
	private TextView mText;

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

	// private LeDeviceListAdapter mLeDeviceListAdapter;
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayList<BluetoothDevice> mLeDevices;
	private boolean mScanning;

	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static FragmentAlfaDebugCommand newInstance(int index) {
		FragmentAlfaDebugCommand f = new FragmentAlfaDebugCommand();

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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mContext = getActivity();
		mParent = getView();

	//	mTitle = (TitleView) mParent.findViewById(R.id.title);
	//	mTitle.setTitle(R.string.title_alfa_debug_command_text);

	//	mText = (TextView) mParent.findViewById(R.id.fragment_command_text);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_alfa_debug_command, container,
				false);
		mContext = getActivity().getApplicationContext();

		return view;
	}

	private void initList(View rootView) {
		geneItems();
		mListView = (XListView) rootView.findViewById(R.id.xListView_tools);
		mListView.setPullLoadEnable(true);
		mAdapter = new ArrayAdapter<String>(mContext, R.layout.list_item, items);
		mListView.setAdapter(mAdapter);
		// mListView.setPullLoadEnable(false);
		// mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(FragmentAlfaDebugCommand.this);
		mListView.setOnItemClickListener(FragmentAlfaDebugCommand.this);
		mHandler = new Handler();
	}

	private void geneItems() {
		// items.add("没有找到设备");

		/*
		 * for (int i = 0; i != 20; ++i) { items.add("refresh cnt " +
		 * (++start)); }
		 */
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
		clearBluetoothAndListView();
		scanLeDevice(true);
		for (int i = 0; i < 5; i++) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					items.clear();
					for (BluetoothDevice dev : mLeDevices) {
						String itemName = dev.getName() + ":"
								+ dev.getAddress();
						items.add(itemName);
					}
					mAdapter = new ArrayAdapter<String>(mContext,
							R.layout.list_item, items);
					mListView.setAdapter(mAdapter);
				}
			}, 2000 * i);
		}
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				items.clear();
				for (BluetoothDevice dev : mLeDevices) {
					String itemName = dev.getAddress() + ":" + dev.getName();
					items.add(itemName);
				}
				mAdapter = new ArrayAdapter<String>(mContext,
						R.layout.list_item, items);
				mListView.setAdapter(mAdapter);
				onLoad();
			}
		}, 2000 * 6);
	}

	private void clearBluetoothAndListView() {
		mLeDevices.clear();
		items.clear();
		mAdapter = new ArrayAdapter<String>(mContext, R.layout.list_item, items);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onLoadMore() {
		RefreshDevice();
		/*
		 * mHandler.postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { //geneItems();
		 * mAdapter.notifyDataSetChanged(); onLoad(); } }, 2000);
		 */
	}

	public boolean checkBluetooth() {

		// Use this check to determine whether BLE is supported on the device.
		// Then you can
		// selectively disable BLE-related features.
		if (!this.getActivity().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			// Toast.makeText(this, R.string.ble_not_supported,
			// Toast.LENGTH_SHORT).show();
			this.getActivity().finish();
		}

		// Initializes a Bluetooth adapter. For API level 18 and above, get a
		// reference to
		// BluetoothAdapter through BluetoothManager.
		final BluetoothManager bluetoothManager = (BluetoothManager) this
				.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// Checks if Bluetooth is supported on the device.
		if (mBluetoothAdapter == null) {
			// Toast.makeText(this, R.string.error_bluetooth_not_supported,
			// Toast.LENGTH_SHORT).show();
			this.getActivity().finish();
			return false;
		}

		// Initializes list view adapter.
		// mLeDeviceListAdapter = new
		// LeDeviceListAdapter(this.getActivity().getLayoutInflater());
		mLeDevices = new ArrayList<BluetoothDevice>();
		// setListAdapter(mLeDeviceListAdapter);
		return true;

	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				final byte[] scanRecord) {
			FragmentAlfaDebugCommand.this.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (!mLeDevices.contains(device)) {
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
					if (mScanning) {
						mScanning = false;
						mBluetoothAdapter.stopLeScan(mLeScanCallback);
						FragmentAlfaDebugCommand.this.getActivity().invalidateOptionsMenu();
					}
				}
			}, SCAN_PERIOD);

			mScanning = true;
			mHandler.sendEmptyMessage(1);
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		FragmentAlfaDebugCommand.this.getActivity().invalidateOptionsMenu();
	}

	@Override
	public void onClick(View arg0) {
		if (mLeDevices.size() > 0)
			return;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		String strDevice = (String) mListView.getItemAtPosition(position);
		System.out.println(strDevice);
		int idx = position - 1;
		if (mLeDevices.size() <= idx) {
			return;
		}
		BluetoothDevice device = mLeDevices.get(idx);
		if (device == null)
			return;
		// final Intent intent = new
		// Intent(PlaceholderFragment.this.getActivity(),ActivityHome.class);

		Intent intent = new Intent(mContext, ActivityDebug.class);
		intent.putExtra(ActivityDebug.EXTRAS_DEVICE_NAME, device.getName());
		intent.putExtra(ActivityDebug.EXTRAS_DEVICE_ADDRESS,
				device.getAddress());
		if (mScanning) {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			mScanning = false;
		}
		startActivity(intent);

		// startActivity(intent);

	}

}