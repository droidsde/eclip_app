package com.security.virusscanner.antivirus.receiver;

import com.security.virusscanner.antivirus.free.DataLite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class RMPReceiver extends BroadcastReceiver {

	private DataLite db;

	@Override
	public void onReceive(Context context, Intent paramIntent) {

		String action = paramIntent.getAction();

		if (action.equals("com.security.virusscanner.antivirus.remove_package")) {
			String packageName = paramIntent.getStringExtra("package_name");
			db = new DataLite(context);
			db.threatDeleted(packageName);

			int count = db.getInfectionsCount();

			if (count < 1) {
				SharedPreferences settings;
				String PREFS_NAME = "VX";
				settings = context.getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor edit = settings.edit();
				edit.putBoolean("STAT", true);
				edit.commit();
			}

			db.close();
		}

	}

}
