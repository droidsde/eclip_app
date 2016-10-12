package com.example.shorcut_launch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Myservice extends Service {

	Handler mHandler;
	Runnable mRunnable;
	ActivityManager mActivityManager;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mHandler = new Handler();
		mRunnable = new showAds();
		mHandler.removeCallbacks(mRunnable);
		mHandler.postDelayed(mRunnable, 2000);
	}
	public class showAds implements Runnable {

		@Override
		public void run() {
			Toast.makeText(getApplication(), "activePackage", 
					   Toast.LENGTH_SHORT).show();
			// TODO Auto-generated method stub
//			String[] activePackages;
//			  if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
//			    activePackages = getActivePackages();
//			  } else {
//			    activePackages = getActivePackagesCompat();
//			  }
//			  if (activePackages != null) {
//			    for (String activePackage : activePackages) {
//			    	Log.i("current_app",activePackage);
//				    Toast.makeText(getApplication(), activePackage, 
//								   Toast.LENGTH_LONG).show();
//			      
//			    }
//			  }
//			  mHandler.postDelayed(this, 1000);
			
			ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
	        List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
	        if(tasks != null){
	        	for(RunningAppProcessInfo task : tasks)
	        	{
	        		Toast.makeText(getApplication(), task.processName, 
							   Toast.LENGTH_LONG).show();
	        	}
	        }
			
			
			if(mHandler!=null){
				mHandler.removeCallbacks(mRunnable);
				mHandler.postDelayed(mRunnable, 2000);
			}
			
		}
		
		String[] getActivePackagesCompat() {
			  
			final List<ActivityManager.RunningTaskInfo> taskInfo = mActivityManager.getRunningTasks(1);
			  final ComponentName componentName = taskInfo.get(0).topActivity;
			  final String[] activePackages = new String[1];
			  activePackages[0] = componentName.getPackageName();
			  return activePackages;
			}

			String[] getActivePackages() {
			  final Set<String> activePackages = new HashSet<String>();
			final List<ActivityManager.RunningAppProcessInfo> processInfos = mActivityManager.getRunningAppProcesses();
			  for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
			    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
			      activePackages.addAll(Arrays.asList(processInfo.pkgList));
			    }
			  }
			  return activePackages.toArray(new String[activePackages.size()]);
			}
		
	}

}
