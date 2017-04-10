package com.batterydoctor.superfastcharger.fastcharger;

import wolfsolflib.com.makemoney.Upgrade;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.System;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.manager.AbsManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MairplaneMode;
import com.batterydoctor.superfastcharger.fastcharger.manager.MaudioManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MbluetoothManger;
import com.batterydoctor.superfastcharger.fastcharger.manager.MconnectivityManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MgpsManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MscreenManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MscreenOffManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MwifiManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.AbsManager.IManager;
import com.batterydoctor.superfastcharger.fastcharger.mode.ModeFragment;
import com.batterydoctor.superfastcharger.fastcharger.optimize.OptimizeActivity;
import com.batterydoctor.superfastcharger.fastcharger.service.AppService;
import com.batterydoctor.superfastcharger.fastcharger.ui.MyBatteryGraph;
import com.batterydoctor.superfastcharger.fastcharger.ui.MyBatteryInfoDetails;
import com.batterydoctor.superfastcharger.fastcharger.ui.MyHistoryChartView;
import com.batterydoctor.superfastcharger.fastcharger.ui.MyScroolRelativeLayout;
import com.batterydoctor.superfastcharger.fastcharger.ui.MyTimeDisplayDigital;
import com.batterydoctor.superfastcharger.fastcharger.ui.RorateImageView;
import com.batterydoctor.superfastcharger.fastcharger.ui.data.BatteryInfo;
import com.batterydoctor.superfastcharger.fastcharger.ui.fragment.MyScrollRelativeLayoutListener;
import com.nvn.log.LogBuider;

@TargetApi(23) public class PowerBatteryActivity extends Activity implements
		MyScrollRelativeLayoutListener, OnClickListener,OnLongClickListener, IManager {
	// am force-stop package_name_to_stop
	// su am force-stop <PACKAGE>

	
//	private static final String TAG = "com.batterydoctor.superfastcharger.fastcharger.PowerBatteryActivity";
	MairplaneMode airplaneMode;
	MaudioManager audioManager;
	MconnectivityManager connectivityManager;
	MgpsManager gpsManager;
	MscreenManager screenManager;
	MscreenOffManager screenOffManager;
	MbluetoothManger bluetoothManger;
	MwifiManager wifiManager;

	private View mSwitch;
	ImageView btnSwitch;
	TextView switchTip;
	Button optimus;
	MyBatteryInfoDetails myBatteryInfoDetails;
	MyBatteryGraph myBatteryGraph;
	MyHistoryChartView historyChartView;
	MyTimeDisplayDigital timeDisplayDigital;
	MyScroolRelativeLayout myScroolRelativeLayout;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_layout_sroll);
