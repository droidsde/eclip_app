package com.batterydoctor.superfastcharger.fastcharger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.batterydoctor.superfastcharger.fastcharger.R;

public class SplashActivity extends Activity {
	// Splash screen timer@Override
	private static int SPLASH_TIME_OUT = 3500;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		StartAnimations();
		 new Handler().postDelayed(new Runnable() {	 
	            @Override
	            public void run() {
	                Intent i = new Intent(SplashActivity.this, PowerMgrTabActivity.class);
	                startActivity(i);
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
	}
	
	 private void StartAnimations() {
	        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
	        anim.reset();
	        RelativeLayout l=(RelativeLayout) findViewById(R.id.lin_lay);
	        l.clearAnimation();
	        l.startAnimation(anim);
	        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
	        anim.reset();
	        ImageView iv = (ImageView) findViewById(R.id.logo);
	        iv.clearAnimation();
	        iv.startAnimation(anim);
	        
	        
	        
	    }
}

