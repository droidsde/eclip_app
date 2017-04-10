package com.batterydoctor.superfastcharger.fastcharger.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.batterydoctor.superfastcharger.fastcharger.notifycation.NotifycationHome;
import com.batterydoctor.superfastcharger.fastcharger.receiver.AlarmModeReceiver;
import com.batterydoctor.superfastcharger.fastcharger.receiver.AlarmReceiver;
import com.batterydoctor.superfastcharger.fastcharger.receiver.ReceiverStatusBattery;

public class AppService extends Service {
	public static final String ACTION_BATTERY_CHANGED = "com.batterydoctor.superfastcharger.fastcharger.ACTION_BATTERY_CHANGED";
	/**
	 * khi có thông báo từ Intent.ACTION_BATTERY_CHANGED , dữ liệu sẽ được gửi tới activity bằng cách sử dụng
	 * ACTION_BATTERY_CHANGED_SEND
	 */
	public static final String ACTION_BATTERY_CHANGED_SEND = "com.batterydoctor.superfastcharger.fastcharger.ACTION_BATTERY_CHANGED_SEND";
	/**
	 * được sử dụng khi ứng dụng bị ngư�?i dùng nhấn back. kill activity chính đi. khi đó, nếu ngư�?i 
	 * dùng mở lại ứng dụng, thì nếu ko có ACTION_BATTERY_NEED_UPDATE thì ứng dụng sẽ mất th�?i gian ch�?
	 * cho tới khi Intent.ACTION_BATTERY_CHANGED có thông báo mới v�?.
	 */
	public static final String ACTION_BATTERY_NEED_UPDATE = "com.batterydoctor.superfastcharger.fastcharger.ACTION_BATTERY_NEED_UPDATE";
	ReceiverStatusBattery receiverStatusBattery;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		receiverStatusBattery = new ReceiverStatusBattery();
		receiverStatusBattery.OnCreate(this);
		startForeground(NotifycationHome.NOTIFYCATION_HOME_ID, NotifycationHome.getInstance(this));
		AlarmReceiver.OnCreate(getApplicationContext());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if(receiverStatusBattery!=null){
			receiverStatusBattery.OnDestroy(getApplicationContext());
			receiverStatusBattery = null;
		}
		startService(new Intent(this,AppService.class));
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
