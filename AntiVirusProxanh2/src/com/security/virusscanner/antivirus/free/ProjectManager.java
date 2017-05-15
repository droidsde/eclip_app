package com.security.virusscanner.antivirus.free;

import java.util.List;

import com.security.virusscanner.antivirus.MainActivity;
import com.security.virusscanner.antivirus.R;
import com.security.virusscanner.antivirus.ScanActivity;
import com.security.virusscanner.antivirus.progress.CircleProgress;
import com.security.virusscanner.antivirus.service.ScanService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectManager {

	private static TextView tv1;
	private static TextView tv2;
	private static TextView tv3;
	private static TextView tvTime;
	// aloha3
	private static ImageView iconView;
	private static CircleProgress pb;

	private static Context cx;

	private static Messenger mService = null;
	private static boolean mIsBound;
	private static boolean isFileScanning = false;

	public ProjectManager(TransferObj w, Context c) {
		tv1 = w.getTV1();
		tv2 = w.getTV2();
		tv3 = w.getTV3();
		tvTime = w.gettvTime();
		iconView = w.getIconView();
		pb = w.getPB();
		isFileScanning = false;
		cx = c;
	}

	public void s(List<App> a, List<String> fP) {
		ScanService.appL = a;
		if (ScanActivity.scanSDCard) {
			ScanService.fileL = fP;
		}
		cx.startService(new Intent(cx, ScanService.class));
		doBindService();
	}

	final static Messenger mMessenger = new Messenger(new IncomingHandler());

	static class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ScanService.MSG_SET_STRING_VALUE:
				Bitmap bitmap = (Bitmap) msg.getData().getParcelable("icon");				
				progress(msg.getData().getInt("i"), msg.getData().getString("f"), msg.getData().getString("cnt"),
						msg.getData().getInt("in"), msg.getData().getBoolean("finished"), bitmap,
						msg.getData().getString("threat_a"));
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	private static ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			try {
				Message msg = Message.obtain(null, ScanService.MSG_REGISTER_CLIENT);
				msg.replyTo = mMessenger;
				mService.send(msg);
			} catch (RemoteException e) {

			}
		}

		public void onServiceDisconnected(ComponentName className) {

			mService = null;
		}
	};

	public void doBindService() {

		cx.bindService(new Intent(cx, ScanService.class), mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	public static void doUnbindService() {
		if (mIsBound) {

			if (mService != null) {
				try {
					Message msg = Message.obtain(null, ScanService.MSG_UNREGISTER_CLIENT);
					msg.replyTo = mMessenger;
					mService.send(msg);
				} catch (RemoteException e) {

				}
			}

			cx.unbindService(mConnection);
			mIsBound = false;
		}
	}

	private static void progress(int arg1, String arg2, String arg3, int arg4, boolean st, Bitmap icon, String arg5) {
		if (!st) {
			pb.setProgress(arg1);
			tv1.setText(arg2);
			tv2.setText(arg3);
			tv3.setText(arg4 + "");
			tvTime.setText(arg5);
			if (icon != null) {
				iconView.setImageBitmap(icon);
				Animation pulse = AnimationUtils.loadAnimation(cx, R.anim.pulse1);
				iconView.startAnimation(pulse);
			} else{
				if(!isFileScanning){
					iconView.setImageResource(R.drawable.file);
					Animation pulse = AnimationUtils.loadAnimation(cx, R.anim.pulse1);
					pulse.setRepeatCount(Animation.INFINITE);
					iconView.startAnimation(pulse);
					isFileScanning = true;
				}
			}
		} else {
			if (ScanActivity.isVisible) {
				doUnbindService();
				cx.stopService(new Intent(cx, ScanService.class));
			}

			Intent localIntent1 = new Intent(cx, MainActivity.class);
			Intent localIntent2 = new Intent("android.free.antivirus.completedscan");
			if (arg4 > 0) {
				localIntent1.putExtra("STAT", false);
				localIntent1.putExtra("SCANRESULT", true);
				localIntent1.putExtra("CNT", arg4);
			} else {
				localIntent1.putExtra("STAT", true);
				localIntent1.putExtra("SCANRESULT", true);
			}

			if (ScanActivity.isVisible) {
				cx.sendBroadcast(localIntent2);
				cx.startActivity(localIntent1);
			} else {
				SharedPreferences settings;
				String PREFS_NAME = "VX";
				settings = cx.getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor saveSettings = settings.edit();
				saveSettings.putBoolean("STAT", false);
				saveSettings.putInt("CNT", arg4);
				saveSettings.commit();
			}
		}
	}

	public void Progress() {
		SharedPreferences settings;
		String PREFS_NAME = "FSPrefs";
		settings = cx.getSharedPreferences(PREFS_NAME, 0);
		pb.setProgress(settings.getInt("progress", 0));
		tv1.setText(settings.getString("file", ""));
		tv2.setText(settings.getString("count", ""));
		tv3.setText(settings.getString("in", ""));
		// tvTime.setText(settings.getString("threat_a", ""));
	}

	public void State() {
		SharedPreferences settings;
		String PREFS_NAME = "FSPrefs";
		settings = cx.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor saveSettings = settings.edit();
		saveSettings.putInt("progress", pb.getProgress());
		saveSettings.putString("file", tv1.getText().toString());
		saveSettings.putString("count", tv2.getText().toString());
		saveSettings.putString("in", tv3.getText().toString());
		saveSettings.putString("threat_a", tvTime.getText().toString());
		saveSettings.commit();

	}

}
