package com.boost.clean.speedbooster.asyncTask;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.boost.clean.speedbooster.model.TaskInfo;
import com.boost.clean.speedbooster.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskList extends AsyncTask<Void, Void, ArrayList<TaskInfo>> {

    private static final String TAG = TaskList.class.getName();
    private Context mContext;
    private ProgressBar mProgressBar;
    private PackageManager mPackageManager;
    private OnTaskListListener mOnTaskListListener;
    private long mTotal;

    public TaskList(Context context, ProgressBar progressBar,
                    OnTaskListListener onTaskListListener) {
        mContext = context;
        mProgressBar = progressBar;
        mOnTaskListListener = onTaskListListener;
        mPackageManager = context.getPackageManager();
    }

    @Override
    protected void onPreExecute() {
        if (null != mProgressBar) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ArrayList<TaskInfo> doInBackground(Void... arg0) {
        ActivityManager am = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        ArrayList<TaskInfo> arrList = new ArrayList<>();
        int mem = 0;
        if (Build.VERSION.SDK_INT <= 21) {
            Iterator<ActivityManager.RunningAppProcessInfo> iterator = list.iterator();
            Log.i(TAG, "tasklist: " + list.size());
            do {
                if (!iterator.hasNext()) {
                    break;
                }
                ActivityManager.RunningAppProcessInfo runproInfo = iterator.next();
                String packagename = runproInfo.processName;
                ApplicationInfo applicationInfo = null;
                try {
                    applicationInfo = mPackageManager.getApplicationInfo(packagename, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "error tasklist: " + e);
                }
                if (!packagename.contains(mContext.getPackageName()) && applicationInfo != null
                        && Utils.isUserApp(applicationInfo)) {
                    if (runproInfo.importance == 130
                            || runproInfo.importance == 300
                            || runproInfo.importance == 100
                            || runproInfo.importance == 400) {

                        TaskInfo info = new TaskInfo(mContext, runproInfo);
                        if (Utils.checkLockedItem(mContext, packagename)) {
                            info.setChceked(false);
                        } else {
                            info.setChceked(true);
                        }
                        info.getAppInfo();

                        if (info.isGoodProcess()) {
                            int j = runproInfo.pid;
                            int i[] = new int[1];
                            i[0] = j;
                            Debug.MemoryInfo memInfo[] = am
                                    .getProcessMemoryInfo(i);
                            for (Debug.MemoryInfo mInfo : memInfo) {
                                int m = mInfo.getTotalPss() * 1024;
                                info.setMem(m);
                                mTotal += m;
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
        } else {
            Log.i(TAG, "tasklist: " + am.getRunningServices(Integer.MAX_VALUE).size());
            for (ActivityManager.RunningServiceInfo runningServiceInfo :
                    am.getRunningServices(Integer.MAX_VALUE)) {
                try {
                    PackageInfo packageInfo = mPackageManager.getPackageInfo(runningServiceInfo
                            .service.getPackageName(), 1);
                    ApplicationInfo applicationInfo = null;
                    try {
                        applicationInfo = mPackageManager.getApplicationInfo(packageInfo.packageName, 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.e(TAG, "error tasklist: " + e);
                    }
                    if (!packageInfo.packageName.contains(mContext.getPackageName()) && applicationInfo != null
                            && Utils.isUserApp(applicationInfo)) {

                        TaskInfo info = new TaskInfo(mContext, applicationInfo);
                        if (Utils.checkLockedItem(mContext, mContext.getPackageName())) {
                            info.setChceked(false);
                        } else {
                            info.setChceked(true);
                        }

                        if (info.isGoodProcess()) {
                            int j = runningServiceInfo.pid;
                            int i[] = new int[1];
                            i[0] = j;
                            Debug.MemoryInfo memInfo[] = am
                                    .getProcessMemoryInfo(i);
                            for (Debug.MemoryInfo mInfo : memInfo) {
                                int m = mInfo.getTotalPss() * 1024;
                                info.setMem(m);
                                mTotal += m;
                                int jl = mInfo.getTotalPss() * 1024;
                                int kl = mem;
                                if (jl > kl)
                                    mem = mInfo.getTotalPss() * 1024;
                            }
                            if (mem > 0)
                                arrList.add(info);
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        ArrayList<TaskInfo> arrListNew = new ArrayList<>();
        for (TaskInfo taskInfo : arrList) {
            if (taskInfo.getMem() != 0) {
                arrListNew.add(taskInfo);
            }
        }
        return arrListNew;
    }

    @Override
    protected void onPostExecute(ArrayList<TaskInfo> taskInfos) {
        if (null != mProgressBar) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (null != mOnTaskListListener) {
            mOnTaskListListener.OnResult(taskInfos, mTotal);
        }
    }

    public interface OnTaskListListener {
        void OnResult(ArrayList<TaskInfo> taskInfos, long total);
    }
}
