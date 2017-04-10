package com.batterydoctor.superfastcharger.fastcharger.ui.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.adapter.MyPagesAdapter;
import com.batterydoctor.superfastcharger.fastcharger.ui.TitleIndicator;

public class AbsListFragment extends FragmentActivity{

	protected ArrayList<TabInfo> tabInfos;
	protected TitleIndicator mTitleIndicator;

	protected Intent mIntent;
	protected ViewPager mViewPager;
	protected MyPagesAdapter myPagesAdapter;
	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.fragment_tab_activity);
		mIntent = getIntent();
		init();
	}
	private void init(){
		tabInfos = new ArrayList<TabInfo>();
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTitleIndicator = (TitleIndicator) findViewById(R.id.pagerindicator);
		myPagesAdapter = new MyPagesAdapter(getSupportFragmentManager(),tabInfos);
		
		mViewPager.setAdapter(myPagesAdapter);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mViewPager.setAdapter(null);
		myPagesAdapter = null;
		mTitleIndicator = null;
		mViewPager = null;
		super.onDestroy();
	}
}
