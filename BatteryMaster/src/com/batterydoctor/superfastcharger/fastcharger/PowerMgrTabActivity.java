package com.batterydoctor.superfastcharger.fastcharger;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.fastercharger.ChargerSetting;
import com.batterydoctor.superfastcharger.fastcharger.ui.MainTitle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nvn.log.LogBuider;


@TargetApi(23) @SuppressWarnings("deprecation")
public class PowerMgrTabActivity extends TabActivity {
	private static final long TIMER_TO_OPEN_INTERSTITIAL_ADS = 6000; // 6s
	private static final String TAG = "package com.batterydoctor.superfastcharger.fastcharger.PowerMgrTabActivity";
	public static final String ACTION_NOTIFY_STATUSBAR = "com.batterydoctor.superfastcharger.fastcharger.notify";
//	private static final Signature SIG_RELEASE = new Signature(
//			"<YOUR SIGNATURE HERE>");
	private TabHost tabHost;
	private MainTitle mainTitle;
	private static final String TAB_HOME = "tab_home";
	private static final String TAB_MODE = "tab_mode";
	private static final String TAB_TOOL = "tab_tool";
	private static final String TAB_MONITOR = "tab_monitor";
	AdView adView;
	AdRequest adRequest;
	InterstitialAd interstitialAd;

	// Animation inleft;
	// Animation outleft;
	// Animation inright;
	// Animation outright;
	int currentTab;
	View lastTabView;
	String prefname = "faster charge";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		        if (!Settings.System.canWrite(getApplicationContext())) {
		            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
		            startActivityForResult(intent, 200);

		        }
		    }
		createTabHost();
		// createAnimation();
//		createAdbanel();
//		countRunApp();
		createInterstitealAd();
	}

//	private void createAdbanel() {
//		adView = (AdView) findViewById(R.id.loQuanCao);
//		adRequest = new AdRequest.Builder().build();
//		adView.loadAd(adRequest);
////			ca-app-pub-7887468927194397/5041585466
////			ca-app-pub-7887468927194397/6518318664
//	}
	
	 
	    
	    


	public void createInterstitealAd() {
		final SharedPreferences preferences = getSharedPreferences("InterstitealAd",Context.MODE_PRIVATE);
		long lastTime = preferences.getLong("Lasttime", 0);
		final long curTime = System.currentTimeMillis();
		if(curTime - lastTime > TIMER_TO_OPEN_INTERSTITIAL_ADS){
			interstitialAd = new InterstitialAd(this);
			interstitialAd.setAdUnitId("ca-app-pub-7887468927194397/6518318664");
			interstitialAd.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					if (interstitialAd.isLoaded()) {
						interstitialAd.show();
						preferences.edit().putLong("Lasttime", curTime).commit();
					} else {
						LogBuider.e(TAG, "Interstitial ad was not ready to be shown.");
					}
				}
				@Override
				public void onAdFailedToLoad(int errorCode) {
				}
			});
			AdRequest adRequest = new AdRequest.Builder().build();
			interstitialAd.loadAd(adRequest);
		}
		
	}

	private void createTabHost() {
		tabHost = getTabHost();
		TabHost tab = this.tabHost;
		tab.addTab(newTab(TAB_TOOL, R.string.tab_tool,
				R.drawable.tab_charge_icon, new Intent(this,
						ToolboxActivity.class)));
		tab.addTab(newTab(TAB_HOME, R.string.tab_home,
				R.drawable.tab_home_icon, new Intent(this,
						PowerBatteryActivity.class)));
		tab.addTab(newTab(TAB_MODE, R.string.tab_saver,
				R.drawable.tab_saver_icon,
				new Intent(this, SaverActivity.class)));
		tab.addTab(newTab(TAB_MONITOR, R.string.tab_monitor,
				R.drawable.tab_monitor_icon, new Intent(this,
						MonitorActivity.class)));
		
		currentTab = getTabHost().getCurrentTab();
		lastTabView = getTabHost().getCurrentView();
		int flag = 0;
		Bundle extras = getIntent().getExtras();
		 if(extras != null){
			flag = extras.getInt("flagCharger", 0);
		 }

		if(flag==1){
		getTabHost().setCurrentTab(2);
		}
	}

	/**
	 * create new tab
	 * 
	 * @param paramString
	 *            :name
	 * @param paramInt1
	 *            : id text display
	 * @param paramInt2
	 *            : img display
	 * @param paramIntent
	 *            : intent to start activity
	 * @return tabspec
	 */
	private TabHost.TabSpec newTab(String paramString, int paramInt1,
			int paramInt2, Intent paramIntent) {
		return this.tabHost.newTabSpec(paramString)
				.setIndicator(a(paramInt1, paramInt2)).setContent(paramIntent);
	}

	private View a(int paramInt1, int paramInt2) {
		LayoutInflater localLayoutInflater = getLayoutInflater();
		View localView = localLayoutInflater.inflate(
				R.layout.tab_main_indicator, null);
		TextView localTextView = (TextView) localView.findViewById(R.id.title);
		localTextView.setText(paramInt1);
		Drawable localDrawable = getApplicationContext().getResources()
				.getDrawable(paramInt2);
		localDrawable.setBounds(0, 0, localDrawable.getMinimumWidth(),
				localDrawable.getMinimumHeight());
		localTextView.setCompoundDrawables(null, localDrawable, null, null);
		return localView;
	}

	@Override
	public void onContentChanged() {
		// TODO Auto-generated method stub
		super.onContentChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getTabWidget().invalidate();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// Destroy the AdView.
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	public void setTitle(int i) {
		Log.v(TAG, "abc");
		this.mainTitle = ((MainTitle) findViewById(R.id.main_title));
		this.mainTitle.setTitleText(i);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
	}
	

}
