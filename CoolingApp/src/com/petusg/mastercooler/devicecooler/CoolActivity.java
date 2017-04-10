package com.petusg.mastercooler.devicecooler;

import java.util.ArrayList;
import java.util.Calendar;

import wolfsolflib.com.view.WAppRaterExit;

import com.petusg.mastercooler.devicecooler.R;
import com.petusg.mastercooler.devicecooler.asyntask.KillTaskList;
import com.petusg.mastercooler.devicecooler.process.TaskInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoolActivity extends Activity implements CallbackListenner {

	private ImageView fan_image;
	private ImageView tick_image;
	private ImageView water_image;
	private TextView tvCoolNow;
	private TextView tvCoolDone;
	private LinearLayout llFinish;
	private TextView tvProcessKill;
	private TextView tvMemoryClean;
	private TextView tvExit;
	private TextView tvDone;
	private LinearLayout fan_layout;

	private long beforeMemory;
	private ActivityManager acm;
	private int ramFreed;
	private int process_killed;
	private MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cool);
		overridePendingTransition(R.anim.slide_in_up, 0);
		SharedPreference.saveTime(getApplicationContext(), Calendar.getInstance().getTimeInMillis());

		fan_image = (ImageView) findViewById(R.id.fan_image);
		tick_image = (ImageView) findViewById(R.id.tick_image);
		water_image = (ImageView) findViewById(R.id.water_image);
		tvCoolNow = (TextView) findViewById(R.id.cooling_now);
		tvCoolDone = (TextView) findViewById(R.id.cooling_done);
		llFinish = (LinearLayout) findViewById(R.id.finish_layout);
		tvProcessKill = (TextView) findViewById(R.id.process_killed);
		tvMemoryClean = (TextView) findViewById(R.id.memory_cleaned);
		fan_layout = (LinearLayout) findViewById(R.id.fan_layout);
		tvExit = (TextView) findViewById(R.id.exit_btn);
		tvDone = (TextView) findViewById(R.id.done_btn);

		tvExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreference.saveExit(getApplicationContext(), true);
				finish();
			}
		});
		tvDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreference.saveCool(getApplicationContext(), true);
				finish();
				overridePendingTransition(0, R.anim.slide_out_down);
			}
		});

		beforeMemory = getIntent().getExtras().getLong("beforeMemory");
		ArrayList<TaskInfo> listTask = getIntent().getExtras().getParcelableArrayList("listTask");
		acm = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		if (listTask != null) {
			new KillTaskList(listTask, acm, this).execute();
		}

		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotator);
		animation.setDuration(4000);
		stopPlaying();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.fan_sound);
        mp.start();
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink_to_middle);
				fan_layout.startAnimation(anim2);

				Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink_to_middle);
				anim1.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						fan_layout.setVisibility(View.GONE);
						Animation anim3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grom_from_midle);
						tick_image.setVisibility(View.VISIBLE);
						tick_image.startAnimation(anim3);

						Animation anim5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_out);
						anim5.setDuration(2000);
						anim5.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {

							}

							@Override
							public void onAnimationRepeat(Animation animation) {

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								tvCoolNow.setVisibility(View.GONE);

								Animation anim4 = AnimationUtils.loadAnimation(getApplicationContext(),
										R.anim.abc_fade_in);
								anim4.setDuration(2000);
								anim4.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(Animation animation) {
										llFinish.setVisibility(View.VISIBLE);
									}

									@Override
									public void onAnimationRepeat(Animation animation) {

									}

									@Override
									public void onAnimationEnd(Animation animation) {

									}
								});
								llFinish.startAnimation(anim4);

								Animation anim6 = AnimationUtils.loadAnimation(getApplicationContext(),
										R.anim.abc_fade_in);
								anim6.setDuration(2000);
								anim6.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(Animation animation) {
										tvCoolDone.setVisibility(View.VISIBLE);
									}

									@Override
									public void onAnimationRepeat(Animation animation) {

									}

									@Override
									public void onAnimationEnd(Animation animation) {

									}
								});
								tvCoolDone.startAnimation(anim6);

								String textProcessKill = getResources().getString(R.string.process_kill) + " "
										+ process_killed;
								tvProcessKill.setText(textProcessKill);
								Animation anim7 = AnimationUtils.loadAnimation(getApplicationContext(),
										R.anim.abc_fade_in);
								anim7.setDuration(2000);
								anim7.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(Animation animation) {
										tvProcessKill.setVisibility(View.VISIBLE);
									}

									@Override
									public void onAnimationRepeat(Animation animation) {

									}

									@Override
									public void onAnimationEnd(Animation animation) {

									}
								});
								tvProcessKill.startAnimation(anim7);

								String textMemoryClean = getResources().getString(R.string.memory_clean) + " "
										+ formatMemSize(ramFreed, 0);
								tvMemoryClean.setText(textMemoryClean);
								Animation anim8 = AnimationUtils.loadAnimation(getApplicationContext(),
										R.anim.abc_fade_in);
								anim8.setDuration(2000);
								anim8.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(Animation animation) {
										tvMemoryClean.setVisibility(View.VISIBLE);
									}

									@Override
									public void onAnimationRepeat(Animation animation) {

									}

									@Override
									public void onAnimationEnd(Animation animation) {

									}
								});
								tvMemoryClean.startAnimation(anim8);
							}
						});
						tvCoolNow.startAnimation(anim5);
					}
				});
				tick_image.startAnimation(anim1);
				stopPlaying();
			}
		});
		fan_image.startAnimation(animation);

		Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
		animation2.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				water_image.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});
		water_image.startAnimation(animation2);
	}

	@Override
	public void onBackPressed() {
		if (llFinish.getVisibility() == View.VISIBLE) {
			SharedPreference.saveCool(getApplicationContext(), true);
			super.onBackPressed();
			overridePendingTransition(0, R.anim.slide_out_down);
		}
	}
	

	@Override
	public void closeScanActivity(ArrayList<TaskInfo> arrList) {
		// dont use
	}

	@Override
	public void updateCoolActivity(int killed) {
		process_killed = killed;
		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		acm.getMemoryInfo(memInfo);
		long aftermemory = memInfo.availMem;
		if (aftermemory > beforeMemory) {
			ramFreed = (int) (aftermemory - beforeMemory);
		} else {
			ramFreed = 0;
		}
	}

	// format memory size to B, KB, MB, GB
	@SuppressLint("DefaultLocale")
	private String formatMemSize(long size, int value) {

		String result = "";
		if (1024L > size) {// size less than 1024, for byte result
			String info = String.valueOf(size);
			result = (new StringBuilder(info)).append(" B").toString();
		} else if (1048576L > size) {// for KB result
			String s2 = (new StringBuilder("%.")).append(value).append("f").toString();
			Object aobj[] = new Object[1];
			Float float1 = Float.valueOf((float) size / 1024F);
			aobj[0] = float1;
			String s3 = String.valueOf(String.format(s2, aobj));
			result = (new StringBuilder(s3)).append(" KB").toString();
		} else if (1073741824L > size) {// for MB result
			String s4 = (new StringBuilder("%.")).append(value).append("f").toString();
			Object aobj1[] = new Object[1];
			Float float2 = Float.valueOf((float) size / 1048576F);
			aobj1[0] = float2;
			String s5 = String.valueOf(String.format(s4, aobj1));
			result = (new StringBuilder(s5)).append(" MB").toString();
		} else {// for GB Result
			Object aobj2[] = new Object[1];
			Float float3 = Float.valueOf((float) size / 1.073742E+009F);
			aobj2[0] = float3;
			String s6 = String.valueOf(String.format("%.2f", aobj2));
			result = (new StringBuilder(s6)).append(" GB").toString();
		}

		return result;
	}
	
	private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
       }
    }
}
