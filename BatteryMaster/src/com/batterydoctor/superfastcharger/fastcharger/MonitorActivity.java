package com.batterydoctor.superfastcharger.fastcharger;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.apps.AppsAdapter;
import com.batterydoctor.superfastcharger.fastcharger.apps.ParserPackage;
import com.batterydoctor.superfastcharger.fastcharger.apps.process.ProcessFragment;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogCustomer;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogNotify;
import com.nvn.lib.Units;

public class MonitorActivity extends FragmentActivity implements OnItemClickListener{
	protected static final int DIALOG_TYPE_WARNING_STOP = 0;
	protected static final int DIALOG_TYPE_NEED_ROOT = 1;
	
	
	private static final String TAG = "powermanager.MonitorActivity";
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
	protected AppsAdapter mAdapter;
	protected ActivityManager activityManager;
	HashMap<String, Boolean> packageNameHadServiceRunning = new HashMap<String, Boolean>();
	protected List<PackageInfo> mPackageInfos = new ArrayList<PackageInfo>();
	protected ArrayList<ParserPackage> parserPackages = new ArrayList<ParserPackage>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apps_layout);
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		mAdapter = new AppsAdapter(this,parserPackages);
		mAdapter.setButtonStopClickListen(new AppsAdapter.IButtonClick() {
			
			@Override
			public void OnButtonCLickListien(ParserPackage parserPackage) {
				
				// TODO Auto-generated method stub
				oncreatedialog(DIALOG_TYPE_WARNING_STOP, parserPackage);
			}
		});
		mAdapter.setMaxUsageMemory((long)(Units.getTotalRAM()*RATE_MAX_MEMORY_PER_APP));
		
		init();
		loadAppsRunning();
	}
	void init(){
		ListView listView = (ListView) findViewById(R.id.list);
		refresh = (Button)findViewById(R.id.refresh);
		openUsage = (Button)findViewById(R.id.open_usage);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		refresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadAppsRunning();
			}
		});
		openUsage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
				startActivity(intent);
			}
		});
	}
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
		(findViewById(R.id.progress)).setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String my_package = getApplicationContext().getPackageName();
				mRunningAppProcessInfos = activityManager.getRunningAppProcesses();
				mRunningServiceInfos = activityManager.getRunningServices(300);
				mPackageInfos =  getPackageManager().getInstalledPackages(PackageManager.GET_DISABLED_COMPONENTS);
				Iterator<RunningAppProcessInfo> iterator = mRunningAppProcessInfos.iterator();
				while(iterator.hasNext()){
					RunningAppProcessInfo appProcessInfo = iterator.next();
					if(!appProcessInfo.processName.equals(my_package)){
						PackageInfo packageInfo = getPackageInfo(appProcessInfo.processName);
						if(packageInfo!=null){
							ParserPackage parserPackage = new ParserPackage(getPackageManager(),getResources(), packageInfo, appProcessInfo.pid	, getusageMemory(appProcessInfo.pid));
							parserPackages.add(parserPackage);
							if(hadServiceRunning(appProcessInfo.processName)){
								packageNameHadServiceRunning.put(appProcessInfo.processName, true);
							}else{
								packageNameHadServiceRunning.put(appProcessInfo.processName, false);
							}
						}
					}
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(parserPackages.size()>0){
							(findViewById(R.id.progress)).setVisibility(View.GONE);
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
	protected Intent getIntentToOpenSettingApp(String pkg) {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.fromParts("package", pkg, null));
		return intent;
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
					// String command = "am kill com.nvn.powermanager";
					// String command = "am kill-all";
					// String command = "am force-stop com.nvn.powermanager";
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
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										parserPackages.remove(parserPackage);
										mAdapter.notifyDataSetChanged();

										Toast.makeText(MonitorActivity.this, "Root", Toast.LENGTH_LONG).show();
									}
								});
							}else{
								Toast.makeText(MonitorActivity.this, "No root", Toast.LENGTH_LONG).show();
								oncreatedialog(DIALOG_TYPE_NEED_ROOT, null);
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
		ActivityManager manager = (ActivityManager) getParent().getSystemService(Context.ACTIVITY_SERVICE);
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();


		PowerMgrTabActivity powerMgrTabActivity = (PowerMgrTabActivity) getParent();
		powerMgrTabActivity.setTitle(R.string.title_tab_monitor);
	}
	protected void oncreatedialog(int type,final ParserPackage data){
		DialogCustomer dialogCustomer = null;
		switch (type) {
		case DIALOG_TYPE_NEED_ROOT:
			dialogCustomer = new DialogNotify(this);
			dialogCustomer.setMessageContent(R.string.dialog_need_root);
			dialogCustomer.show();
			break;
		case DIALOG_TYPE_WARNING_STOP:
			if(data == null) return;
			dialogCustomer = new DialogCustomer(this);
			dialogCustomer.setTitle(R.string.dialog_title_warning);
			dialogCustomer.setMessageContent(R.string.dialog_waring_stop);
			dialogCustomer.setDialogCloseListen(new DialogCustomer.IDialogCloseListen() {
				
				@Override
				public void OnRightButtonDialogClick() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void OnLeftButtonDialogClick() {
					// TODO Auto-generated method stub
					doStopServiceOrProcess(data);
				}
			});
			dialogCustomer.show();
			break;
		default:
			break;
		}
	}
	protected PackageInfo getPackageInfo(String packName){
		Iterator<PackageInfo> iterator = mPackageInfos.iterator();
		while (iterator.hasNext()) {
			PackageInfo info = iterator.next();
			if(info.packageName.equals(packName)){
				return info;
			}
		}
		return null;
	}
}
