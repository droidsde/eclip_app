package com.batterydoctor.superfastcharger.fastcharger.fastercharger;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

public class ChargingService extends Service {

	int level = 0;
	Ringtone r;
	Uri sound;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		registerReceiver(this.mBatInfoReceiver2, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ChargerSetting.saveTimeup(getApplicationContext(), false);
		unregisterReceiver(mBatInfoReceiver2);
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}
	private BroadcastReceiver mBatInfoReceiver2 = new BroadcastReceiver(){

		@Override
		public void onReceive(final Context mContext, Intent mIntent) {
			// TODO Auto-generated method stub
			level = mIntent.getIntExtra("level", -1);
			if(level==100&&ChargerSetting.readEnableSound(mContext)&&!ChargerSetting.readTimeup(mContext)&&ChargerSetting.readFull(mContext)){
				ChargerSetting.saveTimeup(mContext, true);
				sound = RingtoneManager.getDefaultUri(2);
			    r = RingtoneManager.getRingtone(getApplicationContext(), sound);
			    r.play();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(300000L);
							ChargerSetting.saveTimeup(mContext, false);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
		} 
	  };

}
