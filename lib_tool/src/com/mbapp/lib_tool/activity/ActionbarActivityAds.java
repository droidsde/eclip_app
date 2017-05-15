package com.mbapp.lib_tool.activity;

import com.mbapp.lib_tool.makemoney.Admob;
import com.mbapp.lib_tool.makemoney.Billing;
import com.mbapp.lib_tool.view.W7Common;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public abstract class ActionbarActivityAds extends W7Common{

	protected final String TAG = "Wactivity";
	public Admob admob = null;
	protected Billing billing;
	AlertDialog dialogAdsList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	public void WsetAdmobId(String AD_UNIT_ID, String AD_UNIT_ID_INTERSTITIAL) {
		admob = new Admob(this, AD_UNIT_ID, AD_UNIT_ID_INTERSTITIAL);
	}
	public void WaddTestDevice(String TEST_DEVICE_ID) {
		admob.addTestDevice(TEST_DEVICE_ID);
	}
	public void WloadAdsInterstitial() {
		if(admob!=null)
		{
			Log.d(TAG, "PloadAdsInterstitial ");
			admob.displayInterstitial();
		}
	}
	 
	//Banner 
	public void WloadAdsBanner(int LayoutAdsId) {
		if(admob!=null)
		{
			Log.d(TAG, "PloadAdsBanner ");
			admob.displayBanner(LayoutAdsId);
		}
	}
	public void WloadAdsBanner(View view,int LayoutAdsId) {
		if(admob!=null)
		{
			Log.d(TAG, "PloadAdsBanner Fragment");
			admob.displayBanner(view,LayoutAdsId);
		}
	}
	public void WloadExitApp(){
		if(admob!=null)
		{
			Log.d(TAG, "PloadAdsBanner Fragment");
			admob.loadExitApp();
		}
	}
	public void WdisplayExitApp(){
		if(admob!=null)
		{
			Log.d(TAG, "PloadAdsBanner Fragment");
			admob.DisplayExit();
		}
	}
	public boolean isCheckAds(){
		return admob.checkLoadAds();
	}
	@Override
	public void onResume() {
		super.onResume();
		if (admob != null)
			admob.onResume();
	}
	
	@Override
	public void onDestroy() {
		if (admob != null)
			admob.onDestroy();
		super.onDestroy();
		if (billing != null)
			billing.destroyBilling();
	}
	
	@Override
	protected void onPause() {
		if (admob != null)
			admob.onPause();
		super.onPause();
	}
	//Billing
	

	public void WshowBillingDialog() {
		if (billing != null)
			billing.showBillingDialog();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// billing
		if (billing != null)
			if (!billing.onActivityResult(requestCode, resultCode, data)) {
				return;
			}
		super.onActivityResult(requestCode, resultCode, data);
		// -------------------------------------------------
	}
	
}
