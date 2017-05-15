package com.usstudio.easytouch.assistivetouch.asyntask;

import java.util.ArrayList;
import java.util.List;

import com.usstudio.easytouch.assistivetouch.itf.LoadAppDone;
import com.usstudio.easytouch.assistivetouch.util.App;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

public class LoadAppAsynTask extends AsyncTask<String, String, ArrayList<App>> {

	private Context context;
	private LoadAppDone loadAppInterface;

	public LoadAppAsynTask(Context context, LoadAppDone loadAppInterface) {
		this.context = context;
		this.loadAppInterface = loadAppInterface;
	}

	@Override
	protected ArrayList<App> doInBackground(String... params) {
		ArrayList<App> apps = new ArrayList<App>();
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packs = packageManager.getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			ApplicationInfo a = p.applicationInfo;
			if ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
				continue;
			}
			App app = new App();
			app.setTitle(p.applicationInfo.loadLabel(packageManager).toString());
			app.setPackageName(p.packageName);
			app.setIcon(a.loadIcon(packageManager));
			apps.add(app);
		}
		return apps;
	}

	@Override
	protected void onPostExecute(ArrayList<App> result) {
		loadAppInterface.loadAppDone(result);
	}
}
