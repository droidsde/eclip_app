package com.batterydoctor.superfastcharger.fastcharger.shedule;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.batterydoctor.superfastcharger.fastcharger.receiver.AlarmModeReceiver;
import com.batterydoctor.superfastcharger.fastcharger.ui.data.ScheduleItemData;

public class AlarmSchedule {
	// tham khao anq tempbattery
	public static final String ALARMSWITCH = "com.batterydoctor.superfastcharger.fastcharger.ALARMSWITCH";
	public static final String ALARMRESTORE = "com.batterydoctor.superfastcharger.fastcharger.ALARMRESTORE";
	public static final String SWITCH_KEY = "switch_key";
	public static final String RESTORE_KEY = "restore_key";
	public static void addSchedule(Context context,ScheduleItemData data){
		addScheduleSwitch(context, data);
		addScheduleRestore(context, data);
//		Toast.makeText(context, "add", Toast.LENGTH_LONG).show();
	}
	public static void cancelSchedule(Context context,ScheduleItemData data){
		Intent Switch = new Intent(context,AlarmModeReceiver.class);
		Switch.setAction(ALARMSWITCH);
		Intent Restore = new Intent(context,AlarmModeReceiver.class);
		Restore.setAction(ALARMRESTORE);
		PendingIntent intentSwitch = PendingIntent.getBroadcast(context, Integer.valueOf(data.key), Switch, PendingIntent.FLAG_CANCEL_CURRENT);
		PendingIntent intentRestore = PendingIntent.getBroadcast(context, Integer.valueOf(data.key), Restore, PendingIntent.FLAG_CANCEL_CURRENT);
		((AlarmManager)context.getSystemService(Context.ALARM_SERVICE)).cancel(intentSwitch);
		((AlarmManager)context.getSystemService(Context.ALARM_SERVICE)).cancel(intentRestore);
//		Toast.makeText(context, "cancel", Toast.LENGTH_LONG).show();
	}
	private static void addScheduleSwitch(Context context,ScheduleItemData data){
		Intent intenSwitch = new Intent(context,AlarmModeReceiver.class);
		intenSwitch.setAction(ALARMSWITCH);
		intenSwitch.putExtra(SWITCH_KEY, data.typestart);
		PendingIntent pendingIntentSwitch = PendingIntent.getBroadcast(context, Integer.valueOf(data.key), intenSwitch, PendingIntent.FLAG_UPDATE_CURRENT);
		Calendar calendarSwitch = Calendar.getInstance();
		long l1 = calendarSwitch.getTimeInMillis();
		calendarSwitch.set(Calendar.HOUR_OF_DAY, data.startHour);
		calendarSwitch.set(Calendar.MINUTE, data.startMinute);
		calendarSwitch.set(Calendar.SECOND, 0);
		long l2= calendarSwitch.getTimeInMillis();
		if(l1>l2){
			calendarSwitch.add(Calendar.HOUR, 24);
		}
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, calendarSwitch.getTimeInMillis()	, pendingIntentSwitch);
	}
	private static void addScheduleRestore(Context context,ScheduleItemData data){
		Intent intenRestore = new Intent(context,AlarmModeReceiver.class);
		intenRestore.setAction(ALARMRESTORE);
		intenRestore.putExtra(RESTORE_KEY, data.typestop);
		PendingIntent pendingIntentRestore = PendingIntent.getBroadcast(context, Integer.valueOf(data.key), intenRestore, PendingIntent.FLAG_UPDATE_CURRENT);
		Calendar calendarSwitch = Calendar.getInstance();
		long l1 = calendarSwitch.getTimeInMillis();
		calendarSwitch.set(Calendar.HOUR_OF_DAY, data.stopHour);
		calendarSwitch.set(Calendar.MINUTE, data.stopMinute);
		calendarSwitch.set(Calendar.SECOND, 0);
		long l2= calendarSwitch.getTimeInMillis();
		if(l1>l2){
			calendarSwitch.add(Calendar.HOUR, 24);
		}//86400000L
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, calendarSwitch.getTimeInMillis()	, pendingIntentRestore);
//		alarmManager.setRepeating(AlarmManager.RTC, calendarSwitch.getTimeInMillis(), 24*60*60*1000, pendingIntentRestore);
	}
}
