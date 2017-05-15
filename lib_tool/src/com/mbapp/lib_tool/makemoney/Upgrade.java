package com.mbapp.lib_tool.makemoney;

import android.content.Context;
import android.provider.Settings.Secure;

public class Upgrade extends WSettings {

	private static String TAG_PRO = "pro";

	public static boolean isPremium(Context context) {
		return read(context, TAG_PRO, false);
	}

	public static void upgradePremium(Context context) {
		save(context, TAG_PRO, true);
	}
	public static void downgradePremium(Context context) {
		save(context, TAG_PRO, false);
	}
	public static String getHashID(Context context) {
		return Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
	}

	public static boolean checkHashID(Context context, String payload) {

		return payload.compareTo(getHashID(context)) == 0;
	}


}
