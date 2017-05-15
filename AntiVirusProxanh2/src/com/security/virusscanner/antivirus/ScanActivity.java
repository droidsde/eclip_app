package com.security.virusscanner.antivirus;

import java.io.File;
import java.util.List;

import com.security.virusscanner.antivirus.R;
import com.security.virusscanner.antivirus.free.App;
import com.security.virusscanner.antivirus.free.AppAndFile;
import com.security.virusscanner.antivirus.free.ProjectManager;
import com.security.virusscanner.antivirus.free.RemoveViRusAct;
import com.security.virusscanner.antivirus.free.TransferObj;
import com.security.virusscanner.antivirus.progress.CircleProgress;
import com.security.virusscanner.antivirus.service.ScanService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import wolfsolflib.com.activity.AppCompatActivityAds;
import wolfsolflib.com.view.WProgressWheel;
import wolfsolflib.com.view.WProgressWheel.ProgressCallback;

public class ScanActivity extends AppCompatActivityAds {

	private MediaPlayer mp;
	private View view;
	private ImageView rotateView;
	private CircleProgress circleProgress;

	private TextView fileScanned;
	private TextView totalScanned;
	private TextView totalThreats;
	private ImageView iconView;
	private TextView hideScan;
	private TextView stopScan;
	private TextView timeElapse;
	private TextView timeHide;
	private ProjectManager pm;
	public static boolean scanSDCard;
	private boolean isFirstRun;
	private BroadcastReceiver receiver;
	private IntentFilter filter;
	public static boolean isVisible = false;

	private List<App> appList;
	private AppAndFile mAppFile;
	private List<String> fileList;
	private File file[];
	private WProgressWheel wProgress;
	private int second = 0;
	private int minute = 0;
	private String time = "";
	private boolean timeRunning;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		WsetAdmobId(ContentValueApp.AD_UNIT_ID, ContentValueApp.AD_UNIT_ID_INTERSTITIAL);
		WloadAdsBanner(R.id.lineAds1);
		timeRunning = false;

		view = (View) findViewById(R.id.circle_view);
		rotateView = (ImageView) findViewById(R.id.rotate_image);
		iconView = (ImageView) findViewById(R.id.icon_view);
		timeElapse = (TextView) findViewById(R.id.time_elapse);
		timeHide = (TextView) findViewById(R.id.tv_hide_time);
		stopScan = (TextView) findViewById(R.id.stop_btn);
		stopScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO
				AlertDialog.Builder builder = new AlertDialog.Builder(getDialogContext());

				builder.setTitle("AntiVirusPro");

