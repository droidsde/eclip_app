package com.boost.clean.speedbooster.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boost.clean.speedbooster.MainActivity;
import com.boost.clean.speedbooster.R;
import com.boost.clean.speedbooster.asyncTask.TotalMemoryStorageTask;
import com.boost.clean.speedbooster.asyncTask.TotalRamTask;
import com.boost.clean.speedbooster.utils.ScreenUtil;
import com.boost.clean.speedbooster.view.ArcProgress;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST = 1337;
    private ArcProgress mArcProgressMemory;
    private ArcProgress mArcProgressRam;

    private TextView mTvStorage;

    private int mSizeProgressMemory;
    private int mSizeProgressRam;

    private Handler mTimerHandler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSizeProgressMemory = ScreenUtil.getWidthScreen(getActivity()) / 2;
        mSizeProgressRam = ScreenUtil.getWidthScreen(getActivity()) / 4;
        mTimerHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mArcProgressMemory = (ArcProgress) view.findViewById(R.id.arcProgressMemory);
        mArcProgressRam = (ArcProgress) view.findViewById(R.id.arcProgressRam);
        LinearLayout viewClean = (LinearLayout) view.findViewById(R.id.viewClean);
        LinearLayout viewBoost = (LinearLayout) view.findViewById(R.id.viewBoost);
        LinearLayout viewAppManager = (LinearLayout) view.findViewById(R.id.viewAppManager);
        LinearLayout viewBattery = (LinearLayout) view.findViewById(R.id.viewBattery);

        mTvStorage = (TextView) view.findViewById(R.id.tvStorage);

        mArcProgressMemory.getLayoutParams().width = mSizeProgressMemory;
        mArcProgressMemory.getLayoutParams().height = mSizeProgressMemory;
        mArcProgressRam.getLayoutParams().width = mSizeProgressRam;
        mArcProgressRam.getLayoutParams().height = mSizeProgressRam;

        viewClean.setOnClickListener(this);
        viewBoost.setOnClickListener(this);
        viewAppManager.setOnClickListener(this);
        viewBattery.setOnClickListener(this);
        return view;
    }

    /**
     * Runnable interface for update current memory status
     */
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            new TotalMemoryStorageTask(getActivity(), mArcProgressMemory, mTvStorage).execute();
            new TotalRamTask(getActivity(), mArcProgressRam).execute();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewClean:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!canAccessLocation()) {
                        requestPermissions();
                    } else {
                        replaceFragment(new CleanFragment(), false);
                    }
                } else {
                    replaceFragment(new CleanFragment(), false);
                }
                break;
            case R.id.viewBoost:
                replaceFragment(new BoostFragment(), false);
                break;
            case R.id.viewAppManager:
                replaceFragment(new AppManagerFragment(), false);
                break;
            case R.id.viewBattery:
                replaceFragment(new BatteryFragment(), false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimerHandler.removeCallbacks(timerRunnable);// remove timer runnable interface
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeader(getString(R.string.app_name), MainActivity.HeaderBarType.TYPE_HOME);
        mTimerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimerHandler.removeCallbacks(timerRunnable);// remove timer runnable interface
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (PackageManager.PERMISSION_GRANTED == getActivity().checkSelfPermission(permission));
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().requestPermissions(LOCATION_PERMS, WRITE_EXTERNAL_STORAGE_REQUEST);
        }
    }
}
