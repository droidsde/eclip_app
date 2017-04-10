package com.batterydoctor.superfastcharger.fastcharger.Hardware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.ui.fragment.AbsFragment;

public class HardwareFragment extends AbsFragment{
	private View view;
	private LinearLayout linearLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.mode_layout, container,false);
		linearLayout = (LinearLayout)view.findViewById(R.id.mode_scrollview_items);
		return view;
	}
}
