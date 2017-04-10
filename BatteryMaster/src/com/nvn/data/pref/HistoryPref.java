package com.nvn.data.pref;

import java.util.Calendar;

import com.nvn.log.LogBuider;

import android.content.Context;
import android.content.SharedPreferences;

public class HistoryPref {
	private static final String TAG = "HistoryPref";
	public static final int NUMBER_POINT_IN_PER_4_HOUR = 4; // 60 minute 1 điểm
	
	
	public static final String HISTORY_PREF = "history_info"+"history_info".hashCode();
	public static final int DEFAULT_LEVEL = -1;
	public static void putLevel(Context context,int level){
		Calendar calendar = Calendar.getInstance();
		int minute = calendar.get(Calendar.MINUTE);
		if(minute>3 && minute < 57)
			return;
		if( minute>=57)
			calendar.add(Calendar.HOUR_OF_DAY, 1);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		SharedPreferences sharedPreferences = context.getSharedPreferences(HISTORY_PREF, Context.MODE_PRIVATE);
		if(sharedPreferences.contains(getKeyFromTime(day, hour))){
			return;
		}
		putLevel(context,day,hour , level);
	}
	public static void putLevel(Context context,int date,int hour,int level){
		SharedPreferences.Editor editor = context.getSharedPreferences(HISTORY_PREF,Context.MODE_PRIVATE).edit();
		editor.putInt("bat_time_" + String.valueOf(date) + "_" + String.valueOf(hour), level);
		editor.commit();
	}
	public static int getLevel(Context context,String key){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HISTORY_PREF, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, DEFAULT_LEVEL);
	}
	public static void removeLevel(Context context,String key){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HISTORY_PREF, Context.MODE_PRIVATE);
		sharedPreferences.edit().remove(key).commit();
	}
	public static String getKeyFromTime(int date,int hour){
		String string = "bat_time_" + String.valueOf(date) + "_"+ String.valueOf(hour);
		return string;
	}
}
