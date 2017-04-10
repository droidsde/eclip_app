package com.batterydoctor.superfastcharger.fastcharger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wolfsoftlib.com.Json.WCheckNetworkConnection;
import wolfsolflib.com.makemoney.Admob;
import wolfsolflib.com.makemoney.Upgrade;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.fastercharger.ActionSeting;
import com.batterydoctor.superfastcharger.fastcharger.fastercharger.ChargerSetting;
import com.batterydoctor.superfastcharger.fastcharger.fastercharger.Constant;
import com.batterydoctor.superfastcharger.fastcharger.fastercharger.ProgressWheel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

@TargetApi(23) public class ToolboxActivity extends FragmentActivity {

	private ImageView imgbtnBattery, imgbtnMode, imgbtnbatttery_Details,
			imgbtnTask_Killer;
	private LinearLayout llBattery, llCharge, llMode, llbatttery_Details,
			llTask_Killer;
	private SharedPreferences preferences;
	private Editor editor;
	private int devicesize_flag;
	private FrameLayout llfillrect;
	private LinearLayout llbetteryimage;
	private int paramswidhtvaleu;
	private Intent intentBatteryUsage;
	private int currentPercent;
	private TextView txtbattery_text, txtimgchargeicon;
	private int health, icon_small, level = 0, plugged;
	private boolean present;
	private String technology;
	private int scale, status, temperature, voltage;
	ProgressBarAsyncTask mAsyncTask;

	private Animation slideRight;
	private LinearLayout llbtcharge;
	private ImageView imgchargeicon;
	private Animation fade_in_fade_out;
	private ImageView imgfastcharge_circle, imgfastcharge;
	private LinearLayout imgfastcharge_line, imgfullcharge_line;
	private ImageView imgfullcharge_circle, imgfullcharge;
	private ImageView imgtricklecharge_circle, imgtricklecharge;
	private TextView txtfastcharge_title, txtfastcharge, txtfullcharge_title,
			btBattery;
	private TextView txtfullcharge, txttricklecharge_title, txttricklecharge;
	private Animation rotate;
	private Timer timer;
	private TimerTask timerTask;
	private Handler handler;
	private Intent charge_serviceIntent;
	ProgressWheel wheel;
	Ringtone r;
	Uri sound;
	boolean asyncIsRunning = false;
	Admob ab;
	AdView adView;
	private ArrayList<String> TEST_DEVICE = new ArrayList<String>();
	String prefname = "faster charge";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_charge);
		//permision runtime
		initPermission();
		
		displaySmartBanner(R.id.lineAds1);
		//ab = new Admob(((AppCompatActivity) ToolboxActivity).ContentValueApp.AD_UNIT_ID);
		rotate = AnimationUtils.loadAnimation(this,
				R.anim.fade_in_fade_out);
		// llbtcharge.startAnimation(slideRight);
		// overridePendingTransition(R.anim.slide_in_right,
		// R.anim.slide_out_left);
		handler = new Handler();
		this.sound = RingtoneManager.getDefaultUri(2);
		this.r = RingtoneManager.getRingtone(this, this.sound);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		editor = preferences.edit();

		devicesize_flag = preferences.getInt("devicesize_flag", 0);

		txtbattery_text = (TextView) findViewById(R.id.txtbattery_text);
		txtimgchargeicon = (TextView) findViewById(R.id.txtimgchargeicon);
		imgchargeicon = (ImageView) findViewById(R.id.imgchargeicon);

		btBattery = (TextView) findViewById(R.id.startCharging);

		imgfastcharge_circle = (ImageView)findViewById(R.id.imgfastcharge_circle);
		imgfastcharge = (ImageView) findViewById(R.id.imgfastcharge);
		imgfastcharge_line = (LinearLayout)findViewById(R.id.imgfastcharge_line);

		imgfullcharge_circle = (ImageView)findViewById(R.id.imgfullcharge_circle);
		imgfullcharge = (ImageView) findViewById(R.id.imgfullcharge);
		imgfullcharge_line = (LinearLayout)findViewById(R.id.imgfullcharge_line);

		imgtricklecharge_circle = (ImageView)findViewById(R.id.imgtricklecharge_circle);
		imgtricklecharge = (ImageView) findViewById(R.id.imgtricklecharge);

		txtfastcharge_title = (TextView)findViewById(R.id.txtfastcharge_title);
		txtfastcharge = (TextView) findViewById(R.id.txtfastcharge);
		txtfullcharge_title = (TextView)findViewById(R.id.txtfullcharge_title);
		txtfullcharge = (TextView) findViewById(R.id.txtfullcharge);
		txttricklecharge_title = (TextView)findViewById(R.id.txttricklecharge_title);
		txttricklecharge = (TextView) findViewById(R.id.txttricklecharge);
		wheel = (ProgressWheel) findViewById(R.id.progressBarTwo);

		llfillrect = (FrameLayout) findViewById(R.id.llfillrect);
		llbetteryimage = (LinearLayout) findViewById(R.id.llbetteryimage);
		LayoutParams params = llfillrect.getLayoutParams();
		paramswidhtvaleu = params.width;
		
		intEvent();
		wheel.setBarColor(Color.parseColor("#62bdea"));
		statusButton();
		
		//displayInterstitial();
		
