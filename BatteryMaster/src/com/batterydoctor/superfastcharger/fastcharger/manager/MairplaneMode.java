package com.batterydoctor.superfastcharger.fastcharger.manager;

import com.batterydoctor.superfastcharger.fastcharger.R;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.System;

public class MairplaneMode extends AbsManager {

	private static MairplaneMode manager;
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			getState();
			update();
		}
	};
	ContentResolver contentResolver;

	private MairplaneMode(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		contentResolver = this.mContext.getContentResolver();
	}

	public static MairplaneMode getInstance(Context context) {
		if (manager == null)
			manager = new MairplaneMode(context);
		return manager;
	}

	@Override
	public void setImanager(IManager imanager) {
		// TODO Auto-generated method stub
		super.setImanager(imanager);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		this.mContext.registerReceiver(receiver, intentFilter);
		getState();
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
		if (Build.VERSION.SDK_INT < 17) {
			int in = System.getInt(contentResolver, System.AIRPLANE_MODE_ON, 0);
			if (in != 0) {
				this.b = true;
				this.i = R.drawable.settings_app_airplane_on;
			} else {
				this.i = R.drawable.settings_app_airplane_off;
				this.b = false;
			}
		}else{
			this.i = R.drawable.settings_app_airplane_on;
			this.b = false;
		}
		return this.b;
	}

	@Override
	public void setState(boolean isEnable,boolean userChangeMode) {
		// TODO Auto-generated method stub
		int aa = 0;
		if (isEnable) {
			aa = 1;
		}
		if (Build.VERSION.SDK_INT < 17) {
			this.b = isEnable;
			this.isUserChageMode = userChangeMode;
			System.putInt(contentResolver, System.AIRPLANE_MODE_ON, aa);
			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			intent.putExtra("state", isEnable);
			isUserChageMode = userChangeMode;
			this.mContext.sendBroadcast(intent);
		}
	}

	@Override
	public void setLongClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
		try {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.mContext.startActivity(intent);
		} catch (Exception e) {
			intent.setAction(Settings.ACTION_SETTINGS);
			this.mContext.startActivity(intent);
		}
	}

}
