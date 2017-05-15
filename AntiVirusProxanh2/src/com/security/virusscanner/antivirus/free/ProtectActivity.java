package com.security.virusscanner.antivirus.free;

import com.security.virusscanner.antivirus.MainActivity;
import com.security.virusscanner.antivirus.R;
import com.security.virusscanner.antivirus.RippleView;
import com.security.virusscanner.antivirus.RippleView.OnRippleCompleteListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProtectActivity extends Activity {
	
	private ImageView tick_image;
	private TextView tvCoolAlready;
	private LinearLayout llFinish;
	private TextView tvExit;
	private TextView tvDone;
	private TextView tvScan;
	private RippleView rippleView1;
	private RippleView rippleView2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

		tick_image = (ImageView) findViewById(R.id.tick_image);
		tvCoolAlready = (TextView) findViewById(R.id.cooling_already);
		llFinish = (LinearLayout) findViewById(R.id.finish_layout);
		tvExit = (TextView) findViewById(R.id.exit_btn);
		tvDone = (TextView) findViewById(R.id.done_btn);
		tvScan = (TextView) findViewById(R.id.scan_done);
		rippleView1 = (RippleView) findViewById(R.id.ripple_view1);
		rippleView2 = (RippleView) findViewById(R.id.ripple_view2);

		String text = "Scanned 1 app.";
		tvScan.setText(text);

		tvExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				rippleView1.setOnRippleCompleteListener(new OnRippleCompleteListener() {
					
					@Override
					public void onComplete(RippleView rippleView) {
						finish();
						overridePendingTransition(0, R.anim.slide_out_down);
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
						Intent i = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(i);
						onBackPressed();
						overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
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

    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    }
    
}
