package com.batterydoctor.superfastcharger.fastcharger.ui.fragment;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.manager.MscreenManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MscreenOffManager;
import com.batterydoctor.superfastcharger.fastcharger.ui.view.ModeItem;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class AbsFragment extends Fragment{
	protected Activity mActivity = null;
	protected Resources mResources = null;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.mActivity = activity;
		this.mResources = activity.getResources();
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}
	/**
	 * mode mặc định với arrow gone
	 * @param lable 
	 * @param detail
	 * @return
	 */
	protected ModeItem initMode(String lable,String detail){
		ModeItem item = new ModeItem(getActivity());
		item.setLable(lable);
		item.setDetail(detail);
		item.setArrowVisible(View.GONE);
		return item;
	}
	/**
	 * mode ngư�?i dùng add thêm, ko có detail
	 * @param lable
	 * @return
	 */
	protected ModeItem initMode(String lable){
		ModeItem item = new ModeItem(getActivity());
		item.setLable(lable);
		item.setDetailVisible(View.GONE);
		return item;
	}
	/**
	 * add new Item
	 * @return
	 */
	protected ModeItem initModeAddNew(){
		ModeItem item = new ModeItem(getActivity());
		item.setLable(getResources().getString(R.string.mode_add_new));
		item.setDetailVisible(View.GONE);
		item.setIcon(R.drawable.add_mode);
		item.setArrowVisible(View.GONE);
		return item;
	}
}
