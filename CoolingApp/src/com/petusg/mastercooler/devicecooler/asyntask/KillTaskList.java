package com.petusg.mastercooler.devicecooler.asyntask;

import java.util.List;

import com.petusg.mastercooler.devicecooler.CoolActivity;
import com.petusg.mastercooler.devicecooler.process.TaskInfo;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class KillTaskList extends AsyncTask<Void, Void, Integer>{

	private List<TaskInfo> listTask;
	private ActivityManager acm;
	private Context context;
	
	public KillTaskList(List<TaskInfo> listTask, ActivityManager acm, Context context) {
		this.listTask = listTask;
		this.acm = acm;
		this.context = context;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		int j = 0;
		try {			
			TaskInfo info = null;
			while (listTask.size() > j) {
				info = listTask.get(j);
				if (info.isChceked()) {
					Log.d("TaskList: ", info.getPackageName());
					acm.killBackgroundProcesses(info.getPackageName());
				}
				j++;
			}
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		((CoolActivity) context).updateCoolActivity(result.intValue());
	}
	
}
