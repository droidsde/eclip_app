package com.petusg.mastercooler.devicecooler;

import java.util.ArrayList;

import com.petusg.mastercooler.devicecooler.R;
import com.petusg.mastercooler.devicecooler.asyntask.TaskList;
import com.petusg.mastercooler.devicecooler.process.TaskInfo;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

public class ScanActivity extends Activity implements CallbackListenner {

	private ImageView imageScan;
	private boolean isDown;
	private boolean isRunning;
	private int height;
	private MediaPlayer mp;
	private long a;
	private long b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		a = getIntent().getExtras().getLong("a");
		b = getIntent().getExtras().getLong("b");

		isDown = true;
		isRunning = true;
		imageScan = (ImageView) findViewById(R.id.image_scan);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		MarginLayoutParams mlp = (MarginLayoutParams) imageScan.getLayoutParams();
		int top = height / 2 + 400;
		mlp.setMargins(0, top, 0, 0);
		imageScan.setLayoutParams(mlp);
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	public void onAttachedToWindow() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				final MarginLayoutParams mlp = (MarginLayoutParams) imageScan.getLayoutParams();
				int top = height / 2 + 200;
				while (isRunning) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (isDown) {
						mlp.setMargins(0, top += 22, 0, 0);
						if (top > height / 2 + 400) {
							try {
								Thread.sleep(40);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							isDown = false;
						}
					} else {
						mlp.setMargins(0, top -= 22, 0, 0);
						if (top < height / 2 - 500) {
							try {
								Thread.sleep(40);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							isDown = true;
						}
					}
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							imageScan.setLayoutParams(mlp);
						}
					});
				}
			}
		}).start();

		new TaskList(this).execute();
		stopPlaying();
		mp = MediaPlayer.create(getApplicationContext(), R.raw.scanning_sound);
		mp.start();
		super.onAttachedToWindow();
	}

	@Override
	public void closeScanActivity(ArrayList<TaskInfo> arrList) {
		if (b - a > 60000) {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("data", arrList);
			bundle.putBoolean("fromScan", true);
			intent.putExtras(bundle);
			isRunning = false;
			startActivity(intent);
		} else {
			Intent intent = new Intent(getApplicationContext(), AlreadyActivity.class);
			isRunning = false;
			startActivity(intent);
		}

		finish();
	}

	@Override
	public void updateCoolActivity(int killed) {
		// dont use
	}

	private void stopPlaying() {
		if (mp != null) {
			mp.stop();
			mp.release();
			mp = null;
		}
	}

	@Override
	protected void onPause() {
		stopPlaying();
		super.onPause();
	}
}
