package com.petusg.mastercooler.devicecooler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import wolfsolflib.com.activity.AppCompatActivityAds;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.petusg.mastercooler.devicecooler.R;
import com.petusg.mastercooler.devicecooler.wave.WaveView;

public class MainActivity extends AppCompatActivityAds {

	private Toolbar toolbar;
	private TextView tvRamUse;
	private long totalMemory;
	private TextView tvTemp;
	private WaveView waveView;
	private int total = 0;
	private int count = 0;
	private MediaPlayer mp;

	private Handler timerHandler = null;
	private Runnable timerRunnable = new Runnable() {

		@Override
		public void run() {
			updateMemoryStatus(true);
			timerHandler.postDelayed(timerRunnable, 5000);
		}
	};

	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			tvTemp.setText("" + (2+(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10.0f)));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		WsetAdmobId(ContentValueApp.AD_UNIT_ID, ContentValueApp.AD_UNIT_ID_INTERSTITIAL);
		WloadAdsSmartBanner(R.id.lineAds1);
		
		tvRamUse = (TextView) findViewById(R.id.tvRam);
		tvTemp = (TextView) findViewById(R.id.tvTemp);
		waveView = (WaveView) findViewById(R.id.wave_view);

		totalMemory = getTotalMemory();// get total device memory
		updateMemoryStatus(false);
		timerHandler = new Handler();

		toolbar = (Toolbar) findViewById(R.id.toolbar_top);
		setSupportActionBar(toolbar);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
	}

	@Override
	public void onResume() {
		timerHandler.postDelayed(timerRunnable, 0);
		registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		super.onResume();
	}

	@Override
	public void onAttachedToWindow() {
		
		super.onAttachedToWindow();
	}

	@Override
	protected void onPause() {
		stopPlaying();
		timerHandler.removeCallbacks(timerRunnable);
		unregisterReceiver(batteryInfoReceiver);
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (count <= total) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							tvRamUse.setText("" + count);
							waveView.setProgress(count);
						}
					});

					count++;
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				stopPlaying();
			}
		}).start();
		stopPlaying();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.water);
        mp.start();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		switch (id) {
		case R.id.action_more_app:
			WAdsSmartList();
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

	private void updateMemoryStatus(boolean flag) {

		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memInfo);

		long availMem = memInfo.availMem;
		float f = availMem;
		float f1 = totalMemory;
		int i = (int) (100 - ((f / f1) * 100F));
		if (i != 0) {
			total = i;
			if (flag) {
				tvRamUse.setText("" + i);
				waveView.setProgress(i);
			}
		}

	}

	private long getTotalMemory() {

		/**
		 * The entries in the /proc/meminfo can help explain what's going on
		 * with your memory usage
		 */

		String str1 = "/proc/meminfo";
		String str2 = "tag";
		String[] arrayOfString;
		long initial_memory = 0, free_memory = 0;

		try {

			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);

			for (int i = 0; i < 2; i++) {
				str2 = str2 + " " + localBufferedReader.readLine();
			}

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");// show memory info into log
			}

			// total Memory
			initial_memory = Integer.valueOf(arrayOfString[2]).intValue();
			free_memory = Integer.valueOf(arrayOfString[5]).intValue();

			Log.d("MEM", "FREE " + (free_memory / 1024) + " MB");
			Log.d("MEM", "INIT " + (initial_memory * 1024L) + " MB");

			localBufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return (initial_memory * 1024L);
	}
	
	private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
       }
    }
}
