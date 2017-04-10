package com.batterydoctor.superfastcharger.fastcharger.manager;

import com.batterydoctor.superfastcharger.fastcharger.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.provider.Settings;
import android.provider.MediaStore.Audio.Media;

public class MaudioManager extends AbsManager{
	private static MaudioManager manager;
	AudioManager audioManager;
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			checkEnable();
			update();
		}
	};
	private MaudioManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		audioManager = (AudioManager)this.mContext.getSystemService(Context.AUDIO_SERVICE);
		
	}
	private void checkEnable(){
		int state = audioManager.getRingerMode();
		if(state == AudioManager.RINGER_MODE_NORMAL){
			this.b = true;
			this.i = R.drawable.settings_app_volume_100;
		}else{
			this.b = false;
			this.i = R.drawable.settings_app_volume_0;
		}
	}
	public static MaudioManager getInstance(Context context){
		if(manager == null)
			manager = new MaudioManager(context);
		return manager;
	}
	@Override
	public void setImanager(IManager imanager) {
		// TODO Auto-generated method stub
		super.setImanager(imanager);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
		intentFilter.addAction(AudioManager.VIBRATE_SETTING_CHANGED_ACTION);
		mContext.registerReceiver(receiver, intentFilter);
		checkEnable();
		update();
	}
	@Override
	public void removeImanager(IManager manager) {
		// TODO Auto-generated method stub
		super.removeImanager(manager);
		try {
			this.mContext.unregisterReceiver(receiver);
		} catch (Exception e) {

		}
	}
	@Override
	public boolean getState() {
		// TODO Auto-generated method stub
		return b;
	}
	@Override
	public void setState(boolean enable, boolean userChangeMode) {
		// TODO Auto-generated method stub
		this.b = enable;
		this.isUserChageMode = userChangeMode;
		if(enable){
			this.audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}else{
			this.audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		}
	}
	@Override
	public void setLongClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
		try {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.mContext.startActivity(intent);
		} catch (Exception e) {
			intent.setAction(Settings.ACTION_SETTINGS);
			this.mContext.startActivity(intent);
		}
	}

}
