package com.boost.clean.speedbooster.asyncTask;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.boost.clean.speedbooster.view.ArcProgress;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TotalRamTask extends AsyncTask<Void, Integer, Boolean> {

    private static final String TAG = TotalRamTask.class.getName();
    private Context mContext;
    private ArcProgress mArcProgressRam;

    public TotalRamTask(Context context, ArcProgress arcProgressRam) {
        mContext = context;
        mArcProgressRam = arcProgressRam;
        mArcProgressRam.setMax(100);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        long totalRam = getTotalRam();
        long availableRam = getAvaiableRam(mContext);
        long useRam = totalRam - availableRam;
        int percentRam = (int) (((double) useRam / (double) totalRam) * 100);

        if (percentRam > 100) {
            percentRam = percentRam % 100;
        }
        Log.i(TAG, "---percentRam-->>>>>" + percentRam);
        for (int i = 0; i <= percentRam; i++) {
            publishProgress(i);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (null != mArcProgressRam) {
            mArcProgressRam.setProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    private long getTotalRam() {
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            //total Memory
            initial_memory = Integer.valueOf(arrayOfString[1]) * 1024;
            localBufferedReader.close();
            return initial_memory;
        } catch (IOException e) {
            return -1;
        }
    }

    private long getAvaiableRam(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.availMem;
    }
}
