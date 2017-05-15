package com.phonecooler.cooldown.coolermaster;

import java.util.Calendar;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.phonecooler.cooldown.coolermaster.R;

import com.mbapp.lib_tool.activity.AppCompatActivityAds;

public class IntroActivity extends AppCompatActivityAds {

	View mainActivity;
	private Toolbar toolbar;
	private TextView tvDetect;
	private TextView tvTemp;
	private TextView tvStatus;
	private long timeBackPressStart = 0l;
    private long timeStartActivity = 0l;
	String prefname = "cooling the phone 1";
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((Calendar.getInstance().getTimeInMillis()
					- SharedPreference.readTime(getApplicationContext()) < 60000)) {
				tvTemp.setText("" + (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10.0f));
				tvStatus.setText(R.string.cool_cpu);
				tvStatus.setTextColor(getResources().getColor(android.R.color.white));
			} else {
				tvTemp.setText("" + (2 + (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10.0f)));
				tvStatus.setText(R.string.hot_cpu);
				tvStatus.setTextColor(getResources().getColor(R.color.red));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		mainActivity = findViewById(R.id.activity_intro);
		WsetAdmobId(ContentValueApp.AD_UNIT_ID, ContentValueApp.AD_UNIT_ID_INTERSTITIAL);
		WloadAdsSmartBanner(R.id.lineAds1);
		
		tvTemp = (TextView) findViewById(R.id.tvTemp);
		tvDetect = (TextView) findViewById(R.id.detect);
		tvStatus = (TextView) findViewById(R.id.tvStatus);
		tvDetect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				long a = SharedPreference.readTime(getApplicationContext());
				long b = Calendar.getInstance().getTimeInMillis();

				Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
				intent.putExtra("a", a);
				intent.putExtra("b", b);
				startActivity(intent);
			}
		});
		toolbar = (Toolbar) findViewById(R.id.toolbar_top);
		setSupportActionBar(toolbar);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		timeStartActivity = new Date().getTime();
		countRunApp();
	}
	
	public void countRunApp(){
        SharedPreferences pre=getSharedPreferences
                (prefname, MODE_PRIVATE);
        int dem = pre.getInt("dem", 0);
        dem++;
        SharedPreferences.Editor editor=pre.edit();
        editor.putInt("dem", dem);
        //chấp nhận lưu xuống file
        editor.commit();
    }
    
    public void savingPreferences()
    {
        // getSharedPreferences
        SharedPreferences pre=getSharedPreferences
                (prefname, MODE_PRIVATE);
        
        SharedPreferences.Editor editor=pre.edit();
        //lưu vào editor
        editor.putBoolean("rate", true);
         //chấp nhận lưu xuống file
        editor.commit();
    }
    
    public void exitApp2(View view, String message) {
        long currentTime = new Date().getTime();
        if (currentTime - timeBackPressStart <= 2000) {
            finish();
        } //Once press
        else {
            timeBackPressStart = currentTime;
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction(getString(R.string.exit), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).show();
        }
    }
    
    @Override
    public void onBackPressed() {
        SharedPreferences pre=getSharedPreferences
                (prefname,MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false
        boolean bchk = pre.getBoolean("rate", false);
        int dem = pre.getInt("dem",0);
        if(dem>2 && !bchk) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	//thiết lập tiêu đề cho Dialog
        	builder.setTitle("Quick Charge 3.0");
        	//Thiết lập nội dung cho Dialog
        	builder.setMessage("Thank for using my app !");
        	//để thiết lập Icon
        	builder.setIcon(R.drawable.ic_launcher);
        	 
        	builder.setPositiveButton("Rate App", new DialogInterface.OnClickListener() {
        	    @Override
        	    public void onClick(DialogInterface dialog, int which) {
        	    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
                    startActivity(browserIntent);
                    savingPreferences();
                    finish();
        	    }
        	});
        	
        	builder.setNegativeButton("Late", new DialogInterface.OnClickListener() {
        	    @Override
        	    public void onClick(DialogInterface dialog, int which) {
        	    	finish();
        	    }
        	});
        	 
        	builder.create().show();
     
        }else {
        	exitApp2(mainActivity, "Click once again to close app ");
            
        }

    }

	@Override
	public void onResume() {
		if (SharedPreference.readExit(getApplicationContext())) {
			SharedPreference.saveExit(getApplicationContext(), false);
			finish();
		}
		if (SharedPreference.readCool(getApplicationContext())) {
			SharedPreference.saveCool(getApplicationContext(), false);
			tvStatus.setText(R.string.cool_cpu);
			tvStatus.setTextColor(getResources().getColor(android.R.color.white));
		}
		registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(batteryInfoReceiver);
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		switch (id) {
		case R.id.action_more_app:
		//	WAdsSmartList();
			break;
		case R.id.action_rate_app:
			WRateApp();
			break;
		case R.id.action_share_app:
			WShareApp("Cooler master!");
			break;

		case R.id.action_feed_back:
			WSendFeedBack("lta1292@gmail.com", "Your subject to Cooling App");
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