				builder.setMessage("Do you want to stop scan?");

				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						if (isFirstRun) {
							SharedPreferences settings;
							String PREFS_NAME = "VX";
							settings = getSharedPreferences(PREFS_NAME, 0);
							SharedPreferences.Editor editor = settings.edit();
							editor.putBoolean("VS_FIRSTRUN", true);
							editor.commit();
						}
						if (pm != null) {
							ProjectManager.doUnbindService();
						}
						if (ScanService.isRunning()) {
							stopPlaying();
							stopService(new Intent(getApplicationContext(), ScanService.class));
						}
						stopPlaying();
						Intent intent = new Intent(ScanActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
					}
				});

				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				dialog = builder.create();
				dialog.show();
			}
		});

		circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
		wProgress = (WProgressWheel) findViewById(R.id.w_progress);
		wProgress.setCallback(new ProgressCallback() {

			@Override
			public void onProgressUpdate(float progress) {
				AnimationSet anim = new AnimationSet(true);
				Animation pulse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pulse2);
				pulse.setRepeatCount(0);
				pulse.setDuration(2000);
				Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
				fade.setDuration(2000);
				fade.setRepeatCount(0);
				anim.addAnimation(pulse);
				anim.addAnimation(fade);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						wProgress.setVisibility(View.GONE);
						Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
						fade.setDuration(1000);
						fade.setRepeatCount(0);
						fade.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								circleProgress.setVisibility(View.VISIBLE);
								iconView.setVisibility(View.VISIBLE);
								TransferObj w = new TransferObj(fileScanned, totalScanned, totalThreats, circleProgress, iconView, timeHide);
								pm = new ProjectManager(w, ScanActivity.this);
								if (!ScanService.isRunning()){
									stopPlaying();
									mp = MediaPlayer.create(getApplicationContext(), R.raw.fan_sound);
							        mp.start();
									pm.s(appList, fileList);
								}
								else {
									
									pm.doBindService();
									pm.Progress();
								}
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								view.setVisibility(View.VISIBLE);
								rotateView.setVisibility(View.VISIBLE);
								Animation anim = AnimationUtils.loadAnimation(ScanActivity.this, R.anim.rotator);
								rotateView.startAnimation(anim);

								// TODO abc
								new Thread(new Runnable() {

									@Override
									public void run() {
										while (true) {
											second++;
											if (second >= 60) {
												minute++;
												second = 0;
											}
											runOnUiThread(new Runnable() {
												public void run() {

													if (minute <= 0) {
														time = "00:";
													} else if (minute > 0 && minute < 10) {
														time = "0" + minute + ":";
													} else if (minute >= 10) {
														time = minute + ":";
													}

													if (second < 10) {
														time += "0" + second;
													} else if (second >= 10 && second < 59) {
														time += second;
													} else if (second == 59) {
														minute++;
														time += second;
														second = -1;
													}
													timeElapse.setText(time);
												}
											});
											try {
												Thread.sleep(1000);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										}
									}
								}).start();
							}
						});
						circleProgress.startAnimation(fade);

					}
				});
				wProgress.startAnimation(anim);
			}
		});

		isVisible = true;

		Intent caller = getIntent();
		scanSDCard = caller.getBooleanExtra("SDCARD", false);
		isFirstRun = caller.getBooleanExtra("FIRSTRUN", true);

		fileScanned = (TextView) findViewById(R.id.p_scanfile);
		totalScanned = (TextView) findViewById(R.id.p_scan_filecount);
		totalThreats = (TextView) findViewById(R.id.p_scan_threatcount);
		hideScan = (TextView) findViewById(R.id.p_hidescan);

		if (ScanService.isRunning()) {
			appList = ScanService.appL;
			fileList = ScanService.fileL;
			circleProgress.setVisibility(View.VISIBLE);
			view.setVisibility(View.VISIBLE);
			rotateView.setVisibility(View.VISIBLE);
			Animation anim = AnimationUtils.loadAnimation(ScanActivity.this, R.anim.rotator);
			rotateView.startAnimation(anim);
			iconView.setVisibility(View.VISIBLE);
			wProgress.setVisibility(View.GONE);
			TransferObj w = new TransferObj(fileScanned, totalScanned, totalThreats, circleProgress, iconView, timeHide);
			pm = new ProjectManager(w, ScanActivity.this);
			pm.doBindService();
			pm.Progress();

			// TODO abcd
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						if (!timeRunning) {
							String fromService = timeHide.getText().toString();
							if (!"".equals(fromService)) {
								String[] split = fromService.split(":");
								minute = Integer.parseInt(split[0]);
								second = Integer.parseInt(split[1]);
								timeRunning = true;
							}
						} else {
							second++;
							if (second >= 60) {
								minute++;
								second = 0;
							}
							runOnUiThread(new Runnable() {
								public void run() {

									if (minute <= 0) {
										time = "00:";
									} else if (minute > 0 && minute < 10) {
										time = "0" + minute + ":";
									} else if (minute >= 10) {
										time = minute + ":";
									}

									if (second < 10) {
										time += "0" + second;
									} else if (second >= 10 && second < 59) {
										time += second;
									} else if (second == 59) {
										minute++;
										time += second;
										second = -1;
									}
									timeElapse.setText(time);
								}
							});
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		} else {
			new S().execute();
		}

		hideScan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getDialogContext());

				builder.setTitle("AntiVirusPro");

				builder.setMessage("Do you want to hide scan?");

				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						stopPlaying();
						dialog.cancel();
						state();
					}
				});

				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				dialog = builder.create();
				dialog.show();
			}
		});

		filter = new IntentFilter("android.free.antivirus.completedscan");

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				die();

			}
		};
		registerReceiver(receiver, filter);

		if (isFirstRun) {
			SharedPreferences settings;
			String PREFS_NAME = "VX";
			settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("VS_FIRSTRUN", false);
			editor.putString("DEF_CURRENT", "2.2.3");
			editor.commit();
		}
	}
	
	private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
       }
    }

	@Override
	public void onBackPressed() {
		state();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (pm != null) {
			ProjectManager.doUnbindService();
		}
		unregisterReceiver(receiver);
		isVisible = false;
	}

	private void state() {
		if (pm != null) {
			pm.State();
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "Please wait until the initialization finish!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void die() {
		int threat = Integer.parseInt(totalThreats.getText().toString());
		if (threat > 0) {
			stopPlaying();
			Intent intent = new Intent(ScanActivity.this, RemoveViRusAct.class);
			startActivity(intent);
		} else {
			stopPlaying();
			Intent intent = new Intent(ScanActivity.this, DoneActivity.class);
			if (appList != null) {
				intent.putExtra("app_number", appList.size());
			}
			if (fileList != null) {
				intent.putExtra("file_number", fileList.size());
			}
			intent.putExtra("min", minute);
			intent.putExtra("second", second);
			startActivity(intent);
		}
		finish();
		overridePendingTransition(R.anim.slide_in_up, 0);
	}

	class S extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... u) {

			mAppFile = new AppAndFile(getApplicationContext());
			appList = mAppFile.listApp(false);

			if (scanSDCard) {
				file = Environment.getExternalStorageDirectory().listFiles();
				mAppFile.recursiveFile(file);
				fileList = mAppFile.listFile();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			wProgress.setProgress(1);
		}

	}
	
	private Context getDialogContext() {
		Context context;
		if (getParent() != null)
			context = getParent();
		else
			context = this;
		return context;
	}
}
