package com.security.virusscanner.antivirus;

import wolfsolflib.com.activity.AppCompatActivityAds;

import com.security.virusscanner.antivirus.R;
import com.security.virusscanner.antivirus.RippleView.OnRippleCompleteListener;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DoneActivity extends AppCompatActivityAds {

	private ImageView tick_image;
	private TextView tvCoolAlready;
	private LinearLayout llFinish;
	private TextView tvExit;
	private TextView tvDone;
	private TextView tvScan;
	private RippleView rippleView1;
	private RippleView rippleView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_done);
		WsetAdmobId(ContentValueApp.AD_UNIT_ID, ContentValueApp.AD_UNIT_ID_INTERSTITIAL);
		WloadAdsBanner(R.id.lineAds1);
		WloadAdsInterstitial();
		tick_image = (ImageView) findViewById(R.id.tick_image);
		tvCoolAlready = (TextView) findViewById(R.id.cooling_already);
		llFinish = (LinearLayout) findViewById(R.id.finish_layout);
		tvExit = (TextView) findViewById(R.id.exit_btn);
		tvDone = (TextView) findViewById(R.id.done_btn);
		tvScan = (TextView) findViewById(R.id.scan_done);
		rippleView1 = (RippleView) findViewById(R.id.ripple_view1);
		rippleView2 = (RippleView) findViewById(R.id.ripple_view2);

		String text = getString(R.string.dash_Scanned);
		int app = getIntent().getExtras().getInt("app_number", 0);
		int file = getIntent().getExtras().getInt("file_number", 0);
		int min = getIntent().getExtras().getInt("min", 0);
		int sec = getIntent().getExtras().getInt("second", 0);

		if (app != 0) {
			text += " "+ app + " "+ getString(R.string.dash_apps);
		}

		if (file != 0) {
			if (app != 0) {
				text += " "+ getString(R.string.doneand)+ " " + file +" "+  getString(R.string.dash_files);
			} else {
				text += " " +file + getString(R.string.dash_files);
			}
		}
		text += " "+getString(R.string.donein);
		if (min != 0) {
			text += " "+ min+" " + getString(R.string.donem);
		}
		if (sec != 0) {
			text += " "+ sec+" " + getString(R.string.dones);
		}

		tvScan.setText(text);

		tvExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rippleView1.setOnRippleCompleteListener(new OnRippleCompleteListener() {
					
					@Override
					public void onComplete(RippleView rippleView) {
						SharedPreferences settings;
						String PREFS_NAME = "VX";
						settings = getSharedPreferences(PREFS_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putBoolean("exit_a", true);
						editor.commit();
						finish();
					}
				});
				
			}
		});
		tvDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rippleView2.setOnRippleCompleteListener(new OnRippleCompleteListener() {
					
					@Override
					public void onComplete(RippleView rippleView) {
						onBackPressed();
						overridePendingTransition(0, R.anim.slide_out_down);
					}
				});
				
			}
		});

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
				tick_image.setVisibility(View.GONE);
				Animation anim3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grom_from_midle);
				anim3.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						tick_image.setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {

					}
				});
				tick_image.startAnimation(anim3);
				Animation anim4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
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

				Animation anim6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
				anim6.setDuration(2000);
				anim6.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						tvCoolAlready.setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {

					}
				});
				tvCoolAlready.startAnimation(anim6);

				Animation anim7 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
				anim7.setDuration(2000);
				anim7.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						tvScan.setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {

					}
				});
				tvScan.startAnimation(anim7);

			}
		});
		tick_image.startAnimation(anim1);

	}
}
