package com.security.virusscanner.antivirus.free;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppAndFile {

	private Context context;
	List<String> filePaths;

	public AppAndFile(Context c) {
		this.filePaths = new ArrayList<String>();
		this.context = c;
	}

	public List<App> listApp(boolean includeSysApps) {
		ArrayList<App> apps = new ArrayList<App>();

		PackageManager packageManager = context.getPackageManager();

		List<PackageInfo> packs = packageManager.getInstalledPackages(0);

		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			ApplicationInfo a = p.applicationInfo;

			if ((!includeSysApps) && ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
				continue;
			}
			App app = new App();
			app.setTitle(p.applicationInfo.loadLabel(packageManager).toString());
			app.setPackageName(p.packageName);
			app.setVersionName(p.versionName);
			app.setVersionCode(p.versionCode);
			app.setsourcePath(a.sourceDir);
			app.setIcon(a.loadIcon(packageManager));
			apps.add(app);
		}
		return apps;
	}

	public List<String> listFile() {
		return filePaths;
	}
	

	public void recursiveFile(File[] file1) {
		int n = 0;
		String filePath = "";
		if (file1 != null) {
			while (n != file1.length) {
				filePath = file1[n].getAbsolutePath();
				if (file1[n].isDirectory()) {
					File file[] = file1[n].listFiles();
					recursiveFile(file);
				} else {
					filePaths.add(filePath);
				}
				n++;
			}
		}
	}

}
