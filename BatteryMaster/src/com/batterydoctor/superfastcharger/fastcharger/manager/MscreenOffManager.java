package com.batterydoctor.superfastcharger.fastcharger.manager;

import java.util.ArrayList;

import com.batterydoctor.superfastcharger.fastcharger.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.System;
import android.util.Pair;

public class MscreenOffManager extends AbsManager {
	public static final int SCREEN_OFF_TIMEOUT_DEFAULT = 30000; // 30s
	public static final int SCREEN_OFF_TIMEOUT_15s = 15000; // 15s
	public static final int SCREEN_OFF_TIMEOUT_30s = 30000; // 30s
	public static final int SCREEN_OFF_TIMEOUT_1m = 60000; // 1m
	public static final int SCREEN_OFF_TIMEOUT_2m = 120000; // 
	public static final int SCREEN_OFF_TIMEOUT_10m =600000; // 
	public static final int SCREEN_OFF_TIMEOUT_30m =1800000; // 
	
	private static MscreenOffManager manager;
	private int level;
	private ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer,Integer>>();
	private MscreenOffManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initPair();
		contentResolver = this.mContext.getContentResolver();
		contentObserver = new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				getScreenTimerOut();
				update();
			}
		};
	}

	ContentObserver contentObserver;
	ContentResolver contentResolver;

	public static MscreenOffManager getInstance(Context context) {
		if (manager == null)
			manager = new MscreenOffManager(context);
		return manager;
	}

	@Override
	public void setImanager(IManager imanager) {
		// TODO Auto-generated method stub
		super.setImanager(imanager);
		Uri uri = System.getUriFor(System.SCREEN_OFF_TIMEOUT);
		this.contentResolver.registerContentObserver(uri, false,contentObserver);

		getScreenTimerOut();
		update();
	}

	@Override
	public void removeImanager(IManager manager) {
		// TODO Auto-generated method stub
		super.removeImanager(manager);
		try{
			this.contentResolver.unregisterContentObserver(contentObserver);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void getScreenTimerOut() {
		int index = System.getInt(contentResolver, System.SCREEN_OFF_TIMEOUT,
				SCREEN_OFF_TIMEOUT_DEFAULT);
		level = checkLevel(index);
		this.i = pairs.get(level).second;
	}

	@Override
	public boolean getState() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean setTimerOut(){
		level ++;
		if(level == pairs.size()){
			level = 0;
		}
		setTimerOut(pairs.get(level).first,false);
		return true;
	}
	/**
	 * 
	 * @param ix
	 * @return true if change success
	 */
	public boolean setTimerOut(int ix,boolean isUserChangeMode) {
		try {
			this.isUserChageMode = isUserChangeMode;
			System.putInt(contentResolver, System.SCREEN_OFF_TIMEOUT, ix);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}
	public int getInState(){
		switch (level) {
		case 0:
			return SCREEN_OFF_TIMEOUT_15s;
		case 1:
			return SCREEN_OFF_TIMEOUT_30s;
		case 2:
			return SCREEN_OFF_TIMEOUT_1m;
		case 3:
			return SCREEN_OFF_TIMEOUT_2m;
		case 4:
			return SCREEN_OFF_TIMEOUT_10m;
		case 5:
			return SCREEN_OFF_TIMEOUT_30m;

		default:
			return SCREEN_OFF_TIMEOUT_15s;
		}
	}
	private int checkLevel(int in){
		
		in--;
		int lv = 0;
		if(in< SCREEN_OFF_TIMEOUT_15s){
			lv = 0;
		}else if(in< SCREEN_OFF_TIMEOUT_30s){
			lv = 1;
		}else if(in< SCREEN_OFF_TIMEOUT_1m){
			lv = 2;
		}else if(in< SCREEN_OFF_TIMEOUT_2m){
			lv = 3;
		}else if(in< SCREEN_OFF_TIMEOUT_10m){
			lv = 4;
		}else if(in< SCREEN_OFF_TIMEOUT_30m){
			lv = 5;
		}
		return lv;
	}
	void initPair(){
		pairs.clear();
		pairs.add(new Pair<Integer, Integer>(SCREEN_OFF_TIMEOUT_15s, R.drawable.settings_app_screen_timeout_15s));
		pairs.add(new Pair<Integer, Integer>(SCREEN_OFF_TIMEOUT_30s, R.drawable.settings_app_screen_timeout_30s));
		pairs.add(new Pair<Integer, Integer>(SCREEN_OFF_TIMEOUT_1m, R.drawable.settings_app_screen_timeout_1m));
		pairs.add(new Pair<Integer, Integer>(SCREEN_OFF_TIMEOUT_2m, R.drawable.settings_app_screen_timeout_2m));
		pairs.add(new Pair<Integer, Integer>(SCREEN_OFF_TIMEOUT_10m, R.drawable.settings_app_screen_timeout_10m));
		pairs.add(new Pair<Integer, Integer>(SCREEN_OFF_TIMEOUT_30m, R.drawable.settings_app_screen_timeout_30m));
	}
	@Override
	public void setState(boolean i, boolean userChangeMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLongClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
		try {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.mContext.startActivity(intent);
		} catch (Exception e) {
			intent.setAction(Settings.ACTION_SETTINGS);
			this.mContext.startActivity(intent);
		}
	}
}
