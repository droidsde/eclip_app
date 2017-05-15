package com.usstudio.easytouch.assistivetouch.asyntask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.usstudio.easytouch.assistivetouch.itf.KillProcessDone;
import com.usstudio.easytouch.assistivetouch.util.PackagesInfo;
import com.usstudio.easytouch.assistivetouch.util.TaskInfo;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;

public class TaskList extends AsyncTask<Void, Void, ArrayList<TaskInfo>> {

	private Context context;
	private int mem = 0;
	private KillProcessDone killProcessInterface;

	public TaskList(Context context, KillProcessDone killProcessInterface) {
		this.context = context;
		this.killProcessInterface = killProcessInterface;
	}

	@Override
	protected ArrayList<TaskInfo> doInBackground(Void... arg0) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
		mem = 0;
		ArrayList<TaskInfo> arrList = new ArrayList<TaskInfo>();
		PackagesInfo pInfo = new PackagesInfo(context);
		Iterator<ActivityManager.RunningAppProcessInfo> iterator = list.iterator();

		do {
			if (!iterator.hasNext()) {
				break;
			}
			ActivityManager.RunningAppProcessInfo runproInfo = (ActivityManager.RunningAppProcessInfo) iterator.next();
			String s = runproInfo.processName;
			if (!s.contains(context.getPackageName())) {

				if (runproInfo.importance == 130 || runproInfo.importance == 300 || runproInfo.importance == 100
						|| runproInfo.importance == 400) {

					TaskInfo info = new TaskInfo(context, runproInfo);
					info.getAppInfo(pInfo);
					if (!isImportant(s)) {
						info.setChceked(true);
					}

					if (info.isGoodProcess()) {
						int j = runproInfo.pid;
						int i[] = new int[1];
						i[0] = j;
						Debug.MemoryInfo memInfo[] = am.getProcessMemoryInfo(i);
						for (int l = 0; l < memInfo.length; l++) {
							Debug.MemoryInfo mInfo = memInfo[l];
							int m = mInfo.getTotalPss() * 1024;
							info.setMem(m);
							int jl = mInfo.getTotalPss() * 1024;
							int kl = mem;
							if (jl > kl)
								mem = mInfo.getTotalPss() * 1024;
						}
						if (mem > 0)
							arrList.add(info);
					}
				}
			}
		} while (true);

		return arrList;
	}

	@Override
	protected void onPostExecute(ArrayList<TaskInfo> arrList) {
		new KillTaskList(arrList, (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE), context,
				killProcessInterface).execute();
	}

	private boolean isImportant(String pname) {

		if (pname.equals("android") || pname.equals("android.process.acore") || pname.equals("system")
				|| pname.equals("com.android.phone") || pname.equals("com.android.systemui")
				|| pname.equals("com.android.launcher")) {
			return true;
		} else {
			return false;
		}
	}
}
