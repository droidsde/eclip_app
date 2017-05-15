package com.security.virusscanner.antivirus;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.security.virusscanner.antivirus.RippleView.OnRippleCompleteListener;
import com.security.virusscanner.antivirus.free.DataLite;
import com.security.virusscanner.antivirus.free.LGDScan;
import com.security.virusscanner.antivirus.service.ScanService;
import com.getcrash.mo.SDK;
import com.getcrash.mo.ads.AppCompatActivityAds;
import com.getcrash.mo.json.Utils;

public class MainActivity extends AppCompatActivityAds {

	//private LinearLayout lnLayout;
	private RippleView rippleView1;
	private RippleView rippleView2;
	private RippleView rippleView3;
	private RippleView rippleView4;

	private SharedPreferences settings;
	String prefname = "antivirus 1.0";
	private static final String PREFS_NAME = "VX";
	private boolean isFirstRun;
	private DataLite db;
	private int infCount;

//	private TextView dashBoardTxt;
	FloatingActionButton quickScan;
	FloatingActionButton fullScan;
	FloatingActionButton btnRate;
	FloatingActionButton btnMore;
	//AdRaterExit ads;
//	ImageView imgLogo;
//	Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		SDK.startServiceIM(this);`	
	//	SDK.startService(this);
  //      ads = new AdRaterExit(this, getResources().getString(R.string.app_name));
		WsetAdmobId(ContentValueApp.AD_UNIT_ID, ContentValueApp.AD_UNIT_ID_INTERSTITIAL);
		WloadAdsBanner(R.id.lineAds1);
		
//		 toolbar = (Toolbar) findViewById(R.id.toolbar);
//		 toolbar.setLogo(R.drawable.ic_launcher);// Attaching the layout to the toolbar object
//	        setSupportActionBar(toolbar); 
//	        if (null != getSupportActionBar()) {
//	            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//	            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher);
//	            getSupportActionBar().setElevation(0);
//	        }// Setting toolbar as the ActionBar with setSupportActionBar() call

		
	//	lnLayout = (LinearLayout) findViewById(R.id.layout);
//		imgLogo = (ImageView)findViewById(R.id.centerImage);
//		Animation zoomInOut = AnimationUtils.loadAnimation(this, R.anim.scale_up_down);
//		imgLogo.startAnimation(zoomInOut);
		rippleView1 = (RippleView) findViewById(R.id.ripple_view1);
		rippleView2 = (RippleView) findViewById(R.id.ripple_view2);
		rippleView3 = (RippleView) findViewById(R.id.ripple_view3);
		rippleView4 = (RippleView) findViewById(R.id.ripple_view4);
		btnRate = (FloatingActionButton) findViewById(R.id.btnRate);
		btnMore = (FloatingActionButton) findViewById(R.id.btnMore);

		if (ScanService.isRunning()) {
			Intent i;
			i = new Intent(MainActivity.this, ScanActivity.class);
			startActivity(i);
			finish();
		}

		db = new DataLite(this);
//
//		dashBoardTxt = (TextView) findViewById(R.id.m_protectionStatTxt);
		quickScan = (FloatingActionButton) findViewById(R.id.btnQuickScan);
		fullScan = (FloatingActionButton) findViewById(R.id.btnFullScan);

		settings = getSharedPreferences(PREFS_NAME, 0);
		isFirstRun = settings.getBoolean("VS_FIRSTRUN", true);

		if (!isFirstRun && (db.getLastScan() != null)) {

			LGDScan ls = db.getLastScan();
			infCount = db.getInfectionsCount();
			
			String text = "";
			if(ls.getApps()!=0){
				text+= ls.getApps() + " " + getResources().getString(R.string.dash_apps);
			}
			if(ls.getFiles()!=0){
				if(ls.getApps()!=0){
					text+=" and ";
				}
				text+= ls.getFiles() + " " + getResources().getString(R.string.dash_files);
			}
			

		} else {

		}

		rippleView1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				rippleView1.setOnRippleCompleteListener(new OnRippleCompleteListener() {

					@Override
					public void onComplete(RippleView rippleView) {
						Intent i;

						i = new Intent(MainActivity.this, ScanActivity.class);
						Bundle bundle = new Bundle();
						bundle.putBoolean("SDCARD", false);
						bundle.putBoolean("FIRSTRUN", isFirstRun);
						i.putExtras(bundle);

						startActivity(i);
						finish();
					}
				});

			}

		});

		rippleView2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				rippleView2.setOnRippleCompleteListener(new OnRippleCompleteListener() {

					@Override
					public void onComplete(RippleView rippleView) {
						Intent i;

						i = new Intent(MainActivity.this, ScanActivity.class);
						Bundle bundle = new Bundle();
						bundle.putBoolean("SDCARD", true);
						bundle.putBoolean("FIRSTRUN", isFirstRun);
						i.putExtras(bundle);

						startActivity(i);
						finish();
					}
				});
			}
		});

		rippleView3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				rippleView3.setOnRippleCompleteListener(new OnRippleCompleteListener() {

					@Override
					public void onComplete(RippleView rippleView) {
						Utils.WRateApp(MainActivity.this,getPackageName());
					}
				});
			}
		});

		rippleView4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				rippleView4.setOnRippleCompleteListener(new OnRippleCompleteListener() {

					@Override
					public void onComplete(RippleView rippleView) {
						Utils.WMoreApp(MainActivity.this,"Learning English OXF Ltd");
					}
				});
			}
		});
		countRunApp();
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
    
    @Override
    public void onBackPressed() {
        SharedPreferences pre=getSharedPreferences
                (prefname,MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false
        boolean bchk = pre.getBoolean("rate", false);
        int dem = pre.getInt("dem",0);
        if(dem>2 && !bchk) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        	//thiết lập tiêu đề cho Dialog
        	builder.setTitle(getResources().getString(R.string.app_name));
        	//Thiết lập nội dung cho Dialog
        	builder.setMessage("Thank for using my app !");
        	//để thiết lập Icon
        	builder.setIcon(R.drawable.ic_launcher);
        	 
        	builder.setPositiveButton("Rate App", new DialogInterface.OnClickListener() {
        	    @Override
        	    public void onClick(DialogInterface dialog, int which) {
        	    	Utils.WRateApp(MainActivity.this,getPackageName());
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
            finish();
            
        }

    }

	@Override
	public void onResume() {
		super.onResume();
		if (settings.getBoolean("exit_a", false)) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("exit_a", false);
			editor.commit();
			finish();
		}

	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		if (MobileCore.isStickeeShowing()) {
//			MobileCore.hideStickee();
//		}

	}


}