//		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//		        if (!Settings.System.canWrite(getApplicationContext())) {
//		            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
//		            startActivityForResult(intent, 200);
//
//		        }
//		    }
		
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
	
	

    @TargetApi(23) public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_SETTINGS)) {
                    Toast.makeText(ToolboxActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(ToolboxActivity.this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.WRITE_SETTINGS}, 1);

            }
        }
    }
	
	private AdRequest getAdRequest() {
		AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
		
		return adRequestBuilder.build();
	}
	public void displaySmartBanner(int LayoutAdsId) {
		if (!Upgrade.isPremium(ToolboxActivity.this)) {
			adView = new AdView(ToolboxActivity.this);
			adView.setAdSize(AdSize.BANNER);
			adView.setAdUnitId("ca-app-pub-7887468927194397/5041585466");
			final LinearLayout ads = (LinearLayout)findViewById(LayoutAdsId);
			
			ads.removeAllViews();
			ads.addView(adView);
			if(!WCheckNetworkConnection.isConnectionAvailable(this))
				ads.setVisibility(View.GONE);
			AdRequest adRequest = getAdRequest();
			adView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					super.onAdLoaded();Log.d("APPNENENEN", "banner display");
					
				}
			});
			adView.loadAd(adRequest);
			
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
		
			PowerMgrTabActivity powerMgrTabActivity = (PowerMgrTabActivity) getParent();
			powerMgrTabActivity.setTitle(R.string.title_tab_toolbox);
		

		if (Constant.btInfo_Data.getLevel() != 0
				&& Constant.btInfo_Data.getScale() != 0) {
			baterrylevel(Constant.btInfo_Data.getLevel(),
					Constant.btInfo_Data.getScale());
		}

		intentBatteryUsage = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);

		llbetteryimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(intentBatteryUsage);

			}
		});

		this.registerReceiver(mBatchargeReceiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		ChargerSetting.saveEnable(this, false);
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
        	    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.batterydoctor.superfastcharger.fastcharger"));
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
        	super.onBackPressed();
            
        }
	}

	private BroadcastReceiver mBatchargeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context c, Intent intent) {
			int i = intent.getIntExtra("plugged", -1);
			if (i == 0) {

				ChargerSetting.saveCharger(c, false);
				statusButton();
			}

			health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
			Log.e("health", health + "");

			icon_small = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0);
			Log.e("icon_small", icon_small + "");

			plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
			Log.e("plugged", plugged + "");

			present = intent.getExtras().getBoolean(
					BatteryManager.EXTRA_PRESENT);
			Log.e("present", present + "");

			technology = intent.getExtras().getString(
					BatteryManager.EXTRA_TECHNOLOGY);
			Log.e("technology", technology + "");

			temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,
					0);
			Log.e("temperature", temperature + "");

			voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
			Log.e("voltage", voltage + "");

			level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			Log.e("level", level + "");

			status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
			Log.e("status", status + "");
			scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
			Log.e("scale", scale + "");

			currentPercent = level * 100 / scale;

			Constant.btInfo_Data.setHealth(health);
			Constant.btInfo_Data.setLevel(level);
			Constant.btInfo_Data.setPlugged(plugged);
			Constant.btInfo_Data.setScale(scale);
			Constant.btInfo_Data.setStatus(status);
			Constant.btInfo_Data.setTechnology(technology);
			Constant.btInfo_Data.setTemperature(temperature);
			Constant.btInfo_Data.setVoltage(voltage);

			Constant.bat_plugged_hashTable.put("0", "Unknown");
			Constant.bat_plugged_hashTable.put("1", "AC");
			Constant.bat_plugged_hashTable.put("2", "USB");
			Constant.bat_plugged_hashTable.put("3", "Unknown");
			Constant.bat_plugged_hashTable.put("4", "Wireless");

			baterrylevel(Constant.btInfo_Data.getLevel(),
					Constant.btInfo_Data.getScale());

		}

	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (adView != null) {
			adView.destroy();
		}
		try {

			if (charge_serviceIntent != null) {
				this.stopService(charge_serviceIntent);
			}

		} catch (Exception e) {
			// TODO: handle exception

		}

		try {

			if (mBatchargeReceiver != null) {
				this.unregisterReceiver(mBatchargeReceiver);
			}

		} catch (Exception e) {
			// TODO: handle exception

		}

		super.onDestroy();

	}

	// @Override
	// public void onBackPressed() {
	// // TODO Auto-generated method stub
	// super.onBackPressed();
	//
	// finish();
	//
	// }

	private void baterrylevel(int level, int scale2) {
		// TODO Auto-generated method stub

		Log.e("level", level + "");
		Log.e("scale2", scale2 + "");

		LayoutParams params = llfillrect.getLayoutParams();
		Log.e("params width", paramswidhtvaleu + "");
		int batlevel = (level * paramswidhtvaleu) / scale2;
		params.width = batlevel;
		Log.e("params width", params.width + "");
		llfillrect.setLayoutParams(params);

		if (level != 00 && scale2 != 00) {
			currentPercent = level * 100 / scale2;
			txtbattery_text.setText(currentPercent + "%");
		}

		if (Constant.btInfo_Data != null
				&& Constant.bat_plugged_hashTable != null) {
			if (Constant.btInfo_Data.getPlugged() == 0
					|| Constant.btInfo_Data.getPlugged() == 3) {
				txtimgchargeicon.setVisibility(TextView.GONE);
				imgchargeicon.clearAnimation();
				imgchargeicon.setVisibility(ImageView.GONE);

				txtfastcharge.setText(getString(R.string.Waiting));
				txtfullcharge.setText(getString(R.string.Waiting));
				txttricklecharge.setText(getString(R.string.Waiting));

				imgfastcharge_circle.setImageResource(R.drawable.circle_33);
				imgfullcharge_circle.setImageResource(R.drawable.circle_33);
				imgtricklecharge_circle.setImageResource(R.drawable.circle_33);
				imgfastcharge.setImageResource(R.drawable.fast_charge_32);
				imgfullcharge.setImageResource(R.drawable.full_charge_32);
				imgtricklecharge.setImageResource(R.drawable.trickle_32);
				imgfastcharge_line.setBackgroundColor(getResources().getColor(
						R.color.primary));
				imgfullcharge_line.setBackgroundColor(getResources().getColor(
						R.color.primary));
				imgfastcharge_circle.setAnimation(null);
				imgfullcharge_circle.setAnimation(null);
				imgtricklecharge_circle.setAnimation(null);
			} else {

				txtimgchargeicon.setVisibility(TextView.VISIBLE);
				imgchargeicon.setVisibility(ImageView.VISIBLE);
				int intplugged = Constant.btInfo_Data.getPlugged();
				String strplugged = Constant.bat_plugged_hashTable
						.get(intplugged + "");
				txtimgchargeicon.setText(strplugged);

				if (strplugged.equalsIgnoreCase("AC")) {
					imgchargeicon.setImageResource(R.drawable.plug_32);
				} else if (strplugged.equalsIgnoreCase("USB")) {
					imgchargeicon.setImageResource(R.drawable.usb_32);
				} else if (strplugged.equalsIgnoreCase("Wireless")) {
					imgchargeicon.setImageResource(R.drawable.wifi36);
				}

				fade_in_fade_out = AnimationUtils.loadAnimation(this,
						R.anim.fade_in_fade_out);
				imgchargeicon.startAnimation(fade_in_fade_out);

				if (currentPercent <= 80) {
					txtfastcharge.setText(getString(R.string.Ongoing));
					imgfastcharge_circle.setImageResource(R.drawable.circle_33);
					imgfastcharge_circle.setAnimation(rotate);
					imgfastcharge.setImageResource(R.drawable.fast_charge_32);
				} else if (currentPercent < 100) {
					txtfastcharge.setText(getString(R.string.Finished));
					txtfullcharge.setText(getString(R.string.Ongoing));
					imgfastcharge_circle.setImageResource(R.drawable.circle_33);
					imgfullcharge_circle.setImageResource(R.drawable.circle_33);
					imgfullcharge_circle.setAnimation(rotate);
					imgfastcharge.setImageResource(R.drawable.fast_charge_32);
					imgfullcharge.setImageResource(R.drawable.full_charge_32);
					imgfastcharge_line.setBackgroundColor(getResources()
							.getColor(R.color.primary));

				} else {
					txtfastcharge.setText(getString(R.string.Finished));
					txtfullcharge.setText(getString(R.string.Finished));
					txttricklecharge.setText(getString(R.string.Ongoing));
					imgfastcharge_circle.setImageResource(R.drawable.circle_33);
					imgfullcharge_circle.setImageResource(R.drawable.circle_33);
					imgtricklecharge_circle
							.setImageResource(R.drawable.circle_33);
					imgtricklecharge_circle.setAnimation(rotate);
					imgfastcharge.setImageResource(R.drawable.fast_charge_32);
					imgfullcharge.setImageResource(R.drawable.full_charge_32);
					imgtricklecharge.setImageResource(R.drawable.trickle_32);
					imgfastcharge_line.setBackgroundColor(getResources()
							.getColor(R.color.primary));
					imgfullcharge_line.setBackgroundColor(getResources()
							.getColor(R.color.primary));
				}

			}

		} else {

			txtimgchargeicon.setVisibility(TextView.GONE);
			imgchargeicon.setVisibility(ImageView.GONE);
		}

	}

	public void readStatusScreen() {
		Settings.System.putInt(this.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
				ChargerSetting.readAuto(this));

	}

