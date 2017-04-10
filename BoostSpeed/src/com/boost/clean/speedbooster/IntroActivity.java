package com.boost.clean.speedbooster;

import java.util.Timer;
import java.util.TimerTask;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;


public class IntroActivity extends ActionBarActivity implements OnProgressBarListener {
    private Timer timer;

    private NumberProgressBar bnp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio);
    	mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	mPlayer.start();
        bnp = (NumberProgressBar)findViewById(R.id.numberbar1);
        bnp.setOnProgressBarListener(this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bnp.incrementProgressBy(1);
                        
                    }
                });
            }
        }, 25, 25);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(IntroActivity.this,MainActivity.class);
                IntroActivity.this.startActivity(mainIntent);
                IntroActivity.this.finish();
            }
        }, 2500);
    }
    

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onProgressChange(int current, int max) {
        if(current == max) {
        	
        }
    }
}

