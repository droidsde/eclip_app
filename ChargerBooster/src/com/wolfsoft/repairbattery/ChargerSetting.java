package com.wolfsoft.repairbattery;

import android.content.Context;

public class ChargerSetting extends WSettings {
	
	public static String ENABLE="Enable";
	public static String CHARGER="Charger";
	public static String ENABLESOUND="EnableSound";
	public static String TIMEUP="Timeup";
	public static String STTBT="SttBT";
	public static String STTWF="SttWF";
	public static String STTGPS="STTGPS";
	public static String CBSOUND="cbsound";
	public static String CBFULL="cbfull";
	public static String TIMEOUT="Timeout";
	public static String AUTOSCREEN="AutoScreen";
	public static String STTDECBRIGHT="SttDecBright";
	public static String STTKILLPROCESS="SttKillProcess";
	
	
	
	
	public static void saveEnable(Context context, boolean check){
		save(context, ENABLE, check);
	}
	public static boolean readEnable(Context context){
		return read(context, ENABLE, false);
	}
	public static void saveCharger(Context context, boolean check){
		save(context, CHARGER, check);
	}
	public static boolean readCharger(Context context){
		return read(context, CHARGER, true);
	}
	public static void saveEnableSound(Context context, boolean check){
		save(context, ENABLESOUND, check);
	}
	public static boolean readEnableSound(Context context){
		return read(context, ENABLESOUND, true);
	}
	public static void saveTimeup(Context context, boolean check){
		save(context, TIMEUP, check);
	}
	public static boolean readTimeup(Context context){
		return read(context, TIMEUP, false);
	}
	
	//---------------
	public static void saveSttBL(Context context, boolean check){
		save(context, STTBT, check);
	}
	public static boolean readSttBL(Context context){
		return read(context, STTBT, true);
	}
	public static void saveSttWF(Context context, boolean check){
		save(context, STTWF, check);
	}
	public static boolean readSttWF(Context context){
		return read(context, STTWF, true);
	}
	public static void saveSttGPS(Context context, boolean check){
		save(context, STTGPS, check);
	}
	public static boolean readSttGPS(Context context){
		return read(context, STTGPS, true);
	}
	
	public static void saveFull(Context context, boolean check){
		save(context, CBFULL, check);
	}
	public static boolean readFull(Context context){
		return read(context, CBFULL, true);
	}
	public static void saveTimeOut(Context context, int check){
		save(context, TIMEOUT, check);
	}
	public static int readTimeOut(Context context){
		return read(context, TIMEOUT, 3000);
	}
	public static void saveAuto(Context context, int check){
		save(context, AUTOSCREEN, check);
	}
	public static int readAuto(Context context){
		return read(context, AUTOSCREEN, 1);
	}

	public static void saveSttDecBright(Context context, boolean check){
		save(context, STTDECBRIGHT, check);
	}
	public static boolean readSttDecBright(Context context){
		return read(context, STTDECBRIGHT, true);
	}
	public static void saveSttKillProcess(Context context, boolean check){
		save(context, STTKILLPROCESS, check);
	}
	public static boolean readSttKillProcess(Context context){
		return read(context, STTKILLPROCESS, true);
	}
}
