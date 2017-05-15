package com.security.virusscanner.antivirus.free;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.security.virusscanner.antivirus.MainActivity;
import com.security.virusscanner.antivirus.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import wolfsolflib.com.activity.ActionbarActivityAds;

public class RemoveViRusAct extends ActionbarActivityAds implements OnItemClickListener {

	DataLite db;

	private ListView ThreatsList;
	private CustomAdapter Adapter;
	private List<AppDetail> Threats;
	BroadcastReceiver receiver;
	IntentFilter filter;
	private String lastThreatRemoved = null;
	private int clickPosition = -1;
	private String clickPkg = null;
	private int deletions = 0;
	private Context cxt;

	public static boolean isUp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_st);

		isUp = true;
		cxt = this;

		ThreatsList = (ListView) findViewById(R.id.st_threatslist);
		ThreatsList.setOnItemClickListener(this);

		db = new DataLite(this);

		Threats = db.getAllInfections();

		Adapter = new CustomAdapter(getApplicationContext());
		Adapter.setListItems(Threats);
		ThreatsList.setAdapter(Adapter);

		new LoadIconsTask().execute(Threats.toArray(new AppDetail[] {}));

		filter = new IntentFilter("android.free.antivirus.package_removed");

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				lastThreatRemoved = intent.getStringExtra("package_name");

				if (lastThreatRemoved.equals(clickPkg)) {
					if (clickPosition != -1) {

						Threats.remove(clickPosition);
						Adapter.notifyDataSetChanged();
						db.threatDeleted(clickPkg);
						deletions++;
						if (Adapter.getCount() == 0)
							allThreatsCleaned();

					}

				}

			}
		};
		registerReceiver(receiver, filter);

	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
		overridePendingTransition(0, R.anim.slide_out_down);
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		unregisterReceiver(receiver);
		db.updateLastDeletions(deletions);
		db.updateStat_del(deletions);
		db.close();
		isUp = false;
	}

	@Override
	public void onResume() {

		super.onResume();
		registerReceiver(receiver, filter);

	}

	@Override
	protected void onPause() {

		super.onPause();
		// registerReceiver(receiver,filter);

	}

	private void allThreatsCleaned() {
		SharedPreferences settings;
		String PREFS_NAME = "VX";
		settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor edit = settings.edit();
		edit.putBoolean("STAT", true);
		edit.commit();
		cxt.startActivity(new Intent(cxt, MainActivity.class));
		finish();
		overridePendingTransition(0, R.anim.slide_out_down);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		final AppDetail inf = (AppDetail) parent.getItemAtPosition(position);
		final int appPosition = position;

//		ContextThemeWrapper ctw = new ContextThemeWrapper(ST.this, android.support.v7.appcompat.R.style.Theme_AppCompat_Dialog_Alert);
//		RD builder = new RD(ctw);
//
//		String msg = "Threat Name : " + inf.getTitle() + "\n\n" + "Path : " + inf.getInstallDir();
//		builder.setTitle(inf.getPackageName());
//
//		builder.setMessage(msg);
//		builder.setCancelable(true);
//		if (inf.getVersionCode() == 1) {
//			builder.setIcon(getResources().getDrawable(R.drawable.warnning));
//		} else {
//			builder.setIcon(Adapter.getIcons().get(inf.getPackageName()));
//		}
//
//		builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				if (inf.getVersionCode() == 0) {
//					Intent intent = new Intent(Intent.ACTION_DELETE);
//					intent.setData(Uri.parse("package:" + inf.getPackageName()));
//					startActivity(intent);
//					clickPosition = appPosition;
//					clickPkg = inf.getPackageName();
//				} else {
//					try {
//						File f = new File(inf.getInstallDir());
//						f.delete();
//						Threats.remove(appPosition);
//						Adapter.notifyDataSetChanged();
//						db.threatDeleted(inf.getPackageName());
//						deletions++;
//						if (Adapter.getCount() == 0)
//							allThreatsCleaned();
//					} catch (Exception e) {
//						Threats.remove(appPosition);
//						Adapter.notifyDataSetChanged();
//						db.threatDeleted(inf.getPackageName());
//						deletions++;
//
//						if (Adapter.getCount() == 0)
//							allThreatsCleaned();
//
//					}
//				}
//
//			}
//		});
//
//		builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				dialog.cancel();
//				Toast.makeText(ST.this, inf.getPackageName() + " ignored", Toast.LENGTH_SHORT).show();
//			}
//		});
//
//		Dialog dialog = builder.create();
//		dialog.show();
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(
				getDialogContext());		
		String msg = "Threat Name : " + inf.getTitle() + "\n\n" + "Path : " + inf.getInstallDir();
		alt_bld.setTitle(inf.getPackageName());

		alt_bld.setMessage(msg);
		alt_bld.setCancelable(true);
		if (inf.getVersionCode() == 1) {
			alt_bld.setIcon(getResources().getDrawable(R.drawable.warnning));
		} else {
			alt_bld.setIcon(Adapter.getIcons().get(inf.getPackageName()));
		}

		alt_bld.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (inf.getVersionCode() == 0) {
					Intent intent = new Intent(Intent.ACTION_DELETE);
					intent.setData(Uri.parse("package:" + inf.getPackageName()));
					startActivity(intent);
					clickPosition = appPosition;
					clickPkg = inf.getPackageName();
				} else {
					try {
						File f = new File(inf.getInstallDir());
						f.delete();
						Threats.remove(appPosition);
						Adapter.notifyDataSetChanged();
						db.threatDeleted(inf.getPackageName());
						deletions++;
						if (Adapter.getCount() == 0)
							allThreatsCleaned();
					} catch (Exception e) {
						Threats.remove(appPosition);
						Adapter.notifyDataSetChanged();
						db.threatDeleted(inf.getPackageName());
						deletions++;

						if (Adapter.getCount() == 0)
							allThreatsCleaned();

					}
				}

			}
		});

		alt_bld.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				Toast.makeText(RemoveViRusAct.this, inf.getPackageName() + " ignored", Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog alert = alt_bld.create();
		alert.show();
	}

	private class LoadIconsTask extends AsyncTask<AppDetail, Void, Void> {
		@Override
		protected Void doInBackground(AppDetail... infs) {

			Map<String, Drawable> icons = new HashMap<String, Drawable>();
			PackageManager manager = getApplicationContext().getPackageManager();

			for (AppDetail inf : infs) {
				String pkgName = inf.getPackageName();
				Drawable ico = null;
				try {
					Intent i = manager.getLaunchIntentForPackage(pkgName);
					if (i != null) {
						ico = manager.getActivityIcon(i);
					}
				} catch (NameNotFoundException e) {
					Log.e("ERROR", "Unable to find icon for package '" + pkgName + "': " + e.getMessage());
				}
				icons.put(inf.getPackageName(), ico);
			}
			Adapter.setIcons(icons);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Adapter.notifyDataSetChanged();
		}
	}

	private Context getDialogContext() {
		Context context;
		if (getParent() != null)
			context = getParent();
		else
			context = this;
		return context;
	}
}