//		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//		        if (!Settings.System.canWrite(getApplicationContext())) {
//		            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
//		            startActivityForResult(intent, 200);
//
//		        }
//		    }
		init();
		initAction();
		initControl();
		// requestPermissionRoot();
	}

	@Override
	protected void onDestroy() {
		wifiManager.removeImanager(this);
		connectivityManager.removeImanager(this);
		super.onDestroy();
	};
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(AppService.ACTION_BATTERY_CHANGED_SEND)){				
				BatteryInfo info = intent.getParcelableExtra(BatteryInfo.BATTERY_INFO_KEY);
				onUpdataStatus(info);
			}
		}
	};
	
	private void init() {
		myScroolRelativeLayout = (MyScroolRelativeLayout) findViewById(R.id.statusMainLayout);
		myBatteryInfoDetails = (MyBatteryInfoDetails) findViewById(R.id.home_mid_area);
		myBatteryGraph = (MyBatteryGraph) findViewById(R.id.battery_graph);
		timeDisplayDigital = (MyTimeDisplayDigital) findViewById(R.id.battery_info_remaining_time_value);
		mSwitch = (View) findViewById(R.id.switchlayout);
		btnSwitch = (ImageView) findViewById(R.id.switchHandle);
		myScroolRelativeLayout.setOnScrollListener(this);
		historyChartView = (MyHistoryChartView) findViewById(R.id.chart);
		optimus = (Button) findViewById(R.id.power_consumption_analysis_button);
		switchTip = (TextView) findViewById(R.id.buttons_tip);
		optimus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PowerBatteryActivity.this,
						OptimizeActivity.class);
				startActivity(intent);
			}
		});
		int i1 = this.mSwitch.getLayoutParams().height;
		this.myScroolRelativeLayout.setmDownMoveMax(i1);
		this.btnSwitch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (myScroolRelativeLayout.isScrollDown()) {
					myScroolRelativeLayout.scrollUp();
				} else {
					myScroolRelativeLayout.scrollDown();
				}
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		PowerMgrTabActivity powerMgrTabActivity = (PowerMgrTabActivity) getParent();
		powerMgrTabActivity.setTitle(R.string.title_tab_home);
		if (historyChartView != null) {
			historyChartView.initHistoryChart();
		}
		Intent intent = new Intent();
		intent.setAction(AppService.ACTION_BATTERY_NEED_UPDATE);
		sendBroadcast(intent);
	}

	@Override
	public void onScrollBy(int paramInt1, int paramInt2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollTo(int paramInt1, int paramInt2) {
		// TODO Auto-generated method stub
		if (this.mSwitch != null)
			this.mSwitch.scrollTo(paramInt1, this.mSwitch.getHeight() / 2
					+ paramInt2 / 2);
	}

	@Override
	public void reset(boolean paramBoolean) {
		// TODO Auto-generated method stub
		if (paramBoolean)
			this.mSwitch.scrollTo(0, 0);
		else
			this.mSwitch.scrollTo(0, this.mSwitch.getHeight() / 2);
	}

	private void initAction() {
		((RelativeLayout) findViewById(R.id.shortcut_linear_wifi))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_data))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_screen))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_screen_timeout))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_bluetooth))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_volume))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_airplane))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_gps))
				.setOnClickListener(this);

		((RelativeLayout) findViewById(R.id.shortcut_linear_wifi))
				.setOnLongClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_data))
				.setOnLongClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_screen))
				.setOnLongClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_screen_timeout))
				.setOnLongClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_bluetooth))
				.setOnLongClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_volume))
				.setOnLongClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_airplane))
				.setOnLongClickListener(this);
		((RelativeLayout) findViewById(R.id.shortcut_linear_gps))
				.setOnLongClickListener(this);
	}

	void initControl() {
		airplaneMode = MairplaneMode.getInstance(this);
		audioManager = MaudioManager.getInstance(this);
		connectivityManager = MconnectivityManager.getInstance(this);
		gpsManager = MgpsManager.getInstance(this);
		screenManager = MscreenManager.getInstance(this);
		screenOffManager = MscreenOffManager.getInstance(this);
		bluetoothManger = MbluetoothManger.getInstance(this);
		wifiManager = MwifiManager.getInstance(this);

		wifiManager.setImanager(this);
		connectivityManager.setImanager(this);
		audioManager.setImanager(this);
		gpsManager.setImanager(this);
		screenManager.setImanager(this);
		screenOffManager.setImanager(this);
		bluetoothManger.setImanager(this);
		airplaneMode.setImanager(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppService.ACTION_BATTERY_CHANGED_SEND);
		registerReceiver(receiver, filter);
	}

	private void onUpdataStatus(BatteryInfo info) {
		boolean isCharging = info.status == BatteryManager.BATTERY_STATUS_CHARGING ||
				info.status == BatteryManager.BATTERY_STATUS_FULL;
		if (myBatteryInfoDetails != null) {
			myBatteryInfoDetails.initView(info);
		}
		if (myBatteryGraph != null) {
			
			if(isCharging){
				((TextView)findViewById(R.id.battery_info_remaining_time_label)).setText(R.string.battery_info_value_charging_timer_left);
			}else{
				((TextView)findViewById(R.id.battery_info_remaining_time_label)).setText(R.string.battery_info_value_timer_left);
			}
			myBatteryGraph.setBatteryLevel(isCharging, info.level);
		}
		if (timeDisplayDigital != null) {
			if(isCharging && info.level == info.scale){
				timeDisplayDigital.setVisibility(View.INVISIBLE);
				((TextView)findViewById(R.id.status)).setVisibility(View.VISIBLE);
			}else{
				((TextView)findViewById(R.id.status)).setVisibility(View.GONE);
				timeDisplayDigital.setVisibility(View.VISIBLE);
				timeDisplayDigital.setTime(info.hourleft, info.minleft);
			}
		}
	}

	@Override
	public void update(AbsManager manager, boolean paramBoolean, int paramInt,
			boolean userChangeMode) {
		// TODO Auto-generated method stub
		if (!userChangeMode) {
			Intent intent = new Intent();
			intent.setAction(ModeFragment.NOTIFY_USER_CHANGE_MODE);
			intent.putExtra(ModeFragment.MODE_CHANGE,
					ModeFragment.MODE_DEFAULT_MYCUSTOM);
			sendBroadcast(intent);
		}
		if (manager instanceof MwifiManager) {
			wifiChage(paramInt);
		} else if (manager instanceof MconnectivityManager) {
			dataChage(paramBoolean);
		} else if (manager instanceof MaudioManager) {
			audioChage(paramInt);
		} else if (manager instanceof MgpsManager) {
			gpsModeChage(paramInt);
		} else if (manager instanceof MscreenManager) {
			screenChage(paramInt);
		} else if (manager instanceof MscreenOffManager) {
			screenOffChage(paramInt);
		} else if (manager instanceof MbluetoothManger) {
			bluetoothChage(paramInt);
		} else if (manager instanceof MairplaneMode) {
			airModeChage(paramInt);
		}
	}

	void wifiChage(int state) {
		switchTip.setVisibility(View.INVISIBLE);
		((RorateImageView) findViewById(R.id.shortcut_img_wifi)).startRorate(state);
		findViewById(R.id.shortcut_linear_wifi).setEnabled(true);
	}

	void audioChage(int state) {
		((RorateImageView) findViewById(R.id.shortcut_img_volume))
				.startRorate(state);
		findViewById(R.id.shortcut_linear_volume).setEnabled(true);
	}

	void dataChage(boolean state) {
		int resId;
		if (state) {
			resId = R.drawable.settings_app_data_on;
		} else {
			resId = R.drawable.settings_app_data_off;
		}
		switchTip.setVisibility(View.INVISIBLE);
		((RorateImageView) findViewById(R.id.shortcut_img_data))
				.startRorate(resId);
		findViewById(R.id.shortcut_linear_data).setEnabled(true);
	}

	void bluetoothChage(int state) {
		((RorateImageView) findViewById(R.id.shortcut_img_bluetooth))
				.startRorate(state);
		switchTip.setVisibility(View.INVISIBLE);
		findViewById(R.id.shortcut_linear_bluetooth).setEnabled(true);
	}

	void airModeChage(int state) {
		((RorateImageView) findViewById(R.id.shortcut_img_airplane))
				.startRorate(state);
		findViewById(R.id.shortcut_linear_airplane).setEnabled(true);
	}

	void gpsModeChage(int state) {
		((RorateImageView) findViewById(R.id.shortcut_img_gps))
				.startRorate(state);
		findViewById(R.id.shortcut_linear_gps).setEnabled(true);
	}

	void screenChage(int state) {
		((RorateImageView) findViewById(R.id.shortcut_img_screen))
				.startRorate(state);
		findViewById(R.id.shortcut_linear_screen).setEnabled(true);
	}

	void screenOffChage(int state) {
		((RorateImageView) findViewById(R.id.shortcut_img_screen_timeout))
				.startRorate(state);
		findViewById(R.id.shortcut_linear_screen_timeout).setEnabled(true);
	}
	int count = 0;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shortcut_linear_wifi:
			boolean state1 = wifiManager.getState();
			switchTip.setVisibility(View.VISIBLE);
			if(state1){
				switchTip.setText(R.string.turning_off);
			}else{
				switchTip.setText(R.string.turning_on);
			}
			wifiManager.setState(!state1, false);
			v.setEnabled(false);
			break;
		case R.id.shortcut_linear_data:
			boolean state = connectivityManager.getState();
			switchTip.setVisibility(View.VISIBLE);
			if(state){
				switchTip.setText(R.string.turning_off);
			}else{
				switchTip.setText(R.string.turning_on);
			}
			connectivityManager
					.setState(!state, false);
			v.setEnabled(false);
			break;
		case R.id.shortcut_linear_screen:
			screenManager.setIntSate(getParent().getWindow());
			v.setEnabled(false);
			break;
		case R.id.shortcut_linear_screen_timeout:
			screenOffManager.setTimerOut();
			v.setEnabled(false);
			break;
		case R.id.shortcut_linear_bluetooth:
			boolean state3 = connectivityManager.getState();
			switchTip.setVisibility(View.VISIBLE);
			if(state3){
				switchTip.setText(R.string.turning_off);
			}else{
				switchTip.setText(R.string.turning_on);
			}
			bluetoothManger.setState(!state3, false);
			v.setEnabled(false);
			break;
		case R.id.shortcut_linear_volume:
			audioManager.setState(!audioManager.getState(), false);
			v.setEnabled(false);
			break;
		case R.id.shortcut_linear_airplane:
			airplaneMode.setState(!airplaneMode.getState(), false);
			v.setEnabled(false);
			break;
		case R.id.shortcut_linear_gps:
			gpsManager.setState(!gpsManager.getState(), false);
			v.setEnabled(false);
			break;
		case R.id.statusMainLayout:

			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (this.myScroolRelativeLayout != null
				&& this.myScroolRelativeLayout.isScrollDown()) {
			this.myScroolRelativeLayout.scrollUp();
			return;
		} else {
			finish();
			// Process.killProcess(Process.myPid());
			// System.exit(0);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shortcut_linear_wifi:
			wifiManager.setLongClick();
			break;
		case R.id.shortcut_linear_data:
			connectivityManager.setLongClick();
			break;
		case R.id.shortcut_linear_screen:
			screenManager.setLongClick();
			break;
		case R.id.shortcut_linear_screen_timeout:
			screenOffManager.setLongClick();
			break;
		case R.id.shortcut_linear_bluetooth:
			bluetoothManger.setLongClick();
			break;
		case R.id.shortcut_linear_volume:
			audioManager.setLongClick();
			break;
		case R.id.shortcut_linear_airplane:
			airplaneMode.setLongClick();
			break;
		case R.id.shortcut_linear_gps:
			gpsManager.setLongClick();
			break;
		}
		return true;
	}
}
