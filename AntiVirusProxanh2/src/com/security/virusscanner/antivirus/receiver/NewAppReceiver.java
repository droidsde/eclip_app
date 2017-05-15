package com.security.virusscanner.antivirus.receiver;

import com.security.virusscanner.antivirus.MainActivity;
import com.security.virusscanner.antivirus.R;
import com.security.virusscanner.antivirus.free.DataLite;
import com.security.virusscanner.antivirus.free.RemoveViRusAct;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NewAppReceiver extends BroadcastReceiver {

	private DataLite db;
	private String message = "";

	@Override
	public void onReceive(Context context, Intent intent) {

		final Context c = context;

		NotificationManager notifyManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher).setContentTitle("AntiVirusPro")
				.setContentText("Protected by AntiVirusPro Mobile Security.!");
		Intent resultIntent = new Intent(context, MainActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
				PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		builder.setAutoCancel(true);
		Notification notification = builder.build();
		notifyManager.notify(2, notification);

		db = new DataLite(c);

		int count = db.getInfectionsCount();

		if (count > 0) {
			message = count + " infections found. Click to remove.";
			NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.ic_launcher).setContentTitle("AntiVirusPro")
					.setContentText(message);
			Intent resultIntent1 = new Intent(context, RemoveViRusAct.class);
			PendingIntent resultPendingIntent1 = PendingIntent.getActivity(context, 0, resultIntent1,
					PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT);
			builder1.setContentIntent(resultPendingIntent1);
			builder1.setAutoCancel(true);
			builder1.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
			Notification notification1 = builder1.build();
			notifyManager.notify(2, notification1);

		} else {
			message = "Your device is protected.";
			NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.ic_launcher).setContentTitle("AntiVirusPro")
					.setContentText(message);
			Intent resultIntent1 = new Intent(context, MainActivity.class);
			PendingIntent resultPendingIntent1 = PendingIntent.getActivity(context, 0, resultIntent1,
					PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT);
			builder1.setContentIntent(resultPendingIntent1);
			builder1.setAutoCancel(true);
			builder1.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
			Notification notification1 = builder1.build();
			notifyManager.notify(2, notification1);
		}

		db.close();

	}

}
