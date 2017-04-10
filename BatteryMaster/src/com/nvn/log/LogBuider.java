package com.nvn.log;

import android.util.Log;

public class LogBuider {
	private static boolean isLog = false;
	public static void v(String tag, String msg){
		if (isLog) {
			Log.v(tag, msg);
		}
	}
	public static void d(String tag, String msg){
		if (isLog) {
			Log.d(tag, msg);
		}
	}
	public static void e(String tag, String msg){
		if (isLog) {
			Log.e(tag, msg);
		}
	}
	public static void i(String tag, String msg){
		if (isLog) {
			Log.i(tag, msg);
		}
	}
}
