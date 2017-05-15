package com.mbapp.lib_tool.view;

import java.util.Random;

import com.mbapp.lib_tool.Json.WCheckNetworkConnection;
import com.mbapp.lib_tool.Json.WContentValue;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class W7Common extends W6Method {
	public void WstartActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

	public void WstartActivity(Class<?> cls, Bundle data) {
		Intent intent = new Intent(this, cls);
		intent.putExtras(data);
		startActivity(intent);
	}

	public void WstartService(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startService(intent);
	}

	public void WstartService(Class<?> cls, Bundle data) {
		Intent intent = new Intent(this, cls);
		intent.putExtras(data);
		startService(intent);
	}
	public void WstopService(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		stopService(intent);
	}

	
	public void WToastShort(String text) {
		WFuntion.WToastShort(this, text);
	}
	public void WToastLong(String text) {
		WFuntion.WToastLong(this, text);
	}
	public void WOpenBrower(String PACKAGE_APP){
		WFuntion.WOpenBrower(this, PACKAGE_APP);
	}
	public void WRateApp(){
		if(WCheckNetworkConnection.isConnectionAvailable(this)){
		WFuntion.WRateApp(this,getPackageName());
		WToastShort("Thanks you for rate and comment :D");
		}else{
			WToastShort("No internet...");
		}
		
	}
	public void WRateApp(String PACKAGE_APP){
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +PACKAGE_APP )) );
		
	}
	public void WSendFeedBack(String Wemail,String WEmailTitle){
		WFuntion.WSendFeedBack(this,Wemail,WEmailTitle);
		
	}
	public void WShareApp(String nameApp){
		WFuntion.WShareApp(this,nameApp,getPackageName());
	}
	public void WMoreApp(String nameStore){
		WFuntion.WMoreApp(this,nameStore);
	}
	public void WAdsSmartList(){
		WAdsSmart Ads = new WAdsSmart(this,WContentValue.DOMAINEN);
		Ads.AdsList();
	}
	public void WAdsSmartListVn(){
		WAdsSmart Ads = new WAdsSmart(this, WContentValue.DOMAINVN);
		Ads.AdsList();
	}
	public int WrandomAds(int mRandom){
    	Random rand = new Random();
    	int  n = rand.nextInt(mRandom);
    	return n;
    }
}
