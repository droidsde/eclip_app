package com.nvn.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class BatteryPref {
	Context mContext;
	public static final String BATTERY_PREF = "battery_info"
			+ "battery_info".hashCode();
	public static final String EXTRA_LEVEL = "level";
	public static final String EXTRA_TIME_REMAIN = "timeremainning";
	public static final String EXTRA_TIME_CHARGING_AC = "timecharging_ac";
	public static final String EXTRA_TIME_CHARGING_USB = "timecharging_usb";
	public static final String EXTRA_CURENT_TIME = "curenttime";
	/**
	 * thời gian còn lại mặc định cho 1 level
	 */
	public static final long TIME_REMAIN_DEFAULT = 1000 * 3600 * 24 / 100; // 24h - 100%
	public static final long TIME_CHARGING_AC_DEFAULT = 1000 * 3600 * 2 / 100; // 2h - 100%
	public static final long TIME_CHARGING_USB_DEFAULT = 1000 * 3600 * 4 / 100; // 4h - 100%
	
	public static final long TIME_REMAIN_MIN = 1000 * 3600 * 6 / 100; // 6h - 100%
	public static final long TIME_CHARGING_AC_MIN = 1000 * 3600 * 1 / 100; // 1h - 100%
	public static final long TIME_CHARGING_USB_MIN = 1000 * 3600 * 2 / 100; // 2h - 100%
	
	public static final long TIME_REMAIN_MAX = 1000 * 3600 * 60 / 100; // 60h - 100%
	public static final long TIME_CHARGING_AC_MAX = 1000 * 3600 * 5 / 100; // 5h - 100%
	public static final long TIME_CHARGING_USB_MAX = 1000 * 3600 * 10 / 100; // 10h - 100%
	
	
	private static BatteryPref batteryPref;

	private BatteryPref(Context context) {
		mContext = context;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				BATTERY_PREF, Context.MODE_PRIVATE);
		if (!sharedPreferences.contains(EXTRA_TIME_REMAIN) || !sharedPreferences.contains(EXTRA_CURENT_TIME)
				|| ! sharedPreferences.contains(EXTRA_TIME_CHARGING_AC)
				|| ! sharedPreferences.contains(EXTRA_TIME_CHARGING_USB)) {
			sharedPreferences.edit().putLong(EXTRA_TIME_REMAIN, TIME_REMAIN_DEFAULT).commit();
			sharedPreferences.edit().putLong(EXTRA_CURENT_TIME, System.currentTimeMillis()).commit();
			sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_AC, TIME_CHARGING_AC_DEFAULT).commit();
			sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_USB, TIME_CHARGING_USB_DEFAULT).commit();
		}

	}

	public static BatteryPref initilaze(Context context) {
		if (batteryPref == null)
			batteryPref = new BatteryPref(context);
		return batteryPref;
	}
	public static void putLevel(Context context,int level){
		SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);
		if (level != getLevel(context)) {
			sharedPreferences.edit().putInt(EXTRA_LEVEL, level).commit();
		}
	}
	/**
	 * 
	 * @param context
	 * @param level
	 * @return time remain by minute
	 */
	public static int getTimeRemainning(Context context, int level) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);
		int time = (int) (level * sharedPreferences.getLong(EXTRA_TIME_REMAIN,TIME_REMAIN_DEFAULT) / (1000 * 60));
		if (level < getLevel(context)) {
			sharedPreferences.edit().putInt(EXTRA_LEVEL, level).commit();

			long timeRemain = System.currentTimeMillis()- sharedPreferences.getLong(EXTRA_CURENT_TIME,	System.currentTimeMillis());
			if(timeRemain > TIME_REMAIN_MIN){
				if(timeRemain < TIME_REMAIN_MAX){
					sharedPreferences.edit().putLong(EXTRA_TIME_REMAIN, timeRemain).commit();
				}else{
					sharedPreferences.edit().putLong(EXTRA_TIME_REMAIN, timeRemain/2).commit();
				}
			}
			sharedPreferences.edit().putLong(EXTRA_CURENT_TIME, System.currentTimeMillis()).commit();
		}
		
		return time;
	}
	
	public static int getTimeChargingUsb(Context context,int level){
		 SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);

		 int time = (int) ((100-level) * sharedPreferences.getLong(EXTRA_TIME_CHARGING_USB,TIME_CHARGING_USB_DEFAULT) / (1000 * 60));
		 if(level>getLevel(context)){
			 sharedPreferences.edit().putInt(EXTRA_LEVEL, level).commit();
			 
			 long timeRemain = System.currentTimeMillis()- sharedPreferences.getLong(EXTRA_CURENT_TIME,	System.currentTimeMillis());
			 if (timeRemain <TIME_CHARGING_USB_MAX && timeRemain > TIME_CHARGING_USB_MIN) {
				sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_USB, timeRemain).commit();
			 	}
				sharedPreferences.edit().putLong(EXTRA_CURENT_TIME, System.currentTimeMillis()).commit();
		 }
		 return time;
	 }
	
	
	 public static int getTimeChargingAc(Context context,int level){
		 SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);

		 int time = (int) ((100-level) * sharedPreferences.getLong(EXTRA_TIME_CHARGING_AC,TIME_CHARGING_AC_DEFAULT) / (1000 * 60));
		 
		 if(level>getLevel(context)){
			 sharedPreferences.edit().putInt(EXTRA_LEVEL, level).commit();
			 
			 long timeRemain = System.currentTimeMillis()- sharedPreferences.getLong(EXTRA_CURENT_TIME,	System.currentTimeMillis());
			 if (timeRemain <TIME_CHARGING_AC_MAX && timeRemain >TIME_CHARGING_AC_MIN) {
				sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_AC, timeRemain).commit();
			 	}
				sharedPreferences.edit().putLong(EXTRA_CURENT_TIME, System.currentTimeMillis()).commit();
		 }
		 return time;
	 }

	public static int getLevel(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				BATTERY_PREF, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(EXTRA_LEVEL, -1);
	}
}
