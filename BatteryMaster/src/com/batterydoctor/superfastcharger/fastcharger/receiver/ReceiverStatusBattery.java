package com.batterydoctor.superfastcharger.fastcharger.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;

import com.nvn.data.pref.BatteryPref;
import com.nvn.data.pref.HistoryPref;
import com.nvn.log.LogBuider;
import com.batterydoctor.superfastcharger.fastcharger.PowerMgrTabActivity;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.fastercharger.ChargerSetting;
import com.batterydoctor.superfastcharger.fastcharger.notifycation.NotifycationHome;
import com.batterydoctor.superfastcharger.fastcharger.service.AppService;
import com.batterydoctor.superfastcharger.fastcharger.ui.data.BatteryInfo;

public class ReceiverStatusBattery extends BroadcastReceiver {
	private static final String TAG = "ReceiverStatusBattery";
	BatteryInfo batteryInfo = new BatteryInfo();
	Context mContext = null;
	NotificationManager mNotificationManager;
	 NotificationCompat.Builder mBuilder;
	

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
			batteryInfo.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
			batteryInfo.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
			batteryInfo.temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
			batteryInfo.voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
			batteryInfo.technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
			
			//charging
			batteryInfo.status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			if(batteryInfo.status == BatteryManager.BATTERY_STATUS_FULL){
				if(ChargerSetting.readFull(context)){
				playSound(mContext, R.raw.charge_over);
				
				generatenotification(context);
				}
			}
			boolean isCharging = batteryInfo.status == BatteryManager.BATTERY_STATUS_CHARGING ||
					batteryInfo.status == BatteryManager.BATTERY_STATUS_FULL;
			if(isCharging){
				batteryInfo.plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
				boolean usbCharge = batteryInfo.plugged == BatteryManager.BATTERY_PLUGGED_USB;
//				boolean acCharge = batteryInfo.plugged == BatteryManager.BATTERY_PLUGGED_AC;
				int time;
				if(usbCharge){
					time = BatteryPref.getTimeChargingUsb(mContext, batteryInfo.level);
				}else{
					time = BatteryPref.getTimeChargingAc(mContext, batteryInfo.level);
				}
				batteryInfo.hourleft = time/60;
				batteryInfo.minleft = time%60;
			}else{
				int time = BatteryPref.getTimeRemainning(mContext, batteryInfo.level);
				batteryInfo.hourleft = time/60;
				batteryInfo.minleft = time%60;
			}
			intent.setAction(AppService.ACTION_BATTERY_CHANGED_SEND);
			intent.putExtra(BatteryInfo.BATTERY_INFO_KEY, batteryInfo);
			mContext.sendBroadcast(intent);
			NotifycationHome.getInstance(mContext).updateNotify(batteryInfo.level, batteryInfo.temperature, batteryInfo.hourleft, batteryInfo.minleft,isCharging);
		} else if (action.equals(AppService.ACTION_BATTERY_NEED_UPDATE)) {
			intent.setAction(AppService.ACTION_BATTERY_CHANGED_SEND);
			intent.putExtra(BatteryInfo.BATTERY_INFO_KEY, batteryInfo);
			mContext.sendBroadcast(intent);
		}else if(action.equals(Intent.ACTION_POWER_CONNECTED)|| action.equals(Intent.ACTION_POWER_DISCONNECTED)){
			playSound(mContext, R.raw.charging);
		}else if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
			Intent intent2 = new Intent(context,AppService.class);
			context.startService(intent2);
		}
		HistoryPref.putLevel(context, batteryInfo.level);
	}

	public final void OnCreate(Context context) {
		mContext = context.getApplicationContext();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
		intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
		intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		intentFilter.addAction(AppService.ACTION_BATTERY_NEED_UPDATE);

		mContext.registerReceiver(this, intentFilter);
	}

	public final void OnDestroy(Context context) {
		if (context != null) {
			context.unregisterReceiver(this);
		}
	}
	public static void playSound(Context context,int id){
		MediaPlayer mediaPlayer = MediaPlayer.create(context, id);
		mediaPlayer.start();
	}
	public  void generatenotification(Context mContext){
		mNotificationManager = (NotificationManager)mContext.getSystemService("notification");
	    mBuilder = new NotificationCompat.Builder(mContext).setSmallIcon(R.drawable.ic_launcher).setContentTitle("Faster Charger").setContentText("Battery full");
	    Intent localIntent = new Intent(mContext, PowerMgrTabActivity.class);
	    PendingIntent localPendingIntent = PendingIntent.getActivity(mContext, 0, localIntent, 0);
	    mBuilder.setContentIntent(localPendingIntent);
	    mNotificationManager.notify(1, mBuilder.build());
	  }
}
