package com.usstudio.easytouch.assistivetouch.receiver;

import com.usstudio.easytouch.assistivetouch.util.Constants;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class RemoveReceiver extends BroadcastReceiver {

	private String[][] dataApp;

	@Override
	public void onReceive(Context context, Intent intent) {
		Uri app = intent.getData();
		String appName = app.toString();
		String packName = appName.substring(appName.lastIndexOf(":") + 1);
		dataApp = new String[9][3];
		for (int i = 0; i < dataApp.length; i++) {
			dataApp[i] = SharedPreference.readAppPage(context, i + 1).split(Constants.SPECIAL_CHAR);
			if (dataApp[i].length > 1) {
				if (dataApp[i][1].equals(packName)) {
					SharedPreference.saveAppPage(context, Constants.NEW, i + 1);
				}
			}
		}
	}

}
