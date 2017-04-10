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
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class MscreenManager extends AbsManager {
	private static MscreenManager manager;
	public static final int SCREEN_BRIGHTNESS_0 = 60;
	public static final int SCREEN_BRIGHTNESS_50 = 180;
	public static final int SCREEN_BRIGHTNESS_100 = 255;
	private ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer,Integer>>();
	public static MscreenManager getInstance(Context context) {
		if (manager == null)
			manager = new MscreenManager(context);
		return manager;
	}

	private int level;
	ContentObserver contentObserver;
	ContentResolver contentResolver;

	private MscreenManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initPair();
		contentObserver = new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				getBrightness();
				update();
			}
		};
		contentResolver = this.mContext.getContentResolver();
	}

	public void getBrightness() {
		int brig = System.getInt(contentResolver,System.SCREEN_BRIGHTNESS_MODE, 1);
		int in = SCREEN_BRIGHTNESS_0;
		if (brig != 1) {// tự động
			in = System.getInt(contentResolver, System.SCREEN_BRIGHTNESS,
					SCREEN_BRIGHTNESS_0);
		}
		level = checkLevel(in);
		this.i = pairs.get(level).second;
	}

	@Override
	public void setImanager(IManager imanager) {
		// TODO Auto-generated method stub
		super.setImanager(imanager);
		Uri uri = Settings.System.getUriFor(System.SCREEN_BRIGHTNESS);
		Uri uri2 = Settings.System.getUriFor(System.SCREEN_BRIGHTNESS_MODE);
		this.contentResolver.registerContentObserver(uri, false,contentObserver);
		this.contentResolver.registerContentObserver(uri2, false,contentObserver);

		getBrightness();
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

	@Override
	public boolean getState() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getIntState(){
		switch (level) {
		case 0:
			return SCREEN_BRIGHTNESS_0;
		case 1:
			return SCREEN_BRIGHTNESS_50;
		case 2:
			return SCREEN_BRIGHTNESS_100;
		default:
			return SCREEN_BRIGHTNESS_0;
		}
	}
	public void setIntSate(Window window) {
		level++;
		if (level > 2) {
			level = 0;
		}
		int state;
		switch (level) {
		case 0:
			state = SCREEN_BRIGHTNESS_0;
			break;
		case 1:
			state = SCREEN_BRIGHTNESS_50;
			break;
		case 2:
			state = SCREEN_BRIGHTNESS_100;
			break;
		default:
			state = SCREEN_BRIGHTNESS_0;
			break;
		}
		setIntSate(window,state,false);
	}
	void initPair(){
		pairs.clear();
		pairs.add(new Pair<Integer, Integer>(SCREEN_BRIGHTNESS_0, R.drawable.setttings_app_screen_light_10));
		pairs.add(new Pair<Integer, Integer>(SCREEN_BRIGHTNESS_50, R.drawable.setttings_app_screen_light_50));
		pairs.add(new Pair<Integer, Integer>(SCREEN_BRIGHTNESS_100, R.drawable.setttings_app_screen_light_100));
	}
	public void setIntSate(Window window,int state,boolean userChangeMode) {
		this.isUserChageMode = userChangeMode;
		System.putInt(contentResolver, System.SCREEN_BRIGHTNESS, state);
		LayoutParams layoutpars = window.getAttributes();
		//Set the brightness of this window
		layoutpars.screenBrightness = state / (float)255;
		//Apply attribute changes to this window
		window.setAttributes(layoutpars);
	}
	public int getLevel(){
		return level;
	}

	private int checkLevel(int n) {
		int lv = 0;
		n --;
		if (n < SCREEN_BRIGHTNESS_0) {
			lv = 0;// 0 %
		} else if (n < SCREEN_BRIGHTNESS_50) {
			lv = 1; // 50%
		} else if (n < SCREEN_BRIGHTNESS_100) {
			lv = 2; // 100%
		}
		return lv;
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
