package com.tethering.wifihotspot;
import com.tethering.wifihotspot.R;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

public class introActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 550;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.intro);
//        new LongOperation1().execute("");
        MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio);
    	mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	mPlayer.start();
        /* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(introActivity.this,MainActivity.class);
                introActivity.this.startActivity(mainIntent);
                introActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    

}