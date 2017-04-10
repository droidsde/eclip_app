package com.batterydoctor.superfastcharger.fastcharger.fastercharger;

import java.lang.reflect.Method;
import java.util.Hashtable;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Constant {
	public static BatteryData btInfo_Data = new BatteryData();
	public static Hashtable<String, String> bat_plugged_hashTable = new Hashtable<String, String>();
	public static int infocounter = 0;
	public static int devicesize_flag;
	public static int[] timevalues = new int[] { 5, 10, 15, 30, 45, 1, 2, 5,
			10, 15, 20, 25, 30 };
	public static boolean advancebrighness;
	public static String[] lowvalues = new String[] { "5 sec", "10 sec",
			"15 sec", "20 sec", "25 sec", "30 sec" };

	public static int[] lowvalues_int = new int[] { 5, 10, 15, 20, 25, 30 };

	public static String[] values = new String[] { "5 sec", "10 sec", "15 sec",
			"30 sec", "45 sec", "1 min", "2 min", "5 min", "10 min", "15 min",
			"20 min", "25 min", "30 min" };

	public static boolean mobiledata_enable_disable(Context context) {
		boolean mobileDataEnabled = false; // Assume disabled
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {

			Class cmClass = Class.forName(cm.getClass().getName());
			Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
			method.setAccessible(true); // Make the method callable
			// get the setting for "mobile data"
			mobileDataEnabled = (Boolean) method.invoke(cm);

			Log.e("mobileDataEnabled", mobileDataEnabled + "");
		} catch (Exception e) {
			// Some problem accessible private API
			// TODO do whatever error handling you want here
		}
		return mobileDataEnabled;
	}

	public static boolean turnOnDataConnection(boolean ON, Context context) {

		try {

			int bv = Build.VERSION.SDK_INT;

			if (bv == Build.VERSION_CODES.FROYO)

			{
				Method dataConnSwitchmethod;
				Class<?> telephonyManagerClass;
				Object ITelephonyStub;
				Class<?> ITelephonyClass;

				TelephonyManager telephonyManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);

				telephonyManagerClass = Class.forName(telephonyManager
						.getClass().getName());
				Method getITelephonyMethod = telephonyManagerClass
						.getDeclaredMethod("getITelephony");
				getITelephonyMethod.setAccessible(true);
				ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
				ITelephonyClass = Class.forName(ITelephonyStub.getClass()
						.getName());

				if (ON) {
					dataConnSwitchmethod = ITelephonyClass
							.getDeclaredMethod("enableDataConnectivity");
				} else {
					dataConnSwitchmethod = ITelephonyClass
							.getDeclaredMethod("disableDataConnectivity");
				}
				dataConnSwitchmethod.setAccessible(true);
				dataConnSwitchmethod.invoke(ITelephonyStub);

			} else {
				// log.i("App running on Ginger bread+");
				final ConnectivityManager conman = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				final Class<?> conmanClass = Class.forName(conman.getClass()
						.getName());
				final java.lang.reflect.Field iConnectivityManagerField = conmanClass
						.getDeclaredField("mService");
				iConnectivityManagerField.setAccessible(true);
				final Object iConnectivityManager = iConnectivityManagerField
						.get(conman);
				final Class<?> iConnectivityManagerClass = Class
						.forName(iConnectivityManager.getClass().getName());

				final Method setMobileDataEnabledMethod = iConnectivityManagerClass
						.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);

				setMobileDataEnabledMethod.setAccessible(true);
				setMobileDataEnabledMethod.invoke(iConnectivityManager, ON);
			}

			return true;
		} catch (Exception e) {

			Log.e("error turning on/off data", "error turning on/off data");

			return false;
		}

	}
}