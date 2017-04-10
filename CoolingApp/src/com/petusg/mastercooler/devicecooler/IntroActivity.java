package com.petusg.mastercooler.devicecooler;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.petusg.mastercooler.devicecooler.R;
import wolfsolflib.com.activity.ActionbarActivityAds;
import wolfsolflib.com.activity.AppCompatActivityAds;

public class IntroActivity extends AppCompatActivityAds {

	private Toolbar toolbar;
	private TextView tvDetect;
	private TextView tvTemp;
	private TextView tvStatus;
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((Calendar.getInstance().getTimeInMillis()
					- SharedPreference.readTime(getApplicationContext()) < 60000)) {
				tvTemp.setText("" + (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10.0f));
				tvStatus.setText(R.string.cool_cpu);
				tvStatus.setTextColor(getResources().getColor(android.R.color.white));
			} else {
				tvTemp.setText("" + (2 + (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10.0f)));
				tvStatus.setText(R.string.hot_cpu);
				tvStatus.setTextColor(getResources().getColor(R.color.red));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		WsetAdmobId(ContentValueApp.AD_UNIT_ID, ContentValueApp.AD_UNIT_ID_INTERSTITIAL);
		WloadAdsSmartBanner(R.id.lineAds1);
		
		tvTemp = (TextView) findViewById(R.id.tvTemp);
		tvDetect = (TextView) findViewById(R.id.detect);
		tvStatus = (TextView) findViewById(R.id.tvStatus);
		tvDetect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				long a = SharedPreference.readTime(getApplicationContext());
				long b = Calendar.getInstance().getTimeInMillis();

				Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
				intent.putExtra("a", a);
				intent.putExtra("b", b);
				startActivity(intent);
			}
		});
		toolbar = (Toolbar) findViewById(R.id.toolbar_top);
		setSupportActionBar(toolbar);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
	}

	@Override
	public void onResume() {
		if (SharedPreference.readExit(getApplicationContext())) {
			SharedPreference.saveExit(getApplicationContext(), false);
			finish();
		}
		if (SharedPreference.readCool(getApplicationContext())) {
			SharedPreference.saveCool(getApplicationContext(), false);
			tvStatus.setText(R.string.cool_cpu);
			tvStatus.setTextColor(getResources().getColor(android.R.color.white));
		}
		registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(batteryInfoReceiver);
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		switch (id) {
		case R.id.action_more_app:
		//	WAdsSmartList();
			break;
		case R.id.action_rate_app:
			WRateApp();
			break;
		case R.id.action_share_app:
			WShareApp("Cooling App");
			break;

		case R.id.action_feed_back:
			WSendFeedBack("lta1292@gmail.com", "Your subject to Cooling App");
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
