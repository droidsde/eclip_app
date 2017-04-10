package com.batterydoctor.superfastcharger.fastcharger.shedule;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogCustomer;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogDelete;
import com.batterydoctor.superfastcharger.fastcharger.ui.data.ScheduleItemData;
import com.batterydoctor.superfastcharger.fastcharger.ui.fragment.AbsFragment;
import com.batterydoctor.superfastcharger.fastcharger.ui.view.ModeItem;


public class ScheduleFragment extends AbsFragment{
	private static final String ADD_NEW = "add new";
	private View view;
//	ArrayList<ModeItem> modeItems;
	Hashtable<String, ModeItem> hashtable = new Hashtable<String, ModeItem>();
	ArrayList<ScheduleItemData> itemDatas = new ArrayList<ScheduleItemData>();
	private LinearLayout linearLayout;
	LayoutInflater layoutInflater;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layoutInflater = inflater;
		view = inflater.inflate(R.layout.mode_layout, container,false);
		linearLayout = (LinearLayout)view.findViewById(R.id.mode_scrollview_items);
		init();
		return view;
	}
	private void init(){
		try {
			readData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ModeItem addNew = initModeAddNew();
		addNew.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),NewScheduleActivity.class);
				startActivityForResult(intent, NewScheduleActivity.REQUEST_ADD_NEW);
			}
		});
		hashtable.put(ADD_NEW, addNew);
		linearLayout.addView(addNew);
	}
//	[[start_hour,start_minute],[stop_hour,stop_minute],[repeat day],typestart,typestop]
	void readData() throws JSONException{
		itemDatas.clear();
		hashtable.clear();
		linearLayout.removeAllViews();
		SharedPreferences sharedPreferences = mActivity.getSharedPreferences(NewScheduleActivity.SCHEDULE_PREF, Context.MODE_PRIVATE);
		Map<String, ?> map = sharedPreferences.getAll();
		for(Map.Entry<String, ?> entry : map.entrySet()){
			String key = entry.getKey();
			if(!key.equals(NewScheduleActivity.INDEX)){
				String value = (String)entry.getValue();
				JSONArray schedule = new JSONArray(value);
				ScheduleItemData itemData = new ScheduleItemData();
				
				JSONArray start = schedule.getJSONArray(0);
				itemData.startHour = start.getInt(0);
				itemData.startMinute = start.getInt(1);
				
				JSONArray stop = schedule.getJSONArray(1);
				itemData.stopHour = stop.getInt(0);
				itemData.stopMinute = stop.getInt(1);
				
				JSONArray repJson = schedule.getJSONArray(2);
				boolean[] repeat = new boolean[repJson.length()];
				for(int i =0 ; i < repJson.length() ; i++){
					repeat[i] = repJson.getBoolean(i);
				}
				itemData.repeatDay = repeat;
				
				String typestart = schedule.getString(3);
				itemData.typestart = typestart;
				String typestop = schedule.getString(4);
				itemData.typestop = typestop;
				itemData.key = key;
				itemDatas.add(itemData);
			}			
		}
		for(ScheduleItemData itemData:itemDatas){
			final ModeItem item = initMode(itemData.timeToString(), itemData.repeatToString(mResources));
			hashtable.put(itemData.key, item);
			item.setTypeText(itemData.typestart);
			linearLayout.addView(item);
			final ScheduleItemData data = itemData;
			item.setArrowVisible(View.VISIBLE);
			item.setArrowOnClickListen(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),NewScheduleActivity.class);
					intent.putExtra(NewScheduleActivity.EXTRA_KEY_SCHEDULE, data.key);
					startActivityForResult(intent, NewScheduleActivity.REQUEST_ADD_NEW);
				}
			});
			item.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					item.setSelectedIconChange(!item.isSelected());
					if(item.isSelected()){
						AlarmSchedule.addSchedule(mActivity, data);
					}else{
						AlarmSchedule.cancelSchedule(mActivity, data);
					}
				}
			});
			item.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					DialogDelete dialogDelete = new DialogDelete(mActivity);
					dialogDelete
							.setDialogCloseListen(new DialogCustomer.IDialogCloseListen() {

								@Override
								public void OnRightButtonDialogClick() {
									// TODO Auto-generated method stub

								}

								@Override
								public void OnLeftButtonDialogClick() {
									// TODO Auto-generated method stub
									ModeItem modeItem = hashtable.remove(data.key);
									if (modeItem != null) {
											linearLayout.removeView(modeItem);
											SharedPreferences sharedPreferences = mActivity.getSharedPreferences(NewScheduleActivity.SCHEDULE_PREF, Context.MODE_PRIVATE);
											sharedPreferences.edit().remove(data.key).commit();
									}
								}
							});
					dialogDelete.show();
					return false;
				}
			});
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == NewScheduleActivity.REQUEST_ADD_NEW){
			if(resultCode == NewScheduleActivity.RESULT_OK){
				init();
			}
		}
	}
}
