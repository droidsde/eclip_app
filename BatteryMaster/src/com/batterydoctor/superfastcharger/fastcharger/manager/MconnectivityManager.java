package com.batterydoctor.superfastcharger.fastcharger.manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.batterydoctor.superfastcharger.fastcharger.R;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class MconnectivityManager extends AbsManager {
	private static MconnectivityManager manager;

	private MconnectivityManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	ConnectivityManager connectivityManager;
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(getState()){
				i = R.drawable.settings_app_data_on;
				b = true;
			}else{
				i = R.drawable.settings_app_data_off;
				b = false;
			}
			update();
		}
	};
	public static MconnectivityManager getInstance(Context context) {
		if (manager == null)
			manager = new MconnectivityManager(context);
		return manager;
	}

	@Override
	public void setImanager(IManager imanager) {
		// TODO Auto-generated method stub
		super.setImanager(imanager);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		intentFilter.addAction(ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED);
		this.mContext.registerReceiver(receiver, intentFilter);

		if(getState()){
			i = R.drawable.settings_app_data_on;
			b = true;
		}else{
			i = R.drawable.settings_app_data_off;
			b = false;
		}
		update();
	}

	@Override
	public void removeImanager(IManager manager) {
		// TODO Auto-generated method stub
		super.removeImanager(manager);
		try{
			this.mContext.unregisterReceiver(receiver);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean getState() {
		// TODO Auto-generated method stub
		final ConnectivityManager conman = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

	    try {
	        Class<?> c = Class.forName(conman.getClass().getName());
	        Method m = c.getDeclaredMethod("getMobileDataEnabled");
	        m.setAccessible(true);
	        return (Boolean)m.invoke(conman);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	@Override
	public void setState(boolean isEnable, boolean userChangeMode) {
		// TODO Auto-generated method stub
		try {
			this.b = isEnable;
			this.isUserChageMode = userChangeMode;
			setMobileDataEnabled(mContext, isEnable);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Method method;
//		try {
//			this.isUserChageMode = userChangeMode;
//			method = ConnectivityManager.class.getMethod(
//					"setMobileDataEnabled", Boolean.TYPE);
//			method.setAccessible(true);
//			method.invoke(this.i, isEnable);
//			b = isEnable;
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	private void setMobileDataEnabled(Context context, boolean enabled) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
	    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    final Class conmanClass = Class.forName(conman.getClass().getName());
	    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
	    iConnectivityManagerField.setAccessible(true);
	    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
	    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
	    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	    setMobileDataEnabledMethod.setAccessible(true);

	    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}

	@Override
	public void setLongClick() {
		// TODO Auto-generated method stub
		try {
			Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//			ComponentName cName = new ComponentName("com.android.phone","com.android.phone.Settings");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.setComponent(cName); 
			this.mContext.startActivity(intent);
		} catch (Exception e) {
			Intent intent =  new Intent();
			intent.setAction(Settings.ACTION_SETTINGS);
			this.mContext.startActivity(intent);
		}
	}
}
