package com.batterydoctor.superfastcharger.fastcharger.apps;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppsPackageName {
	private static AppsPackageName mPackageName = null;
	PackageManager mPackageManager;
	ActivityManager mActivityManager;

	private AppsPackageName(Context context) {
		// TODO Auto-generated constructor stub
		mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		mPackageManager = context.getPackageManager();
	}
	public ActivityManager getActivityManager(){
		return mActivityManager;
	}
	public static AppsPackageName getApps(Context context) {
		if (mPackageName == null) {
			mPackageName = new AppsPackageName(context);
			return mPackageName;
		} else {
			return mPackageName;
		}
	}
	public List<RunningServiceInfo> getServicesRunning(){
		return mActivityManager.getRunningServices(300);
	}
	public List<RunningAppProcessInfo> getAppsRunning(){
		return mActivityManager.getRunningAppProcesses();
	}
	public List<PackageInfo> getPackageInfo(){
		return mPackageManager.getInstalledPackages(PackageManager.GET_DISABLED_COMPONENTS);
	}
}
