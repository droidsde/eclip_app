package com.security.virusscanner.antivirus.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.security.virusscanner.antivirus.MainActivity;
import com.security.virusscanner.antivirus.R;
import com.security.virusscanner.antivirus.ScanActivity;
import com.security.virusscanner.antivirus.free.App;
import com.security.virusscanner.antivirus.free.AppDetail;
import com.security.virusscanner.antivirus.free.DataHelper;
import com.security.virusscanner.antivirus.free.DataLite;
import com.security.virusscanner.antivirus.free.Envi;
import com.security.virusscanner.antivirus.free.FSEnvi;
import com.security.virusscanner.antivirus.free.LGD;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ScanService extends Service {
	private NotificationManager nm;
	private static boolean isRunning = false;
	private int i;
	private int in;
	public static List<App> appL;
	public static List<String> fileL;
	private int appSize;
	private int indices[][];

	static ArrayList<Messenger> mClients = new ArrayList<Messenger>();
	int mValue = 0;
	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_SET_INT_VALUE = 3;
	public static final int MSG_SET_STRING_VALUE = 4;
	final Messenger mMessenger = new Messenger(new IncomingHandler());

	private PowerManager.WakeLock wl;
	private PowerManager pm;

	private int second = 0;
	private int minute = 0;
	private String time = "";
	private Handler handle = new Handler();
	private Runnable run = new Runnable() {
		
		@Override
		public void run() {
			sendStrMessageToUI(false);
			handle.postDelayed(run, 1000);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	static class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REGISTER_CLIENT:
				mClients.add(msg.replyTo);
				break;
			case MSG_UNREGISTER_CLIENT:
				mClients.remove(msg.replyTo);
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	private void sendStrMessageToUI(boolean st) {
		for (int j = mClients.size() - 1; j >= 0; j--) {
			try {

				Bundle b = new Bundle();
				b.putBoolean("c", st);
				b.putInt("i", (int) ((i + 1) * 100) / appSize);
				b.putInt("in", in);
				b.putString("threat_a", time);

				if (i < appL.size()) {
					b.putString("f", appL.get(i).getTitle());
					// aloha1
					// b.putInt("icon", a.get(i).getIcon());
					Bitmap bitmap = ((BitmapDrawable) appL.get(i).getIcon()).getBitmap();
					b.putParcelable("icon", bitmap);
				} else if (i >= appL.size()) {
					b.putString("f", fileL.get(i + fileL.size() - appSize));
					try {
						Thread.sleep(8);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				b.putString("cnt", i + 1 + " / " + appSize);
				b.putBoolean("finished", st);
				Message msg = Message.obtain(null, MSG_SET_STRING_VALUE);
				msg.setData(b);
				mClients.get(j).send(msg);

			} catch (Exception e) {
				if(j<mClients.size()){
					mClients.remove(j);
				}
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		LGD load = new LGD(this);

		Envi.i(this);

		showNotification();
		if(appL!=null){
			new ScanAsync().execute();
		}else{
			stopSelf();
		}
		
		isRunning = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (!isRunning) {
						break;
					}
					second++;

					if (minute == 0) {
						time = "00:";
					} else if (minute > 0 && minute < 10) {
						time = "0" + minute + ":";
					} else if (minute >= 10) {
						time = minute + ":";
					}

					if (second < 10) {
						time += "0" + second;
					} else if (second >= 10 && second < 59) {
						time += second;
					} else if (second >= 59) {
						minute++;
						time += second;
						second = -1;
					}
					Log.e("aaaaaaaaaa", time);

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void showNotification() {		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher).setContentTitle("AntiVirusPro")
				.setContentText("Scanning for threats!");
		Intent resultIntent = new Intent(this, ScanActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
				PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
//		builder.setAutoCancel(true);
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = builder.build();
		nm.notify(1, notification);
		startForeground(1, notification);
	}

	private void showResult(boolean b) {
		nm.cancel(1);
		
		//save to prevent task manager killed
		SharedPreferences settings;
		String PREFS_NAME = "VX";
		settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("tuananh_in", in);
		editor.commit();
		
		Intent localIntent = new Intent(this, DoneIntentService.class);
		localIntent.putExtra("state", in);
		startService(localIntent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	public static boolean isRunning() {
		return isRunning;
	}

	class ScanAsync extends AsyncTask<String, String, String> {

		private int rCode;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AntiVirusPro WakeLock");
			wl.acquire();

			appSize = appL.size();
			if (ScanActivity.scanSDCard)
				appSize += fileL.size();

			i = 0;
			in = 0;
			indices = new int[appSize][3];
		}

		@Override
		protected String doInBackground(String... u) {

			while (i < appSize) {
				if (isCancelled() || !isRunning) {
					break;
				}
				sendStrMessageToUI(false);
				handle.postDelayed(run, 1000);
				if (i < appL.size()) {
					rCode = FSEnvi.scanFile(appL.get(i).getsourcePath());

					if (rCode > 0) {
						indices[in][0] = i;
						indices[in][1] = rCode;
						indices[in][2] = 0;
						in++;
					}
				} else if (i >= appL.size()) {
					rCode = FSEnvi.scanFile(fileL.get(i + fileL.size() - appSize));

					if (rCode > 0) {
						indices[in][0] = i + fileL.size() - appSize;
						indices[in][1] = rCode;
						indices[in][2] = 1;
						in++;
					}
				}
				handle.removeCallbacks(run);
				i++;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			if(isRunning){
				i--;	
				u();
				sendStrMessageToUI(true);
				if (in > 0)
					showResult(false);
				else
					showResult(true);
				wl.release();
				stopSelf();
			}else{
				nm.cancel(1);
				NotificationCompat.Builder builder = new NotificationCompat.Builder(ScanService.this)
						.setSmallIcon(R.drawable.ic_launcher).setContentTitle("AntiVirusPro")
						.setContentText("Scanning has stopped!");
				Intent resultIntent = new Intent(ScanService.this, MainActivity.class);
				PendingIntent resultPendingIntent = PendingIntent.getActivity(ScanService.this, 0, resultIntent,
						PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT);
				builder.setContentIntent(resultPendingIntent);
				builder.setAutoCancel(true);
				nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notification = builder.build();
				nm.notify(1, notification);
			}
		}

	}

	private void u() {
		DataHelper d = new DataHelper(this);

		try {

			d.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}

		try {

			d.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}

		DataLite dataLite = new DataLite(this);

		for (int i = 0; i < in; i++) {
			String title;

			if (indices[i][2] == 0) {
				title = d.getVname(indices[i][1]);
				dataLite.newInfection(new AppDetail(title, appL.get(indices[i][0]).getPackageName(),
						appL.get(indices[i][0]).getVersionName(), 0, appL.get(indices[i][0]).getsourcePath(), null, null));
			} else if (indices[i][2] == 1) {
				title = d.getVname(indices[i][1]);
				String name = fileL.get(indices[i][0]).substring(fileL.get(indices[i][0]).lastIndexOf("/") + 1);
				dataLite.newInfection(new AppDetail(title, name, null, 1, fileL.get(indices[i][0]), null, null));
			}

		}

		Date now = new Date();
		int apps = 0;
		int files = 0;
		if(appL!=null){
			apps = appL.size();
		}
		if(fileL!=null){
			files = fileL.size();
		}
		dataLite.updateLastScan(apps, files, in, 0, "0", now.toLocaleString());

		d.close();
		dataLite.close();

	}

	@Override
	public void onDestroy() {
		if(nm!=null){
			nm.cancel(1);
		}
		super.onDestroy();
		Envi.releaseAll();
		isRunning = false;
	}
}