package com.novery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novery.activity.FragmentDegugParameters.MyAdapter;
import com.novery.activity.FragmentDegugParameters.ViewHolder;
import com.novery.icivilphone.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class ActivityParameters extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_parameters);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_parameters, menu);
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
		private List<Map<String, Object>> mData;
		private List<Map<String, MyListener>>  mlstListener;
		private int flag;
		public static String title[] = new String[] 
				{ "��վ����", "���Ļ�վ���", "����վIP", "APN","�豸���", "MSG����", "�ڵ�����", "RFƵ��", "ͨ������", "����Ƶ��","����Ƶ��01","����Ƶ��02","����Ƶ��03","����Ƶ��04","����Ƶ��05","����Ƶ��06","����Ƶ��07","����Ƶ��08","����Ƶ��09","����Ƶ��10" };
		public static String info[] = new String[] 
				{"STATION50", "20", "182.92.232.25", "4G","50","18212345678", "16", "9000", "8888", "30", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
		
		private View rootView;
		private Context  mContext;
		public ListView mlistView;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_activity_debug_parameters, null);
			rootView = view;
			mContext = container.getContext();
			mlstListener = new  ArrayList<Map<String,MyListener>> ();
			
			mData = getData();
			ListView listView = (ListView) rootView.findViewById(R.id.listView);
			mlistView = listView;
			MyAdapter adapter = new MyAdapter( mContext );
			listView.setAdapter(adapter);
			
			
		return view;
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		        inflater.inflate(R.menu.fragment_debug_parameters, menu);
		        super.onCreateOptionsMenu(menu,inflater);
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

	// ��ȡ��̬�������� �����������ط�����(json��)
	private List<Map<String, Object>> getData() {
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
		// ****************************************�ڶ��ַ���������һ�㶼�ô��ַ���,����ԭ���һ������,�д��о�
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			MyListener myListener = null;
			if (convertView == null) {

				holder = new ViewHolder();

				// �������Ϊ��vlist��ȡview ֮���view���ظ�ListView
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

			holder.title.setText((String) mData.get(position).get("title"));
			holder.info.setText((String) mData.get(position).get("info"));
			holder.viewBtn.setTag(position);
			// ��Button��ӵ����¼� ���Button֮��ListView��ʧȥ���� ��Ҫ��ֱ�Ӱ�Button�Ľ���ȥ��
			holder.viewBtn.setOnClickListener(myListener);

			// holder.viewBtn.setOnClickListener(MyListener(position));

			return convertView;
		}
	}


	private class MyListener implements OnClickListener {
		int mPosition;

		public MyListener(int inPosition) {
			mPosition = inPosition;
			ListView listView = (ListView) rootView.findViewById(R.id.listView);
			listView.setSelected(true);
			listView.setSelection(mPosition);
		}
		
		

		@Override
		public void onClick(View v) {
			
			//Toast.makeText( mContext, title[mPosition],Toast.LENGTH_SHORT).show();
			//showInfo( mPosition);
			
			
			LayoutInflater inflater = LayoutInflater.from( mContext);
			final View viewParameter = inflater.inflate(R.layout.fragment_edit_parameter,
					null);
			TextView textparameterName = (TextView) viewParameter.findViewById(R.id.textParameterName);
			TextView textparameter = (TextView) viewParameter.findViewById(R.id.editParameterValue);
			
			textparameterName.setText( title[ mPosition ]);
			textparameter.setText( info[mPosition] );		
			new AlertDialog.Builder( mContext )
			.setTitle( "You click me" )
			.setView(viewParameter)
			.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					
					TextView textparameter = (TextView) viewParameter.findViewById(R.id.editParameterValue);
					
					String newValue = textparameter.getText().toString();
					info[mPosition] = newValue;
					MyAdapter adp = (MyAdapter) mlistView.getAdapter();
					adp.notifyDataSetChanged();
				}})
			.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
						}
					}).show();
					
					
		}

	}

	// ��ȡ���������
	public final class ViewHolder {
		public TextView title;
		public TextView info;
		public Button viewBtn;
	}

	public void showInfo(int position) {

		ImageView img = new ImageView( mContext );
		img.setImageResource(R.drawable.b);
		
		new AlertDialog.Builder( mContext )
				.setView(img)
				.setTitle("����" + position)
				.setMessage("ͨ����" + title[position] + "   ����:" + info[position])
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	}

}
