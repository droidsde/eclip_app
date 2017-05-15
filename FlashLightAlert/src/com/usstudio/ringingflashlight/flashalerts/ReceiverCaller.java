package com.usstudio.ringingflashlight.flashalerts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class ReceiverCaller extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		FlashlightCaller callerFlashlight = (FlashlightCaller) context.getApplicationContext();
		MyPhoneStateListener phoneListener = new MyPhoneStateListener(callerFlashlight);
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

	}
	static class MyPhoneStateListener extends PhoneStateListener {


		private static String callState = "";
		private final FlashlightCaller callerFlashlight;

		public MyPhoneStateListener(FlashlightCaller cf) {
			callerFlashlight = cf;

		}

		public void onCallStateChanged(int state, String incomingNumber) {

			if (callerFlashlight.isCallFlash()) {
				switch (state) {
					case TelephonyManager.CALL_STATE_IDLE:

						callState = "IDLE";
						
						break;

					case TelephonyManager.CALL_STATE_OFFHOOK:

						callState = "OFFHOOK";
						
						break;

					case TelephonyManager.CALL_STATE_RINGING:

						callState = "RINGING";
					
						if (FlashLight.getRunning() < 1 && callerFlashlight.isEnabled()) {
							callerFlashlight.registerVolumeButtonReceiver();
							new ManageFlash().execute(callerFlashlight.getCallFlashOnDuration(), callerFlashlight.getCallFlashOnDuration());
						}
						break;
					default:
						callState = "DEFAULT";
						break;
				}
			}
		}

		public class ManageFlash extends AsyncTask<Integer, Integer, String> {


			private FlashLight flash = new FlashLight(callerFlashlight);

			public ManageFlash() {
				FlashLight.incRunning();

			}

			//			@Override
			//			protected void onPreExecute() {
			//				super.onPreExecute();
			//				flash = new Flash(callerFlashlight);
			//			}

			@Override
			protected String doInBackground(Integer... integers) {
				
				int tries = 3;
				while (callState.equals("RINGING") && tries > 0 && !callerFlashlight.isVolumeButtonPressed()) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
					if (!FlashLight.gotCam) {
						
						tries = tries - 1;
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				
				FlashLight.decRunning();
				if (FlashLight.getRunning() == 0) {
					FlashLight.releaseCam();
					callerFlashlight.unregisterVolumeButtonReceiver();
				}
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
				
				FlashLight.decRunning();
				if (FlashLight.getRunning() == 0) FlashLight.releaseCam();
			}


		}
	}

}
