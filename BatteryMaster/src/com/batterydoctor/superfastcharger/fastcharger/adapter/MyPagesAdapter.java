package com.batterydoctor.superfastcharger.fastcharger.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.batterydoctor.superfastcharger.fastcharger.mode.ModeFragment;
import com.batterydoctor.superfastcharger.fastcharger.shedule.ScheduleFragment;
import com.batterydoctor.superfastcharger.fastcharger.ui.fragment.TabInfo;

public class MyPagesAdapter extends FragmentPagerAdapter{
	ArrayList<TabInfo> infos;
//	private static final int SIZE = 2;
	public MyPagesAdapter(FragmentManager fm,ArrayList<TabInfo> arrayList) {
		super(fm);
		// TODO Auto-generated constructor stub
		infos = arrayList;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return infos.get(arg0).getFragemnt();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}


}
