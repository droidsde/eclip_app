package com.batterydoctor.superfastcharger.fastcharger.manager;

import com.batterydoctor.superfastcharger.fastcharger.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

public class MwifiManager extends AbsManager {
	private static MwifiManager manager;
	WifiManager wifiManager;

	private MwifiManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		wifiManager = (WifiManager) this.mContext.getSystemService(Context.WIFI_SERVICE);
			
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if( arg1.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)|| 
					arg1.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)||
					arg1.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
				int a = wifiManager.getWifiState();
				if(a == WifiManager.WIFI_STATE_ENABLED || a == WifiManager.WIFI_STATE_ENABLING){
					b = true;
					i = R.drawable.settings_app_wifi_on;
				}else{
					b = false;
					i = R.drawable.settings_app_wifi_off;
				}
				update();
			}
		}
	};

	public static MwifiManager getInstance(Context context) {
		if (manager == null) {
			manager = new MwifiManager(context);
		}
		return manager;
	}

	@Override
	public void setImanager(IManager imanager) {
		// TODO Auto-generated method stub
		super.setImanager(imanager);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		this.mContext.registerReceiver(receiver, intentFilter);

		int a = wifiManager.getWifiState();
		if(a == WifiManager.WIFI_STATE_ENABLED || a == WifiManager.WIFI_STATE_ENABLING){
			this.b = true;
			this.i = R.drawable.settings_app_wifi_on;
		}else{
			this.b = false;
			this.i = R.drawable.settings_app_wifi_off;
		}
		update();
	}

	@Override
	public void removeImanager(IManager manager) {
		// TODO Auto-generated method stub
		super.removeImanager(manager);
		try {
			this.mContext.unregisterReceiver(receiver);
		} catch (Exception e) {

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
		this.isUserChageMode = userChangeMode;
		this.b = enable;
		wifiManager.setWifiEnabled(enable);
	}

	@Override
	public void setLongClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		try {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.mContext.startActivity(intent);
		} catch (Exception e) {
			intent.setAction(Settings.ACTION_SETTINGS);
			this.mContext.startActivity(intent);
		}
	}
}
