package com.wolfsoft.repairbattery.Fragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;
import com.wolfsoft.repairbattery.ChargerSetting;

import com.wolfsoft.repairbattery.R;


/**
 * @author Adrián García Lomas
 */
public class Pager extends Fragment {

    private DashedCircularProgress dashedCircularProgress;
    private TextView tvProcess;
    private Boolean isClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pager, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dashedCircularProgress = (DashedCircularProgress) view.findViewById(R.id.simple);
        tvProcess = (TextView) view.findViewById(R.id.tvProcess);
        onSpeed();

        dashedCircularProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick==false)
                {
                    onStrong();
                }
                else {
                    onSpeed2();
                }
            }
        });
    }

    private void onSpeed() {
        dashedCircularProgress.setValue(0);
        dashedCircularProgress.setIcon(R.drawable.star);
        isClick = false;
    }

    private void onSpeed2()
    {
        new star3().execute();
        isClick = false;
    }

    private void onStrong() {
        new star().execute();
        isClick = true;
    }

    public static Pager getInstance() {
        return new Pager();
    }




    private class star extends
            AsyncTask<Void, Integer, Void> {
        int myProgress;
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            Random rand = new Random();

            int  n = rand.nextInt(28) + 70;
            int n2 = rand.nextInt(28)+40;

            while(myProgress<101){
                myProgress++;
                if(myProgress  == n) turnOff1();
                if(myProgress == n2) turnOff2();
                publishProgress(myProgress);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            tvProcess.setText(getResources().getText(R.string.restart));
        }

        @Override
        protected void onPreExecute() {
            myProgress = 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            dashedCircularProgress.setValue(values[0]);
            tvProcess.setText(Integer.toString(values[0] - 1) + "%");
        }
    }


    private class star3 extends
            AsyncTask<Void, Integer, Void> {
        int myProgress;
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            while(myProgress>0){
                myProgress--;
                publishProgress(myProgress);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            tvProcess.setText(getResources().getText(R.string.stars));
        }

        @Override
        protected void onPreExecute() {
            myProgress = 101;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            dashedCircularProgress.setValue(values[0]);
            tvProcess.setText(Integer.toString(values[0]-1) + "%");
        }
    }

    public void turnOff1(){
        if(ChargerSetting.readSttKillProcess(getActivity())){
            killApps(getActivity());
        }

    }

    public void turnOff2()
    {
        if(ChargerSetting.readSttBL(getActivity())){
            BluetoothAdapter btEnable = BluetoothAdapter.getDefaultAdapter();
            btEnable.disable();
        }
        if(ChargerSetting.readSttWF(getActivity())){
            ((WifiManager)getActivity().getSystemService(getActivity().WIFI_SERVICE)).setWifiEnabled(false);
        }

    }

    private void setMobileDataEnabled(Context context, boolean enabled) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void killApps(Context paramContext){
        List localList = paramContext.getPackageManager().getInstalledApplications(0);
        ActivityManager localActivityManager = (ActivityManager)paramContext.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator localIterator = localList.iterator();
        while (true){
            if (!localIterator.hasNext()){
                return;
            }
            ApplicationInfo localApplicationInfo = (ApplicationInfo)localIterator.next();
            if (localApplicationInfo.packageName.equals(paramContext.getPackageName()))
                continue;
            localActivityManager.killBackgroundProcesses(localApplicationInfo.packageName);
        }
    }




}
