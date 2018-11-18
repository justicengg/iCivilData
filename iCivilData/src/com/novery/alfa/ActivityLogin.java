package com.novery.alfa;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.novery.base.AppConf;
import com.novery.icivilphone.R;
import com.novery.rest.RestApiLoginRunnable;
import com.novery.stack.NovRestLoginInfo;
import com.novery.stack.NovUserInfo;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLogin extends Activity {

	private NotifyReceiver receiverNotify = null;
	private Intent intentNotify = null;
	private Context mContext ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		mContext = this.getApplicationContext();
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		AppConf.init();
		InitBroadcast();
		
		
	}


	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}*/

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
	private void InitBroadcast()
	{	
		receiverNotify = new NotifyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.novery.rest.login");
		intentNotify = registerReceiver(receiverNotify, filter);
	}
	public void unregisterService() {
		if (intentNotify != null) {
			unregisterReceiver(receiverNotify);
		}
		NovRestLoginInfo info = NovUserInfo.getInstance().getLogin();
		try{
			final Intent intentHome = new Intent( this, ActivityAlfa.class);
			intentHome.putExtra("USER_INFO_USERID",info.getUserID());
			intentHome.putExtra("USER_INFO_USERID",info.getUserID());
			intentHome.putExtra("USER_INFO_CLIENTID",info.getClientID());
			intentHome.putExtra("USER_INFO_CLIENTNAME",info.getClientName());
	        startActivity(intentHome);
		}
		catch(Exception e){
			System.out.println( e.getMessage() );
		}
		this.finish();
		
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private EditText etUserName;
		private EditText etUserPwd;
		private ImageButton imgBtLogin;
		private Thread thdService;
		private View rootView ;

		private Context  mContext;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_login,
					container, false);
			
			mContext = container.getContext();
			InitActions();
			return rootView;
		}
		
		private void InitActions() {
			imgBtLogin = (ImageButton) rootView.findViewById( R.id.imgBtLogin );
			etUserName=(EditText)rootView.findViewById(R.id.et_login_user);
			etUserPwd=(EditText)rootView.findViewById(R.id.et_login_pwd);
			//etUserName.setText("dc007");
			//etUserPwd.setText("010007");
			etUserName.setText( String.valueOf( AppConf.strFtpUsername));
			etUserPwd.setText( String.valueOf( AppConf.strFtpUserpassword));
			
			imgBtLogin.setOnClickListener( new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					NovRestLoginInfo login = new NovRestLoginInfo();
					String strUserName = etUserName.getText().toString();
					String strUserPwd = etUserPwd.getText().toString();
					
					 Pattern pattern = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*");
					 Matcher matcher = pattern.matcher( strUserName );
					 boolean b= matcher.matches();
					if( !b ){
						Toast.makeText(mContext, "用户名或密码错误", 0).show();
						return ;
					}
					AppConf.strFtpUsername = strUserName;
					AppConf.strFtpUserpassword = strUserPwd;
					try {
						AppConf.update();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					RestApiLoginRunnable runRest = new RestApiLoginRunnable( rootView.getContext(), strUserName,strUserPwd );
					thdService = new Thread( runRest );
					thdService.start();
					
				}
			});
		}
		
	}
	private class NotifyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();			
			String strmsg = bundle.getString("MSG_LOGIN_RESULT");
			if( strmsg != null && strmsg.equalsIgnoreCase("OK")) {				
				unregisterService();
			}
			else{
				 Toast.makeText(mContext, "用户名或密码错误", 0).show();
			}

		}
	}


}
