package com.batterydoctor.superfastcharger.fastcharger.manager;

import java.net.URI;

import com.batterydoctor.superfastcharger.fastcharger.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.System;

public class MgpsManager extends AbsManager {
	private static MgpsManager manager;
	ContentResolver contentResolver;
	ContentObserver contentObserver;

	private MgpsManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		contentResolver = this.mContext.getContentResolver();
		contentObserver = new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				getState();
				update();
			}
		};
	}

	public static MgpsManager getInstance(Context context) {
		if (manager == null)
			manager = new MgpsManager(context);
		return manager;
	}

	@Override
	public void setImanager(IManager imanager) {
		// TODO Auto-generated method stub
		super.setImanager(imanager);
		@SuppressWarnings("deprecation")
		Uri uri = Settings.Secure.getUriFor(System.LOCATION_PROVIDERS_ALLOWED);
		this.contentResolver
				.registerContentObserver(uri, true, contentObserver);
		getState();
		update();
	}

	@Override
	public void removeImanager(IManager manager) {
		// TODO Auto-generated method stub
		super.removeImanager(manager);
		try{
			this.contentResolver.unregisterContentObserver(contentObserver);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean getState() {
		// TODO Auto-generated method stub
		String string = System.getString(contentResolver,
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (string != null && string.contains("gps")) {
			this.b = true;
			this.i = R.drawable.settings_app_gps_on;
			return true;
		}
		this.b = false;
		this.i = R.drawable.settings_app_gps_off;
		return false;
	}

	@Override
	public void setState(boolean state, boolean userChangeMode) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		try {
			this.b = state;
			this.isUserChageMode = userChangeMode;
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.mContext.startActivity(intent);
		} catch (Exception e) {
			intent.setAction(Settings.ACTION_SETTINGS);
			this.mContext.startActivity(intent);
		}
		// b = state;
		// this.isUserChageMode = userChangeMode;
		// turnGPS(state);
	}

	private void turnGPS(boolean enable) {

		String provider = Settings.Secure.getString(contentResolver,
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		// String string = System.getString(contentResolver,
		// System.LOCATION_PROVIDERS_ALLOWED);

		if (provider.contains("gps") == enable) {
			return; // the GPS is already in the requested state
		}

		final Intent poke = new Intent();
		poke.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		poke.addCategory(Intent.CATEGORY_SELECTED_ALTERNATIVE);
		poke.setData(Uri.parse("3"));
		mContext.sendBroadcast(poke);
	}

	@Override
	public void setLongClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		try {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.mContext.startActivity(intent);
		} catch (Exception e) {
			intent.setAction(Settings.ACTION_SETTINGS);
			this.mContext.startActivity(intent);
		}
	}

}
