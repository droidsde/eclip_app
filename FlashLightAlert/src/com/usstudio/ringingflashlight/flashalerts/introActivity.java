package com.usstudio.ringingflashlight.flashalerts;

import java.util.Timer;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;


import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class introActivity extends Activity {
    private Timer timer;
    ShimmerTextView tv;
    Shimmer shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        tv = (ShimmerTextView) findViewById(R.id.shimmer_tv);
        final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio);
        toggleAnimation();
        StartAnimations();
    	mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	mPlayer.start();
        
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(introActivity.this,FlashMainActivity.class);
                mPlayer.stop();
                introActivity.this.startActivity(mainIntent);
                introActivity.this.finish();
            }
        }, 3500);
    }
    
    public void toggleAnimation() {
        if (shimmer != null && shimmer.isAnimating()) {
            shimmer.cancel();
        } else {
            shimmer = new Shimmer();
            shimmer.start(tv);
        }
    }
    
    private void StartAnimations() {
        
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);
        
        
        
    }

   
}

