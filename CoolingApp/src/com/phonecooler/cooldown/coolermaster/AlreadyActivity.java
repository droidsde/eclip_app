package com.phonecooler.cooldown.coolermaster;

import com.mbapp.lib_tool.view.WAppRaterExit;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.phonecooler.cooldown.coolermaster.R;

public class AlreadyActivity extends Activity {

	private ImageView tick_image;
	private ImageView water_image;
	private TextView tvCoolAlready;
	private LinearLayout llFinish;
	private TextView tvExit;
	private TextView tvDone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_already);
		overridePendingTransition(R.anim.slide_in_up, 0);
		tick_image = (ImageView) findViewById(R.id.tick_image);
		water_image = (ImageView) findViewById(R.id.water_image);
		tvCoolAlready = (TextView) findViewById(R.id.cooling_already);
		llFinish = (LinearLayout) findViewById(R.id.finish_layout);
		tvExit = (TextView) findViewById(R.id.exit_btn);
		tvDone = (TextView) findViewById(R.id.done_btn);

		tvExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreference.saveExit(getApplicationContext(), true);
			
			}
		});
		tvDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
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

			}
		});
		tick_image.startAnimation(anim1);

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
		// TODO Auto-generated method stub
		SharedPreference.saveExit(getApplicationContext(), true);
		WAppRaterExit.show(this, this.getString(R.string.app_name))	;
	}
}
