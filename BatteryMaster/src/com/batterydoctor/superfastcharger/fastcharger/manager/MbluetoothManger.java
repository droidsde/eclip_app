package com.batterydoctor.superfastcharger.fastcharger.manager;

import com.batterydoctor.superfastcharger.fastcharger.R;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.provider.Settings;

public class MbluetoothManger extends AbsManager{
	private static MbluetoothManger manger;
	BluetoothAdapter bluetoothAdapter;
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
	            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
	            switch (state) {
	            case BluetoothAdapter.STATE_OFF:
	            case BluetoothAdapter.STATE_TURNING_OFF:
	            	b = false;
	            	i = R.drawable.settings_app_bluetooth_off;
	                break;
	            case BluetoothAdapter.STATE_ON:
	            case BluetoothAdapter.STATE_TURNING_ON:
	            	b = true;
	            	i = R.drawable.settings_app_bluetooth_on;
	                break;
	            }
				update();
	        }
		}
	};
	private MbluetoothManger(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	public static MbluetoothManger getInstance(Context context){
		if(manger == null){
			manger = new MbluetoothManger(context);
		}
		return manger;
	}
	@Override
	public void setImanager(IManager imanager) {
		// TODO Auto-generated method stub
		super.setImanager(imanager);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		this.mContext.registerReceiver(receiver, intentFilter);
		
		if(bluetoothAdapter!=null){
			this.b = bluetoothAdapter.isEnabled();
			if(bluetoothAdapter.isEnabled()){
				this.i = R.drawable.settings_app_bluetooth_on;
			}else{
				this.i = R.drawable.settings_app_bluetooth_off;
			}
		}else{
			this.b = true;
			this.i = R.drawable.settings_app_bluetooth_on;
		}
		update();
	}
	@Override
	public void removeImanager(IManager manager) {
		// TODO Auto-generated method stub
		super.removeImanager(manager);
		try{
			this.mContext.unregisterReceiver(receiver);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public boolean getState() {
		// TODO Auto-generated method stub
		return this.b;
	}

	@Override
	public void setState(boolean enable, boolean userChangeMode) {
		// TODO Auto-generated method stub
		this.b = enable;
		this.isUserChageMode = userChangeMode;
		if(bluetoothAdapter!=null){
			if(enable){
				bluetoothAdapter.enable();
			}else{
				bluetoothAdapter.disable();
			}
		}
		
	}
	@Override
	public void setLongClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
		try {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.mContext.startActivity(intent);
		} catch (Exception e) {
			intent.setAction(Settings.ACTION_SETTINGS);
			this.mContext.startActivity(intent);
		}
	}

}
