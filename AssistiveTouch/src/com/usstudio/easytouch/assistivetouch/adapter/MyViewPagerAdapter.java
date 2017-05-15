package com.usstudio.easytouch.assistivetouch.adapter;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.customview.MainPageView;
import com.usstudio.easytouch.assistivetouch.customview.SettingPageView;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class MyViewPagerAdapter extends PagerAdapter {

	private Context context;

	public MyViewPagerAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public void startUpdate(View arg0) {
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = null;
		if (position == 0) {
			view = lif.inflate(R.layout.main_page_layout, null);
			MainPageView mainView = new MainPageView(view, context);
			mainView.init();
		} else if (position == 1) {
			view = lif.inflate(R.layout.setting_page_layout, null);
			SettingPageView pageView = new SettingPageView(view, context);
			pageView.init();
		}
		((ViewPager) collection).addView(view, 0);
		return view;
	}

}
