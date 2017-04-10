package com.batterydoctor.superfastcharger.fastcharger.optimize;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nvn.lib.Units;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.apps.AppsPackageName;
import com.batterydoctor.superfastcharger.fastcharger.apps.ParserPackage;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogNotify;
import com.batterydoctor.superfastcharger.fastcharger.ui.MainTitle;

public class OptimizeActivity extends Activity implements OnClickListener {
	private int PERIOD = 3000;
	static final String PREF_OPTIMIZE = "optimize_shedule";
	static final String LAST_OPTIMIZE_TIME = "last_optimize_time";
	static final long TIMER_TO_OPTIMIZE = 2 * 60 * 1000; // 2 phut
	static final int THRESHOLD_RAM = 15; // MB
	static final String TAG = "OptimizeActivity";
	/**
	 * totalRam in MB
	 */
	int totalRam = -1;
	Button optimizeButton;
	ListView listViewApps;
	MainTitle mainTitle;
	LayoutInflater inflater;
	ArrayList<ParserPackage> parserPackages;
	AppsOptimizeAdapter adapter;
	CheckBox usingRoot;
	TextView ramInfo;
	ActivityManager activityManager;
	boolean isSaved;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_optimize);
		isSaved = false;
		totalRam = Units.getTotalRAM();
		initAction();
		initControls();
		initListView();
	}

	void initAction() {
		optimizeButton = (Button) findViewById(R.id.optimize_button);
		ramInfo = (TextView) findViewById(R.id.optimize_ram_info_values);
		listViewApps = (ListView) findViewById(R.id.list);
		mainTitle = (MainTitle) findViewById(R.id.optimize_title);

		usingRoot = (CheckBox) findViewById(R.id.optimize_using_root);
		listViewApps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		inflater = getLayoutInflater();

		activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
	}

	void initControls() {
		mainTitle.setTitleText(R.string.optimize_title);
		mainTitle.setLeftButtonIcon(R.drawable.title_bar_button_back);
		mainTitle.setLeftButtonOnClickListen(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		optimizeButton.setOnClickListener(this);
		optimizeButton.setText(R.string.optimize_save_now);

		findViewById(R.id.optimize_root).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						usingRoot.setChecked(!usingRoot.isChecked());
					}
				});
		usingRoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							DialogNotify dialogNotify = new DialogNotify(
									OptimizeActivity.this);
							dialogNotify
									.setMessageContent(R.string.optimize_using_root);
							dialogNotify.show();
						}
					}
				});
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MemoryInfo memoryInfo = new MemoryInfo();
				activityManager.getMemoryInfo(memoryInfo);
				final long memoryAvaiable = memoryInfo.availMem/(1024*1024);
				String total;
				if(totalRam==-1){
					total = String.valueOf("/NA MB");
				}else{
					total = "/"+String.valueOf(totalRam) + " MB";
				}
				final String[] strings = {String.valueOf(totalRam - memoryAvaiable),total};
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String string;
						if(memoryAvaiable < totalRam * 0.7f){// xanh
							string = getResources().getString(R.string.battery_info_value_2, (Object[])strings);
						}else{//�?�?
							string = getResources().getString(R.string.battery_info_value_3, (Object[])strings);
						}

						ramInfo.setText(Html.fromHtml(string));
					}
				});
			}
		}, 0, PERIOD);
	}

	Timer timer = new Timer();
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		timer.cancel();
		super.onDestroy();
	}
	void initListView() {
		parserPackages = new ArrayList<ParserPackage>();
		adapter = new AppsOptimizeAdapter(getApplicationContext(),
				android.R.layout.simple_list_item_1, parserPackages);
		listViewApps.setAdapter(adapter);
		if (allowOptimize())
			loadAppsUsage();
		else {
			isSaved = true;
			(findViewById(R.id.optimize_root)).setVisibility(View.GONE);
			(findViewById(R.id.progress)).setVisibility(View.GONE);
			(findViewById(R.id.list)).setVisibility(View.GONE);
			optimizeButton.setText(R.string.optimize_go_back);
			((TextView) findViewById(R.id.optimize_running_number))
					.setText(R.string.optimize_running_apps_stops);
		}
	}

	void loadAppsUsage() {
		if (parserPackages == null) {
			parserPackages = new ArrayList<ParserPackage>();
		}
		parserPackages.clear();
		(findViewById(R.id.progress)).setVisibility(View.VISIBLE);
		optimizeButton.setEnabled(false);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String my_package = getApplicationContext().getPackageName();
				List<RunningAppProcessInfo> mRunningAppProcessInfos = AppsPackageName
						.getApps(OptimizeActivity.this).getAppsRunning();
				List<PackageInfo> packageInfos = AppsPackageName.getApps(
						OptimizeActivity.this).getPackageInfo();
				Iterator<RunningAppProcessInfo> iterator = mRunningAppProcessInfos
						.iterator();
				while (iterator.hasNext()) {
					RunningAppProcessInfo appProcessInfo = iterator.next();
					if (!appProcessInfo.processName.equals(my_package)) {
						PackageInfo packageInfo = getPackageInfo(packageInfos,
								appProcessInfo.processName);
						if (packageInfo != null) {
							int ram = getusageMemory(appProcessInfo.pid);
							if (ram >= THRESHOLD_RAM) {
								ParserPackage parserPackage = new ParserPackage(
										getPackageManager(), packageInfo, ram,
										appProcessInfo.pid);
								parserPackages.add(parserPackage);
							}
						}
					}
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (parserPackages.size() > 0) {
							((TextView) findViewById(R.id.optimize_running_number))
									.setText(getResources().getString(
											R.string.optimize_running_apps)+ " ("+ parserPackages.size()+ ")");
							optimizeButton.setEnabled(true);
						} else {
							isSaved = true;
							optimizeButton.setEnabled(true);
							(findViewById(R.id.list)).setVisibility(View.GONE);
							optimizeButton.setText(R.string.optimize_go_back);
							((TextView) findViewById(R.id.optimize_running_number))
									.setText(R.string.optimize_running_apps_stops);
						}
						(findViewById(R.id.progress)).setVisibility(View.GONE);
						adapter.notifyDataSetChanged();
					}
				});

			}
		}).start();
	}

	void doSave() {
		if (usingRoot.isChecked()) {
			dokillWithRoot();
		} else {
			dokillWithoutRoot();
		}
		SharedPreferences preferences = getSharedPreferences(PREF_OPTIMIZE,
				Context.MODE_PRIVATE);
		preferences
				.edit()
				.putLong(LAST_OPTIMIZE_TIME,
						java.lang.System.currentTimeMillis()).commit();
	}

	void dokillWithRoot() {
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
					String command = "am kill-all";
					osw.write(command);
					osw.flush();
					osw.close();
					try {
						p.waitFor();
						if (p.exitValue() != 255) {
							animKill();
						} else {
							dokillWithoutRoot();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dokillWithoutRoot();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					dokillWithoutRoot();
				}
			}
		}).start();

	}

	void dokillWithoutRoot() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ActivityManager manager =(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
				int size = parserPackages.size();
				for (int i = size - 1; i >= 0; i--) {
					final ParserPackage parserPackage = parserPackages.get(i);
					manager.killBackgroundProcesses(parserPackage.getNamePacket());
//					android.os.Process.killProcess(parserPackages.get(i).getPid());
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							parserPackages.remove(parserPackage);
							adapter.notifyDataSetChanged();
						}
					});
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						findViewById(R.id.text).setVisibility(View.VISIBLE);
						((TextView) findViewById(R.id.optimize_running_number))
								.setText(R.string.optimize_running_apps_stops);
						optimizeButton.setText(R.string.optimize_go_back);
						optimizeButton.setEnabled(true);
						isSaved = true;
					}
				});
			}
		}).start();
	}

	void animKill() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int size = parserPackages.size();
				for (int i = size - 1; i >= 0; i--) {
					final ParserPackage parserPackage = parserPackages.get(i);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							parserPackages.remove(parserPackage);
							adapter.notifyDataSetChanged();
						}
					});
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						findViewById(R.id.text).setVisibility(View.VISIBLE);
						((TextView) findViewById(R.id.optimize_running_number))
								.setText(R.string.optimize_running_apps_stops);
						optimizeButton.setText(R.string.optimize_go_back);
						optimizeButton.setEnabled(true);
						isSaved = true;
					}
				});
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(optimizeButton)) {
			if (!isSaved) {
				optimizeButton.setEnabled(false);
				optimizeButton.setText(R.string.optimize_saving);
				doSave();
			} else {
				onBackPressed();
			}
		}
	}

	PackageInfo getPackageInfo(List<PackageInfo> mPackageInfos, String packName) {
		Iterator<PackageInfo> iterator = mPackageInfos.iterator();
		while (iterator.hasNext()) {
			PackageInfo info = iterator.next();
			if (info.packageName.equals(packName)) {
				return info;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param pId
	 * @return ram MB
	 */
	int getusageMemory(int pId) {
		int pids[] = new int[1];
		pids[0] = pId;
		android.os.Debug.MemoryInfo[] MI = activityManager
				.getProcessMemoryInfo(pids);
		return MI[0].getTotalPss() / 1024;
	}

	class AppsOptimizeAdapter extends ArrayAdapter<ParserPackage> {

		public AppsOptimizeAdapter(Context context, int resource,
				List<ParserPackage> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			Holder holder;
			if (view == null) {
				LayoutInflater layoutInflater = inflater;
				view = layoutInflater.inflate(R.layout.optimize_listview_item,
						parent, false);
				holder = new Holder();
				holder.icon = (ImageView) view.findViewById(R.id.icon);
				holder.name = (TextView) view.findViewById(R.id.label);
				holder.usageMemory = (TextView) view.findViewById(R.id.desc);

				ParserPackage parserPackage = getItem(position);
				holder.icon.setImageDrawable(parserPackage.getIcon());
				holder.name.setText(parserPackage.getName());
				holder.usageMemory.setText(getResources().getString(
						R.string.optimize_memory)
						+ " "
						+ String.valueOf(parserPackage.getUsageRam())
						+ "MB");
				view.setTag(holder);
			} else {
				holder = (Holder) view.getTag();
			}
			return view;
		}
	}


	boolean allowOptimize() {
		SharedPreferences preferences = getSharedPreferences(PREF_OPTIMIZE,Context.MODE_PRIVATE);
		if (!preferences.contains(LAST_OPTIMIZE_TIME)) {
			preferences.edit().putLong(LAST_OPTIMIZE_TIME, 0l).commit();
		}
		long last = preferences.getLong(LAST_OPTIMIZE_TIME, 0l);
		long cur = java.lang.System.currentTimeMillis();
		if (cur - last > TIMER_TO_OPTIMIZE) {
			// preferences.edit().putLong(LAST_OPTIMIZE_TIME, cur).commit();
			return true;
		} else {
			return false;
		}
	}

	class Holder {
		ImageView icon;
		TextView name;
		TextView usageMemory;
	}
}
