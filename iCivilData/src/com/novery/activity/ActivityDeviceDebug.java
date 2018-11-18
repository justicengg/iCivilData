package com.novery.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.novery.base.AppConf;
import com.novery.icivilphone.R;
import com.novery.rest.RestRunnable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityDeviceDebug extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_device_debug);
		
		AppConf.init();
		AppConf.load();
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_device_debug, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			
			LayoutInflater inflater = LayoutInflater.from( this);
			final View viewParameter = inflater.inflate(R.layout.fragment_config_device_debug,
					null);
			EditText etUri = (EditText) viewParameter.findViewById(R.id.et_uri);
			EditText etStation = (EditText) viewParameter.findViewById(R.id.et_station);
			
			String strUri = AppConf.strRestUri;
			etUri.setText(strUri);
			etStation.setText(String.valueOf(AppConf.nDefaultStation));
				
			new AlertDialog.Builder( this )
			.setTitle( "参数设置" )
			.setView(viewParameter)
			.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					EditText etUri = (EditText) viewParameter.findViewById(R.id.et_uri);
					EditText etStation = (EditText) viewParameter.findViewById(R.id.et_station);
					String strUri = etUri.getText().toString();
					AppConf.strDataBaseUri = strUri;
					AppConf.nDefaultStation = Integer.parseInt(etStation.getText().toString());

					String strStation = String.valueOf( AppConf.nDefaultStation );

					etStation.setText( strStation );
					try {
						AppConf.update();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}})
			.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
					}
				})
			.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private List<Map<String, Object>> mData;
		private List<Map<String, MyListener>>  mlstListener;
		private int flag;
		public static String title[] = new String[] 
				{ "-" };
		public static String info[] = new String[] 
				{"-"};
		
		private View rootView;
		private Context  mContext;
		public ListView mlistView;
		private RestRunnable runRest ;
		private Thread thdService;
		private NotifyReceiver receiverNotify = null;
		private Intent intentNotify = null;
		
		public PlaceholderFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_activity_device_debug, null);
			rootView = view;
			mContext = container.getContext();
			
			
			receiverNotify = new NotifyReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.novery.rest");
			intentNotify = mContext.registerReceiver(receiverNotify, filter);

			
			mlstListener = new  ArrayList<Map<String,MyListener>> ();
			
			mData = getData();
			ListView listView = (ListView) rootView.findViewById(R.id.lv_device_debug);
			mlistView = listView;
			MyAdapter adapter = new MyAdapter( mContext );
			listView.setAdapter(adapter);
			EditText edtStation = (EditText) rootView.findViewById(R.id.et_station);			
			edtStation.setText( String.valueOf( AppConf.nDefaultStation));
			
			runRest = new RestRunnable( mContext, this.mlstListener,this.mlistView,this.mData,edtStation );
			thdService = new Thread( runRest );
			thdService.start();
			
			timerCountDown = new Timer(true);
			timerCountDown.schedule(taskCountDown, 1000,1000);
			return view;
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		       // inflater.inflate(R.menu.fragment_device_debug_parameters, menu);
		        super.onCreateOptionsMenu(menu,null);
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setHasOptionsMenu(true);
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.action_reload_parameters) {
				
						
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
		@Override
		public void onDestroy()
		{
			super.onDestroy();
			runRest.stopAction();
			unregisterService();
			timerCountDown.cancel();
		}

		private TimerTask taskCountDown = new TimerTask(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				NotifyRefresh();
			}	
			private void NotifyRefresh(){
				// 发送广播
				Intent intent = new Intent();
				intent.putExtra("msg","refresh_countdown");
				intent.setAction("com.novery.rest");
				mContext.sendBroadcast(intent);	
			}
		};

		private Timer timerCountDown ;
	// 获取动态数组数据 可以由其他地方传来(json等)
	private List<Map<String, Object>> getData() {
		/*
		ListNovRawData lsRows = NovRawDataApi.getStationRows( 50 );
		if( null == lsRows ){
			return null;
		}
		mlstListener.clear();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < lsRows.getLstData().size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String strCreated = App.mysqlDateToFullString(lsRows.getLstData().get(i).getRawCreated());
			String strText = lsRows.getLstData().get(i).getRawContent();
			map.put("title",strCreated );
			map.put("info", strText );
			list.add(map);		
			Map<String, MyListener> mapListen = new HashMap<String,MyListener>();
			mlstListener.add(mapListen);
		}
		return list;
		*/
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < title.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", title[i]);
			map.put("info", info[i]);
			list.add(map);
			
			Map<String, MyListener> mapListen = new HashMap<String,MyListener>();
			mlstListener.add(mapListen);
		}
		return list;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		public void onResume(){
			Toast.makeText( mContext, "onResume",Toast.LENGTH_SHORT).show();
		}

		public void onPause(){
			Toast.makeText( mContext, "onPause",Toast.LENGTH_SHORT).show();
		}
		// ****************************************第二种方法，高手一般都用此种方法,具体原因，我还不清楚,有待研究
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			MyListener myListener = null;
			
			if( position >= mData.size() )
				return convertView;
			
			if (convertView == null) {

				holder = new ViewHolder();

				// 可以理解为从vlist获取view 之后把view返回给ListView
				myListener = mlstListener.get( position ).get("listener");
				if( null == myListener){
					myListener = new MyListener(position);
					mlstListener.get( position ).put("listener", myListener);
				}
				convertView = mInflater.inflate(R.layout.vlist, null);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.info = (TextView) convertView.findViewById(R.id.info);;
				holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				myListener = mlstListener.get( position ).get("listener");
				if( null == myListener){
					myListener = new MyListener(position);
					mlstListener.get( position ).put("listener", myListener);
				}
			}

			if( null != mData && (! mData.isEmpty())){				
				holder.title.setText((String) mData.get(position).get("title"));
				String strItem =(String) mData.get(position).get("info");
				if( strItem.length() > 10 ){
					strItem = strItem.substring(0, 10) ;
				}
				holder.info.setText(strItem);
			}
			else{
				holder.title.setText("none");
				holder.info.setText("none");
			}
			holder.viewBtn.setTag(position);
			// 给Button添加单击事件 添加Button之后ListView将失去焦点 需要的直接把Button的焦点去掉
			holder.viewBtn.setOnClickListener(myListener);

			// holder.viewBtn.setOnClickListener(MyListener(position));

			return convertView;
		}
	}


	public class MyListener implements OnClickListener {
		int mPosition;

		public MyListener(int inPosition) {
			mPosition = inPosition;
			ListView listView = (ListView) rootView.findViewById(R.id.lv_device_debug);
			listView.setSelected(true);
			listView.setSelection(mPosition);
		}
		
		

		@Override
		public void onClick(View v) {
			
			//Toast.makeText( mContext, title[mPosition],Toast.LENGTH_SHORT).show();
			//showInfo( mPosition);
			
			
			LayoutInflater inflater = LayoutInflater.from( mContext);
			final View viewParameter = inflater.inflate(R.layout.fragment_device_debug_detail,
					null);
			TextView textparameterName = (TextView) viewParameter.findViewById(R.id.textParameterName);
			TextView textparameter = (TextView) viewParameter.findViewById(R.id.editParameterValue);
			
			mData.get(mPosition).get("info").toString();
		
			
			textparameterName.setText( mData.get(mPosition).get("title").toString());
			textparameter.setText( mData.get(mPosition).get("info").toString());		
			new AlertDialog.Builder( mContext )
			.setTitle( "详细数据" )
			.setView(viewParameter)
			.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					
					
				}})
			.show();
					
					
		}

	}

	// 提取出来方便点
	public final class ViewHolder {
		public TextView title;
		public TextView info;
		public Button viewBtn;
	}
	public void unregisterService() {
		
		if (intentNotify != null) {
			mContext.unregisterReceiver(receiverNotify);
		}
	}
	public void showInfo(int position) {

		ImageView img = new ImageView( mContext );
		img.setImageResource(R.drawable.b);
		
		new AlertDialog.Builder( mContext )
				.setView(img)
				.setTitle("详情" + position)
				.setMessage("通道：" + title[position] + "   类型:" + info[position])
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
		}
	

		private class NotifyReceiver extends BroadcastReceiver {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();			
				String strmsg = bundle.getString("msg");
				if( strmsg != null && strmsg.equalsIgnoreCase("over")) {
					PlaceholderFragment.this.unregisterService();
					return;
				}
				if( strmsg != null && strmsg.equalsIgnoreCase("refresh_rawdata")) {
					ListView listView = (ListView) rootView.findViewById(R.id.lv_device_debug);				
					MyAdapter adapterOld = (MyAdapter) listView.getAdapter();
					adapterOld.notifyDataSetChanged();
					TextView txtSeconds = (TextView) rootView.findViewById(R.id.tv_remain_seconds);
					txtSeconds.setText( "000" );
					return;
				}
				if( strmsg != null && strmsg.equalsIgnoreCase("refresh_countdown")) {
					TextView txtSeconds = (TextView) rootView.findViewById(R.id.tv_remain_seconds);
					int n = Integer.parseInt(txtSeconds.getText().toString());
					n++;
					String strValue = String.valueOf(n);
					if( n>1000 )
						n = 0 ;
					if( n <10 )
						strValue = '0' + strValue;
					if( n<100 )
						strValue = '0' + strValue;
					txtSeconds.setText(strValue );
				}
	
			}
		}
	}//end of placement
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {			
			new AlertDialog.Builder( this )
			.setTitle("退出确认")
			.setMessage("确定退出吗？")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {					
					finish();
					onDestroy();  
					System.exit(0);
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			})
			.show();
		}
		return false;
	}
	
}
