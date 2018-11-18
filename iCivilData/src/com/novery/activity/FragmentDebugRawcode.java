package com.novery.activity;
import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.novery.activity.ActivityHome.PlaceholderFragment;
import com.novery.base.App;
import com.novery.util.AppLog;
import com.novery.blue.BluetoothLeService;
import com.novery.icivilphone.R;
import com.novery.switchplugin.SwitchButton;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentDebugRawcode 
	extends Fragment 
	implements OnCheckedChangeListener,
				IXListViewListener,
				AdapterView.OnItemClickListener,
				View.OnClickListener{
	
	FragmentActivity mActivity ;
	Context mContext;
	View  rootView ;
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    SwitchButton connectSwitch ;
    Button btSendCommand;
    TextView textDeviceName;
    Intent mIntent ;
    
    
    ///
    //
    private XListView mListView;
    private ArrayAdapter<String> mAdapter;
	private ArrayList<String> itemsOfListView = new ArrayList<String>();
	private Handler mHandler;
	private Handler handlerReceiveData;
	private String strDataStack="";
	private int nPacketCount= 0;
	 // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
               // Log.e(TAG, "Unable to initialize Bluetooth");
                //finish();
            	connectSwitch.setChecked(false);
            	Toast.makeText(mContext, "连接蓝牙服务失败", Toast.LENGTH_SHORT).show();
            }
           
            //Log.e(TAG, "mBluetoothLeService is okay");
            // Automatically connects to the device upon successful start-up initialization.
            //mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity =FragmentDebugRawcode.this.getActivity();
		mContext = container.getContext();
		View view = inflater.inflate(R.layout.fargment_activity_debug_rawcode, null);
		rootView = view;
		mIntent = mActivity.getIntent();
		 
		initList( rootView);
		initBlueteeth();
		initSwitch();
		return view;
	}
	private void initList( View rootView ){
		
		mListView = (XListView) rootView.findViewById(R.id.xListViewRawcode);
		mListView.setPullLoadEnable(true);
		mAdapter = new ArrayAdapter<String>( mContext , R.layout.list_item, itemsOfListView);
		mListView.setAdapter(mAdapter);
		mListView.setXListViewListener(FragmentDebugRawcode.this);
		mListView.setOnItemClickListener(FragmentDebugRawcode.this);
		mHandler = new Handler();
		handlerReceiveData = new Handler();
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(App.getSQLDatetime());
	}

	private void initSwitch(){
		connectSwitch = (SwitchButton) rootView.findViewById(R.id.connectSwitch);
		connectSwitch.setOnCheckedChangeListener(this);
		btSendCommand = (Button)rootView.findViewById(R.id.btCodesSend);
		btSendCommand.setOnClickListener(FragmentDebugRawcode.this);
	}
	private boolean initBlueteeth(){
		final Intent intent = mActivity.getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        
        textDeviceName = (TextView) rootView.findViewById( R.id.textFieldBluetoothName);
        textDeviceName.setText( mDeviceName );
        
        Intent gattServiceIntent = new Intent( rootView.getContext(), BluetoothLeService.class);
        if(! rootView.getContext().bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)){
        	Toast.makeText(mContext, "蓝牙服务启动失败", Toast.LENGTH_SHORT).show();
        	return false;
        }
        
        rootView.getContext().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        return true;
	}
	 private static IntentFilter makeGattUpdateIntentFilter() {                        //注册接收的事件
	        final IntentFilter intentFilter = new IntentFilter();
	        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
	        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
	        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
	        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
	        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
	        return intentFilter;
	    }
	
	
   
 // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {  //连接成功
            	//Log.e(TAG, "Only gatt, just wait");
            	AppLog.log("Only gatt, just wait");
            	Toast.makeText(mContext, "蓝牙连接成功", Toast.LENGTH_SHORT).show();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //断开连接
                mConnected = false;
                //invalidateOptionsMenu();
                //btnSend.setEnabled(false);
                //clearUI();
                Toast.makeText(mContext, "蓝牙断开", Toast.LENGTH_SHORT).show();
            }else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //可以开始干活了
            {
            	mConnected = true;
            	//mDataField.setText("");
            	//ShowDialog();
            	//btnSend.setEnabled(true);
            	//Log.e(TAG, "In what we need");
            	//invalidateOptionsMenu();
            	 Toast.makeText(mContext, "蓝牙准备就绪", Toast.LENGTH_SHORT).show();
            }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //收到数据
            	//Log.e(TAG, "RECV DATA");
            	String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
            	if (data != null) {
            		 Toast.makeText(mContext, "收到数据", Toast.LENGTH_SHORT).show();
            		AppLog.log(data);
            		if( 0 == nPacketCount ){
            			strDataStack = data;
            		}
            		else{
            			strDataStack += data;
            		}
            		nPacketCount ++;            		
            		handlerReceiveData.postDelayed(new Runnable() {
            			@Override
            			public void run() {
            				nPacketCount --;
            				if( nPacketCount <=0 ){
	            				itemsOfListView.add( strDataStack);
	        					mAdapter = new ArrayAdapter<String>( mContext, R.layout.list_item, itemsOfListView);
	        					mListView.setAdapter(mAdapter);
	        					
	        					strDataStack="";
	        					nPacketCount = 0 ;
            				}
            			}
            		},
            		3000
            		);
            		
            		
					
            		/*
                	if (mDataField.length() > 500)
                		mDataField.setText("");
                	//此处添加对数组的定义
                	
                    mDataField.append(data); 
                    svResult.post(new Runnable() {
            			public void run() {
            				svResult.fullScroll(ScrollView.FOCUS_DOWN);
            			}
            			
            		});
            		*/
                }
            }
        }
    };
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if(  null == mBluetoothLeService)
			return;
		
		if( connectSwitch.isChecked()){
			if( !mConnected   ){
				mConnected = mBluetoothLeService.connect(mDeviceAddress);
				Toast.makeText(mContext, "蓝牙设备连接成功", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			if( mConnected  ){
				mBluetoothLeService.disconnect();
				mConnected = false;
				Toast.makeText(mContext, "蓝牙设备断开", Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	@Override
	public void onClick(View btClicked) {
		if( btClicked.getId() == btSendCommand.getId()){
			EditText editTextCodes = (EditText)rootView.findViewById(R.id.editTextCodes);
			
			String strCommand = editTextCodes.getText().toString();
			if( strCommand.length() > 0 ){
				mBluetoothLeService.WriteValue(editTextCodes.getText().toString());
				
				itemsOfListView.add( strCommand );
				mAdapter = new ArrayAdapter<String>( mContext, R.layout.list_item, itemsOfListView);
				mListView.setAdapter(mAdapter);
				
				editTextCodes.setText("");
				InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				if(imm.isActive())
					imm.hideSoftInputFromWindow(editTextCodes.getWindowToken(), 0);
				
			}
			
		}
		
		if( btClicked.getId() == connectSwitch.getId()){
			if( connectSwitch.isChecked()){
				if( !mConnected )
					mConnected = mBluetoothLeService.connect(mDeviceAddress);
			}
			else{
				if( mConnected ){
					mBluetoothLeService.disconnect();
					mConnected = false;
				}
			}
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh() {
		RefreshRawCodes( true );
	}
	private void RefreshRawCodes( boolean bClear ) {
		if( bClear )
			itemsOfListView.clear();
        mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
					if( itemsOfListView.size() == 0 ){
						itemsOfListView.add( App.getSQLDatetime());
					}
					mAdapter = new ArrayAdapter<String>( mContext, R.layout.list_item, itemsOfListView);
					mListView.setAdapter(mAdapter);
					onLoad();
			}
		},
		100 
		);
	}
	@Override
	public void onLoadMore() {
		RefreshRawCodes( false );
		
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
        rootView.getContext().unregisterReceiver(mGattUpdateReceiver);
        rootView.getContext().unbindService(mServiceConnection);
        if(mBluetoothLeService != null)
        {
        	mBluetoothLeService.close();
        	mBluetoothLeService = null;
        	Toast.makeText(mContext, "蓝牙关闭", Toast.LENGTH_SHORT).show();
        }
	}
	
	
    
}
