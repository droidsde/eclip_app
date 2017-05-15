package com.security.virusscanner.antivirus.service;

import com.security.virusscanner.antivirus.MainActivity;
import com.security.virusscanner.antivirus.R;
import com.security.virusscanner.antivirus.free.RemoveViRusAct;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class DoneIntentService extends IntentService {

	private NotificationManager nm;

	public DoneIntentService() {
		super("DoneIntentService");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	protected void onHandleIntent(Intent intent) {

			nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Notification notification;

			int b;
			if(intent!=null){
				b = intent.getIntExtra("state", 0);
			}else{
				b=getSharedPreferences("VX", 0).getInt("tuananh_in", 0);
			}

			if (b == 0) {
				NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.ic_launcher).setContentTitle("AntiVirusPro")
						.setContentText("Everything is ok!");
				Intent resultIntent = new Intent(this, MainActivity.class);
				PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
						PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT);
				builder.setContentIntent(resultPendingIntent);
				builder.setAutoCancel(true);
				builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
				notification = builder.build();
				nm.notify(2, notification);
			} else {
				NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.ic_launcher).setContentTitle("AntiVirusPro")
						.setContentText(b + " threats found. Click to remove!");
				Intent resultIntent = new Intent(this, RemoveViRusAct.class);
				PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
						PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT);
				builder.setContentIntent(resultPendingIntent);
				builder.setAutoCancel(true);
				builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
				notification = builder.build();
				nm.notify(2, notification);
			}

	}
}
