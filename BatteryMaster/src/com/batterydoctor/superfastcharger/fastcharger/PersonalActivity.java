package com.batterydoctor.superfastcharger.fastcharger;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.ui.MainTitle;

public class PersonalActivity extends Activity{
	/**
	 * dispatchTouchEvent
	 */
	private float x = 0.0F;
	private float y = 0.0F;
	private boolean b = false;
	MainTitle mainTitle;
	@TargetApi(23) @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		        if (!Settings.System.canWrite(getApplicationContext())) {
		            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
		            startActivityForResult(intent, 200);

		        }
		    }
		init();
	}
	private void init(){
		mainTitle = (MainTitle) findViewById(R.id.pesonal_title);
		mainTitle.setLeftButtonIcon(R.drawable.title_bar_button_back);
		mainTitle.setLeftButtonOnClickListen(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		mainTitle.setTitleTextMid(R.string.personal_title);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
		// TODO Auto-generated method stub
		switch (paramMotionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.x = paramMotionEvent.getX();
	        this.y = paramMotionEvent.getY();
			break;
		case MotionEvent.ACTION_UP:
			if(b){
				b = false;
				onBackPressed();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float f1 = paramMotionEvent.getX();
	        float f2 = paramMotionEvent.getY();
	        if ((f1 - this.x > 80.0F) || (f2 - this.y > 40.0F))
	        	b =true;
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(paramMotionEvent);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
	}
}
