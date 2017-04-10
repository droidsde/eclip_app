package com.batterydoctor.superfastcharger.fastcharger;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.batterydoctor.superfastcharger.fastcharger.notifycation.NotifycationHome;
import com.batterydoctor.superfastcharger.fastcharger.service.AppService;
import com.nvn.data.pref.BatteryPref;
import com.nvn.data.pref.ModePref;


public class PowerManagerApplication extends Application {
	private static PowerManagerApplication application = null;
	private static boolean b;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		application = this;
		ModePref.initilaze(this);
		BatteryPref.initilaze(this);
		startService(new Intent(this, AppService.class));
	}
	public static PowerManagerApplication getApplication() {
		return application;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
