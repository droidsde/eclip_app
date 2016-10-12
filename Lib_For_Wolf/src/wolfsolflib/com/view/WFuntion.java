package wolfsolflib.com.view;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class WFuntion {
	public static void WToastShort(Context mContext,String text) {
		Toast.makeText(mContext, text,
				Toast.LENGTH_SHORT).show();
	}
	public static void WToastLong(Context mContext,String text) {
		Toast.makeText(mContext, text,
				Toast.LENGTH_LONG).show();
	}
	public static void WOpenBrower(Context mContext,String PACKAGE_APP){
		Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(PACKAGE_APP));
		mContext.startActivity(intent);
	}
	public static void WRateApp(Context mContext,String PACKAGE_APP){
		mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +PACKAGE_APP )));
	}
	public static void WSendFeedBack(Context mContext,String Wemail,String WEmailTitle) {
 		String[] TO = { Wemail };
 		Intent intentEmail = new Intent(Intent.ACTION_SEND);
 		intentEmail.setData(Uri.parse("mailto:"));
 		intentEmail.setType("message/rfc822");

 		intentEmail.putExtra(Intent.EXTRA_EMAIL, TO);
 		intentEmail.putExtra(Intent.EXTRA_SUBJECT, WEmailTitle);
 		intentEmail.putExtra(Intent.EXTRA_TEXT, "Enter your FeedBack");

 		try {
 			mContext.startActivity(Intent.createChooser(intentEmail, "Send FeedBack..."));
 		} catch (ActivityNotFoundException e) {
 			WToastShort(mContext,"There is no email client installed.");
 		}
 	}
	public static void WShareApp(Context mContext,String nameApp,String PACKAGE_APP) {
 		Intent intent = new Intent("android.intent.action.SEND");
 		intent.setType("text/plain");
 		intent.putExtra("android.intent.extra.SUBJECT",nameApp);
 		intent.putExtra("android.intent.extra.TEXT",
 				"https://play.google.com/store/apps/details?id="+PACKAGE_APP);
 		mContext.startActivity(Intent.createChooser(intent, "Share App Via"));
 	}
	public static void WMoreApp(Context mContext,String mStore) {
 		Intent intent = new Intent(Intent.ACTION_VIEW);
 		intent.setData(Uri.parse("market://search?q=pub:"+mStore));
 		mContext.startActivity(intent);
 	}
	public static boolean isMyServiceRunning(Context mContext,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
