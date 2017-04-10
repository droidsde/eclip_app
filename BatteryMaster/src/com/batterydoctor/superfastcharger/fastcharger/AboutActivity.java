package com.batterydoctor.superfastcharger.fastcharger;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.batterydoctor.superfastcharger.fastcharger.R;

public class AboutActivity extends Activity {
	TextView tvAppName;
	ImageView ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		getWidgets();
	}
	
	void getWidgets(){
		tvAppName =(TextView)findViewById(R.id.tvAppName);
		ivBack =(ImageView)findViewById(R.id.ivBack);
		
		PackageManager manager = this.getPackageManager();
		PackageInfo info;
		String appVersion = null;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
			appVersion = info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tvAppName.setText(getResources().getString(R.string.app_name)+appVersion);
		ivBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		LinearLayout llTranslate = (LinearLayout)findViewById(R.id.llTranslate);
		llTranslate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "quang.bme.hust.55@gmail.com" });
				i.putExtra(Intent.EXTRA_SUBJECT,
						"I want to help you translate "+getResources().getString(R.string.app_name));
				i.putExtra(Intent.EXTRA_TEXT, "");
				try {
					startActivity(Intent.createChooser(i,
							"Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(getApplicationContext(),
							"There are no email clients installed.",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
}
