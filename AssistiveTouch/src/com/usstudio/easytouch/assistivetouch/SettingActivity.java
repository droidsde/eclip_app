package com.usstudio.easytouch.assistivetouch;

import com.usstudio.easytouch.assistivetouch.R;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.mbapp.lib_tool.activity.ActionbarActivityAds;
import android.content.Intent;
import android.graphics.PorterDuff;

public class SettingActivity extends ActionbarActivityAds implements OnClickListener {

	private Toolbar toolbar;
	private LinearLayout configSetting;
	private LinearLayout displaySetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		overridePendingTransition(R.anim.slide_in_right, R.anim.hold);

		configSetting = (LinearLayout) findViewById(R.id.config_setting);
		displaySetting = (LinearLayout) findViewById(R.id.display_setting);
		configSetting.setOnClickListener(this);
		displaySetting.setOnClickListener(this);
		toolbar = (Toolbar) findViewById(R.id.toolbar_top);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(getResources().getString(R.string.setting_title));

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
		getSupportActionBar().setHomeAsUpIndicator(upArrow);
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
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.slide_out_right);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.config_setting:
			Intent intent1 = new Intent(SettingActivity.this, ConfigSettingActivity.class);
			startActivity(intent1);
			break;
		case R.id.display_setting:
			Intent intent2 = new Intent(SettingActivity.this, DisplaySettingActivity.class);
			startActivity(intent2);
			break;

		default:
			break;
		}
	}
}
