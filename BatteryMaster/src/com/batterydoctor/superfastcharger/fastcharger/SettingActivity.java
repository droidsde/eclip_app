package com.batterydoctor.superfastcharger.fastcharger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.fastercharger.ChargerSetting;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
//		Button btnOK = (Button) findViewById(R.id.btnOK);
//	     final CheckBox cbSound = (CheckBox)findViewById(R.id.checkBox1);
//	     final CheckBox cbFull = (CheckBox)findViewById(R.id.checkBox2);
//	     TextView txt1 = (TextView) findViewById(R.id.textView1);
//	     TextView txt2 = (TextView) findViewById(R.id.textView2);
//	     
//	     
//	     cbSound.setChecked(ChargerSetting.readEnableSound(this));
//	     cbFull.setChecked(ChargerSetting.readFull(this));
//		 btnOK.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					
//					ChargerSetting.saveEnableSound(SettingActivity.this, cbSound.isChecked());
//					ChargerSetting.saveFull(SettingActivity.this, cbFull.isChecked());
//				
//				}
//			});
	}

	
}
