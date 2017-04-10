package com.nvn.data.pref;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.nvn.log.LogBuider;

public class ModePref {
	private static final String TAG = "ModePref";
	public static final String MODE_PREF = "mode_pref"+"mode_pref".hashCode();
	public static final String NAME = "name";
//	public static final String NUMBER = "number"+"number".hashCode();
	public static final String MODEID = "modeId";
	public static final int SCREEN_TIMEOUT = 1;
	public static final int WIFI = 3;
	public static final int BLUETOOTH = 4;
	public static final int MOBILE_DATA = 5;
	public static final int GPS = 7;
	public static final int SCREEN_BRIGHTNESS = 0;
	public static final int ARIPLANE = 6;
	public static final int SOUND = 2;
	Context mContext;
	private static SharedPreferences sharedPreferences;
	private static ModePref modePref;

	private ModePref(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context.getApplicationContext();
		if(sharedPreferences!=null)
			return;
		sharedPreferences = context.getSharedPreferences(MODE_PREF, Context.MODE_PRIVATE);
	}
	public static ModePref initilaze(Context context){
		if(modePref ==null)
			modePref = new ModePref(context);
		LogBuider.e(TAG, "initilaze ModePref");
		return modePref;
	}
	public static Map<String,?> getAllString(){
		return sharedPreferences.getAll();
	}
	public static void putBoolean(int paramInt1, int paramInt2, boolean paramBoolean) {
		putBoolean(sharedPreferences.edit(), paramInt1, paramInt2, paramBoolean);
	}
	private  static void putBoolean(SharedPreferences.Editor editor,int paramInt1, int paramInt2, boolean paramBoolean) {
		editor.putBoolean(getStringItemMode(paramInt1, paramInt2), paramBoolean).commit();
	}
	public static void putString(String key, String string) {
		sharedPreferences.edit().putString(key, string).commit();
	}
	/**
	 * 
	 * @param paramInt1
	 *            vi tri cua mode
	 * @param paramInt2
	 *            kieu can put
	 * @param string
	 *            : value
	 */
	public static void putString(int paramInt1, int paramInt2, String string) {
		putString(sharedPreferences.edit(), paramInt1, paramInt2, string);
	}
	public static void putInt(String key,int value){
		sharedPreferences.edit().putInt(key, value).commit();
	}
	/**
	 * 
	 * @param paramInt1
	 * @param paramInt2
	 * @param string
	 */
	public static void putInt(int paramInt1, int paramInt2, int paramInt3) {
		putInt(sharedPreferences.edit(), paramInt1, paramInt2, paramInt3);
	}
	private static void putInt(SharedPreferences.Editor editor,int paramInt1, int paramInt2, int paramInt3) {
		editor.putInt(getStringItemMode(paramInt1, paramInt2), paramInt3).commit();
	}
	private static void putString(SharedPreferences.Editor editor, int paramInt1,
			int paramInt2, String string) {
		editor.putString(getStringItemMode(paramInt1, paramInt2), string).commit();
	}
	public static void remove(String key) {
		sharedPreferences.edit().remove(key).commit();
	}
	/**
	 * 
	 * @param paramInt1
	 *            : vi tri cua mode
	 * @param paramInt2
	 *            : kieu can remove
	 */
	public static void remove(int paramInt1, int paramInt2) {
		remove(sharedPreferences.edit(), paramInt1, paramInt2);
	}

	private static void remove(SharedPreferences.Editor editor, int paramInt1,
			int paramInt2) {
		editor.remove(getStringItemMode(paramInt1, paramInt2)).commit();
	}
	public static int getInt(int paramInt1,int paramInt2){
		return sharedPreferences.getInt(getStringItemMode(paramInt1, paramInt2), -1);
	}
	public static boolean getBoolean(int paramInt1,int paramInt2){
		return sharedPreferences.getBoolean(getStringItemMode(paramInt1, paramInt2), false);
	}
	public static String getString (int paramInt1,int paramInt2){
		return sharedPreferences.getString(getStringItemMode(paramInt1, paramInt2), "");
	}
	public static String getString (String key){
		return sharedPreferences.getString(key,"");
	}
	private static String getStringItemMode(int paramInt1, int paramInt2) {
		String str = "";
		switch (paramInt2) {
		case SCREEN_BRIGHTNESS:
			str = "mode" + paramInt1 + "-brightness";
			break;
		case SCREEN_TIMEOUT:
			str = "mode" + paramInt1 + "-screen_timeout";
			break;
		case WIFI:
			str = "mode" + paramInt1 + "-wifi";
			break;
		case BLUETOOTH:
			str = "mode" + paramInt1 + "-bluetooth";
			break;
		case MOBILE_DATA:
			str = "mode" + paramInt1 + "-mobile_data";
			break;
		case GPS:
			str = "mode" + paramInt1 + "-gps";
			break;
		case ARIPLANE:
			str = "mode" + paramInt1 + "-airplane";
			break;
		case SOUND:
			str = "mode" + paramInt1 + "-sound";
			break;
		}
		return str;
	}
}
