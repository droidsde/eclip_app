package com.batterydoctor.superfastcharger.fastcharger.fastercharger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

public class ActionSeting {

	public static void killApps(Context paramContext) {
		List localList = paramContext.getPackageManager().getInstalledApplications(0);
		ActivityManager localActivityManager = (ActivityManager) paramContext.getSystemService("activity");
		Iterator localIterator = localList.iterator();
		while (true) {
			if (!localIterator.hasNext()) {
				return;
			}
			ApplicationInfo localApplicationInfo = (ApplicationInfo) localIterator.next();
			if (localApplicationInfo.packageName.equals(paramContext.getPackageName()))
				continue;
			localActivityManager.killBackgroundProcesses(localApplicationInfo.packageName);
		}
	}

	private static void setMobileDataEnabled(Context context, boolean enabled) {
		try {
			final ConnectivityManager conman = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final Class conmanClass = Class.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField.get(conman);
			final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);

			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void swwifi(Context mContext, boolean flagwifi) {
		((WifiManager) mContext.getSystemService("wifi")).setWifiEnabled(flagwifi);
	}

	public static void swBluethooth(Context mContext, boolean flagBlue) {
		BluetoothAdapter btEnable = BluetoothAdapter.getDefaultAdapter();
		if (btEnable != null) {
			if (flagBlue) {
				btEnable.enable();
			} else {
				btEnable.disable();
			}
		}
	}
}
