package com.batterydoctor.superfastcharger.fastcharger.fastercharger;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.batterydoctor.superfastcharger.fastcharger.PowerMgrTabActivity;
import com.batterydoctor.superfastcharger.fastcharger.R;

public class ChargeReceiver extends BroadcastReceiver {

	Ringtone r;
	Uri sound;
	static NotificationManager mNotificationManager;
	static NotificationCompat.Builder mBuilder;
	
	 final public static String STATUSSERVICE= "statusservice";
	 private static final String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
	 private static final int SCREEN_BRIGHTNESS_MODE_MANUAL = 0;
	 private static final int SCREEN_BRIGHTNESS_MODE_AUTOMATIC = 1;
	 int defTimeOut = 0;

	@Override
	public void onReceive(Context mContext, Intent mIntent) {
		// TODO Auto-generated method stub
		
		sound = RingtoneManager.getDefaultUri(2);
	    r = RingtoneManager.getRingtone(mContext, sound);
	   
	   
	    if (mIntent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")){
	    	ChargerSetting.saveCharger(mContext, false);
	    	if(ChargerSetting.readEnable(mContext)){
	    		mNotificationManager = (NotificationManager)mContext.getSystemService("notification");
	    		mNotificationManager.cancel(1);
	            if (ChargerSetting.readEnableSound(mContext))
	              r.play();
	         //   readStatusScreen(mContext);
	    	}
	    	
	    	mContext.stopService(new Intent(mContext, ChargingService.class));
	    	
	    }
	    if(mIntent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")){
	    	ChargerSetting.saveCharger(mContext, true);
	    	 if (ChargerSetting.readEnableSound(mContext))
	              r.play();
//	    	if(ChargerSetting.readEnable(mContext)){
	    	 if(ChargerSetting.readOPEN(mContext)){
	    		Intent intent1 = new Intent(mContext, PowerMgrTabActivity.class);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent1.putExtra("flagCharger", 1);
				mContext.startActivity(intent1);
	    	 }
	    		//killApps(mContext);
	    		//generatenotification2(mContext);
	    		//generatenotification(mContext);
	    	//	getSupperCharger();
	    		
	    	//	restartMode(c);
//	    		saveStatusScreen(mContext);
//	    		setStatusScreen(mContext);
	    	
	    //	}
	    	}
	    
	}
	public static void killApps(Context paramContext){
	    List localList = paramContext.getPackageManager().getInstalledApplications(0);
	    ActivityManager localActivityManager = (ActivityManager)paramContext.getSystemService("activity");
	    Iterator localIterator = localList.iterator();
	    while (true){
	      if (!localIterator.hasNext()){
	        return;
	      }
	      ApplicationInfo localApplicationInfo = (ApplicationInfo)localIterator.next();
	      if (localApplicationInfo.packageName.equals(paramContext.getPackageName()))
	        continue;
	      localActivityManager.killBackgroundProcesses(localApplicationInfo.packageName);
	    }
	  }

	  public static void generatenotification(Context mContext){
		 mNotificationManager = (NotificationManager)mContext.getSystemService("notification");
	    mBuilder = new NotificationCompat.Builder(mContext).setSmallIcon(R.drawable.ic_launcher).setContentTitle("Faster Charger").setContentText("Charging is being optimised");
	    Intent localIntent = new Intent(mContext, PowerMgrTabActivity.class);
	    PendingIntent localPendingIntent = PendingIntent.getActivity(mContext, 0, localIntent, 0);
	    mBuilder.setContentIntent(localPendingIntent);
	    mNotificationManager.notify(1, mBuilder.build());
	  }
	  public static void generatenotification2(Context mContext){
		mNotificationManager = (NotificationManager)mContext.getSystemService("notification");
	    mBuilder = new NotificationCompat.Builder(mContext).setSmallIcon(R.drawable.ic_launcher).setContentTitle("Faster Charger").setContentText("Speed up charging using Faster Charger");
	    Intent localIntent = new Intent(mContext, PowerMgrTabActivity.class);
	    localIntent.putExtra("flagCharger", 1);
	    PendingIntent localPendingIntent = PendingIntent.getActivity(mContext, 0, localIntent, 0);
	    mBuilder.setContentIntent(localPendingIntent);
	    mNotificationManager.notify(1, mBuilder.build());
	  }
	  public void getSupperCharger(){
		//	 ((WifiManager)c.getSystemService("wifi")).setWifiEnabled(false);
			 BluetoothAdapter btEnable = BluetoothAdapter.getDefaultAdapter();
			 btEnable.disable();
			 //gps
		//	 setMobileDataEnabled(c,false);
			
			 
		}
//		public  void setMobileDataEnabled(Context context, boolean enabled) {
//			try { 
//				final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//			    final Class conmanClass = Class.forName(conman.getClass().getName());
//			    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
//			    iConnectivityManagerField.setAccessible(true);
//			    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
//			    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
//			    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
//			    setMobileDataEnabledMethod.setAccessible(true);
//
//			    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
//		    }
//		    catch (Exception e)
//		    {
//		        e.printStackTrace();
//		    }     
//		}
//		 public static void restartMode(Context mContext){
//	    	 AlarmManager am=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
//	         Intent intent = new Intent(mContext, AlarmReceiver.class);
//	         intent.putExtra(STATUSSERVICE, Boolean.TRUE);
//	         PendingIntent pi = PendingIntent.getBroadcast(mContext, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//	         am.cancel(pi);
//	         am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000  , pi);
//	    }
//	    public static void cancelRestartMode(Context context){
//	        Intent intent = new Intent(context, AlarmReceiver.class);
//	        PendingIntent sender = PendingIntent.getBroadcast(context, 124, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//	        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//	        alarmManager.cancel(sender);
//	    }
	    public void saveStatusScreen(Context mContext){
			ChargerSetting.saveTimeOut(mContext, Settings.System.getInt(mContext.getContentResolver(), 
	                Settings.System.SCREEN_OFF_TIMEOUT, ChargerSetting.readTimeOut(mContext)));
			ChargerSetting.saveAuto(mContext, Settings.System.getInt(mContext.getContentResolver(),
	    			Settings.System.SCREEN_BRIGHTNESS_MODE, ChargerSetting.readAuto(mContext)));
		}
		public void readStatusScreen(Context mContext){
			Settings.System.putInt(mContext.getContentResolver(),
	    			android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE, ChargerSetting.readAuto(mContext));
			Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, ChargerSetting.readTimeOut(mContext));
		}
		public void setStatusScreen(Context mContext){
			Settings.System.putInt(mContext.getContentResolver(),
	    			android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE, 1);
			Settings.System.putInt( mContext.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 3000);
		}


}