public void statusButton(){
		
		if(ChargerSetting.readEnable(this)&&ChargerSetting.readCharger(this)){
			progressWheel_text("STOP");
			wheel.setProgress(360);
		}else{
		
		progressWheel_text("START");
		wheel.setProgress(0);
		
		}
	}

	public void intEvent() {
		wheel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!asyncIsRunning){
					if(ChargerSetting.readEnable(ToolboxActivity.this)){ 
						readStatusScreen();
						ChargerSetting.saveEnable(ToolboxActivity.this, false);
						statusButton();
					} else{
						if(mAsyncTask==null){
							mAsyncTask = new ProgressBarAsyncTask();
							mAsyncTask.execute();
							asyncIsRunning = true;
							
						}	
					}
						
					
				} 	
					
			}
		});
	}

	public void saveStatusScreen() {

		ChargerSetting.saveAuto(this, Settings.System.getInt(
				this.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				ChargerSetting.readAuto(this)));
	}

	public void progressWheel_text(final String paramString) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				btBattery.setText(paramString);

			}
		});
	}

	public void killApps(Context paramContext) {
		List localList = paramContext.getPackageManager()
				.getInstalledApplications(0);
		ActivityManager localActivityManager = (ActivityManager) paramContext
				.getSystemService("activity");
		Iterator localIterator = localList.iterator();
		while (true) {
			if (!localIterator.hasNext()) {
				return;
			}
			ApplicationInfo localApplicationInfo = (ApplicationInfo) localIterator
					.next();
			if (localApplicationInfo.packageName.equals(paramContext
					.getPackageName()))
				continue;
			localActivityManager
					.killBackgroundProcesses(localApplicationInfo.packageName);
		}
	}

	private void setMobileDataEnabled(Context context, boolean enabled) {
		try {
			final ConnectivityManager conman = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final Class conmanClass = Class
					.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass
					.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField
					.get(conman);
			final Class iConnectivityManagerClass = Class
					.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);

			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getSupperCharger() {
		killApps(this);
		((WifiManager) this.getSystemService("wifi"))
				.setWifiEnabled(false);
		BluetoothAdapter btEnable = BluetoothAdapter.getDefaultAdapter();
		if(btEnable!=null){
			btEnable.disable();
		}
		
		setMobileDataEnabled(this, false);
		// gps

	}

	public void setStatusScreen() {
		Settings.System.putInt(this.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE, 1);

	}
	public void setStatus(int mp){
		
			if(mp<=40){
				progressWheel_text("Checking...");
				
			}
			if(mp==60){
				progressWheel_text("Wifi...");
					ActionSeting.swwifi(this, false);
				
			}
		
			if(mp==100){
				progressWheel_text("Bluetooth...");
					ActionSeting.swBluethooth(this, false);
			}
			
			if(mp==120){
				progressWheel_text("3G...");
			}
			if(mp==180){
				progressWheel_text("Bright...");
					setStatusScreen();
				
			}
			if(mp==240){
				progressWheel_text("Optimizations...");
				killApps(this);
				
			}
		
	}


	public class ProgressBarAsyncTask extends AsyncTask<Void, Integer, Void> {
		int myProgress = 0;
		float size;

		@Override
		protected Void doInBackground(Void... params) {
			while(myProgress<361){
				 myProgress = 1+ myProgress;
				 SystemClock.sleep(25L);
				 publishProgress(myProgress);
					 wheel.incrementProgress();	
				
				 
				
			}
			 
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			((Vibrator)ToolboxActivity.this.getSystemService("vibrator")).vibrate(30L);
			r.play();
			
		
				progressWheel_text("STOP");
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			        if (!Settings.System.canWrite(getApplicationContext())) {
			            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
			            startActivityForResult(intent, 200);
			            
			        }else{
			        	getSupperCharger();
			        }
			    }else{
			    	getSupperCharger();
			    }
				setStatusScreen();
				ChargerSetting.saveEnable(ToolboxActivity.this, true);
			asyncIsRunning = false;
			mAsyncTask =null;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			 myProgress = 0 ;
//			 wheel.setProgress(0);
			 saveStatusScreen();

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			setStatus(values[0]);

		}

	}
	

	
	@Override
	protected void onPause() {
		super.onPause();
		if (adView != null) {
			adView.pause();
		}
	}
	
	

}