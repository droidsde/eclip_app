package com.batterydoctor.superfastcharger.fastcharger.notifycation;

import com.batterydoctor.superfastcharger.fastcharger.PowerBatteryActivity;
import com.batterydoctor.superfastcharger.fastcharger.PowerMgrTabActivity;
import com.batterydoctor.superfastcharger.fastcharger.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.RemoteViews;

public class NotifycationHome extends Notification{
	public static final int NOTIFYCATION_HOME_ID = 2;
	private int iconRes[] = {
			R.drawable.battery_green_0,R.drawable.battery_green_1,
			R.drawable.battery_green_2,R.drawable.battery_green_3,
			R.drawable.battery_green_4,R.drawable.battery_green_5,
			R.drawable.battery_green_6,R.drawable.battery_green_7,
			R.drawable.battery_green_8,R.drawable.battery_green_9,
			R.drawable.battery_green_10,R.drawable.battery_green_11,
			R.drawable.battery_green_12,R.drawable.battery_green_13,
			R.drawable.battery_green_14,R.drawable.battery_green_15,
			R.drawable.battery_green_16,R.drawable.battery_green_17,
			R.drawable.battery_green_18,R.drawable.battery_green_19,
			R.drawable.battery_green_20,R.drawable.battery_green_21,
			R.drawable.battery_green_22,R.drawable.battery_green_23,
			R.drawable.battery_green_24,R.drawable.battery_green_25,
			R.drawable.battery_green_26,R.drawable.battery_green_27,
			R.drawable.battery_green_28,R.drawable.battery_green_29,
			R.drawable.battery_green_30,R.drawable.battery_green_31,
			R.drawable.battery_green_32,R.drawable.battery_green_33,
			R.drawable.battery_green_34,R.drawable.battery_green_35,
			R.drawable.battery_green_36,R.drawable.battery_green_37,
			R.drawable.battery_green_38,R.drawable.battery_green_39,
			R.drawable.battery_green_40,R.drawable.battery_green_41,
			R.drawable.battery_green_42,R.drawable.battery_green_43,
			R.drawable.battery_green_44,R.drawable.battery_green_45,
			R.drawable.battery_green_46,R.drawable.battery_green_47,
			R.drawable.battery_green_48,R.drawable.battery_green_49,
			R.drawable.battery_green_50,R.drawable.battery_green_51,
			R.drawable.battery_green_52,R.drawable.battery_green_53,
			R.drawable.battery_green_54,R.drawable.battery_green_55,
			R.drawable.battery_green_56,R.drawable.battery_green_57,
			R.drawable.battery_green_58,R.drawable.battery_green_59,
			R.drawable.battery_green_60,R.drawable.battery_green_61,
			R.drawable.battery_green_62,R.drawable.battery_green_63,
			R.drawable.battery_green_64,R.drawable.battery_green_65,
			R.drawable.battery_green_66,R.drawable.battery_green_67,
			R.drawable.battery_green_68,R.drawable.battery_green_69,
			R.drawable.battery_green_70,R.drawable.battery_green_71,
			R.drawable.battery_green_72,R.drawable.battery_green_73,
			R.drawable.battery_green_74,R.drawable.battery_green_75,
			R.drawable.battery_green_76,R.drawable.battery_green_77,
			R.drawable.battery_green_78,R.drawable.battery_green_79,
			R.drawable.battery_green_80,R.drawable.battery_green_81,
			R.drawable.battery_green_82,R.drawable.battery_green_83,
			R.drawable.battery_green_84,R.drawable.battery_green_85,
			R.drawable.battery_green_86,R.drawable.battery_green_87,
			R.drawable.battery_green_88,R.drawable.battery_green_89,
			R.drawable.battery_green_90,R.drawable.battery_green_91,
			R.drawable.battery_green_92,R.drawable.battery_green_93,
			R.drawable.battery_green_94,R.drawable.battery_green_95,
			R.drawable.battery_green_96,R.drawable.battery_green_97,
			R.drawable.battery_green_98,R.drawable.battery_green_99,
			R.drawable.battery_green_100
			
	};
	NotificationManager notificationManager;
	private static NotifycationHome notifycationHome;
	Context mContext;
	private NotifycationHome(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context.getApplicationContext();
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	public static NotifycationHome getInstance(Context context){
		if(notifycationHome==null)
			notifycationHome = new NotifycationHome(context);
		return notifycationHome;
	}
	public void updateNotify(int lv,int temp,int hourleft,int minleft,boolean ischarging){
		RemoteViews remoteViews = new RemoteViews("com.batterydoctor.superfastcharger.fastcharger", R.layout.notifycation_home);
		remoteViews.setTextViewText(R.id.battery_num, String.valueOf(lv)+"%");
		//temperature
		String string[] = intToArray(mContext, temp);
		
		
		remoteViews.setTextViewText(R.id.temperature, string[0]+string[1]);
		remoteViews.setTextViewText(R.id.remaining_time, convertTime(lv,hourleft, minleft,ischarging));
		Intent intent = new Intent(mContext,PowerMgrTabActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		
		
		this.contentView = remoteViews;
		this.icon = iconRes[lv];
		this.contentIntent = pendingIntent;
		
		this.notificationManager.notify(NOTIFYCATION_HOME_ID, this);
	}
	private String convertTime(int lv,int hour,int min,boolean isCharging){
		StringBuilder stringBuilder = new StringBuilder();
		if(isCharging){
			if(lv == 100){
				return mContext.getResources().getString(R.string.home_status_battery_full);
			}
			stringBuilder.append(mContext.getResources().getString(R.string.battery_info_value_charging_timer_left));
		}else{
			stringBuilder.append(mContext.getResources().getString(R.string.battery_info_value_timer_left));
		}
		stringBuilder.append(" ");
		stringBuilder.append(hour);
		stringBuilder.append("h");
		stringBuilder.append(min);
		stringBuilder.append("m");
		return stringBuilder.toString();
	}
	private String[] intToArray(Context context, int i) {
		String str = Double.toString(i / 10f);
		if(str.length()>4) 
			str = str.substring(0, 4);
		if (true) {
			// do C
			String string = context.getString(R.string.celsius);
			return new String[] { str, string };
		} else {
			// do F
			String string = context.getString(R.string.fahrenheit);
			return new String[] { str, string };
		}
	}
	public void cancel(){
		notificationManager.cancel(NOTIFYCATION_HOME_ID);
	}
}
