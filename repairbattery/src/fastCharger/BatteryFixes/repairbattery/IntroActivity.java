package fastCharger.BatteryFixes.repairbattery;

import wolfsolflib.com.activity.AppCompatActivityAds;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.victor.loading.newton.NewtonCradleLoading;


public class IntroActivity extends AppCompatActivityAds {

    private NewtonCradleLoading newtonCradleLoading;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.start();
        button = (Button) findViewById(R.id.button);
        button.setText("Loading...");
        WsetAdmobId("ca-app-pub-7232493315188211/9258021084","ca-app-pub-7232493315188211/1734754282");
        
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                	button.setText("Start");
                	WloadAdsInterstitial();
                	button.setOnClickListener(new View.OnClickListener() {
        				@Override
        				public void onClick(View v) {
        					
        					new Handler().postDelayed(new Runnable(){
        			            @Override
        			            public void run() {
        						Intent mIntent = new Intent(IntroActivity.this,MainActivity.class);
            	                IntroActivity.this.startActivity(mIntent);
            	                IntroActivity.this.finish();
        					
        			            }
        			        }, 1900);
        				}
        			});
                
            }
        }, 300);
        
    }

}


