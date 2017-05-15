package com.usstudio.ringingflashlight.flashalerts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class ReceiverSMS extends BroadcastReceiver {

	private static final String TAG = ReceiverSMS.class.getSimpleName();
	private FlashlightCaller callerFlashlight;

	@Override
	public void onReceive(Context context, Intent intent) {
		callerFlashlight = (FlashlightCaller) context.getApplicationContext();
		if (FlashLight.getRunning() < 1 && callerFlashlight.isMsgFlash() && callerFlashlight.isEnabled()) {
			new ManageFlash().execute(callerFlashlight.getMsgFlashOnDuration(), callerFlashlight.getMsgFlashOffDuration(),
					callerFlashlight.getMsgFlashDuration());
		}
	}

	public class ManageFlash extends AsyncTask<Integer, Integer, String> {


		private FlashLight flash = new FlashLight(callerFlashlight);

		public ManageFlash() {
			FlashLight.incRunning();
		}


		@Override
		protected String doInBackground(Integer... integers) {
			if (FlashlightCaller.LOG) Log.d(TAG, "doInBackgroung Started");
			long start = System.currentTimeMillis();
			int tries = 3;
			if (callerFlashlight.getMsgFlashType() == 1) {
				int durMillis = integers[2] * 1000;
				while (System.currentTimeMillis() - start <= durMillis && tries > 0) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
					if (!FlashLight.gotCam) {
						if (FlashlightCaller.LOG) Log.d(TAG, "Flash failed, retrying..." + tries);
						tries = tries - 1;
					}
				}
			} else if (callerFlashlight.getMsgFlashType() == 2) {
				int times = 0;
				int repeats = integers[2];
				while (times < repeats && tries > 0) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
					if (!FlashLight.gotCam) {
						if (FlashlightCaller.LOG) Log.d(TAG, "Flash failed, retrying..." + tries);
						tries = tries - 1;
					} else {
						times = times + 1;
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if (FlashlightCaller.LOG) Log.d(TAG, "onPostExecute Started");
			FlashLight.decRunning();
			if (FlashLight.getRunning() == 0) FlashLight.releaseCam();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			if (FlashlightCaller.LOG) Log.d(TAG, "onCancelled Started");
			FlashLight.decRunning();
			if (FlashLight.getRunning() == 0) FlashLight.releaseCam();
		}


	}

}
