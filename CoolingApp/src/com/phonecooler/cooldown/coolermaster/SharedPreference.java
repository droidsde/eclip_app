package com.phonecooler.cooldown.coolermaster;

import android.content.Context;
import com.mbapp.lib_tool.makemoney.WSettings;

public class SharedPreference extends WSettings{

	public static String Exit ="exit";
	public static String Cool ="cool";
	public static String Time="time";
	
	public static void saveExit(Context context, boolean exit){
		save(context, Exit, exit);
	}
	public static boolean readExit(Context context){
		return read(context, Exit, false);
	}
	
	public static void saveCool(Context context, boolean cool){
		save(context, Cool, cool);
	}
	public static boolean readCool(Context context){
		return read(context, Cool, false);
	}
	
	public static void saveTime(Context context, long time){
		saveLong(context, Time, time);
	}
	public static long readTime(Context context){
		return readLong(context, Time, 0l);
	}
}
