package com.usstudio.ringingflashlight.flashalerts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;


public class MediaButtonReceiver extends BroadcastReceiver {

	private static final String TAG = MediaButtonReceiver.class.getSimpleName();
	private static int  times;


	public MediaButtonReceiver() {
		times = 0;


	}

	@Override
	public void onReceive(Context context, Intent intent) {
		FlashlightCaller callerFlashlight = (FlashlightCaller) context.getApplicationContext();
		try {

			if (FlashlightCaller.LOG) Log.d(TAG, "onReceived: " + intent.getAction());
			times += 1;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		if (FlashlightCaller.LOG) Log.d(TAG, "setVolumeButtonPressed, times: " + times);
		if (times > 2 && callerFlashlight.isVolumeButtonPref())
			callerFlashlight.setVolumeButtonPressed(true);
		//		}
	}
}
