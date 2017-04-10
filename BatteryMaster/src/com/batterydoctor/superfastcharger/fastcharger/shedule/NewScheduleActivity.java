package com.batterydoctor.superfastcharger.fastcharger.shedule;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nvn.data.pref.ModePref;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogCustomer;
import com.batterydoctor.superfastcharger.fastcharger.mode.ModeFragment;
import com.batterydoctor.superfastcharger.fastcharger.ui.MainTitle;
import com.batterydoctor.superfastcharger.fastcharger.ui.data.ScheduleItemData;

public class NewScheduleActivity extends Activity{
	public static final String SCHEDULE_PREF = "schedule_pref";
	public static final String INDEX = "index";
	public static final String EXTRA_KEY_SCHEDULE = "key_schedule";
	public static final int REQUEST_ADD_NEW = 0;
	public static final int RESULT_OK = 0;
	public static final int RESULT_CANLE = 1;
	TimePicker pickerStart;
	TimePicker pickerStop;
	String key_extra = null;
	TextView lableStart;
	TextView lableStop;
	Button save;
	Button cancel;
	ArrayList<ScheduleItemData> itemDatas = new ArrayList<ScheduleItemData>();
	ArrayList<String> types = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_shedule_activity);
		key_extra = getIntent().getStringExtra(EXTRA_KEY_SCHEDULE);
		pickerStart = (TimePicker) findViewById(R.id.timerPickerStart);
		pickerStop = (TimePicker) findViewById(R.id.timerPickerStop);
		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);
		lableStart = (TextView) findViewById(R.id.labelstart);
		lableStop = (TextView) findViewById(R.id.labelstop);
		pickerStart.setIs24HourView(true);
		pickerStop.setIs24HourView(true);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				doSave();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANLE);
				finish();
			}
		});
		try {
			doInit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			final DialogCustomer dialogDefault = new DialogCustomer(this);
			dialogDefault.setButtonRightVisible(View.GONE);
			dialogDefault.setTitle(getResources().getString(R.string.error_title));
			dialogDefault.setMessageContent(getResources().getString(R.string.error_json_excepton));
			dialogDefault.setDialogCloseListen(new DialogCustomer.IDialogCloseListen() {
				
				@Override
				public void OnRightButtonDialogClick() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void OnLeftButtonDialogClick() {
					// TODO Auto-generated method stub
					dialogDefault.dismiss();
					finish();
				}
			});
			dialogDefault.show();
		}

		((RelativeLayout)findViewById(R.id.typestart)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				doCreateDialogSelectType(true);
			}
		});
		((RelativeLayout)findViewById(R.id.typestop)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				doCreateDialogSelectType(false);
			}
		});
	}
	void doInit() throws JSONException{
		itemDatas.clear();
		types.clear();
		SharedPreferences sharedPreferences = getSharedPreferences(SCHEDULE_PREF, Context.MODE_PRIVATE);
		Map<String, ?> map = sharedPreferences.getAll();
		for(Map.Entry<String, ?> entry: map.entrySet()){
			if(!entry.getKey().equals(INDEX)){
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
				
				String typeStart = schedule.getString(3);
				String typeStop = schedule.getString(4);
				itemData.typestart = typeStart;
				itemData.typestop = typeStop;
				itemData.key = entry.getKey();
				itemDatas.add(itemData);
			}
		}
		Map<String, ?> map2 = ModePref.getAllString();
		for(Map.Entry<String, ?> entry : map2.entrySet()){
			types.add(entry.getKey());
		}
		types.add(ModeFragment.MODE_DEFAULT_GENERAL);
		types.add(ModeFragment.MODE_DEFAULT_LONG);
		types.add(ModeFragment.MODE_DEFAULT_SLEEP);
		if(key_extra == null){
			((MainTitle)findViewById(R.id.shedule_title)).setTitleTextMid(R.string.schedule_newshedule_add_new);
			lableStart.setText(types.get(0));
			lableStop.setText(types.get(1));
		}else{
			ScheduleItemData itemData = getItemdate(key_extra);
			((MainTitle)findViewById(R.id.shedule_title)).setTitleTextMid(R.string.schedule_newshedule_edit_new);
			if(itemData!=null){
				lableStart.setText(itemData.typestart);
				lableStop.setText(itemData.typestop);
				pickerStart.setCurrentHour(itemData.startHour);
				pickerStart.setCurrentMinute(itemData.startMinute);
				pickerStop.setCurrentHour(itemData.stopHour);
				pickerStop.setCurrentMinute(itemData.stopMinute);
				for(int i = 0 ; i < dayResId.length ; i++){
					((CheckBox)findViewById(dayResId[i])).setChecked(itemData.repeatDay[i]);
				}
			}
		}
	}
	void doCreateDialogSelectType(final boolean istypeStart){
		final LinearLayout linearLayout = initListSettingView(types, istypeStart?lableStart.getText().toString(): lableStop.getText().toString());
		DialogCustomer dialogDefault = new DialogCustomer(this);
		dialogDefault.setTitle(getResources().getString(R.string.dialog_title_slect_mode));
		dialogDefault.setViewContent(linearLayout);
		dialogDefault.setDialogCloseListen(new DialogCustomer.IDialogCloseListen() {
			
			@Override
			public void OnRightButtonDialogClick() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OnLeftButtonDialogClick() {
				// TODO Auto-generated method stub
				int size = linearLayout.getChildCount();
				for (int i = 0; i < size; i++) {
					View view = linearLayout.getChildAt(i);
					if (view.isSelected()) {
						if(istypeStart){
							lableStart.setText(types.get(i));
						}else{
							lableStop.setText(types.get(i));
						}
						break;
					}
				}
			}
		});
		dialogDefault.show();
	}
	ScheduleItemData getItemdate(String key){
		for(ScheduleItemData data : itemDatas){
			if(data.key.equals(key)){
				return data;
			}
		}
		return null;
	}
	/**
	 *  lưu dưới dạng Json,
	 *  [[start_hour,start_minute],[stop_hour,stop_minute],[repeat day],typestart,typestop]
	 *  [repeat day] chứa 7 phần tử
	 *  type: kiểu mode
	 */
	private void doSave(){
		if(pickerStart.getCurrentHour()== pickerStop.getCurrentHour()&&
				pickerStart.getCurrentMinute() == pickerStop.getCurrentMinute()){
			DialogCustomer dialogDefault = new DialogCustomer(this);
			dialogDefault.setTitle(getResources().getString(R.string.error_title));
			dialogDefault.setMessageContent(getResources().getString(R.string.error_the_time_match));
			dialogDefault.setButtonRightVisible(View.GONE);
			dialogDefault.show();
			return;
		}
		String typestart = lableStart.getText().toString();
		String typestop = lableStop.getText().toString();
		JSONArray mySchedule = new JSONArray();
		JSONArray timeStart = new JSONArray();
		timeStart.put(pickerStart.getCurrentHour());
		timeStart.put(pickerStart.getCurrentMinute());
		mySchedule.put(timeStart);
		//
		JSONArray timeStop = new JSONArray();
		timeStop.put(pickerStop.getCurrentHour());
		timeStop.put(pickerStop.getCurrentMinute());
		mySchedule.put(timeStop);
		// repeat
		JSONArray repeat = new JSONArray();
		for(int i = 0 ; i < dayResId.length; i++){
			boolean check = ((CheckBox)findViewById(dayResId[i])).isChecked();
			repeat.put(check);
		}
		mySchedule.put(repeat);
		// type
		mySchedule.put(typestart);
		mySchedule.put(typestop);
// save
		SharedPreferences sharedPreferences = getSharedPreferences(SCHEDULE_PREF, Context.MODE_PRIVATE);
		if(!sharedPreferences.contains(INDEX)){
			sharedPreferences.edit().putInt(INDEX, 0).commit();
		}
		if(key_extra==null){
			int index = sharedPreferences.getInt(INDEX, 0);
			sharedPreferences.edit().putString(String.valueOf(index), mySchedule.toString()).commit();
			sharedPreferences.edit().putInt(INDEX, index+1).commit();
		}else{
			sharedPreferences.edit().putString(key_extra, mySchedule.toString()).commit();
		}
		//
		setResult(RESULT_OK);
		finish();
	}
	LinearLayout initListSettingView(ArrayList<String> lable, String lableSelect) {
		final LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < lable.size(); i++) {
			View view = getLayoutInflater().inflate(
					R.layout.mode_setting_list_item, null);
			((TextView) view.findViewById(R.id.summary)).setText(lable.get(i));
			ImageView imageView = (ImageView) view.findViewById(R.id.check);
			if(lableSelect.equals(lable.get(i))){
				imageView.setImageResource(R.drawable.mode_on);
			} else {
				imageView.setImageResource(R.drawable.mode_off);
			}
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int size = linearLayout.getChildCount();
					for (int j = 0; j < size; j++) {
						View vv = linearLayout.getChildAt(j);
						((ImageView) vv.findViewById(R.id.check))
								.setImageResource(R.drawable.mode_off);
						vv.setSelected(false);
					}
					((ImageView) v.findViewById(R.id.check))
							.setImageResource(R.drawable.mode_on);
					v.setSelected(true);
				}
			});
			linearLayout.addView(view);
		}
		return linearLayout;
	}
	private int dayResId[] = {
		R.id.ck_monday,R.id.ck_tuesday,R.id.ck_wednesday,R.id.ck_thursday,R.id.ck_friday
		,R.id.ck_saturday,R.id.ck_sunday
	};
}
