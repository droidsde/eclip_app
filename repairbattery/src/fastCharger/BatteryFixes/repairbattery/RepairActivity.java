package fastCharger.BatteryFixes.repairbattery;

import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

public class RepairActivity extends Activity {

  private AnimatedCircleLoadingView animatedCircleLoadingView;
  public Dialog dialog;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_repair);
    animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
    startLoading();
    startPercentMockThread();
 	
  }

  private void startLoading() {
    animatedCircleLoadingView.startDeterminate();
  }

  private void startPercentMockThread() {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(1500);
          for (int i = 0; i <= 100; i++) {
            Thread.sleep(350);
            changePercent(i);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    new Thread(runnable).start();
  }

  private void changePercent(final int percent) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
    	  animatedCircleLoadingView.setPercent(percent);
    	  if(percent==100)
    	  {
    		  showDialog();
    	  }
      }
    });
  }

  public void resetLoading() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        animatedCircleLoadingView.resetLoading();
      }
    });
  }
  
  public void showDialog(){
	  	// custom dialog
	 	dialog = new Dialog(this);
	 	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	
	 	dialog.setContentView(R.layout.dialog_repair);
	 	dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
	 	// set the custom dialog components - text, image and button
	 	TextView low = (TextView) dialog.findViewById(R.id.low);
	 	TextView inactive = (TextView) dialog.findViewById(R.id.inactive);
	 	TextView healthy = (TextView) dialog.findViewById(R.id.healthy);
	 	Random rn = new Random();
	 	int range = 5;
	 	final int randomNum =  rn.nextInt(range) + 4;
	 	low.setText(getString(R.string.low) + " ("+randomNum+"/275) 1%");
	 	range = 4;
	 	final int randomNum2 = rn.nextInt(range) + 1;
	 	inactive.setText(getString(R.string.inactive) + " ("+randomNum2+"/275) 1%");
	 	range = 275 - randomNum - randomNum2;
	 	healthy.setText(getString(R.string.healthy) + " ("+range+"/275) 98%");
	 	
	 	TextView alert1 = (TextView) dialog.findViewById(R.id.alert1);
	 	range = randomNum + randomNum2;
	 	alert1.setText(getString(R.string.app_name) + " " + range + " " + getString(R.string.problem));
	 	
	 	TextView alert2 = (TextView) dialog.findViewById(R.id.alert2);
	 	range = range*2 + 3;
	 	alert2.setText(getString(R.string.alert2) + " " + range + "%");
	 	
	 	Button btnCan = (Button) dialog.findViewById(R.id.dialogButtonCancel);
	 	// if button is clicked, close the custom dialog
	 	btnCan.setOnClickListener(new OnClickListener() {
	 		@Override
	 		public void onClick(View v) {
	 			dialog.dismiss();
	 			finish();
	 		   }
	 		});
	 	Button btnFix = (Button) dialog.findViewById(R.id.dialogButtonOK);
	 	btnFix.setOnClickListener(new OnClickListener() {
	 		@Override
	 		public void onClick(View v) {
	 			dialog.dismiss();
	 			Intent fixIntent=new Intent(RepairActivity.this, FixActivity.class);
	 			Bundle bundle = new Bundle();
	 			bundle.putInt("low", randomNum);
	 			bundle.putInt("inactive", randomNum2);
	 			fixIntent.putExtra("data", bundle);
	 			startActivity(fixIntent);
	 			finish();
	 		   }
	 		});
	 	
	 	dialog.show();
  }
}