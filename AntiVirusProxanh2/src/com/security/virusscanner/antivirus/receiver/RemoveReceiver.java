package com.security.virusscanner.antivirus.receiver;

import java.io.IOException;
import java.util.List;

import com.security.virusscanner.antivirus.MainActivity;
import com.security.virusscanner.antivirus.R;
import com.security.virusscanner.antivirus.free.AppDetail;
import com.security.virusscanner.antivirus.free.CusStruct;
import com.security.virusscanner.antivirus.free.DataHelper;
import com.security.virusscanner.antivirus.free.DataLite;
import com.security.virusscanner.antivirus.free.Envi;
import com.security.virusscanner.antivirus.free.ProtectActivity;
import com.security.virusscanner.antivirus.free.RemoveViRusAct;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

public class RemoveReceiver extends BroadcastReceiver {

	public SharedPreferences settings;
	public boolean state = true;
	public static final String PREFS_NAME = "MainPrefs";
	String appFile;
	DataHelper db;
	DataLite db1;
	int rCode;
	String message;

	Intent i;
	PendingIntent destIntent;

	List<CusStruct> st1;
	List<CusStruct> st2;

	@Override
	public void onReceive(Context context, Intent intent) {

		Uri app = intent.getData();
		final String appName = app.toString();
		final Context c = context;
		String action = intent.getAction();

		if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
			Intent localIntent1 = new Intent("android.free.antivirus.package_removed");
			localIntent1.putExtra("package_name", appName.substring(appName.lastIndexOf(":") + 1));
			context.sendBroadcast(localIntent1);

			if (!RemoveViRusAct.isUp) {
				Intent localIntent2 = new Intent("android.free.antivirus.remove_package");
				localIntent2.putExtra("package_name", appName.substring(appName.lastIndexOf(":") + 1));
				context.sendBroadcast(localIntent2);
			}
		}

		else if (action.equals("android.intent.action.PACKAGE_INSTALL")
				|| action.equals("android.intent.action.PACKAGE_ADDED")) {
			settings = context.getSharedPreferences(PREFS_NAME, 0);
			boolean b = settings.getBoolean("PROTECTION_STATE", true);
			if (b) {

				NotificationManager notiManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
						.setSmallIcon(R.drawable.ic_launcher).setContentTitle("AntiVirusPro")
						.setContentText("Scannning " + appName);
				i = new Intent(context, MainActivity.class);
				PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, i,
						PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT);
				builder.setContentIntent(resultPendingIntent);
				builder.setAutoCancel(true);
				Notification noti = builder.build();
				notiManager.notify(1, noti);

				db = new DataHelper(context);

				try {

					db.createDataBase();

				} catch (IOException ioe) {

					throw new Error("Unable to create database");

				}

				try {

					db.openDataBase();

				} catch (SQLException sqle) {

					throw sqle;

				}

				notiManager.cancel(1);

				st1 = db.getStrings(0x0a786564);
				st2 = db.getStrings(0x464c457f);

				Envi.cusSt1 = st1;
				Envi.cusSt2 = st2;

				try {

					PackageManager pm = c.getPackageManager();
					String packageName = appName.substring(appName.lastIndexOf(":") + 1);
					ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
					appFile = appInfo.sourceDir;

					Envi.i(c);

					rCode = Envi.scanApk(appFile);
					db1 = new DataLite(context);
					if (rCode > 0) {
						String title = db.getVname(rCode);
						db1.lastAppIsThreat(new AppDetail(title, packageName, "1.0.0", 0, appFile, null, null));
						db1.newInfection(new AppDetail(title, packageName, "1.0.0", 0, appFile, null, null));
						i = new Intent(context, RemoveViRusAct.class);
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
						Intent k = new Intent(Intent.ACTION_DELETE);
						k.setData(Uri.parse("package:" + packageName));
						destIntent = PendingIntent.getActivity(context, 0, k, 0);
						message = appName + " is malicious. Click to remove.";
//						noti = new Notification(R.drawable.ic_launcher, message, System.currentTimeMillis());
//						noti.setLatestEventInfo(c, "AntiVirusPro", message, destIntent);
//						noti.flags = Notification.FLAG_AUTO_CANCEL;
						NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context)
						.setSmallIcon(R.drawable.ic_launcher).setContentTitle("Free Antivirus")
						.setContentText(message);
					builder1.setContentIntent(destIntent);
					builder1.setAutoCancel(true);
					noti = builder1.build();
						notiManager.notify(1, noti);
						SystemClock.sleep(2000);
						notiManager.cancel(1);

					} else {
						db1.LastSafeApp(packageName);
						NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context)
								.setSmallIcon(R.drawable.ic_launcher).setContentTitle("AntiVirusPro")
								.setContentText("Click here to scan " + appName +".");
						i = new Intent(context, ProtectActivity.class);
						PendingIntent resultPendingIntent1 = PendingIntent.getActivity(context, 0, i,
								PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT);
						builder1.setContentIntent(resultPendingIntent1);
						builder1.setAutoCancel(true);
						builder1.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
						Notification noti1 = builder1.build();
						notiManager.notify(1, noti1);
					}

					Envi.releaseAll();
					db.close();
					db1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}

}
