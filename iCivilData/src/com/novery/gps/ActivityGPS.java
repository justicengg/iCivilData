package com.novery.gps;

/*
 * AlimysoYang
 * QQ:86373007
 * 最后一次修改2012-03-16
 * */

import java.util.Date;
import java.util.Iterator;

import com.novery.icivilphone.R;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityGPS extends Activity implements OnClickListener
{
	private LocationManager lm;
	private Location loc;
	private Criteria ct;
	private String provider;
	private GpsStatus gpsstatus;

	private TextView tvLatitude;
	private TextView tvLongitude;
	private TextView tvHigh;
	private TextView tvDirection;
	private TextView tvSpeed;
	private TextView tvSpeed1;
	private TextView tvGpsTime;
	private TextView tvvSaltnum;
	private TextView tvcSaltnum;
	private TextView tvInfoType;
	private EditText etSetTimeSpace;
	private Button btnmanual;
	private Button btnsettimespace;
	private Button btnexit;

	private DBGps dbgps = new DBGps(this);

	//private boolean isLocation = false;// 是否定位
	private int timespace = 100;
	private AutoThread athread = new AutoThread();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tvLatitude = (TextView) findViewById(R.id.tvlatitude);
		tvLongitude = (TextView) findViewById(R.id.tvlongitude);
		tvHigh = (TextView) findViewById(R.id.tvhigh);
		tvDirection = (TextView) findViewById(R.id.tvdirection);
		tvSpeed = (TextView) findViewById(R.id.tvspeed);
		tvSpeed1 = (TextView) findViewById(R.id.tvspeed1);
		tvGpsTime = (TextView) findViewById(R.id.tvgpstime);
		tvvSaltnum = (TextView) findViewById(R.id.tvvsatlnum);
		tvcSaltnum = (TextView) findViewById(R.id.tvcsatlnum);
		tvInfoType = (TextView) findViewById(R.id.tvinfotype);
		etSetTimeSpace = (EditText) findViewById(R.id.ettimespace);
		btnmanual = (Button) findViewById(R.id.btnmanual);
		btnmanual.setOnClickListener(this);
		btnsettimespace = (Button) findViewById(R.id.btnsettimespace);
		btnsettimespace.setOnClickListener(this);
		btnexit = (Button) findViewById(R.id.btnexit);
		btnexit.setOnClickListener(this);

		// dbgps.openDB();
		initLocation();
	}

	private final LocationListener locationListener = new LocationListener()
	{
		public void onLocationChanged(Location arg0)
		{
			if(null == athread || athread.isExited() )
				return;
			
			//isLocation = true;
			if (!athread.isAlive())
				athread.start();
		}

		public void onProviderDisabled(String arg0)
		{
			showInfo(null, -1);
		}

		public void onProviderEnabled(String arg0)
		{
		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2)
		{
			//isLocation = true;
			if(null == athread || !athread.isRunning() )
			if (!athread.isAlive())
				athread.start();
		}
	};

	private GpsStatus.Listener statuslistener = new GpsStatus.Listener()
	{

		public void onGpsStatusChanged(int event)
		{
			
			if(null == athread || !athread.isRunning() )
				return ;
			gpsstatus = lm.getGpsStatus(null);
			switch (event)
			{
				case GpsStatus.GPS_EVENT_FIRST_FIX:// 第一次定位
					int c = gpsstatus.getTimeToFirstFix();
					Log.i("AlimysoYang", String.valueOf(c));
					break;
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				{
					// 得到所有收到的卫星的信息，包括 卫星的高度角、方位角、信噪比、和伪随机号（及卫星编号）
					Iterable<GpsSatellite> allgps = gpsstatus.getSatellites();
					Iterator<GpsSatellite> items = allgps.iterator();
					int i = 0;
					int ii = 0;
					while (items.hasNext())
					{
						GpsSatellite tmp = (GpsSatellite) items.next();
						if (tmp.usedInFix())
							ii++;
						i++;
					}
					tvvSaltnum.setText(String.format("可见卫星数:%d", i));
					tvcSaltnum.setText(String.format("已定位卫星数:%d", ii));
					break;
				}
				case GpsStatus.GPS_EVENT_STARTED:
					break;
				case GpsStatus.GPS_EVENT_STOPPED:
					break;
			}
		}
	};

	private void initLocation()
	{
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			ct = new Criteria();
			// ct.setAccuracy(Criteria.ACCURACY_FINE);// 高精度定位慢
			ct.setAccuracy(Criteria.ACCURACY_COARSE);//非高精度定位更快
			ct.setAltitudeRequired(true);// 显示海拔
			ct.setBearingRequired(true);// 显示方向
			ct.setSpeedRequired(true);// 显示速度
			ct.setCostAllowed(true);// 不允许有花费
			ct.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
			provider = lm.getBestProvider(ct, true);
			// 位置变化监听,默认1秒一次,0表示不考虑距离变化(距离10米以上)
			lm.requestLocationUpdates(provider, 1000, 0, locationListener);
			lm.addGpsStatusListener(statuslistener);
		} else
			showInfo(null, -1);
	}

	private gpsdata getLastPosition()
	{
		if(null == athread || !athread.isRunning() )
			return null ;
		gpsdata result = new gpsdata();
		loc = lm.getLastKnownLocation(provider);
		if (loc != null)
		{
			result.Latitude = (int) (loc.getLatitude() * 1E6);
			result.Longitude = (int) (loc.getLongitude() * 1E6);
			result.High = loc.getAltitude();
			result.Direct = loc.getBearing();
			result.Speed = loc.getSpeed();
			Date d = new Date();
			d.setTime(loc.getTime() + 28800000);// UTC时间,转北京时间+8小时,不同的手机GPS时间不同,有的手机GPS时间不需要+8小时
			result.GpsTime = DateFormat.format("yyyy-MM-dd kk:mm:ss", d).toString();
			d = null;
		}
		return result;
	}

	private void showInfo(gpsdata cdata, int infotype)
	{
		if(null == athread || !athread.isRunning() )
			return ;
		if (cdata == null)
		{
			if (infotype == -1)
			{
				tvLatitude.setText("GPS功能已关闭");
				tvLongitude.setText("");
				tvHigh.setText("");
				tvDirection.setText("");
				tvSpeed.setText("");
				tvSpeed1.setText("");
				tvGpsTime.setText("");
				tvInfoType.setText("");
				btnmanual.setEnabled(false);
				btnsettimespace.setEnabled(false);
				etSetTimeSpace.setEnabled(false);
			}
		} else
		{
			tvLatitude.setText(String.format("纬度:%d", cdata.Latitude));
			tvLongitude.setText(String.format("经度:%d", cdata.Longitude));
			tvHigh.setText(String.format("海拔:%f", cdata.High));
			tvDirection.setText(String.format("方向:%f", cdata.Direct));
			tvSpeed.setText(String.format("速度:%fm/s", cdata.Speed));
			tvSpeed1.setText(String.format("速度:%fkm/h", (cdata.Speed * 3600.0) / 1000.0));
			tvGpsTime.setText(String.format("GPS时间:%s", cdata.GpsTime));
			cdata.InfoType = infotype;
			switch (infotype)
			{
				case 1:
					tvInfoType.setText("信息来源状态:手动获取更新");
					break;
				case 2:
					tvInfoType.setText("信息来源状态:位置改变更新");
					break;
			/*
			 * case 3: tvInfoType.setText("信息来源状态:位置改变更新"); break;
			 */
			}

			if( null != dbgps ){
				dbgps.addGpsData(cdata);
			}
		}

	}

	public void onClick(View v)
	{
		if (v.equals(btnmanual))
		{
			// if (isLocation)
			showInfo(getLastPosition(), 1);
		}
		if (v.equals(btnsettimespace))
		{
			if (TextUtils.isEmpty(etSetTimeSpace.getText().toString()))
			{
				Toast.makeText(this, "请输入更新时间间隔", Toast.LENGTH_LONG).show();
				etSetTimeSpace.requestFocus();
				return;
			}

			/*
			 * int timespace =
			 * Integer.valueOf(etSetTimeSpace.getText().toString()) * 1000; if
			 * (lm.isProviderEnabled(lm.GPS_PROVIDER))
			 * lm.requestLocationUpdates(provider, timespace, 10,
			 * locationListener);
			 */
			timespace = Integer.valueOf(etSetTimeSpace.getText().toString()) * 1000;
		}
		
		if (v.equals(btnexit)){
			athread.stopRun();
			this.finish();
		}
			//android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onDestroy()
	{
		if (dbgps != null)
		{
			dbgps.closeDB();
			dbgps = null;
		}
	//	android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

	private class AutoThread extends Thread
	{
		private boolean running = false;
		private boolean exited = false;
		private Handler h = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				if( running ){
					showInfo(getLastPosition(), 2);
				}
			}
		};

		
		public boolean isExited(){
			return exited;
		}
		public AutoThread()
		{

		}
		
		public
		void stopRun(){
			running = false;
		}
		
		public boolean isRunning()
		{
			return running;
		}

		@Override
		public void run()
		{
			running = true;
			while (running)
			{
				if( !running )
					break;
				try
				{
					
					h.sendEmptyMessage(0);
					Thread.sleep(timespace);
				} catch (Exception e)
				{

				}
			}
			exited = true;
		}

	}
}