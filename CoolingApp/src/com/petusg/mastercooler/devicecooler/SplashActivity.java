package com.petusg.mastercooler.devicecooler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.petusg.mastercooler.devicecooler.R;

public class SplashActivity extends Activity{

	private ImageView fan_img;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		fan_img = (ImageView) findViewById(R.id.fan_img);
		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotator);
		animation.setDuration(3500);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
				startActivity(intent);
				finish();
			}
		});
		fan_img.startAnimation(animation);
	}
}
