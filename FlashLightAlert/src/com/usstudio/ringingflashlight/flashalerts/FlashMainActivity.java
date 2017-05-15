package com.usstudio.ringingflashlight.flashalerts;

import com.mbapp.lib_tool.activity.ActionbarActivityAds;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.usstudio.ringingflashlight.flashalerts.content.ContentValue;

public class FlashMainActivity extends ActionbarActivityAds{

	Boolean checkPer = true;
	SwitchCompat togcall,togsms;
	SeekBar seekbarcall,seekbarsms;
	Button btntest,btntestsms;
	EditText edsms;
	FlashlightCaller cf;
	Boolean flagcall = false, flagsms =false;
	private SharedPreferences sharedPref;
	private static final int FLAG = 0x29A;
	public  static final int MY_REQUEST__CODE = 1;
	
	TextView tvdemo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkPermission();
		WsetAdmobId(ContentValue.admob_banner, ContentValue.admob_full);
		WloadAdsInterstitial();
		WloadAdsBanner(R.id.lineAds1);
		cf = (FlashlightCaller) getApplicationContext();
		addView ();
		setEvent();
		sharedPref = getSharedPreferences(ContentValue.KEY,MODE_PRIVATE);
		init();
		
		
	}
	
	@TargetApi(23) public void checkPermission(){
		 if( ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                 requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.RECEIVE_SMS},
                         MY_REQUEST__CODE);
             }
         }
	}
	
	@SuppressLint("NewApi") @Override
	public void onRequestPermissionsResult(int requestCode,
	                                       @NonNull String[] permissions, @NonNull int[] grantResults) {

	    switch (requestCode) {
	        case MY_REQUEST__CODE:
	            if (grantResults.length > 0) {
	               boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
	               boolean receiveSMS = grantResults[0] == PackageManager.PERMISSION_GRANTED;

	                if(cameraPermission && receiveSMS)
	                {
	                    // write your logic here 
	                	
	                } else {
	                    Snackbar.make(this.findViewById(android.R.id.content),
	                        "Please Grant Permissions to upload profile photo",
	                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
	                        new View.OnClickListener() {
	                            @Override
	                            public void onClick(View v) {
	                                requestPermissions(
	                                        new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.RECEIVE_SMS},
	                                                MY_REQUEST__CODE);
	                            }
	                        }).show();
	                    checkPer = false;
	                }
	           }
	           break;
	    }
	}
	
	
	public void addView (){
		togcall = WgetSwitchCompat(R.id.togcall);
		togsms = WgetSwitchCompat(R.id.togsms);
		seekbarcall = (SeekBar) findViewById(R.id.seekbarcall) ;
		seekbarsms = (SeekBar) findViewById(R.id.seekbarsms) ; 
		btntest = (Button) findViewById(R.id.btntest);
		btntestsms = (Button) findViewById(R.id.btntestsms);
		edsms = WgetEditText(R.id.edsms);
		tvdemo = WgetTextView(R.id.tvdemo);

	}
	public void init(){
		togcall.setChecked(sharedPref.getBoolean(ContentValue.callFlash, false));
		togsms.setChecked(sharedPref.getBoolean(ContentValue.msgFlash, false));
		seekbarcall.setProgress((1100-cf.getCallFlashOnDuration())/100);
		seekbarsms.setProgress((1100-cf.getMsgFlashOnDuration())/100);
		edsms.setText(String.valueOf(sharedPref.getInt(ContentValue.msgFlashDuration, 3)));
	}
	public void setEvent(){
		
		Integer[] arrayId = {R.id.btntest,R.id.btntestsms,R.id.tvdemo,R.id.togcall,R.id.togsms};
		WsetOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkPermission();
//				if(checkPer)
//				{
					switch (v.getId()) {
					case R.id.btntest:
						if(flagcall == false)
						{
							flagcall = true;
							btntest.setText("Stop");
						}
						else 
						{
							flagcall =false;
							btntest.setText("Test");
						}
						cf.setCallFlashTest(flagcall);
						new ManageFlash(R.id.btntest).execute();
						break;
					case R.id.btntestsms:
						if(flagcall == false)
						{
							flagcall = true;
							btntestsms.setText("Stop");
						}
						else 
						{
							flagcall =false;
							btntestsms.setText("Test");
						}
						cf.setMsgFlashTest(flagcall);
						new ManageFlash(R.id.btntestsms).execute();
						break;
					case R.id.tvdemo:
						break;
					case R.id.togcall:
						cf.setCallFlash(togcall.isChecked());
						Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
						break;
					case R.id.togsms:
						cf.setMsgFlash(togsms.isChecked());
						Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
						break;

					default:
						break;
					}
//				}else{
//					Toast.makeText(FlashMainActivity.this, "Please allow camera permissions!", Toast.LENGTH_LONG).show();
//				}
				
				
			}
		}, arrayId);
		
		seekbarcall.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				cf.setCallFlashOnDuration(progress);
				
			}
		});
		seekbarsms.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				cf.setMsgFlashOnDuration(progress);
				
			}
		});
		Integer[] arrayIed = {R.id.edsms};

		WaddTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String text = edsms.getText().toString();
				if(isInteger(text)){
				int coverTime = Integer.parseInt(text);
				cf.setMsgFlashDuration(coverTime);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		},arrayIed);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		switch (id) {
		case R.id.action_support:
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class ManageFlash extends AsyncTask<Integer, Integer, String> {

		private int button;

		public ManageFlash(int button) {
			this.button = button;
			FlashLight.incRunning();
		}

		private FlashLight flash = new FlashLight(cf);

		@Override
		protected String doInBackground(Integer... integers) {
			
			
			if (cf.getCallFlashOnDuration() == cf.getCallFlashOffDuration())
				if (cf.getMsgFlashOnDuration() == cf.getMsgFlashOffDuration())
					if (cf.getMsgFlashOnDuration() == FLAG)
						flash.enableFlash(FLAG * 5, 0);

			switch (button) {
				case R.id.btntest:
					while (flagcall) {
						flash.enableFlash(cf.getCallFlashOnDuration(), cf.getCallFlashOnDuration());
					}
					break;
				case R.id.btntestsms:
					while (flagcall) {
						flash.enableFlash(cf.getMsgFlashOnDuration(), cf.getMsgFlashOnDuration());
					}
					break;
			}


			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			
			FlashLight.decRunning();
			if (FlashLight.getRunning() == 0) FlashLight.releaseCam();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		
			FlashLight.decRunning();
			if (FlashLight.getRunning() == 0) FlashLight.releaseCam();
		}


	}
	

	public boolean isInteger( String input ) {
	    try {
	        Integer.parseInt( input );
	        return true;
	    }
	    catch( Exception e ) {
	        return false;
	    }
	}
	
	@Override
	public void onBackPressed() {
		flagcall=false;
		super.onBackPressed();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}
	
}
