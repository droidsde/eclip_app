package com.batterydoctor.superfastcharger.fastcharger.mode;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nvn.data.pref.ModePref;
import com.nvn.lib.Units;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogCustomer;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogNotify;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogCustomer.IDialogCloseListen;
import com.batterydoctor.superfastcharger.fastcharger.manager.MscreenManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MscreenOffManager;
import com.batterydoctor.superfastcharger.fastcharger.ui.MainTitle;


public class NewModeActivity extends Activity {
	public static final int REQUEST_ADD_NEW = 0;
	public static final int RESULT_OK = 0;
	public static final int RESULT_CANLE = 1;
	public static final String IS_ADD = "is_add";
	public static final String KEY = "key";
	private static final int DIALOG_BRIGHTNESS = 0;
	private static final int DIALOG_BRIGHTNESS_TIMEOUT = 1;
	boolean is_add = false;
	String key = null;
	ArrayList<Pair<Integer, Integer>> newMode = new ArrayList<Pair<Integer,Integer>>();
	EditText modeName;
	Button save;
	Button canel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_mode_activity);
		Intent intent = getIntent();

		is_add = intent.getBooleanExtra(IS_ADD, true);
		if(!is_add)
			key = intent.getStringExtra(KEY);
		initControls();
		initView();
	}
	
	void initControls(){

		modeName = (EditText) findViewById(R.id.nameedit);
		save = (Button) findViewById(R.id.save);
		canel = (Button) findViewById(R.id.cancel);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					doSave();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		canel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	private void doSave() throws JSONException {
		if(is_add){
			Map<String, ?> map = ModePref.getAllString();
			String name = modeName.getText().toString();
			for (Map.Entry<String, ?> entry : map.entrySet()) {
				if (entry.getKey().equals(name)) {
					DialogCustomer dialogDefault = new DialogCustomer(this);
					dialogDefault.setMessageContent(getResources().getString(
							R.string.dialog_message_mode_exits));
					dialogDefault.show();
					return;
				}
				if(name.equals(ModeFragment.MODE_DEFAULT_ADDNEW)||
						name.equals(ModeFragment.MODE_DEFAULT_GENERAL)||
						name.equals(ModeFragment.MODE_DEFAULT_LONG)||
						name.equals(ModeFragment.MODE_DEFAULT_SLEEP)||
						name.equals(ModeFragment.MODE_DEFAULT_MYCUSTOM)){
					DialogCustomer dialogDefault = new DialogCustomer(this);
					dialogDefault.setMessageContent(getResources().getString(
							R.string.dialog_message_mode_Invalid));
					dialogDefault.show();
					return;
					
				}
			}
		}else{
			if(!key.equals(modeName.getText().toString())){
				ModePref.remove(key);
			}
		}
		JSONArray jsonArray = new JSONArray();
		
		for (int i = 0; i < newMode.size(); i++) {
			JSONArray json = new JSONArray();
			json.put(newMode.get(i).first);
			json.put(newMode.get(i).second);
			jsonArray.put(json);
		}
		ModePref.putString(modeName.getText().toString(), jsonArray.toString());
		Intent intent = new Intent(NewModeActivity.this, ModeFragment.class);
		setResult(RESULT_OK, intent);
		finish();
	}
	private void initView() {
		if (key == null) {
			((MainTitle)findViewById(R.id.mode_title)).setTitleTextMid(R.string.mode_newmode_add_new);
			modeName.setText(getResources().getString(R.string.mode_newmode_name));
			newMode.clear();
			newMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_screen_setting, MscreenManager.SCREEN_BRIGHTNESS_0));
			newMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_screen_timeout_setting, MscreenOffManager.SCREEN_OFF_TIMEOUT_15s));
			newMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_volume_switch, Units.OFF));
			newMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_wifinet_switch, Units.OFF));
			newMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_bluetooth_switch, Units.OFF));
			newMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_mobiledata_switch, Units.OFF));
			newMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_ariplane_switch, Units.OFF));
			newMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_gps_switch, Units.OFF));
		}else{
			((MainTitle)findViewById(R.id.mode_title)).setTitleTextMid(R.string.mode_newmode_edit_new);
			modeName.setText(key);
			
			try {
				newMode.clear();
				String data = ModePref.getString(key);
				JSONArray jsonArray = new JSONArray(data);
				for(int i = 0 ; i < jsonArray.length() ; i++){
					JSONArray array = jsonArray.getJSONArray(i);
					newMode.add(new Pair<Integer, Integer>((Integer)array.get(0), (Integer)array.get(1)));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				DialogNotify dialogNotify = new DialogNotify(getApplication());
				dialogNotify.setTitle(R.string.error_title);
				dialogNotify.setMessageContent(R.string.error_json_excepton);
				dialogNotify.show();
			}
		}
		if(newMode.size() == 0){
			return;
		}
		for (int i = 0; i < newMode.size(); i++) {
			View view = findViewById(idsubLayout[i]);;
			if(i == 0){
				((TextView) view.findViewById(R.id.label)).setText(newMode.get(i).first);
				((TextView) view.findViewById(R.id.settingvalue)).setText(Units.parserIntMode(newMode.get(i).second));
				view.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						OnCreateDialog(DIALOG_BRIGHTNESS, null);
					}
				});
			}else if(i == 1){
				((TextView) view.findViewById(R.id.label)).setText(newMode.get(i).first);
				((TextView) view.findViewById(R.id.settingvalue)).setText(Units.parserIntMode(newMode.get(i).second));
				view.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						OnCreateDialog(DIALOG_BRIGHTNESS_TIMEOUT, null);
					}
				});
			}else{
				((TextView) view.findViewById(R.id.label)).setText(newMode.get(i).first);
				if(newMode.get(i).second == R.string.mode_on){
					((CheckBox) view.findViewById(R.id.switchicon)).setChecked(true);
				}else if(newMode.get(i).second == R.string.mode_off){
					((CheckBox) view.findViewById(R.id.switchicon)).setChecked(false);
				}
				final int ii = i;
				view.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CheckBox checkBox = ((CheckBox) v.findViewById(R.id.switchicon));
						checkBox.setChecked(!checkBox.isChecked());
						Pair<Integer, Integer> pair = newMode.get(ii);
						if(checkBox.isChecked()){
							newMode.set(ii, new Pair<Integer, Integer>(pair.first, Units.ON));
						}else{
							newMode.set(ii, new Pair<Integer, Integer>(pair.first, Units.OFF));
						}
					}
				});
			}
		}
	}
	private int idsubLayout[] = {
			R.id.brightness_layout,R.id.screentimeout_layout,R.id.vibrate_layout,
			R.id.wlan_layout,R.id.bluetooth_layout,R.id.mobile_layout,R.id.airplane_layout,R.id.gps_layout
	};


	protected void OnCreateDialog(int id, Object object) {
		// TODO Auto-generated method stub
		DialogCustomer dialogDefault = new DialogCustomer(this);
		switch (id) {
		case DIALOG_BRIGHTNESS:
			final ArrayList<Integer> arrayList = new ArrayList<Integer>();
			arrayList.add(MscreenManager.SCREEN_BRIGHTNESS_0);
			arrayList.add(MscreenManager.SCREEN_BRIGHTNESS_50);
			arrayList.add(MscreenManager.SCREEN_BRIGHTNESS_100);
			final LinearLayout linearLayout = initListSettingView(arrayList,newMode.get(0).second);
			dialogDefault.setTitle(getResources()
					.getString(R.string.mode_newmode_screen_setting));
			dialogDefault.setDialogCloseListen(new IDialogCloseListen() {

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
							View v = findViewById(idsubLayout[0]);
							((TextView) v.findViewById(R.id.settingvalue)).setText(Units.parserIntMode(arrayList.get(i)));
							newMode.set(DIALOG_BRIGHTNESS, new Pair<Integer, Integer>(R.string.mode_newmode_screen_setting, arrayList.get(i)));
							break;
						}
					}
				}
			});
			dialogDefault.setViewContent(linearLayout);
			break;
		case DIALOG_BRIGHTNESS_TIMEOUT:
			final ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
			arrayList2.add(MscreenOffManager.SCREEN_OFF_TIMEOUT_15s);
			arrayList2.add(MscreenOffManager.SCREEN_OFF_TIMEOUT_30s);
			arrayList2.add(MscreenOffManager.SCREEN_OFF_TIMEOUT_1m);
			arrayList2.add(MscreenOffManager.SCREEN_OFF_TIMEOUT_2m);
			arrayList2.add(MscreenOffManager.SCREEN_OFF_TIMEOUT_10m);
			arrayList2.add(MscreenOffManager.SCREEN_OFF_TIMEOUT_30m);
			final LinearLayout linearLayout3 = initListSettingView(arrayList2,newMode.get(1).second);
			dialogDefault.setTitle(getResources().getString(R.string.mode_newmode_screen_timeout_setting));
			dialogDefault.setDialogCloseListen(new IDialogCloseListen() {

				@Override
				public void OnRightButtonDialogClick() {
					// TODO Auto-generated method stub

				}

				@Override
				public void OnLeftButtonDialogClick() {
					// TODO Auto-generated method stub
					int size = linearLayout3.getChildCount();
					for (int i = 0; i < size; i++) {
						View view = linearLayout3.getChildAt(i);
						if (view.isSelected()) {
							View v = findViewById(idsubLayout[1]);
							((TextView) v.findViewById(R.id.settingvalue)).setText(Units.parserIntMode(arrayList2.get(i)));
							newMode.set(DIALOG_BRIGHTNESS_TIMEOUT, new Pair<Integer, Integer>(R.string.mode_newmode_screen_timeout_setting, arrayList2.get(i)));
							break;
						}
					}
				}
			});
			dialogDefault.setViewContent(linearLayout3);
			break;
		default:
			break;
		}
		dialogDefault.show();
	}

	/**
	 * 
	 * @param lable
	 *            : resource ID String
	 * @param indexSelect
	 * @param arr
	 *            : chua 1 phan tu?. chỉ vị trí được select
	 * @return
	 */
	LinearLayout initListSettingView(ArrayList<Integer> list, int valueSelected) {
		final LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < list.size(); i++) {
			View view = getLayoutInflater().inflate(
					R.layout.mode_setting_list_item, null);
			((TextView) view.findViewById(R.id.summary)).setText(Units.parserIntMode(list.get(i)));
			ImageView imageView = (ImageView) view.findViewById(R.id.check);
			if (list.get(i) == valueSelected) {
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
						((ImageView) vv.findViewById(R.id.check)).setImageResource(R.drawable.mode_off);
						vv.setSelected(false);
					}
					((ImageView) v.findViewById(R.id.check)).setImageResource(R.drawable.mode_on);
					v.setSelected(true);
				}
			});
			linearLayout.addView(view);
		}
		return linearLayout;
	}
}
