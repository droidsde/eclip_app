package com.security.virusscanner.antivirus;

import java.io.IOException;
import java.util.Calendar;

import com.security.virusscanner.antivirus.R;
import com.security.virusscanner.antivirus.free.DataHelper;
import com.security.virusscanner.antivirus.service.ScanService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	ImageView scan_img;
	private MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(ScanService.isRunning()){
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.activity_splash);

		setRA(this);
		if(getSharedPreferences("VX", 0).getBoolean("VS_FIRSTRUN", true)){
			DataHelper d = new DataHelper(getApplicationContext());
			try {

				d.createDataBase();

			} catch (IOException ioe) {

				throw new Error("Unable to create database");

			}

			try {

				d.openDataBase();

			} catch (SQLException sqle) {

				throw sqle;

			}
		}

		scan_img = (ImageView) findViewById(R.id.scan_img);
		Animation anim = new MyAnimation(scan_img, 100);
		anim.setDuration(2500);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				scan_img.setVisibility(View.GONE);
				stopPlaying();
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		anim.setRepeatCount(1);
		scan_img.startAnimation(anim);
		stopPlaying();
		mp = MediaPlayer.create(getApplicationContext(), R.raw.scanning_sound);
        mp.start();

	}
	
	private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
       }
    }

	private void setRA(Context context) {

		Calendar updateTime = Calendar.getInstance();
		updateTime.set(Calendar.HOUR_OF_DAY, 17);
		updateTime.set(Calendar.MINUTE, 38);

		int r = 1 + (int) (Math.random() * 15);

		int s = r * 1000;
		
	}
}
