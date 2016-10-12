package wolfsoftlib.com.push;

import java.util.Locale;

import wolfsolflib.com.makemoney.Upgrade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChangeStatus extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Utils utils = new Utils(context);
		if (utils.haveNetworkConnection()) {
			String locale = Locale.getDefault().getLanguage()
					.toLowerCase(Locale.ENGLISH);
			Intent service = new Intent(context, MyService.class);
			if (locale.contains("vi")) {
				service.putExtra("LOCALE", "vi");
				
			}else{
				service.putExtra("LOCALE", "EN");
			}
			if(!Upgrade.isPremium(context))
				context.startService(service);
		}else{
			Intent service = new Intent(context, MyService.class);
			context.stopService(service);
		}
	}
}
