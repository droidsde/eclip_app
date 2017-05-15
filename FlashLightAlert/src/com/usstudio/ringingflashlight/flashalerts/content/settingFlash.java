package com.usstudio.ringingflashlight.flashalerts.content;

import android.content.Context;
import com.mbapp.lib_tool.makemoney.WSettings;

public class settingFlash extends WSettings {
	public boolean readEnbleCall(Context context){
		return read(context, ContentValue.callFlash, false);
	}
	public void saveEnbleCall(Context context, boolean check){
		save(context, ContentValue.callFlash, check);
	}
	public boolean readEnbleSMS(Context context){
		return read(context, ContentValue.msgFlash, false);
	}
	public void saveEnbleSMS(Context context, boolean check){
		save(context, ContentValue.msgFlash, check);
	}
	public int getMsgFlashDuration(Context context){
		return (1100-read(context, ContentValue.msgFlashDuration, 300)/100);
	}
	public void setMsgFlashDuration(Context context, int text){
		save(context, ContentValue.msgFlashDuration, text);
	}
	public int getCallFlashOnDuration(Context context){
		return read(context, ContentValue.callFlashOnDuration, 3);
	}
	public void setCallFlashOnDuration(Context context, int check){
		 save(context, ContentValue.callFlashOnDuration, check);
	}
	public int getMsgFlashOnDuration(Context context){
		return read(context, ContentValue.msgFlashOnDuration, 300);
	}
	public void setMsgFlashOnDuration(Context context, int text){
		save(context, ContentValue.msgFlashOnDuration, text);
	}
	
}
