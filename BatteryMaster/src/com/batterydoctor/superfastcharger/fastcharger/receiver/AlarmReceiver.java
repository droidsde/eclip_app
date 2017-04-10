package com.batterydoctor.superfastcharger.fastcharger.receiver;

import java.util.Calendar;

import com.nvn.data.pref.BatteryPref;
import com.nvn.data.pref.HistoryPref;
import com.nvn.log.LogBuider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * tạo bộ đếm gi�?. cứ sau 1 khoảng th�?i gian 1h thì sẽ lưu dữ liệu vào history
 * @author nghia
 *
 */
public class AlarmReceiver extends BroadcastReceiver {
	private static final String ACTION_AUTOSTART_ALARM = "com.batterydoctor.superfastcharger.fastcharger.ACTION_AUTOSTART_ALARM";
	private static final String TAG = "AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(ACTION_AUTOSTART_ALARM)) {
			Calendar calendar = Calendar.getInstance();
			int level = BatteryPref.getLevel(context);

			LogBuider.e(TAG, ACTION_AUTOSTART_ALARM);

			HistoryPref.putLevel(context, calendar.get(Calendar.DAY_OF_MONTH),
					calendar.get(Calendar.HOUR_OF_DAY), level);
		}
	}

	public static void OnCreate(Context context) {

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction(ACTION_AUTOSTART_ALARM);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long l = calendar.getTimeInMillis();
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				l, 60 * 60 * 1000, pendingIntent);
	}
}
