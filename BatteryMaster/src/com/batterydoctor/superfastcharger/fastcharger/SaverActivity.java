package com.batterydoctor.superfastcharger.fastcharger;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.mode.ModeFragment;
import com.batterydoctor.superfastcharger.fastcharger.shedule.ScheduleFragment;
import com.batterydoctor.superfastcharger.fastcharger.ui.fragment.AbsListFragment;
import com.batterydoctor.superfastcharger.fastcharger.ui.fragment.TabInfo;

@TargetApi(23) public class SaverActivity extends AbsListFragment{
	private static final String TAG = "powermanager.SaverActivity";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		        if (!Settings.System.canWrite(getApplicationContext())) {
		            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
		            startActivityForResult(intent, 200);

		        }
		    }
		addTab();
	}
	
	
	
	private void addTab(){
		tabInfos.add(new TabInfo(0,getString(R.string.tab_saver_mode),ModeFragment.class));
		tabInfos.add(new TabInfo(1, getString(R.string.tab_saver_smart), ScheduleFragment.class));
		mTitleIndicator.setViewPager(mViewPager, 0, tabInfos);
		myPagesAdapter.notifyDataSetChanged();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		PowerMgrTabActivity powerMgrTabActivity = (PowerMgrTabActivity) getParent();
		powerMgrTabActivity.setTitle(R.string.title_tab_saver);
	}
}
