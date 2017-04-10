package com.batterydoctor.superfastcharger.fastcharger.apps.serviceapps;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.nvn.log.LogBuider;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.apps.AppsAdapter;
import com.batterydoctor.superfastcharger.fastcharger.apps.AppsFragment;
import com.batterydoctor.superfastcharger.fastcharger.apps.ParserPackage;
import com.batterydoctor.superfastcharger.fastcharger.apps.ParserPackage.drawUsageHistory;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogCustomer;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogNotify;

public class ServiceAppsFragment extends AppsFragment {
	List<RunningServiceInfo> mRunningServiceInfos;
	/**
	 * key is package name
	 */
	Hashtable<String, RunningServiceInfo> hashtable = new Hashtable<String, ActivityManager.RunningServiceInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAdapter = new AppsAdapter(getActivity(), parserPackages);
		mAdapter.setButtonStopClickListen(new AppsAdapter.IButtonClick() {

			@Override
			public void OnButtonCLickListien(ParserPackage parserPackage) {
				// TODO Auto-generated method stub
				onCreateDialog(DIALOG_TYPE_WARNING_STOP, parserPackage);
			}
		});
	}
	
	@Override
	protected void loadAppsRunning() {
		if (mRunningServiceInfos != null) {
			mRunningServiceInfos.clear();
		}
		(view.findViewById(R.id.progress)).setVisibility(View.VISIBLE);
		hashtable.clear();
		mPackageInfos.clear();
		parserPackages.clear();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String my_package = getActivity().getApplicationContext().getPackageName();
				mRunningServiceInfos = mAppsPackageName.getServicesRunning();
				mPackageInfos = mAppsPackageName.getPackageInfo();
				Iterator<RunningServiceInfo> iterator = mRunningServiceInfos
						.iterator();
				while (iterator.hasNext()) {
					RunningServiceInfo serviceInfo = iterator.next();
					String string = serviceInfo.service.getPackageName();
					if (string != null) {
						if(!string.contains("com.android.") && !string.equals(my_package)){
							hashtable.put(string, serviceInfo);
						}
					}
				}
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (parserPackages.size() > 0) {
							(view.findViewById(R.id.progress))
									.setVisibility(View.GONE);
						}
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}
	@Override
	protected void doStopServiceOrProcess(ParserPackage parserPackage){
		OutputStreamWriter osw = null;
		// String command = "am kill com.batterydoctor.superfastcharger.fastcharger";
		// String command = "am kill-all";
		// String command = "am force-stop com.batterydoctor.superfastcharger.fastcharger";
		try {
			java.lang.Process p;
			p = Runtime.getRuntime().exec("su");
			osw = new OutputStreamWriter(p.getOutputStream());
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("am force-stop ");
			stringBuilder.append(parserPackage.getNamePacket());
			osw.write(stringBuilder.toString());
			osw.flush();
			osw.close();
			try {
				p.waitFor();
				if(p.exitValue() !=255){
					Toast.makeText(getActivity(), "Root", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getActivity(), "No root", Toast.LENGTH_LONG).show();
					onCreateDialog(DIALOG_TYPE_NEED_ROOT, null);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void setItemClickListen() {
		// TODO Auto-generated method stub
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		ParserPackage parserPackage = parserPackages.get(position);
		Intent intent = getIntentToOpenSettingApp(parserPackage.getNamePacket());
		startActivity(intent);
	}
}
