package com.usstudio.easytouch.assistivetouch.dialog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.MainActivity;
import com.usstudio.easytouch.assistivetouch.asyntask.LoadAppAsynTask;
import com.usstudio.easytouch.assistivetouch.asyntask.TaskList;
import com.usstudio.easytouch.assistivetouch.itf.KillProcessDone;
import com.usstudio.easytouch.assistivetouch.itf.LoadAppDone;
import com.usstudio.easytouch.assistivetouch.receiver.MyAdmin;
import com.usstudio.easytouch.assistivetouch.util.App;
import com.usstudio.easytouch.assistivetouch.util.Constants;
import com.usstudio.easytouch.assistivetouch.util.MyHelper;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;
import com.usstudio.easytouch.assistivetouch.util.TaskObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDialog extends Dialog implements android.view.View.OnClickListener, OnSeekBarChangeListener,
		OnCheckedChangeListener, LoadAppDone, OnLongClickListener, KillProcessDone {

	private final int BACKGROUND_ALPHA = 210;

	DevicePolicyManager mDPM;
	ComponentName mAdminName;

	private int currentLayout;
	private ArrayList<TaskObject> taskList;
	private ArrayList<App> apps;
	private AudioManager audioManager;
	private WifiManager wifiManager;
	private BluetoothAdapter bluetoothAdapter;
	private LocationManager locationManager;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	private boolean flashLight_enabled;
	private boolean has_flashLight = true;
	private static Camera cam;
	private Context context;
	private FrameLayout parentLayout;
	private TableLayout taskLayout;
	private LinearLayout taskLayout1;
	private LinearLayout taskLayout2;
	private LinearLayout taskLayout3;
	private LinearLayout taskLayout4;
	private LinearLayout taskLayout5;
	private LinearLayout taskLayout6;
	private LinearLayout taskLayout7;
	private LinearLayout taskLayout8;
	private LinearLayout taskLayout9;
	private ImageView taskImage1;
	private ImageView taskImage2;
	private ImageView taskImage3;
	private ImageView taskImage4;
	private ImageView taskImage5;
	private ImageView taskImage6;
	private ImageView taskImage7;
	private ImageView taskImage8;
	private ImageView taskImage9;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView textView4;
	private TextView textView5;
	private TextView textView6;
	private TextView textView7;
	private TextView textView8;
	private TextView textView9;
	private String[][] data;
	private int currentVersion;

	private TableLayout taskLayoutSetting;
	private LinearLayout taskLayout1Setting;
	private LinearLayout taskLayout2Setting;
	private LinearLayout taskLayout3Setting;
	private LinearLayout taskLayout4Setting;
	private LinearLayout taskLayout5Setting;
	private LinearLayout taskLayout6Setting;
	private LinearLayout taskLayout7Setting;
	private LinearLayout taskLayout8Setting;
	private LinearLayout taskLayout9Setting;
	private ImageView taskImage1Setting;
	private ImageView taskImage2Setting;
	private ImageView taskImage3Setting;
	private ImageView taskImage4Setting;
	private ImageView taskImage5Setting;
	private ImageView taskImage6Setting;
	private ImageView taskImage7Setting;
	private ImageView taskImage8Setting;
	private ImageView taskImage9Setting;
	private TextView textView1Setting;
	private TextView textView2Setting;
	private TextView textView3Setting;
	private TextView textView4Setting;
	private TextView textView5Setting;
	private TextView textView6Setting;
	private TextView textView7Setting;
	private TextView textView8Setting;
	private TextView textView9Setting;
	private String[][] dataSetting;

	private TableLayout brightnessLayout;
	private ImageView backImage;
	private TextView timeoutTextView;
	private SeekBar seekbarBrightness;
	private SeekBar seekbarTimeout;
	private CheckBox checkbox;

	private TableLayout appLayout;
	private LinearLayout appLayout1;
	private LinearLayout appLayout2;
	private LinearLayout appLayout3;
	private LinearLayout appLayout4;
	private LinearLayout appLayout5;
	private LinearLayout appLayout6;
	private LinearLayout appLayout7;
	private LinearLayout appLayout8;
	private LinearLayout appLayout9;
	private ImageView appImage1;
	private ImageView appImage2;
	private ImageView appImage3;
	private ImageView appImage4;
	private ImageView appImage5;
	private ImageView appImage6;
	private ImageView appImage7;
	private ImageView appImage8;
	private ImageView appImage9;
	private TextView appTextView1;
	private TextView appTextView2;
	private TextView appTextView3;
	private TextView appTextView4;
	private TextView appTextView5;
	private TextView appTextView6;
	private TextView appTextView7;
	private TextView appTextView8;
	private TextView appTextView9;
	private String[][] dataApp;
	private ImageView[] listAppImage;
	private TextView[] listAppTextView;
	private LinearLayout[] listTaskLayout;
	private LinearLayout[] listTaskLayoutSetting;
	private LinearLayout animLayout;

	private BroadcastReceiver ringModeChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			if (paramIntent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
				for (int i = 0; i < taskList.size(); i++) {
					if (taskList.get(i).getTaskRes().equals(Constants.VOLUME_OFF)) {
						switch (audioManager.getRingerMode()) {
						case AudioManager.RINGER_MODE_NORMAL:
							taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_volume_up_white);
							break;
						case AudioManager.RINGER_MODE_VIBRATE:
							taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_vibration_white);
							break;
						case AudioManager.RINGER_MODE_SILENT:
							taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_volume_off_white);
							break;

						default:
							break;
						}
					}
				}
			}
		}
	};

	private BroadcastReceiver wifiChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			if (paramIntent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				for (int i = 0; i < taskList.size(); i++) {
					if (taskList.get(i).getTaskRes().equals(Constants.WIFI)) {
						if (wifiManager.isWifiEnabled()) {
							taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_signal_wifi_on_white);
						} else {
							taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_signal_wifi_off_grey);
						}
					}
				}
			}
		}
	};

	private BroadcastReceiver bluetoothChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			if (paramIntent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				updateBluetoothImage();
			}
		}
	};

	private BroadcastReceiver locationChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			if (paramIntent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
				updateLocationImage();
			}
		}
	};

	private BroadcastReceiver airplaneModeChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			if (paramIntent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
				updateAirplaneImage();
			}
		}
	};

	private ContentObserver rotateObserver = new ContentObserver(new Handler()) {

		@Override
		public void onChange(boolean selfChange) {
			updateAutoRotateImage();
		};
	};

	private ContentObserver brightnessObserver = new ContentObserver(new Handler()) {

		@Override
		public void onChange(boolean selfChange) {
			try {
				seekbarBrightness.setProgress(
						Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) * 100
								/ 255);
			} catch (SettingNotFoundException e) {

			}
		};
	};

	private ContentObserver brightnessModeObserver = new ContentObserver(new Handler()) {

		@Override
		public void onChange(boolean selfChange) {
			try {
				checkbox.setChecked(Settings.System.getInt(context.getContentResolver(),
						Settings.System.SCREEN_BRIGHTNESS_MODE) == 1);
			} catch (SettingNotFoundException e) {

			}
		};
	};

	public TaskDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.assistive_task_layout);

		mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mAdminName = new ComponentName(context, MyAdmin.class);

		currentVersion = Build.VERSION.SDK_INT;
		currentLayout = 1;
		taskList = new ArrayList<>();
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		init();
		new LoadAppAsynTask(context, this).execute();

		GradientDrawable bgShape = (GradientDrawable) parentLayout.getBackground();
		bgShape.setColor(Color.parseColor(SharedPreference.readBgColor(context)));
		bgShape.setAlpha(BACKGROUND_ALPHA);
		context.registerReceiver(ringModeChangeReceiver, new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION));
		context.registerReceiver(wifiChangeReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
		context.registerReceiver(locationChangeReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
		context.registerReceiver(bluetoothChangeReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
		context.registerReceiver(airplaneModeChangeReceiver, new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
		context.getContentResolver().registerContentObserver(
				Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), true, rotateObserver);
		context.getContentResolver().registerContentObserver(
				Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, brightnessObserver);
		context.getContentResolver().registerContentObserver(
				Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE), true, brightnessModeObserver);
		updateLocationImage();
		updateBluetoothImage();
		updateAirplaneImage();
		updateAutoRotateImage();
		if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			has_flashLight = false;
		} else {
			flashLight_enabled = SharedPreference.readFlashLight(context);
			for (int i = 0; i < taskList.size(); i++) {
				if (taskList.get(i).getTaskRes().equals(Constants.FLASH)) {
					if (flashLight_enabled) {
						taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_flash_on_white);
					} else {
						taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_flash_off_grey);
					}
				}
			}
		}
	}

	@Override
	protected void onStop() {
		context.unregisterReceiver(ringModeChangeReceiver);
		context.unregisterReceiver(wifiChangeReceiver);
		context.unregisterReceiver(locationChangeReceiver);
		context.unregisterReceiver(bluetoothChangeReceiver);
		context.unregisterReceiver(airplaneModeChangeReceiver);
		context.getContentResolver().unregisterContentObserver(rotateObserver);
		context.getContentResolver().unregisterContentObserver(brightnessObserver);
		context.getContentResolver().unregisterContentObserver(brightnessModeObserver);
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.task_layout1:
			onProcessClick(data[0][0], 1);
			break;
		case R.id.task_layout2:
			onProcessClick(data[1][0], 2);
			break;
		case R.id.task_layout3:
			onProcessClick(data[2][0], 3);
			break;
		case R.id.task_layout4:
			onProcessClick(data[3][0], 4);
			break;
		case R.id.task_layout5:
			onProcessClick(data[4][0], 5);
			break;
		case R.id.task_layout6:
			onProcessClick(data[5][0], 6);
			break;
		case R.id.task_layout7:
			onProcessClick(data[6][0], 7);
			break;
		case R.id.task_layout8:
			onProcessClick(data[7][0], 8);
			break;
		case R.id.task_layout9:
			onProcessClick(data[8][0], 9);
			break;
		case R.id.task_layout1_setting:
			onProcessClick(dataSetting[0][0], 1);
			break;
		case R.id.task_layout2_setting:
			onProcessClick(dataSetting[1][0], 2);
			break;
		case R.id.task_layout3_setting:
			onProcessClick(dataSetting[2][0], 3);
			break;
		case R.id.task_layout4_setting:
			onProcessClick(dataSetting[3][0], 4);
			break;
		case R.id.task_layout5_setting:
			onProcessClick(dataSetting[4][0], 5);
			break;
		case R.id.task_layout6_setting:
			onProcessClick(dataSetting[5][0], 6);
			break;
		case R.id.task_layout7_setting:
			onProcessClick(dataSetting[6][0], 7);
			break;
		case R.id.task_layout8_setting:
			onProcessClick(dataSetting[7][0], 8);
			break;
		case R.id.task_layout9_setting:
			onProcessClick(dataSetting[8][0], 9);
			break;
		case R.id.app_layout1_setting:
			onProcessClick(dataApp[0][0], 1);
			break;
		case R.id.app_layout2_setting:
			onProcessClick(dataApp[1][0], 2);
			break;
		case R.id.app_layout3_setting:
			onProcessClick(dataApp[2][0], 3);
			break;
		case R.id.app_layout4_setting:
			onProcessClick(dataApp[3][0], 4);
			break;
		case R.id.app_layout5_setting:
			onProcessClick(dataApp[4][0], 5);
			break;
		case R.id.app_layout6_setting:
			onProcessClick(dataApp[5][0], 6);
			break;
		case R.id.app_layout7_setting:
			onProcessClick(dataApp[6][0], 7);
			break;
		case R.id.app_layout8_setting:
			onProcessClick(dataApp[7][0], 8);
			break;
		case R.id.app_layout9_setting:
			onProcessClick(dataApp[8][0], 9);
			break;
		case R.id.backImage:
			onProcessClick(Constants.BACK, -1);
			break;

		default:
			break;
		}
	}

	private void initLayout(LinearLayout taskLayout, ImageView taskImage, TextView textView, String taskRes,
			String taskTitle) {
		if (taskRes.equals(Constants.NEW)) {
			taskLayout.setVisibility(View.INVISIBLE);
		} else {
			taskImage.setImageResource(
					context.getResources().getIdentifier(taskRes, "drawable", context.getPackageName()));
			textView.setText(taskTitle);
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void initLayoutApp(ImageView taskImage, TextView textView, String appAction, int pos) {

		if (appAction.equals(Constants.NEW)) {
			taskImage.setImageResource(
					context.getResources().getIdentifier(appAction, "drawable", context.getPackageName()));
			textView.setText("");
			if (currentVersion >= Build.VERSION_CODES.JELLY_BEAN) {
				taskImage.setBackground(ContextCompat.getDrawable(context, R.drawable.dash_border_bg));
			} else {
				taskImage.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dash_border_bg));
			}
		} else if (appAction.equals(Constants.BACK)) {
			taskImage.setImageResource(
					context.getResources().getIdentifier(appAction, "drawable", context.getPackageName()));
			taskImage.setBackgroundColor(Color.TRANSPARENT);
		} else if (appAction.equals(Constants.APP)) {
			taskImage.setBackgroundColor(Color.TRANSPARENT);
			try {
				taskImage.setImageBitmap(MyHelper.loadBitmap(context, dataApp[pos][1]));
				textView.setText(dataApp[pos][2]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void onProcessClick(String taskRes, final int page) {
		switch (taskRes) {
		case Constants.HOME:
			homeAction();
			break;
		case Constants.SETTING:
			if (currentLayout != 2) {
				changeLayoutAction(taskLayout, taskLayoutSetting);
				currentLayout = 2;
			}
			break;
		case Constants.LOCK:
			if (!mDPM.isAdminActive(mAdminName)) {
				Intent intent = new Intent(context, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intent.putExtra("adminRequired", true);
				context.startActivity(intent);
			} else {
				mDPM.lockNow();
			}
			dismiss();
			break;
		case Constants.STAR:
			if (currentLayout != 4) {
				changeLayoutAction(taskLayout, appLayout);
				currentLayout = 4;
			}
			break;
		case Constants.LOCATION:
			Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(locationIntent);
			dismiss();
			break;
		case Constants.WIFI:
			if (wifiManager.isWifiEnabled()) {
				wifiManager.setWifiEnabled(false);
			} else {
				wifiManager.setWifiEnabled(true);
			}
			break;
		case Constants.AIRPLANE:
			Intent airplaneIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
			airplaneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(airplaneIntent);
			dismiss();
			break;
		case Constants.BLUETOOTH:
			if (bluetoothAdapter.isEnabled()) {
				bluetoothAdapter.disable();
			} else {
				bluetoothAdapter.enable();
				Toast.makeText(context, context.getString(R.string.bluetooth_is_enabling), Toast.LENGTH_SHORT).show();
			}
			break;
		case Constants.ORIENTATION:
			if (isAutoRotateModeOn(context)) {
				Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
			} else {
				Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
			}
			break;
		case Constants.CLEAR:
			// TODO
			if (animLayout == null) {
				if ((Calendar.getInstance().getTimeInMillis() - SharedPreference.readTime(context) < 60000) 
						&& !SharedPreference.readClean(context)) {
					Toast.makeText(context, context.getString(R.string.phone_boosted), Toast.LENGTH_SHORT).show();
				} else {
					SharedPreference.saveTime(context, Calendar.getInstance().getTimeInMillis());
					SharedPreference.saveClean(context, false);
					new TaskList(context, this).execute();
					new CountDownTimer(60000, 60000) {

						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							SharedPreference.saveClean(context, true);
						}

					}.start();
					AnimationSet animSet = new AnimationSet(true);
					Animation anim1 = AnimationUtils.loadAnimation(context, R.anim.fade_alpha);
					Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.pulse_alpha);
					animSet.addAnimation(anim1);
					animSet.addAnimation(anim2);
					if (currentLayout == 1) {
						listTaskLayout[page - 1].startAnimation(animSet);
						animLayout = listTaskLayout[page - 1];
					} else if (currentLayout == 2) {
						listTaskLayoutSetting[page - 1].startAnimation(animSet);
						animLayout = listTaskLayoutSetting[page - 1];
					}
				}
			} else {
				Toast.makeText(context, context.getString(R.string.task_is_running), Toast.LENGTH_SHORT).show();
			}
			break;
		case Constants.FLASH:
			flashLightAction();
			break;
		case Constants.VOLUME_OFF:
			soundModeAction();
			break;
		case Constants.VOLUME_UP:
			volumeUpAction();
			break;
		case Constants.VOLUME_DOWN:
			volumeDownAction();
			break;
		case Constants.BRIGHTNESS:
			if (currentLayout != 3) {
				changeLayoutAction(taskLayout, brightnessLayout);
				currentLayout = 3;
			}
			break;
		case Constants.RECENT:
			openRecentAction();
			break;
		case Constants.BACK:
			if (currentLayout == 2) {
				changeLayoutAction(taskLayoutSetting, taskLayout);
			} else if (currentLayout == 3) {
				changeLayoutAction(brightnessLayout, taskLayout);
			} else if (currentLayout == 4) {
				changeLayoutAction(appLayout, taskLayout);
			}
			currentLayout = 1;
			break;
		case Constants.NEW:
			if (apps != null) {
				AppSettingDialog dialog = new AppSettingDialog(context, apps, page);
				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						try {
							dataApp[page - 1] = SharedPreference.readAppPage(context, page)
									.split(Constants.SPECIAL_CHAR);
							initLayoutApp(listAppImage[page - 1], listAppTextView[page - 1], dataApp[page - 1][0],
									page - 1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				dialog.show();
			}
			break;
		case Constants.APP:
			try {
				// Toast.makeText(context, "" + dataApp[appPage - 1][1],
				// Toast.LENGTH_SHORT).show();
				MyHelper.openApp(context, dataApp[page - 1][1]);
				dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	private void changeLayoutAction(final TableLayout beforeLayout, final TableLayout afterLayout) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.pulse);
		anim.setDuration((SharedPreference.readPageChangeSpeed(context) + 1) * 100);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				beforeLayout.setVisibility(View.INVISIBLE);
				Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.pulse_reverse);
				anim2.setDuration((SharedPreference.readPageChangeSpeed(context) + 1) * 100);
				anim2.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						afterLayout.setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {

					}
				});
				afterLayout.startAnimation(anim2);
			}
		});
		beforeLayout.startAnimation(anim);
	}

	private void homeAction() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(startMain);
		dismiss();
	}

	private void volumeUpAction() {
		audioManager.setStreamVolume(AudioManager.STREAM_RING,
				audioManager.getStreamVolume(AudioManager.STREAM_RING) + 1,
				AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 2, AudioManager.FLAG_ALLOW_RINGER_MODES);
	}

	private void volumeDownAction() {
		if (audioManager.getStreamVolume(AudioManager.STREAM_RING) > 0) {
			audioManager.setStreamVolume(AudioManager.STREAM_RING,
					audioManager.getStreamVolume(AudioManager.STREAM_RING) - 1, AudioManager.FLAG_ALLOW_RINGER_MODES
							| AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_VIBRATE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_RING) == 0) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_ALLOW_RINGER_MODES);
			} else {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 2,
						AudioManager.FLAG_ALLOW_RINGER_MODES);
			}
		} else {
			audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_ALLOW_RINGER_MODES
					| AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_VIBRATE);
		}
	}

	private void openRecentAction() {
		try {
			Class<?> serviceManagerClass = Class.forName("android.os.ServiceManager");
			Method getService = serviceManagerClass.getMethod("getService", String.class);
			IBinder retbinder = (IBinder) getService.invoke(serviceManagerClass, "statusbar");
			Class<?> statusBarClass = Class.forName(retbinder.getInterfaceDescriptor());
			Object statusBarObject = statusBarClass.getClasses()[0].getMethod("asInterface", IBinder.class).invoke(null,
					new Object[] { retbinder });
			Method clearAll = statusBarClass.getMethod("toggleRecentApps");
			clearAll.setAccessible(true);
			clearAll.invoke(statusBarObject);
			dismiss();
		} catch (Exception e) {
			Toast.makeText(context, context.getResources().getString(R.string.device_not_support), Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void updateLocationImage() {
		try {
			gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskRes().equals(Constants.LOCATION)) {
				if (!gps_enabled && !network_enabled) {
					taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_location_off_grey);
				} else {
					taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_location_on_white);
				}
			}
		}
	}

	private void updateAirplaneImage() {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskRes().equals(Constants.AIRPLANE)) {
				if (isAirplaneModeOn(context)) {
					taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_airplanemode_on_white);
				} else {
					taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_airplanemode_off_grey);
				}
			}
		}
	}

	private void updateAutoRotateImage() {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskRes().equals(Constants.ORIENTATION)) {
				if (isAutoRotateModeOn(context)) {
					taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_screen_rotation_white);
					taskList.get(i).getTextView().setText(context.getResources().getString(R.string.auto_rotate));
				} else {
					taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_stay_current_portrait_white);
					taskList.get(i).getTextView().setText(context.getResources().getString(R.string.potrait));
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	private boolean isAirplaneModeOn(Context context) {
		if (currentVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
		} else {
			return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
		}
	}

	private boolean isAutoRotateModeOn(Context context) {
		return Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) != 0;
	}

	private void updateBluetoothImage() {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskRes().equals(Constants.BLUETOOTH)) {
				if (bluetoothAdapter.isEnabled()) {
					taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_bluetooth_white);
				} else {
					taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_bluetooth_disabled_grey);
				}
			}
		}
	}

	private void soundModeAction() {
		switch (audioManager.getRingerMode()) {
		case AudioManager.RINGER_MODE_SILENT:
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			break;
		case AudioManager.RINGER_MODE_NORMAL:
			audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			break;
		default:
			break;
		}
	}

	private void flashLightAction() {
		if (has_flashLight) {
			// try {
			if (flashLight_enabled) {
				if (cam == null) {
					cam = Camera.open();
				}
				Parameters params = cam.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_OFF);
				cam.setParameters(params);
				cam.stopPreview();
				cam.release();
				cam = null;
				flashLight_enabled = false;
				SharedPreference.saveFlashLight(context, flashLight_enabled);
				for (int i = 0; i < taskList.size(); i++) {
					if (taskList.get(i).getTaskRes().equals(Constants.FLASH)) {
						taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_flash_off_grey);
					}
				}
			} else {
				cam = Camera.open();
				// cam.setPreviewTexture(new SurfaceTexture(0));
				cam.startPreview();
				Parameters params = cam.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				cam.setParameters(params);
				flashLight_enabled = true;
				SharedPreference.saveFlashLight(context, flashLight_enabled);
				for (int i = 0; i < taskList.size(); i++) {
					if (taskList.get(i).getTaskRes().equals(Constants.FLASH)) {
						taskList.get(i).getTaskImage().setImageResource(R.drawable.ic_flash_on_white);
					}
				}
			}
			// } catch (Exception e) {
			// Toast.makeText(context,
			// context.getResources().getString(R.string.device_not_support),
			// Toast.LENGTH_SHORT).show();
			// }
		} else {
			Toast.makeText(context, context.getResources().getString(R.string.device_not_support), Toast.LENGTH_SHORT)
					.show();
		}
	}

	private int getScreenTimeout() {
		int time = 15000;
		try {
			time = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		timeoutTextView.setText(time / 1000 + " " + context.getResources().getString(R.string.sec));
		switch (time) {
		case 15000:
			return 0;
		case 30000:
			return 1;
		case 60000:
			return 2;
		case 120000:
			return 3;
		case 300000:
			return 4;
		case 600000:
			return 5;
		case 900000:
			return 6;
		default:
			return 0;
		}
	}

	private void init() {
		parentLayout = (FrameLayout) findViewById(R.id.parent_layout);
		taskLayout = (TableLayout) findViewById(R.id.task_layout);
		taskLayout1 = (LinearLayout) findViewById(R.id.task_layout1);
		taskLayout2 = (LinearLayout) findViewById(R.id.task_layout2);
		taskLayout3 = (LinearLayout) findViewById(R.id.task_layout3);
		taskLayout4 = (LinearLayout) findViewById(R.id.task_layout4);
		taskLayout5 = (LinearLayout) findViewById(R.id.task_layout5);
		taskLayout6 = (LinearLayout) findViewById(R.id.task_layout6);
		taskLayout7 = (LinearLayout) findViewById(R.id.task_layout7);
		taskLayout8 = (LinearLayout) findViewById(R.id.task_layout8);
		taskLayout9 = (LinearLayout) findViewById(R.id.task_layout9);
		taskImage1 = (ImageView) findViewById(R.id.task_image1);
		taskImage2 = (ImageView) findViewById(R.id.task_image2);
		taskImage3 = (ImageView) findViewById(R.id.task_image3);
		taskImage4 = (ImageView) findViewById(R.id.task_image4);
		taskImage5 = (ImageView) findViewById(R.id.task_image5);
		taskImage6 = (ImageView) findViewById(R.id.task_image6);
		taskImage7 = (ImageView) findViewById(R.id.task_image7);
		taskImage8 = (ImageView) findViewById(R.id.task_image8);
		taskImage9 = (ImageView) findViewById(R.id.task_image9);
		textView1 = (TextView) findViewById(R.id.task_title1);
		textView2 = (TextView) findViewById(R.id.task_title2);
		textView3 = (TextView) findViewById(R.id.task_title3);
		textView4 = (TextView) findViewById(R.id.task_title4);
		textView5 = (TextView) findViewById(R.id.task_title5);
		textView6 = (TextView) findViewById(R.id.task_title6);
		textView7 = (TextView) findViewById(R.id.task_title7);
		textView8 = (TextView) findViewById(R.id.task_title8);
		textView9 = (TextView) findViewById(R.id.task_title9);
		taskLayout1.setOnClickListener(this);
		taskLayout2.setOnClickListener(this);
		taskLayout3.setOnClickListener(this);
		taskLayout4.setOnClickListener(this);
		taskLayout5.setOnClickListener(this);
		taskLayout6.setOnClickListener(this);
		taskLayout7.setOnClickListener(this);
		taskLayout8.setOnClickListener(this);
		taskLayout9.setOnClickListener(this);

		taskLayoutSetting = (TableLayout) findViewById(R.id.task_layout_setting);
		taskLayout1Setting = (LinearLayout) findViewById(R.id.task_layout1_setting);
		taskLayout2Setting = (LinearLayout) findViewById(R.id.task_layout2_setting);
		taskLayout3Setting = (LinearLayout) findViewById(R.id.task_layout3_setting);
		taskLayout4Setting = (LinearLayout) findViewById(R.id.task_layout4_setting);
		taskLayout5Setting = (LinearLayout) findViewById(R.id.task_layout5_setting);
		taskLayout6Setting = (LinearLayout) findViewById(R.id.task_layout6_setting);
		taskLayout7Setting = (LinearLayout) findViewById(R.id.task_layout7_setting);
		taskLayout8Setting = (LinearLayout) findViewById(R.id.task_layout8_setting);
		taskLayout9Setting = (LinearLayout) findViewById(R.id.task_layout9_setting);
		taskImage1Setting = (ImageView) findViewById(R.id.task_image1_setting);
		taskImage2Setting = (ImageView) findViewById(R.id.task_image2_setting);
		taskImage3Setting = (ImageView) findViewById(R.id.task_image3_setting);
		taskImage4Setting = (ImageView) findViewById(R.id.task_image4_setting);
		taskImage5Setting = (ImageView) findViewById(R.id.task_image5_setting);
		taskImage6Setting = (ImageView) findViewById(R.id.task_image6_setting);
		taskImage7Setting = (ImageView) findViewById(R.id.task_image7_setting);
		taskImage8Setting = (ImageView) findViewById(R.id.task_image8_setting);
		taskImage9Setting = (ImageView) findViewById(R.id.task_image9_setting);
		textView1Setting = (TextView) findViewById(R.id.task_title1_setting);
		textView2Setting = (TextView) findViewById(R.id.task_title2_setting);
		textView3Setting = (TextView) findViewById(R.id.task_title3_setting);
		textView4Setting = (TextView) findViewById(R.id.task_title4_setting);
		textView5Setting = (TextView) findViewById(R.id.task_title5_setting);
		textView6Setting = (TextView) findViewById(R.id.task_title6_setting);
		textView7Setting = (TextView) findViewById(R.id.task_title7_setting);
		textView8Setting = (TextView) findViewById(R.id.task_title8_setting);
		textView9Setting = (TextView) findViewById(R.id.task_title9_setting);
		taskLayout1Setting.setOnClickListener(this);
		taskLayout2Setting.setOnClickListener(this);
		taskLayout3Setting.setOnClickListener(this);
		taskLayout4Setting.setOnClickListener(this);
		taskLayout5Setting.setOnClickListener(this);
		taskLayout6Setting.setOnClickListener(this);
		taskLayout7Setting.setOnClickListener(this);
		taskLayout8Setting.setOnClickListener(this);
		taskLayout9Setting.setOnClickListener(this);

		appLayout = (TableLayout) findViewById(R.id.app_layout_setting);
		appLayout1 = (LinearLayout) findViewById(R.id.app_layout1_setting);
		appLayout2 = (LinearLayout) findViewById(R.id.app_layout2_setting);
		appLayout3 = (LinearLayout) findViewById(R.id.app_layout3_setting);
		appLayout4 = (LinearLayout) findViewById(R.id.app_layout4_setting);
		appLayout5 = (LinearLayout) findViewById(R.id.app_layout5_setting);
		appLayout6 = (LinearLayout) findViewById(R.id.app_layout6_setting);
		appLayout7 = (LinearLayout) findViewById(R.id.app_layout7_setting);
		appLayout8 = (LinearLayout) findViewById(R.id.app_layout8_setting);
		appLayout9 = (LinearLayout) findViewById(R.id.app_layout9_setting);
		appImage1 = (ImageView) findViewById(R.id.app_image1_setting);
		appImage2 = (ImageView) findViewById(R.id.app_image2_setting);
		appImage3 = (ImageView) findViewById(R.id.app_image3_setting);
		appImage4 = (ImageView) findViewById(R.id.app_image4_setting);
		appImage5 = (ImageView) findViewById(R.id.app_image5_setting);
		appImage6 = (ImageView) findViewById(R.id.app_image6_setting);
		appImage7 = (ImageView) findViewById(R.id.app_image7_setting);
		appImage8 = (ImageView) findViewById(R.id.app_image8_setting);
		appImage9 = (ImageView) findViewById(R.id.app_image9_setting);
		appTextView1 = (TextView) findViewById(R.id.app_title1_setting);
		appTextView2 = (TextView) findViewById(R.id.app_title2_setting);
		appTextView3 = (TextView) findViewById(R.id.app_title3_setting);
		appTextView4 = (TextView) findViewById(R.id.app_title4_setting);
		appTextView5 = (TextView) findViewById(R.id.app_title5_setting);
		appTextView6 = (TextView) findViewById(R.id.app_title6_setting);
		appTextView7 = (TextView) findViewById(R.id.app_title7_setting);
		appTextView8 = (TextView) findViewById(R.id.app_title8_setting);
		appTextView9 = (TextView) findViewById(R.id.app_title9_setting);
		appLayout1.setOnClickListener(this);
		appLayout2.setOnClickListener(this);
		appLayout3.setOnClickListener(this);
		appLayout4.setOnClickListener(this);
		appLayout5.setOnClickListener(this);
		appLayout6.setOnClickListener(this);
		appLayout7.setOnClickListener(this);
		appLayout8.setOnClickListener(this);
		appLayout9.setOnClickListener(this);
		appLayout1.setOnLongClickListener(this);
		appLayout2.setOnLongClickListener(this);
		appLayout3.setOnLongClickListener(this);
		appLayout4.setOnLongClickListener(this);
		appLayout6.setOnLongClickListener(this);
		appLayout7.setOnLongClickListener(this);
		appLayout8.setOnLongClickListener(this);
		appLayout9.setOnLongClickListener(this);

		brightnessLayout = (TableLayout) findViewById(R.id.brightnessLayout);
		backImage = (ImageView) findViewById(R.id.backImage);
		timeoutTextView = (TextView) findViewById(R.id.timeoutTextView);
		seekbarBrightness = (SeekBar) findViewById(R.id.seekbarBrightness);
		seekbarTimeout = (SeekBar) findViewById(R.id.seekbarTimeout);
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		try {
			seekbarBrightness
					.setProgress(Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS)
							* 100 / 255);
			seekbarTimeout.setProgress(getScreenTimeout());
			checkbox.setChecked(
					Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == 1);
		} catch (SettingNotFoundException e) {
			Toast.makeText(context, context.getResources().getString(R.string.device_not_support), Toast.LENGTH_SHORT)
					.show();
		}
		checkbox.setOnCheckedChangeListener(this);
		backImage.setOnClickListener(this);
		seekbarBrightness.setOnSeekBarChangeListener(this);
		seekbarTimeout.setOnSeekBarChangeListener(this);

		data = new String[9][2];
		for (int i = 0; i < data.length; i++) {
			data[i] = SharedPreference.readMainPage(context, i + 1).split("[.]");
		}
		initLayout(taskLayout1, taskImage1, textView1, data[0][0], data[0][1]);
		initLayout(taskLayout2, taskImage2, textView2, data[1][0], data[1][1]);
		initLayout(taskLayout3, taskImage3, textView3, data[2][0], data[2][1]);
		initLayout(taskLayout4, taskImage4, textView4, data[3][0], data[3][1]);
		initLayout(taskLayout5, taskImage5, textView5, data[4][0], data[4][1]);
		initLayout(taskLayout6, taskImage6, textView6, data[5][0], data[5][1]);
		initLayout(taskLayout7, taskImage7, textView7, data[6][0], data[6][1]);
		initLayout(taskLayout8, taskImage8, textView8, data[7][0], data[7][1]);
		initLayout(taskLayout9, taskImage9, textView9, data[8][0], data[8][1]);

		dataSetting = new String[9][2];
		for (int i = 0; i < dataSetting.length; i++) {
			dataSetting[i] = SharedPreference.readSettingPage(context, i + 1).split("[.]");
		}

		initLayout(taskLayout1Setting, taskImage1Setting, textView1Setting, dataSetting[0][0], dataSetting[0][1]);
		initLayout(taskLayout2Setting, taskImage2Setting, textView2Setting, dataSetting[1][0], dataSetting[1][1]);
		initLayout(taskLayout3Setting, taskImage3Setting, textView3Setting, dataSetting[2][0], dataSetting[2][1]);
		initLayout(taskLayout4Setting, taskImage4Setting, textView4Setting, dataSetting[3][0], dataSetting[3][1]);
		initLayout(taskLayout5Setting, taskImage5Setting, textView5Setting, dataSetting[4][0], "");
		initLayout(taskLayout6Setting, taskImage6Setting, textView6Setting, dataSetting[5][0], dataSetting[5][1]);
		initLayout(taskLayout7Setting, taskImage7Setting, textView7Setting, dataSetting[6][0], dataSetting[6][1]);
		initLayout(taskLayout8Setting, taskImage8Setting, textView8Setting, dataSetting[7][0], dataSetting[7][1]);
		initLayout(taskLayout9Setting, taskImage9Setting, textView9Setting, dataSetting[8][0], dataSetting[8][1]);

		taskList.add(new TaskObject(data[0][0], textView1, taskImage1));
		taskList.add(new TaskObject(data[1][0], textView2, taskImage2));
		taskList.add(new TaskObject(data[2][0], textView3, taskImage3));
		taskList.add(new TaskObject(data[3][0], textView4, taskImage4));
		taskList.add(new TaskObject(data[4][0], textView5, taskImage5));
		taskList.add(new TaskObject(data[5][0], textView6, taskImage6));
		taskList.add(new TaskObject(data[6][0], textView7, taskImage7));
		taskList.add(new TaskObject(data[7][0], textView8, taskImage8));
		taskList.add(new TaskObject(data[8][0], textView9, taskImage9));
		taskList.add(new TaskObject(dataSetting[0][0], textView1Setting, taskImage1Setting));
		taskList.add(new TaskObject(dataSetting[1][0], textView2Setting, taskImage2Setting));
		taskList.add(new TaskObject(dataSetting[2][0], textView3Setting, taskImage3Setting));
		taskList.add(new TaskObject(dataSetting[3][0], textView4Setting, taskImage4Setting));
		taskList.add(new TaskObject(dataSetting[5][0], textView6Setting, taskImage6Setting));
		taskList.add(new TaskObject(dataSetting[6][0], textView7Setting, taskImage7Setting));
		taskList.add(new TaskObject(dataSetting[7][0], textView8Setting, taskImage8Setting));
		taskList.add(new TaskObject(dataSetting[8][0], textView9Setting, taskImage9Setting));

		dataApp = new String[9][3];
		for (int i = 0; i < dataApp.length; i++) {
			dataApp[i] = SharedPreference.readAppPage(context, i + 1).split(Constants.SPECIAL_CHAR);
		}

		initLayoutApp(appImage1, appTextView1, dataApp[0][0], 0);
		initLayoutApp(appImage2, appTextView2, dataApp[1][0], 1);
		initLayoutApp(appImage3, appTextView3, dataApp[2][0], 2);
		initLayoutApp(appImage4, appTextView4, dataApp[3][0], 3);
		initLayoutApp(appImage5, appTextView5, dataApp[4][0], 4);
		initLayoutApp(appImage6, appTextView6, dataApp[5][0], 5);
		initLayoutApp(appImage7, appTextView7, dataApp[6][0], 6);
		initLayoutApp(appImage8, appTextView8, dataApp[7][0], 7);
		initLayoutApp(appImage9, appTextView9, dataApp[8][0], 8);

		listAppImage = new ImageView[9];
		listAppImage[0] = appImage1;
		listAppImage[1] = appImage2;
		listAppImage[2] = appImage3;
		listAppImage[3] = appImage4;
		listAppImage[4] = appImage5;
		listAppImage[5] = appImage6;
		listAppImage[6] = appImage7;
		listAppImage[7] = appImage8;
		listAppImage[8] = appImage9;
		listAppTextView = new TextView[9];
		listAppTextView[0] = appTextView1;
		listAppTextView[1] = appTextView2;
		listAppTextView[2] = appTextView3;
		listAppTextView[3] = appTextView4;
		listAppTextView[4] = appTextView5;
		listAppTextView[5] = appTextView6;
		listAppTextView[6] = appTextView7;
		listAppTextView[7] = appTextView8;
		listAppTextView[8] = appTextView9;
		listTaskLayout = new LinearLayout[9];
		listTaskLayout[0] = taskLayout1;
		listTaskLayout[1] = taskLayout2;
		listTaskLayout[2] = taskLayout3;
		listTaskLayout[3] = taskLayout4;
		listTaskLayout[4] = taskLayout5;
		listTaskLayout[5] = taskLayout6;
		listTaskLayout[6] = taskLayout7;
		listTaskLayout[7] = taskLayout8;
		listTaskLayout[8] = taskLayout9;
		listTaskLayoutSetting = new LinearLayout[9];
		listTaskLayoutSetting[0] = taskLayout1Setting;
		listTaskLayoutSetting[1] = taskLayout2Setting;
		listTaskLayoutSetting[2] = taskLayout3Setting;
		listTaskLayoutSetting[3] = taskLayout4Setting;
		listTaskLayoutSetting[4] = taskLayout5Setting;
		listTaskLayoutSetting[5] = taskLayout6Setting;
		listTaskLayoutSetting[6] = taskLayout7Setting;
		listTaskLayoutSetting[7] = taskLayout8Setting;
		listTaskLayoutSetting[8] = taskLayout9Setting;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.seekbarBrightness:
			// To handle the auto
			// Settings.System.putInt(context.getContentResolver(),
			// Settings.System.SCREEN_BRIGHTNESS_MODE,
			// Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
			if (fromUser) {
				Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
						progress * 255 / 100);
			}
			break;
		case R.id.seekbarTimeout:
			// if (fromUser) {
			int timeout = 0;
			String time = "";
			switch (progress) {
			case 0:
				timeout = 15000;
				time = "15" + " " + context.getResources().getString(R.string.sec);
				break;
			case 1:
				timeout = 30000;
				time = "30" + " " + context.getResources().getString(R.string.sec);
				break;
			case 2:
				timeout = 60000;
				time = "1" + " " + context.getResources().getString(R.string.min);
				break;
			case 3:
				timeout = 120000;
				time = "2" + " " + context.getResources().getString(R.string.min);
				break;
			case 4:
				timeout = 300000;
				time = "5" + " " + context.getResources().getString(R.string.min);
				break;
			case 5:
				timeout = 600000;
				time = "10" + " " + context.getResources().getString(R.string.min);
				break;
			case 6:
				timeout = 900000;
				time = "15" + " " + context.getResources().getString(R.string.min);
				break;

			default:
				break;
			}
			Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeout);
			timeoutTextView.setText(time);
			// }
			break;

		default:
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (checkbox.isChecked()) {
			Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
					Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		} else {
			Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
					Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		}
	}

	@Override
	public void loadAppDone(ArrayList<App> apps) {
		this.apps = apps;
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.app_layout1_setting:
			onProcessLongClick(dataApp[0][0], 1);
			break;
		case R.id.app_layout2_setting:
			onProcessLongClick(dataApp[1][0], 2);
			break;
		case R.id.app_layout3_setting:
			onProcessLongClick(dataApp[2][0], 3);
			break;
		case R.id.app_layout4_setting:
			onProcessLongClick(dataApp[3][0], 4);
			break;
		case R.id.app_layout6_setting:
			onProcessLongClick(dataApp[5][0], 6);
			break;
		case R.id.app_layout7_setting:
			onProcessLongClick(dataApp[6][0], 7);
			break;
		case R.id.app_layout8_setting:
			onProcessLongClick(dataApp[7][0], 8);
			break;
		case R.id.app_layout9_setting:
			onProcessLongClick(dataApp[8][0], 9);
			break;

		default:
			break;
		}
		return true;
	}

	private void onProcessLongClick(String action, int appPage) {
		switch (action) {
		case Constants.APP:
			SharedPreference.saveAppPage(context, Constants.NEW, appPage);
			String temp = dataApp[appPage - 1][1];
			boolean delete = true;
			dataApp[appPage - 1] = SharedPreference.readAppPage(context, appPage).split(Constants.SPECIAL_CHAR);
			for (int i = 0; i < dataApp.length; i++) {
				if (dataApp[i].length > 1) {
					if (temp.equals(dataApp[i][1])) {
						delete = false;
						break;
					}
				}
			}
			if (delete) {
				MyHelper.deleteFile(context, temp);
			}
			initLayoutApp(listAppImage[appPage - 1], listAppTextView[appPage - 1], dataApp[appPage - 1][0],
					appPage - 1);
			break;

		default:
			break;
		}
	}

	@Override
	public void killProcessDone() {
		if (animLayout != null) {
			animLayout.clearAnimation();
			animLayout = null;
		}
	}
}