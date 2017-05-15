package com.usstudio.easytouch.assistivetouch.asyntask;

import java.util.List;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.itf.KillProcessDone;
import com.usstudio.easytouch.assistivetouch.util.TaskInfo;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class KillTaskList extends AsyncTask<Void, Void, Integer> {

	private List<TaskInfo> listTask;
	private ActivityManager acm;
	private Context context;
	private long beforeMemory;
	private KillProcessDone killProcessInterface;

	public KillTaskList(List<TaskInfo> listTask, ActivityManager acm, Context context,
			KillProcessDone killProcessInterface) {
		this.listTask = listTask;
		this.acm = acm;
		this.context = context;
		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		acm.getMemoryInfo(memInfo);
		beforeMemory = memInfo.availMem;
		this.killProcessInterface = killProcessInterface;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		int j = 0;
		TaskInfo info = null;
		while (listTask.size() > j) {
			info = listTask.get(j);
			if (info.isChceked()) {
				acm.killBackgroundProcesses(info.getPackageName());
			}
			j++;
		}
		return j;
	}

	@Override
	protected void onPostExecute(Integer result) {
		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		acm.getMemoryInfo(memInfo);
		long aftermemory = memInfo.availMem;
		int ramFreed = 0;
		if (aftermemory > beforeMemory) {
			ramFreed = (int) (aftermemory - beforeMemory);
		}
		Toast.makeText(context, context.getString(R.string.freed_ram) + " " + formatMemSize(ramFreed, 0),
				Toast.LENGTH_SHORT).show();
		killProcessInterface.killProcessDone();
	}

	// format memory size to B, KB, MB, GB
	@SuppressLint("DefaultLocale")
	private String formatMemSize(long size, int value) {
		String result = "";
		if (1024L > size) {// size less than 1024, for byte result
			String info = String.valueOf(size);
			result = (new StringBuilder(info)).append(" B").toString();
		} else if (1048576L > size) {// for KB result
			String s2 = (new StringBuilder("%.")).append(value).append("f").toString();
			Object aobj[] = new Object[1];
			Float float1 = Float.valueOf((float) size / 1024F);
			aobj[0] = float1;
			String s3 = String.valueOf(String.format(s2, aobj));
			result = (new StringBuilder(s3)).append(" KB").toString();
		} else if (1073741824L > size) {// for MB result
			String s4 = (new StringBuilder("%.")).append(value).append("f").toString();
			Object aobj1[] = new Object[1];
			Float float2 = Float.valueOf((float) size / 1048576F);
			aobj1[0] = float2;
			String s5 = String.valueOf(String.format(s4, aobj1));
			result = (new StringBuilder(s5)).append(" MB").toString();
		} else {// for GB Result
			Object aobj2[] = new Object[1];
			Float float3 = Float.valueOf((float) size / 1.073742E+009F);
			aobj2[0] = float3;
			String s6 = String.valueOf(String.format("%.2f", aobj2));
			result = (new StringBuilder(s6)).append(" GB").toString();
		}
		return result;
	}
}