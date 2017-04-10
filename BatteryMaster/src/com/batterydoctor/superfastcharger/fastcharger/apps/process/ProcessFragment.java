package com.batterydoctor.superfastcharger.fastcharger.apps.process;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.nvn.lib.Units;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.apps.AppsAdapter;
import com.batterydoctor.superfastcharger.fastcharger.apps.AppsFragment;
import com.batterydoctor.superfastcharger.fastcharger.apps.ParserPackage;

public class ProcessFragment extends AppsFragment {
	
	protected List<RunningAppProcessInfo> mRunningAppProcessInfos;
	protected List<RunningServiceInfo> mRunningServiceInfos;
	/**
	 * dùng để vẽ thanh process, cho mỗi item app, với max thanh tương tứng là
	 * RATE_MAX_MEMORY_PER_APP*memoryAvaiable
	 */
	private static final float RATE_MAX_MEMORY_PER_APP = 0.075f;
	/**
	 * là lượng ram tổng cộng theo đơn vị Mb
	 */
	
	long memoryAvaiable = 1;
	private Button refresh;
	private Button openUsage;
	HashMap<String, Boolean> packageNameHadServiceRunning = new HashMap<String, Boolean>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAdapter = new AppsAdapter(getActivity(),parserPackages);
		mAdapter.setButtonStopClickListen(new AppsAdapter.IButtonClick() {
			
			@Override
			public void OnButtonCLickListien(ParserPackage parserPackage) {
				
				// TODO Auto-generated method stub
				onCreateDialog(DIALOG_TYPE_WARNING_STOP, parserPackage);
			}
		});
		mAdapter.setMaxUsageMemory((long)(Units.getTotalRAM()*RATE_MAX_MEMORY_PER_APP));
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		refresh = (Button)view.findViewById(R.id.refresh);
		refresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadAppsRunning();
			}
		});
		openUsage = (Button)view.findViewById(R.id.open_usage);
		openUsage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
				startActivity(intent);
			}
		});
		loadAppsRunning();
	}
	@Override
	protected void setItemClickListen() {
		// TODO Auto-generated method stub
	}
	@Override
	protected void loadAppsRunning() {
		if (mRunningAppProcessInfos != null) {
			mRunningAppProcessInfos.clear();
		}
		if(mRunningServiceInfos!=null){
			mRunningServiceInfos.clear();
		}
		mPackageInfos.clear();
		parserPackages.clear();
		mAdapter.notifyDataSetChanged();
		refresh.setEnabled(false);
		(view.findViewById(R.id.progress)).setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String my_package = getActivity().getApplicationContext().getPackageName();
				mRunningAppProcessInfos = mAppsPackageName.getAppsRunning();
				mRunningServiceInfos = mAppsPackageName.getServicesRunning();
				mPackageInfos = mAppsPackageName.getPackageInfo();
				Iterator<RunningAppProcessInfo> iterator = mRunningAppProcessInfos.iterator();
				while(iterator.hasNext()){
					RunningAppProcessInfo appProcessInfo = iterator.next();
					if(!appProcessInfo.processName.equals(my_package)){
						PackageInfo packageInfo = getPackageInfo(appProcessInfo.processName);
						if(packageInfo!=null){
							ParserPackage parserPackage = new ParserPackage(packageManager, getActivity().getResources(), packageInfo, appProcessInfo.pid	, getusageMemory(appProcessInfo.pid));
							parserPackages.add(parserPackage);
							if(hadServiceRunning(appProcessInfo.processName)){
								packageNameHadServiceRunning.put(appProcessInfo.processName, true);
							}else{
								packageNameHadServiceRunning.put(appProcessInfo.processName, false);
							}
						}
					}
				}
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(parserPackages.size()>0){
							(view.findViewById(R.id.progress)).setVisibility(View.GONE);
							refresh.setEnabled(true);
						}
						mAdapter.notifyDataSetChanged();
					}
				});

			}
		}).start();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ParserPackage parserPackage = parserPackages.get(arg2);
		Intent intent = getIntentToOpenSettingApp(parserPackage.getNamePacket());
		startActivity(intent);
	}
	boolean hadServiceRunning(String packageName){
		Iterator<RunningServiceInfo> iterator = mRunningServiceInfos.iterator();
		while (iterator.hasNext()) {
			RunningServiceInfo info = iterator.next();
			if(packageName.equals(info.service.getPackageName())){
				mRunningServiceInfos.remove(info);
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doStopServiceOrProcess(final ParserPackage parserPackage) {
		// TODO Auto-generated method stub
		/**
		 * co service running
		 */
		if(packageNameHadServiceRunning.get(parserPackage.getNamePacket())){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					OutputStreamWriter osw = null;
					// String command = "am kill com.batterydoctor.superfastcharger.fastcharger";
					// String command = "am kill-all";
					// String command = "am force-stop com.batterydoctor.superfastcharger.fastcharger";
					try {
						Process p;
						p = Runtime.getRuntime().exec("su");
						osw = new OutputStreamWriter(p.getOutputStream());
						StringBuilder builder = new StringBuilder();
						builder.append("am force-stop ");
						builder.append(parserPackage.getNamePacket());
						osw.write(builder.toString());
						osw.flush();
						osw.close();
						try {
							p.waitFor();
							if(p.exitValue() !=255){
								getActivity().runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										parserPackages.remove(parserPackage);
										mAdapter.notifyDataSetChanged();

										Toast.makeText(getActivity(), "Root", Toast.LENGTH_LONG).show();
									}
								});
							}else{
								Toast.makeText(getActivity(), "No root", Toast.LENGTH_LONG).show();
								onCreateDialog(DIALOG_TYPE_NEED_ROOT, null);
								dokill(parserPackage);
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							dokill(parserPackage);
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dokill(parserPackage);
					}
				}
			}).start();
			
		}else{
//			android.os.Process.killProcess(parserPackage.getPid());
			dokill(parserPackage);
		}
	}
	private void dokill(ParserPackage parserPackage){
		ActivityManager manager = (ActivityManager) getActivity().getParent().getSystemService(Context.ACTIVITY_SERVICE);
		manager.killBackgroundProcesses(parserPackage.getNamePacket());
		parserPackages.remove(parserPackage);
		mAdapter.notifyDataSetChanged();
	}
	/**
	 * 
	 * @param pId là pId của process
	 * @return
	 */
	float getusageMemory(int pId){ 
		float len = 0;
		int pids[] = new int[1];
		pids[0] = pId;
		android.os.Debug.MemoryInfo[] MI = activityManager.getProcessMemoryInfo(pids);
		len = (float)((float)MI[0].getTotalPss()/(1024f * 1024f));
		len = len/((float)RATE_MAX_MEMORY_PER_APP*memoryAvaiable);
		return len;
	}
}
