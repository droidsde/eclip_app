package com.usstudio.easytouch.assistivetouch;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.service.AssistiveTouchService;
import com.usstudio.easytouch.assistivetouch.service.AssistiveTouchService.LocalBinder;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.mbapp.lib_tool.activity.ActionbarActivityAds;

public class DisplaySettingActivity extends ActionbarActivityAds implements OnSeekBarChangeListener {

	private final int MIN_SIZE = 74;
	private final int MAX_SIZE = 220;
	private final int MIN_TRANSPARENT = 20;
	private final int MAX_TRANSPARENT = 100;
	private Toolbar toolbar;
	private SeekBar seekbar1;
	private SeekBar seekbar2;
	private SeekBar seekbar3;
	private TextView speedText;
	private AssistiveTouchService atService;
	private boolean isBounded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_setting);
		overridePendingTransition(R.anim.slide_in_right, R.anim.hold);

		toolbar = (Toolbar) findViewById(R.id.toolbar_top);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(getResources().getString(R.string.display_setting_title));

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
		getSupportActionBar().setHomeAsUpIndicator(upArrow);

		seekbar1 = (SeekBar) findViewById(R.id.seekbar1);
		seekbar2 = (SeekBar) findViewById(R.id.seekbar2);
		seekbar3 = (SeekBar) findViewById(R.id.seekbar3);
		speedText = (TextView) findViewById(R.id.speed_text);

		int temp = SharedPreference.readPageChangeSpeed(this);
		seekbar1.setProgress(temp);
		seekbar1.setOnSeekBarChangeListener(this);
		switch (temp) {
		case 0:
			speedText.setText(getResources().getString(R.string.slow_speed));
			break;
		case 1:
			speedText.setText(getResources().getString(R.string.normal_speed));
			break;
		case 2:
			speedText.setText(getResources().getString(R.string.fast_speed));
			break;
		default:
			break;
		}
		seekbar2.setMax(MAX_SIZE - MIN_SIZE);
		seekbar2.setProgress(SharedPreference.readIconSize(this) - MIN_SIZE);
		seekbar2.setOnSeekBarChangeListener(this);
		seekbar3.setMax(MAX_TRANSPARENT - MIN_TRANSPARENT);
		seekbar3.setProgress(SharedPreference.readIconTransparent(this) - MIN_TRANSPARENT);
		seekbar3.setOnSeekBarChangeListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; goto parent activity.
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (SharedPreference.readSwitch(this)) {
			Intent intent = new Intent(this, AssistiveTouchService.class);
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
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.slide_out_right);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.seekbar1:
			SharedPreference.savePageChangeSpeed(DisplaySettingActivity.this, progress);
			switch (progress) {
			case 0:
				speedText.setText(getResources().getString(R.string.slow_speed));
				break;
			case 1:
				speedText.setText(getResources().getString(R.string.normal_speed));
				break;
			case 2:
				speedText.setText(getResources().getString(R.string.fast_speed));
				break;
			default:
				break;
			}
			break;
		case R.id.seekbar2:
			SharedPreference.saveIconSize(DisplaySettingActivity.this, progress + MIN_SIZE);
			if (isBounded) {
				atService.resizeAssistiveIcon();
			}
			break;
		case R.id.seekbar3:
			SharedPreference.saveIconTransparent(DisplaySettingActivity.this, progress + MIN_TRANSPARENT);
			if (isBounded) {
				atService.setAlphaAssistiveIcon();
			}
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