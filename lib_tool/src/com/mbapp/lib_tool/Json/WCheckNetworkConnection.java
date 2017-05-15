package com.mbapp.lib_tool.Json;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class WCheckNetworkConnection {

	public static boolean isConnectionAvailable(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()
					&& netInfo.isConnectedOrConnecting()
					&& netInfo.isAvailable()) {
				return true;
			}
		}
		return false;
	}
}
