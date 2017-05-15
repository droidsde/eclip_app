package com.usstudio.easytouch.assistivetouch;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.dialog.BgSettingDialog;
import com.usstudio.easytouch.assistivetouch.dialog.IconSettingDialog;
import com.usstudio.easytouch.assistivetouch.receiver.MyAdmin;
import com.usstudio.easytouch.assistivetouch.service.AssistiveTouchService;
import com.usstudio.easytouch.assistivetouch.service.AssistiveTouchService.LocalBinder;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;
import com.mbapp.lib_tool.activity.AppCompatActivityAds;

public class MainActivity extends AppCompatActivityAds implements OnClickListener {

	private final int REQUEST_ENABLE_ADMIN = 1;

	private FrameLayout switchLayout;
	private View viewLayout;
	private TextSwitcher switchText;
	private FrameLayout bgColorSetting;
	private FrameLayout iconSetting;
	private FrameLayout appSetting;
	private FrameLayout rateApp;
	private FrameLayout moreApp;
	private FrameLayout uninstallApp;

	private AssistiveTouchService atService;
	private boolean isBounded = false;

	DevicePolicyManager mDPM;
	ComponentName mAdminName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WsetAdmobId(ContentValueApp.AD_UNIT_ID, ContentValueApp.AD_UNIT_ID_INTERSTITIAL);
		WloadAdsInterstitial();
		WloadAdsSmartBanner(R.id.lineAds1);
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mAdminName = new ComponentName(this, MyAdmin.class);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.getBoolean("adminRequired")) {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.device_admin_description));
			startActivityForResult(intent, REQUEST_ENABLE_ADMIN);
		}

		viewLayout = (View) findViewById(R.id.view_layout);
		bgColorSetting = (FrameLayout) findViewById(R.id.bgcolor_setting);
		iconSetting = (FrameLayout) findViewById(R.id.icon_setting);
		appSetting = (FrameLayout) findViewById(R.id.app_setting);
		rateApp = (FrameLayout) findViewById(R.id.rate_app);
		moreApp = (FrameLayout) findViewById(R.id.more_app);
		uninstallApp = (FrameLayout) findViewById(R.id.uninstall_app);

		switchText = (TextSwitcher) findViewById(R.id.switch_text);
		switchText.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				TextView myText = new TextView(MainActivity.this);
				myText.setTextSize(18);
				myText.setTextColor(Color.WHITE);
				return myText;
			}
		});
		switchText.setInAnimation(this, android.R.anim.slide_in_left);
		switchText.setOutAnimation(this, android.R.anim.slide_out_right);
		if (SharedPreference.readSwitch(this)) {
			Intent intent = new Intent(MainActivity.this, AssistiveTouchService.class);
			startService(intent);
			switchText.setText(getResources().getString(R.string.app_on));
		} else {
			switchText.setText(getResources().getString(R.string.app_off));
		}

		switchLayout = (FrameLayout) findViewById(R.id.switch_layout);
		switchLayout.setOnClickListener(this);
		bgColorSetting.setOnClickListener(this);
		iconSetting.setOnClickListener(this);
		appSetting.setOnClickListener(this);
		rateApp.setOnClickListener(this);
		moreApp.setOnClickListener(this);
		uninstallApp.setOnClickListener(this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (SharedPreference.readSwitch(MainActivity.this)) {
			Intent intent = new Intent(MainActivity.this, AssistiveTouchService.class);
			bindService(intent, connection, Context.BIND_AUTO_CREATE);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (isBounded) {
			unbindService(connection);
			isBounded = false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_ADMIN) {
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_layout:
			SharedPreference.saveSwitch(MainActivity.this, !SharedPreference.readSwitch(MainActivity.this));
			if (SharedPreference.readSwitch(MainActivity.this)) {
				switchText.setText(getResources().getString(R.string.app_on));
				Intent intent = new Intent(MainActivity.this, AssistiveTouchService.class);
				startService(intent);
				bindService(intent, connection, Context.BIND_AUTO_CREATE);
			} else {
				switchText.setText(getResources().getString(R.string.app_off));
				if (isBounded) {
					unbindService(connection);
					isBounded = false;
				}
				stopService(new Intent(MainActivity.this, AssistiveTouchService.class));
			}
			Animation anim = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out);
			anim.setDuration(200);
			anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					viewLayout.setVisibility(View.GONE);
					Animation anim2 = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
					anim2.setDuration(200);
					anim2.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {
							viewLayout.setVisibility(View.VISIBLE);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {

						}
					});
					viewLayout.setAnimation(anim2);
				}
			});
			viewLayout.startAnimation(anim);
			break;
		case R.id.bgcolor_setting:
			BgSettingDialog dialogBG = new BgSettingDialog(MainActivity.this);
			dialogBG.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			dialogBG.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialogBG.show();
			break;
		case R.id.icon_setting:
			IconSettingDialog dialogIcon = new IconSettingDialog(MainActivity.this);
			dialogIcon.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					if (isBounded) {
						atService.setAssistiveIcon();
					}
				}
			});
			dialogIcon.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			dialogIcon.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialogIcon.show();
			break;
		case R.id.app_setting:
			Intent intent = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.rate_app:

			break;
		case R.id.more_app:

			break;
		case R.id.uninstall_app:
			mDPM.removeActiveAdmin(mAdminName);
			Intent i = new Intent(Intent.ACTION_DELETE);
			i.setData(Uri.parse("package:" + this.getPackageName()));
			startActivity(i);
			break;

		default:
			break;
		}
	}

	/** Defines callback for service binding, passed to bindService() */
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocalBinder binder = (LocalBinder) service;
			atService = binder.getService();
			isBounded = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			isBounded = false;
		}
	};
}