package wolfsoftlib.com.push.spam;

import java.util.Locale;

import wolfsolflib.com.makemoney.Upgrade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Utils utils = new Utils(context);
		if (utils.haveNetworkConnection()) {
			String locale = Locale.getDefault().getLanguage()
					.toLowerCase(Locale.ENGLISH);
			Intent service = new Intent(context, NewAppService.class);
			if (locale.contains("vi")) {
				service.putExtra("LOCALE", "vi");
				
			}else{
				service.putExtra("LOCALE", "EN");
			}
			if(!Upgrade.isPremium(context))
				context.startService(service);
		}
	}
}
