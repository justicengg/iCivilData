package com.novery.inclimeter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.novery.activity.ActivityDeviceDebug.PlaceholderFragment.MyAdapter;
import com.novery.base.App;
import com.novery.blue.BluetoothLeService;
import com.novery.icivilphone.R;
import com.novery.util.AppLog;

public class FramentIncliBluetooth extends Fragment implements
		OnItemClickListener{

	public static final String ACTION_INCLINOMETER = "com.novery.2.inclinometer";
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	private View rootView;
	private Context mContext;
	public ListView mlistView;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	private Handler mHandler;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;

	// private LeDeviceListAdapter mLeDeviceListAdapter;
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayList<BluetoothDevice> mLeDevices;
	private boolean mScanning;

	// private String mDeviceName;
	private String mDeviceAddress;
	private BluetoothLeService mBluetoothLeService;
	private boolean mConnected = false;
	private boolean mBlueSwitch = false;
	//private SwitchButton connectSwitch;

	private NotifyReceiver receiverNotify = null;
	private Intent intentNotify;
	private ImageButton imgbtBlueSwitch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = container.getContext();
		// mContext = getActivity(); // container.getContext();
		rootView = inflater.inflate(R.layout.fragment_inclinometer, container,
				false);

		initBluetoothConnectService();
		initList(rootView);

		checkBluetooth();
		initButtons();
		initTimer();

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				ProgressBar spinner = (ProgressBar) ((Activity) mContext)
						.findViewById(R.id.progressBar_blue);
				spinner.setVisibility(View.GONE);
				spinner.setVisibility(View.VISIBLE);
				RefreshDevice();
			}
		}, 3000);
		return rootView;
	}

	public FramentIncliBluetooth() {
		// TODO Auto-generated constructor stub
	}

	public void unregisterService() {

		if (intentNotify != null) {
			mContext.unregisterReceiver(receiverNotify);
		}
	}

	private void initTimer() {
		ProgressBar spinner = (ProgressBar) rootView
				.findViewById(R.id.progressBar_blue);
		spinner.setVisibility(View.GONE);
		spinner.setVisibility(View.INVISIBLE);

		receiverNotify = new NotifyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_INCLINOMETER);
		intentNotify = this.getActivity().registerReceiver(receiverNotify,
				filter);

		
	}

	private void initButtons() {
		ImageButton imgbtScan = (ImageButton) rootView
				.findViewById(R.id.imgbt_incli_scan);
		imgbtScan.setOnClickListener(new OnClickListener() {

			private ProgressBar spinner;

			@Override
			public void onClick(View arg0) {
				spinner = (ProgressBar) ((Activity) mContext)
						.findViewById(R.id.progressBar_blue);
				spinner.setVisibility(View.GONE);
				spinner.setVisibility(View.VISIBLE);
				RefreshDevice();
				Intent intent = new Intent();
				intent.putExtra("msg", "countdown_reset");
				intent.setAction(ACTION_INCLINOMETER);
				mContext.sendBroadcast(intent);

				vibrateButton();

			}

		});
		
		
		imgbtBlueSwitch = (ImageButton) rootView
				.findViewById(R.id.imgbt_switch_bluetooth);
		imgbtBlueSwitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				vibrateButton();
				if (null == mBluetoothLeService)
					return;

				if (!mBlueSwitch) { // change false to true
					
					if (!mConnected) {
						connectInclinometer();
						
					}
					
				} else {// change true to false
					if (mConnected) {
						disconnectInclinometer();
					}
				}
				mBlueSwitch = mConnected;
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (mBlueSwitch)
							imgbtBlueSwitch
									.setImageResource(R.drawable.switch_on);
						else
							imgbtBlueSwitch
									.setImageResource(R.drawable.switch_off);
					}
				}, 500);

			}

			

		});

		ImageButton imgbtStart = (ImageButton) rootView
				.findViewById(R.id.imgbt_incli_start);
		imgbtStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != mBluetoothLeService && mConnected) {
					deviceWrite("start(1)");

				} else {
					Toast.makeText(mContext, "No device is available",
							Toast.LENGTH_SHORT).show();
				}
				vibrateButton();
			}

		});
		ImageButton imgbtStop = (ImageButton) rootView
				.findViewById(R.id.imgbt_incli_stop);
		imgbtStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != mBluetoothLeService && mConnected) {
					deviceWrite("stop()");
				} else {
					Toast.makeText(mContext, "No device is available",
							Toast.LENGTH_SHORT).show();
				}
				vibrateButton();
			}

		});
		
		/*
		ImageButton imgbtSetting = (ImageButton) rootView .findViewById(R.id.imgbt_incli_setting);
		imgbtSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				vibrateButton();
				 final EditText inputServer = new EditText(mContext);
			        inputServer.setFocusable(true);

			        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			        builder.setTitle(getString(R.string.record_save_dialog_title)).setView(inputServer).setNegativeButton(
			                getString(R.string.record_save_dialog_cancel), null);
			        builder.setPositiveButton(getString(R.string.record_save_dialog_ok),
			                new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int which) {
			                        String inputName = inputServer.getText().toString();
			                        if( inputName.endsWith("2016")){
			                        	if (null != mBluetoothLeService
												&& mConnected) {
											deviceWrite("SZR(1,1)");
										} else {
											Toast.makeText(mContext,
													"No device is available",
													Toast.LENGTH_SHORT).show();
										}
			                        }
			                        else {
										Toast.makeText(mContext,
												"Password is incorrect",
												Toast.LENGTH_SHORT).show();
									}
			                    }
			                });
			        builder.show();
				
				
			}

		});
		*/
		
		ImageButton imgbtPoweroff = (ImageButton) rootView
				.findViewById(R.id.imgbt_incli_poweroff);
		imgbtPoweroff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				vibrateButton();
				new AlertDialog.Builder(mContext)
						.setTitle("System Message")
						.setMessage("Do you really want to power off?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (null != mBluetoothLeService
												&& mConnected) {
											deviceWrite("Sleep(1)");

											mDeviceAddress = "";
											ProgressBar spinner = (ProgressBar) ((Activity) mContext)
													.findViewById(R.id.progressBar_blue);
											spinner.setVisibility(View.GONE);
											spinner.setVisibility(View.VISIBLE);
											
											ImageButton imgbtState = (ImageButton) rootView.findViewById( R.id.imgbt_incli_state);			
											imgbtState.setImageResource(R.drawable.bluetooth_inactive);
											mConnected = false;
											//disconnectInclinometer();
											
											RefreshDevice();

										} else {
											Toast.makeText(mContext,
													"No device is available",
													Toast.LENGTH_SHORT).show();
										}
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).show();

			}

		});

		

		// connectSwitch = (SwitchButton)
		// rootView.findViewById(R.id.imgbt_switch_bluetooth);
		// connectSwitch.setWidth(100);
		// connectSwitch.setHeight(45);
		// connectSwitch.setOnCheckedChangeListener(this);
		// btSendCommand = (Button)rootView.findViewById(R.id.btCodesSend);
		// btSendCommand.setOnClickListener(FragmentDebugRawcode.this);
	}
	private void disconnectInclinometer() {
		if ( null != mBluetoothLeService ){
			mBluetoothLeService.disconnect();
			mConnected = false;
			//Toast.makeText(mContext, "Bluetooth disconnected",Toast.LENGTH_SHORT).show();
			mBlueSwitch = mConnected;
			if (mBlueSwitch)
				imgbtBlueSwitch.setImageResource(R.drawable.switch_on);
			else
				imgbtBlueSwitch.setImageResource(R.drawable.switch_off);
			
		}
	}
	private void vibrateButton() {
		Vibrator vibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(50);
	}

	private void initList(View rootView) {

		mlistView = (ListView) rootView.findViewById(R.id.lv_incli_bluetooth);

		mAdapter = new ArrayAdapter<String>(mContext, R.layout.list_item, items);
		mlistView.setAdapter(mAdapter);
		mlistView.setOnItemClickListener(this);
		mHandler = new Handler();

	}

	public void deviceWrite(String strText) {
		synchronized (BluetoothLeService.class) {
			mBluetoothLeService.WriteValue(strText);
		}
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
			((Activity) mContext).runOnUiThread(new Runnable() {
				private ProgressBar spinner;

				@Override
				public void run() {
					// mLeDeviceListAdapter.addDevice(device);

					if (device.getName() != null
							&& !mLeDevices.contains(device)) {
						mLeDevices.add(device);

						mHandler.sendEmptyMessage(1);

						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								items.clear();
								for (BluetoothDevice dev : mLeDevices) {
									if (dev.getName() != null) {
										String itemName = dev.getName();
										items.add(itemName);
									}
								}
								mAdapter = new ArrayAdapter<String>(mContext,
										R.layout.list_item, items);
								mlistView.setAdapter(mAdapter);
							}
						}, 200);
					}
				}
			});
		}
	};
	private Thread thdService;
	private Intent intentBlue;

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					ProgressBar spinner = (ProgressBar) ((Activity) mContext)
							.findViewById(R.id.progressBar_blue);
					spinner.setVisibility(View.GONE);
					spinner.setVisibility(View.INVISIBLE);
					if (mScanning) {
						mScanning = false;
						mBluetoothAdapter.stopLeScan(mLeScanCallback);
					} else {
						mBluetoothAdapter.stopLeScan(mLeScanCallback);
					}
				}
			}, 1000 * 10);
			mScanning = true;
			mLeDevices.clear();
			mHandler.sendEmptyMessage(1);
			items.clear();
			mAdapter = new ArrayAdapter<String>(mContext, R.layout.list_item,
					items);
			mlistView.setAdapter(mAdapter);
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		// PlaceholderFragment.this.getActivity().invalidateOptionsMenu();
	}

	private void RefreshDevice() {
		// clearBluetoothAndListView();
		scanLeDevice(true);
		/*
		 * for( int i=0;i<10;i++){ mHandler.postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { items.clear(); for( BluetoothDevice dev
		 * : mLeDevices){ String itemName = dev.getName() +":"+dev.getAddress()
		 * ; items.add( itemName ); } mAdapter = new ArrayAdapter<String>(
		 * mContext, R.layout.list_item, items); (mAdapter); } },1000*i ); }
		 */
	}

	private class NotifyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String strmsg = bundle.getString("msg");
			if (strmsg != null && strmsg.equalsIgnoreCase("over")) {
				// mContext.getApplicationContext().unregisterService();
				return;
			}
			if (strmsg != null && strmsg.equalsIgnoreCase("refresh_rawdata")) {
				ListView listView = (ListView) rootView
						.findViewById(R.id.lv_device_debug);
				MyAdapter adapterOld = (MyAdapter) listView.getAdapter();
				adapterOld.notifyDataSetChanged();
				TextView txtSeconds = (TextView) rootView
						.findViewById(R.id.txt_heart);
				txtSeconds.setText("00");
				return;
			}
			if (strmsg != null && strmsg.equalsIgnoreCase("countdown_click")) {
				TextView txtSeconds = (TextView) rootView
						.findViewById(R.id.txt_heart);
				String strSec = txtSeconds.getText().toString();
				if (strSec.isEmpty() || strSec.length() == 0)
					strSec = "0";

				int n = 0;
				try {
					n = Integer.valueOf(strSec);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

				n++;
				String strValue = String.valueOf(n);

				if (n < 10)
					strValue = '0' + strValue;
				if (n < 100)
					strValue = '0' + strValue;
				txtSeconds.setText(strValue);
			}

			if (strmsg != null && strmsg.equalsIgnoreCase("countdown_reset")) {
				TextView txtSeconds = (TextView) rootView
						.findViewById(R.id.txt_heart);
				int n = 0;
				String strValue = String.valueOf(n);

				txtSeconds.setText(strValue);
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		String strDevice = (String) mlistView.getItemAtPosition(position);
		System.out.println(strDevice);
		int idx = position;// - 1;

		BluetoothDevice device = mLeDevices.get(idx);
		mDeviceAddress = device.getAddress();
		v.setBackgroundResource(R.color.blue);
		
		connectInclinometer();
		/*
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				deviceStop();
			}
		}, 3000);
		*/
	}

	private boolean initBluetoothConnectService() {

		Intent gattServiceIntent = new Intent(rootView.getContext(),
				BluetoothLeService.class);
		
		if (null != intentBlue) {
			rootView.getContext().unbindService(mServiceConnection);
		}
		if (!rootView.getContext().bindService(gattServiceIntent,
				mServiceConnection, Context.BIND_AUTO_CREATE)) {
			Toast.makeText(mContext, "Failed to start bluetooth service",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (null != intentBlue) {
			rootView.getContext().unregisterReceiver(mGattUpdateReceiver);
		}

		intentBlue = rootView.getContext().registerReceiver(
				mGattUpdateReceiver, makeGattUpdateIntentFilter());
		if (null == intentBlue)
			intentBlue = gattServiceIntent;
		return true;
	}

	private void initHeart() {
		if (null == thdService || (!thdService.isAlive())) {
			thdService = new Thread(new Runnable() {
				@Override
				public void run() {
					int n = 0;
					while (null != mBluetoothLeService && mConnected) {
						n++;
						if (n >= 20) {
							n = 0;
							if (null != mBluetoothLeService && mConnected) {
								deviceWrite("heartbeat(1)");
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

							Intent intent = new Intent();
							intent.putExtra("msg", "countdown_reset");
							intent.setAction(ACTION_INCLINOMETER);
							mContext.sendBroadcast(intent);
						} else {
							Intent intent = new Intent();
							intent.putExtra("msg", "countdown_click");
							intent.setAction(ACTION_INCLINOMETER);
							mContext.sendBroadcast(intent);
						}

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			thdService.start();
		}

	}

	private void deviceGetName() {
		Thread thdService = new Thread(new Runnable() {
			@Override
			public void run() {
				if (null != mBluetoothLeService && mConnected) {
					deviceWrite("stop()");
				}
			}
		});
		thdService.start();
	}

	private void deviceStop() {
		Thread thdService = new Thread(new Runnable() {
			@Override
			public void run() {
				if (null != mBluetoothLeService && mConnected) {
					deviceWrite("stop()");
				}
			}
		});
		thdService.start();

	}

	private void deviceStart0() {
		Thread thdService = new Thread(new Runnable() {
			@Override
			public void run() {
				if (null != mBluetoothLeService && mConnected) {
					deviceWrite("start(0)");
				}
			}
		});
		thdService.start();
	}

	private void deviceStart1() {
		Thread thdService = new Thread(new Runnable() {
			@Override
			public void run() {
				if (null != mBluetoothLeService && mConnected) {
					deviceWrite("start(1)");
				}
			}
		});
		thdService.start();
	}

	private void deviceStart2() {
		Thread thdService = new Thread(new Runnable() {
			@Override
			public void run() {
				if (null != mBluetoothLeService && mConnected) {
					deviceWrite("start(2)");
				}
			}
		});
		thdService.start();
	}

	// Handles various events fired by the Service.
	// ACTION_GATT_CONNECTED: connected to a GATT server.
	// ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
	// ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
	// ACTION_DATA_AVAILABLE: received data from the device. This can be a
	// result of read
	// or notification operations.
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		private String bufferStack = "";

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) { // 连接成功
				// Log.e(TAG, "Only gatt, just wait");
				AppLog.log("Only gatt, just wait");
				Toast.makeText(mContext, "Bluetooth connected",
						Toast.LENGTH_SHORT).show();
				onInclinometerConnected();
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) { // 断开连接
				mConnected = false;
				// invalidateOptionsMenu();
				// btnSend.setEnabled(false);
				// clearUI();
				Toast.makeText(mContext, "Bluetooth disconnected",
						Toast.LENGTH_SHORT).show();
				onInclinometerDisconnected();
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) // 可以开始干活了
			{
				mConnected = true;
				Toast.makeText(mContext, "Bluetooth is ready",
						Toast.LENGTH_SHORT).show();
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { // 收到数据
				// Log.e(TAG, "RECV DATA");
				String data = intent
						.getStringExtra(BluetoothLeService.EXTRA_DATA);
				bufferStack += data;
				if (bufferStack.length() >= 30) {
					String stritem = bufferStack.substring(0, 30);

					onInclinometerDataReceived(stritem);
					bufferStack = "";
				}
				/*
				 * if (data != null) { data = data.replaceAll("\r", ""); data =
				 * data.replaceAll("\n", ""); data = data.trim();
				 * //Toast.makeText(mContext, "收到数据",
				 * Toast.LENGTH_SHORT).show(); AppLog.log(data); String
				 * dataItems[] = data.split(" "); if( dataItems.length== 2 ){
				 * String itemX[] = dataItems[0].split(":"); if( itemX.length ==
				 * 2 ){ TextView txtScreenX = (TextView)
				 * rootView.findViewById(R.id.txt_incli_x); txtScreenX.setText(
				 * itemX[1]); } String itemY[] = dataItems[1].split(":"); if(
				 * itemY.length == 2 ){ TextView txtScreenY = (TextView)
				 * rootView.findViewById(R.id.txt_incli_y); txtScreenY.setText(
				 * itemY[1]); } } else{ String itemValue[] =
				 * dataItems[0].split(":"); if( itemValue.length == 2 ){ if(
				 * itemValue[0].equalsIgnoreCase("X")){ TextView txtScreen =
				 * (TextView) rootView.findViewById(R.id.txt_incli_x); String
				 * strvalue = getNumberString(itemValue); txtScreen.setText(
				 * strvalue); } else{ TextView txtScreen = (TextView)
				 * rootView.findViewById(R.id.txt_incli_y); String strvalue =
				 * getNumberString(itemValue); txtScreen.setText( strvalue); } }
				 * else{ byte bytedata[] = data.getBytes(); if( bytedata[0] ==
				 * 0x58){ Toast.makeText(mContext,"X is OK" ,
				 * Toast.LENGTH_SHORT).show(); } else if( bytedata[0] == 0x79){
				 * Toast.makeText(mContext,"Y is OK" ,
				 * Toast.LENGTH_SHORT).show(); } } }
				 * 
				 * }
				 */
			}
		}

		private void onInclinometerConnected(){
			ImageButton imgbtState = (ImageButton) rootView.findViewById( R.id.imgbt_incli_state);			
			imgbtState.setImageResource(R.drawable.bluetooth_active);
		}
		private void onInclinometerDisconnected(){
			ImageButton imgbtState = (ImageButton) rootView.findViewById( R.id.imgbt_incli_state);			
			imgbtState.setImageResource(R.drawable.bluetooth_inactive);
		}
		private void onInclinometerDataReceived(String rawdata) {

			if (null == rawdata || rawdata.isEmpty())
				return;

			byte bytearr[] = App.stringTobytes(rawdata);
			if (bytearr.length != 15) {
				Toast.makeText(mContext, rawdata, Toast.LENGTH_SHORT).show();
				return;
			}
			int cmd = bytearr[0];
			int result = bytearr[1];

			switch (cmd) {
			case 0x03:// X angle degree
			{
				byte angleVal[] = new byte[2];
				angleVal[0] = bytearr[8];
				angleVal[1] = bytearr[9];
				int n = App.doubleByteValue(angleVal);
				int minus = (-1) * (n & 0x8000);
				int val = n & 0x7FFF;
				int angle = val;
				if (minus < 0) {
					angle = (-1) * val;
				}
				double dbAngle = ((double) angle) / 1000;
				TextView txtScreen = (TextView) rootView
						.findViewById(R.id.txt_incli_x);
				String strvalue = String.valueOf(dbAngle);
				txtScreen.setText(strvalue);
				/*
				byte tempVal[] = new byte[2];
				tempVal[0] = bytearr[5];
				tempVal[1] = bytearr[6];
				int nTemp = App.doubleByteValue(tempVal);
				int minusTemp = (-1) * (nTemp & 0x8000);
				int valTemp = nTemp & 0x7FFF;
				int temp = valTemp;
				if (minusTemp < 0) {
					temp = (-1) * valTemp;
				}
				double dbTemp = ((double) temp) / 10;
				txtScreen = (TextView) rootView
						.findViewById(R.id.txt_incli_temp);
				strvalue = String.valueOf(dbTemp);
				txtScreen.setText(strvalue);
				
				byte voltVal[] = new byte[2];
				voltVal[0] = bytearr[13];
				voltVal[1] = bytearr[14];
				int nVolt = App.doubleByteValue(voltVal);
				int minusVolt = (-1) * (nVolt & 0x8000);
				int valVolt = nVolt & 0x7FFF;
				int volt = valVolt;
				if (minusVolt < 0) {
					volt = (-1) * valVolt;
				}
				double dbVolt = ((double) volt) / 100;
				txtScreen = (TextView) rootView
						.findViewById(R.id.txt_incli_volt);
				strvalue = String.valueOf(dbVolt);
				txtScreen.setText(strvalue);
				*/
			}
				break;
			case 0x04:// Y angle degree
			{
				byte angleVal[] = new byte[2];
				angleVal[0] = bytearr[8];
				angleVal[1] = bytearr[9];
				int n = App.doubleByteValue(angleVal);
				int minus = (-1) * (n & 0x8000);
				int val = n & 0x7FFF;
				int angle = val;
				if (minus < 0) {
					angle = (-1) * val;
				}
				double dbAngle = ((double) angle) / 1000;

				TextView txtScreen = (TextView) rootView
						.findViewById(R.id.txt_incli_y);
				String strvalue = String.valueOf(dbAngle);
				txtScreen.setText(strvalue);
			}
				break;

			case 0x05:// calibrate
				if (0x08 == result) {
					Toast.makeText(mContext, "X calibrating ended",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 0x06:// calibrate
				if (0x08 == result) {
					Toast.makeText(mContext, "Y calibrating ended",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 0x07:// stop
				if (0x08 == result) {
					Toast.makeText(mContext, "Operation is stopped",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}

		private String getNumberString(String[] itemValue) {
			String str = itemValue[1];
			// int len = 0;
			String strvalue = "";
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) >= '0' || str.charAt(i) <= '9'
						|| str.charAt(i) == '-' || str.charAt(i) >= '.') {
					// len ++ ;
					strvalue += str.charAt(i);
					continue;
				} else {
					break;
				}
			}
			return strvalue;
		}
	};

	private static IntentFilter makeGattUpdateIntentFilter() { // 注册接收的事件
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothDevice.ACTION_UUID);
		return intentFilter;
	}

	// Code to manage Service lifecycle.
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();

			if (!mBluetoothLeService.initialize()) {
				Toast.makeText(mContext, "Failed to initialize bluetooth",
						Toast.LENGTH_SHORT).show();
			}
						
		}

		

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
			mConnected = false;
			mBlueSwitch = mConnected;
			if (mBlueSwitch)
				imgbtBlueSwitch.setImageResource(R.drawable.switch_on);
			else
				imgbtBlueSwitch.setImageResource(R.drawable.switch_off);
		}
	};

	// Code to manage Service lifecycle.
	private void connectInclinometer() {
		if( null == mBluetoothLeService )
			return ;
		
		
		if (mConnected) {
			mBluetoothLeService.disconnect();
		}
		mConnected = false;
		mBlueSwitch = mConnected;
		if (!mDeviceAddress.isEmpty()) {
			mConnected = mBluetoothLeService.connect(mDeviceAddress);
			mBlueSwitch = mConnected;
			Toast.makeText(mContext, "Bluetooth conneceted",Toast.LENGTH_SHORT).show();
			initHeart();
		}
		
		if (mBlueSwitch)
			imgbtBlueSwitch.setImageResource(R.drawable.switch_on);
		else
			imgbtBlueSwitch.setImageResource(R.drawable.switch_off);
	}
	

	/*
	 * public class OnChronometerTickListenerImpl implements
	 * OnChronometerTickListener {
	 * 
	 * @Override public void onChronometerTick(Chronometer chronometer) { String
	 * time = chronometer.getText().toString(); time = time.substring(3); if (
	 * Integer.parseInt( time) >= 20 ) {// 判断五秒之后，让手机震动 //vibrator.vibrate(new
	 * long[] { 1000, 10, 100, 10 }, 0);// 设置震动周期和是否循环震动，如果不想循环震动把0改为-1 if( null
	 * != mBluetoothLeService && mConnected) { mBluetoothLeService.WriteValue(
	 * "heartbeat(1)"); } chronometer.setBase(SystemClock.elapsedRealtime());//
	 * 复位键 }
	 * 
	 * } }
	 */
}
