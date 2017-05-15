package com.phonecooler.cooldown.coolermaster;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.phonecooler.cooldown.coolermaster.R;

public class SplashActivity extends Activity{

	private ImageView fan_img;
	private MediaPlayer mp;
	
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
				stopPlaying();
				Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
				startActivity(intent);
				finish();
			}
		});
		fan_img.startAnimation(animation);
		stopPlaying();
		mp = MediaPlayer.create(getApplicationContext(), R.raw.fan_sound);
        mp.start();
        
	}
	
	private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
       }
    }
}
