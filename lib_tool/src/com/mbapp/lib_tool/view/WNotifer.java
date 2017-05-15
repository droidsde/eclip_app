package com.mbapp.lib_tool.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;


public class WNotifer {

	public static void notificationState(Context mContext,String title, String content,
			int resourceId,Class<?> cls) {
		Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(),resourceId);
		
		String notificationData=content;
		
		Intent intent =new Intent(mContext, cls);
		intent.putExtra("NOTIFICATION_DATA", notificationData);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		NotificationManager notificationManager =(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE); 
				
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				mContext)
				.setContentText(content)
				.setContentTitle(title)
				.setAutoCancel(true)
				.setSmallIcon(resourceId)
				.setTicker(title)
				.setLargeIcon(largeIcon)
				.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND)
				.setContentIntent(pendingIntent);
		
		Notification notification=notificationBuilder.build();
		
		notificationManager.notify(0,notification);

		
	       

	}
//	public static Notification notification(Context ctx, String title, int icon,
//	        long when, PendingIntent pIntent, String contentTitle,
//	        String contentText,int flags, int defaults){
//	    NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
//	    Notification notification = builder.setContentIntent(pIntent)
//	          .setSmallIcon(icon).setTicker(title).setWhen(when).setContentTitle(contentTitle)
//	          .setContentText(contentText).setDefaults(defaults).build();
//	    notification.flags = flags;
//	    return notification;
	
}

