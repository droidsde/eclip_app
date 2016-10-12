package com.tethering.wifihotspot;

import com.skyfishjy.library.RippleBackground;
import com.tethering.wifihotspot.R;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ImageView;
//import com.skyfishjy.library.RippleBackground;


public class loadActiviTy extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private Boolean  isStart = true;
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_activity);
        
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        rippleBackground.startRippleAnimation();
       
        /* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new LongOperation().execute("");
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                    finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    
    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 3; i++) {
                try {
                	 MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio);
                     mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                     mPlayer.start();
                    Thread.sleep(900);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}