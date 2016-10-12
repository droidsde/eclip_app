package fastCharger.BatteryFixes.repairbattery;


import com.dd.CircularProgressButton;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

public class FixActivity extends Activity {

	  public Dialog dialog;
	  CircularProgressButton circularButton1, circularButton2;
	  Integer time1, time2;
	  
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_fix);
	    
	    Intent callerIntent=getIntent();
	    Bundle packageFromCaller = callerIntent.getBundleExtra("data");
	    time1 = packageFromCaller.getInt("low");
	    time2 = packageFromCaller.getInt("inactive");
	    time1 = time1*1000;
	    time2 = time2*1000;
	    
	    circularButton1 = (CircularProgressButton) findViewById(R.id.circularButton1);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setProgress(50);
        circularButton2 = (CircularProgressButton) findViewById(R.id.circularButton2);
        circularButton2.setIndeterminateProgressMode(true);
        circularButton2.setProgress(50);
        doStart();
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {
                    circularButton1.setProgress(50);
                } else if (circularButton1.getProgress() == 100) {
                    circularButton1.setProgress(0);
                } else {
                    circularButton1.setProgress(100);
                }
            }
        });

	  }
	  
	  public void doStart()
	  {
		  
		  new Handler().postDelayed(new Runnable(){
	            @Override
	            public void run() {
	            	circularButton1.setProgress(100);
	            	doStart2();
	            }
	        }, time1);
	  }
	  
	  public void doStart2(){
		  new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				circularButton2.setProgress(100); 
				showDialog();
			}
		}, time2);
	  }
	  
	  public void showDialog(){
		  new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					AlertDialog.Builder b=new AlertDialog.Builder(FixActivity.this);
					  b.setTitle(getString(R.string.information));
					  b.setMessage(getString(R.string.alert3));
					  b.setPositiveButton("OK", new DialogInterface. OnClickListener() {
						  @Override
						  public void onClick(DialogInterface dialog, int which)
						  {
							  myPreference.saveData();
							  finish();
						  }});
					  b.create().show();
				}
			}, 800);
		  
	  }
	  
		  
	  
	 
}
