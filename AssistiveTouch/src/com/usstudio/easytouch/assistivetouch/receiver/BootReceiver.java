package com.usstudio.easytouch.assistivetouch.receiver;

import com.usstudio.easytouch.assistivetouch.service.AssistiveTouchService;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if (SharedPreference.readSwitch(context)) {
			Intent i = new Intent(context, AssistiveTouchService.class);
			context.startService(i);
		}
	}

}