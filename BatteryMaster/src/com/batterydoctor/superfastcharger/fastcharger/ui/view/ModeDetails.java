package com.batterydoctor.superfastcharger.fastcharger.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.batterydoctor.superfastcharger.fastcharger.R;

public class ModeDetails extends LinearLayout {
	ArrayList<String> lablesArr;
	ArrayList<Pair<String, Boolean>> valuesArrPair;
	ArrayList<Boolean> valuesArrBoolean;
	ArrayList<View> items;

	public ModeDetails(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}

	public ModeDetails(Context context, ArrayList<String> lables,
			ArrayList<Boolean> values) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		this.lablesArr = lables;
		this.valuesArrBoolean = values;
	}

	public ModeDetails(Context context, ArrayList<String> lables) {
		super(context);
		// TODO Auto-generated constructor stub
		this.lablesArr = lables;
	}

	public void setLables(ArrayList<String> arrayList) {
		this.lablesArr = arrayList;
	}

	public void setValuesBoolean(ArrayList<Boolean> booleans) {
		this.valuesArrBoolean = booleans;
	}

	/**
	 * 
	 * @param booleans
	 */
	public void setValuesPair(ArrayList<Pair<String, Boolean>> booleans) {
		this.valuesArrPair = booleans;
	}

	public void create() {
		init();
	}

	private void init() {
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		if (lablesArr != null) {
			int size = lablesArr.size();
			if (valuesArrBoolean != null) {
				if (valuesArrBoolean.size() == size) {
					
				}
			} else if (valuesArrPair != null) {
				if (valuesArrPair.size() == size) {

				}
			}
		}
	}

	private View newItem() {
		View view = inflate(getContext(), R.layout.mode_setting_list_item, null);
		return view;
	}

}
