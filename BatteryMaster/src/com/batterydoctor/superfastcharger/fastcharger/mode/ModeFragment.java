package com.batterydoctor.superfastcharger.fastcharger.mode;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nvn.data.pref.ModePref;
import com.nvn.lib.Units;
import com.nvn.log.LogBuider;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogCustomer;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogDelete;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogNotify;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogCustomer.IDialogCloseListen;
import com.batterydoctor.superfastcharger.fastcharger.manager.AbsManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MairplaneMode;
import com.batterydoctor.superfastcharger.fastcharger.manager.MaudioManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MbluetoothManger;
import com.batterydoctor.superfastcharger.fastcharger.manager.MconnectivityManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MgpsManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MscreenManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MscreenOffManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MwifiManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.AbsManager.IManager;
import com.batterydoctor.superfastcharger.fastcharger.ui.fragment.AbsFragment;
import com.batterydoctor.superfastcharger.fastcharger.ui.view.ModeItem;


public class ModeFragment extends AbsFragment implements OnClickListener {
	private static final String TAG = "ModeFragment";
	public static final String NOTIFY_USER_CHANGE_MODE = "notify_user_change_mode";
	public static final String MODE_DEFAULT_LONG = "Long";
	public static final String MODE_DEFAULT_GENERAL = "General";
	public static final String MODE_DEFAULT_SLEEP = "Sleep";
	public static final String MODE_DEFAULT_MYCUSTOM = "Mycustom";
	public static final String MODE_CHANGE = "modeChange";
	public static final String MODE_DEFAULT_ADDNEW = "Addnew";
	private static final int DIALOG_DELETE = 0;
	private static final int DIALOG_DETAIL_MODE = 3;
	private View view;
	private LinearLayout linearLayout;
	LayoutInflater layoutInflater;
	Hashtable<String, ModeItem> hashtable = new Hashtable<String, ModeItem>();
	ArrayList<Pair<Integer, Integer>> longMode = new ArrayList<Pair<Integer,Integer>>();
	ArrayList<Pair<Integer, Integer>> genaralMode = new ArrayList<Pair<Integer,Integer>>();
	ArrayList<Pair<Integer, Integer>> sleepMode = new ArrayList<Pair<Integer,Integer>>();
	ArrayList<String> listAddName = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layoutInflater = getLayoutInflater(savedInstanceState);
		view = inflater.inflate(R.layout.mode_layout, container, false);
		linearLayout = (LinearLayout) view
				.findViewById(R.id.mode_scrollview_items);
		init();
		initView();
		return view;
	}

	private void init() {
		ModeItem item1 = initMode(
				getResources().getString(R.string.mode_label_longest_standby),
				getResources().getString(R.string.mode_longest_detail));
		ModeItem item2 = initMode(
				getResources().getString(R.string.mode_label_general_standby),
				getResources().getString(R.string.mode_general_detail));
		ModeItem item3 = initMode(
				getResources().getString(R.string.mode_label_sleep_standby),
				getResources().getString(R.string.mode_sleep_detail));
		//
		// add list
		hashtable.put(MODE_DEFAULT_LONG, item1);
		hashtable.put(MODE_DEFAULT_GENERAL, item2);
		hashtable.put(MODE_DEFAULT_SLEEP, item3);
		linearLayout.addView(item1);
		linearLayout.addView(item2);
		linearLayout.addView(item3);
		Map<String, ?> map = ModePref.getAllString();
		for(Map.Entry<String, ?> entry : map.entrySet()){
			final String key = entry.getKey();
			final ModeItem item = initMode(key);
			hashtable.put(key, item);
			listAddName.add(key);
			linearLayout.addView(item);
			item.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					OnCreateDialog(DIALOG_DELETE, key);
					return true;
				}
			});
			item.setArrowOnClickListen(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), NewModeActivity.class);
					intent.putExtra(NewModeActivity.IS_ADD, false);
					intent.putExtra(NewModeActivity.KEY, key);
					startActivityForResult(intent, NewModeActivity.REQUEST_ADD_NEW);
				}
			});
		}
		ModeItem addNew = initModeAddNew();
		hashtable.put(MODE_DEFAULT_ADDNEW, addNew);
		linearLayout.addView(addNew);
	}

	void initView() {
		Enumeration<ModeItem> modeItems = hashtable.elements();
		while (modeItems.hasMoreElements()) {
			ModeItem modeItem = (ModeItem) modeItems.nextElement();
			modeItem.setOnClickListener(this);
		}
	}
	BroadcastReceiver receiverModeChange = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String string = intent.getStringExtra(MODE_CHANGE);
			if(string!=null){
				doChangeMode(string);
			}
		}
	};
	void OnCreateDialog(int type, final String values) {
		final DialogCustomer dialogDefault = new DialogCustomer(mActivity);
		switch (type) {
		case DIALOG_DELETE:
			Resources resources = getResources();
			dialogDefault.setTitle(resources
					.getString(R.string.dialog_delete_item_title));
			dialogDefault.setLeftButtonText(resources
					.getString(R.string.dialog_delete));
			dialogDefault.setMessageContent(resources
					.getString(R.string.dialog_delete_item_message));
			dialogDefault
					.setDialogCloseListen(new DialogCustomer.IDialogCloseListen() {

						@Override
						public void OnRightButtonDialogClick() {
							// TODO Auto-generated method stub

						}

						@Override
						public void OnLeftButtonDialogClick() {
							// TODO Auto-generated method stub
							ModeItem modeItem = hashtable.remove(values);
							if (modeItem != null) {
									linearLayout.removeView(modeItem);
									listAddName.remove(modeItem);
									doRemove(values);
							}
						}
					});
			break;
		case DIALOG_DETAIL_MODE:
			Resources resources4 = getResources();
			try {
				
				final LinearLayout linearLayout4 = initListDetailMode(getValuesForMode(values));
				dialogDefault.setTitle(values);
				dialogDefault.setLeftButtonText(resources4
						.getString(R.string.dialog_change));
				dialogDefault.setDialogCloseListen(new IDialogCloseListen() {

					@Override
					public void OnRightButtonDialogClick() {
						// TODO Auto-generated method stub
					}

					@Override
					public void OnLeftButtonDialogClick() {
						// TODO Auto-generated method stub
						doChangeMode(values);
					}
				});
				dialogDefault.setViewContent(linearLayout4);
				//check if current mode is selected
				if(hashtable.get(values).isSelected()){
					dialogDefault.setVisibleButtonPanel(View.GONE);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				DialogNotify dialogNotify = new DialogNotify(getActivity());
				dialogNotify.setTitle(R.string.error_title);
				dialogNotify.setMessageContent(R.string.error_json_excepton);
				dialogNotify.show();
			}
			break;
		}
		dialogDefault.show();
	}

	void doRemove(String values) {
		ModePref.remove(values);
	}
	void doChangeMode(String key){

		if(key.equals(MODE_DEFAULT_MYCUSTOM)){
			return;
		}
		for(Map.Entry<String, ModeItem> entry: hashtable.entrySet()){
				entry.getValue().setSelectedIconChange(false);
		}
		try{
			ModeItem item = hashtable.get(key);
			item.setSelectedIconChange(true);
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			ArrayList<Pair<Integer, Integer>> pairs = getValuesForMode(key);
			MairplaneMode mairplaneMode = MairplaneMode.getInstance(mActivity);
			MaudioManager maudioManager = MaudioManager.getInstance(mActivity);
			MconnectivityManager mconnectivityManager = MconnectivityManager.getInstance(mActivity);
			MscreenManager mscreenManager = MscreenManager.getInstance(mActivity);
			MscreenOffManager mscreenOffManager = MscreenOffManager.getInstance(mActivity);
			MbluetoothManger mbluetoothManger = MbluetoothManger.getInstance(mActivity);
			MwifiManager mwifiManager = MwifiManager.getInstance(mActivity);
			MgpsManager mgpsManager = MgpsManager.getInstance(mActivity);
			if(pairs.get(0).second != mscreenManager.getIntState()){
				mscreenManager.setIntSate(getActivity().getParent().getWindow(),pairs.get(0).second,true);
				LogBuider.e(TAG, "mgpsManager");
			}
			if(pairs.get(1).second!=mscreenOffManager.getInState()){
				mscreenOffManager.setTimerOut(pairs.get(1).second,true);
				LogBuider.e(TAG, "mscreenOffManager");
			}
			boolean enableAudio = pairs.get(2).second == Units.ON?true:false;
			if(enableAudio!= maudioManager.getState()){
				maudioManager.setState(enableAudio, true);
				LogBuider.e(TAG, "maudioManager");
			}
			boolean enableWifi = pairs.get(3).second == Units.ON?true:false;
			if(enableWifi!= mwifiManager.getState()){
				mwifiManager.setState(enableWifi, true);
				LogBuider.e(TAG, "mwifiManager");
			}
			boolean enableAirplane = pairs.get(6).second == Units.ON?true: false ;
			if(enableAirplane!= mairplaneMode.getState()){
				mairplaneMode.setState(enableAirplane, true);
				LogBuider.e(TAG, "mairplaneMode");
			}
			boolean enableData = pairs.get(5).second == Units.ON?true:false;
			if(enableData!= mconnectivityManager.getState()){
				mconnectivityManager.setState(enableData, true);
				LogBuider.e(TAG, "mconnectivityManager");
			}
			boolean enableBluetooth = pairs.get(4).second == Units.ON?true:false;
			if(enableBluetooth!=mbluetoothManger.getState()){
				mbluetoothManger.setState(enableBluetooth, true);
				LogBuider.e(TAG, "mbluetoothManger");
			}
			boolean enableGps = pairs.get(7).second == Units.ON?true:false;
			if(enableGps!=mgpsManager.getState()){
//				mgpsManager.setState(enableGps, true);
//				LogBuider.e(TAG, "mgpsManager");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(mActivity, getResources().getString(R.string.error_json_excepton), Toast.LENGTH_LONG).show();
		}
		
	}
	void doSaveCurrentMode(){
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(NOTIFY_USER_CHANGE_MODE);
		getActivity().registerReceiver(receiverModeChange, intentFilter);
		initDefautMode();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == NewModeActivity.REQUEST_ADD_NEW) {
			if (resultCode == NewModeActivity.RESULT_OK) {
				linearLayout.removeAllViews();
				listAddName.clear();
				hashtable.clear();
				init();
				initView();
			}
			if (requestCode == NewModeActivity.RESULT_CANLE) {

			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(receiverModeChange);
		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 == hashtable.get(MODE_DEFAULT_ADDNEW)) {
			Intent intent = new Intent(getActivity(), NewModeActivity.class);
			intent.putExtra(NewModeActivity.IS_ADD, true);
			startActivityForResult(intent, NewModeActivity.REQUEST_ADD_NEW);
		} else {
			showModeDetail(arg0);
		}
	}

	ArrayList<Pair<Integer, Integer>> getValuesForMode(String modeId) throws JSONException {
		if (modeId.equals(MODE_DEFAULT_LONG)) {
			return longMode;
		} else if (modeId.equals(MODE_DEFAULT_GENERAL)) {
			return genaralMode;
		}  else if (modeId.equals(MODE_DEFAULT_SLEEP)) {
			return sleepMode;
		} else {
			String data = ModePref.getString(modeId);
			JSONArray jsonArray = new JSONArray(data);
			ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer,Integer>>();
			for(int j = 0 ; j < jsonArray.length() ; j++){
				JSONArray array = jsonArray.getJSONArray(j);
				pairs.add(new Pair<Integer, Integer>((Integer)array.get(0), (Integer)array.get(1)));
			}	
			return pairs;
		}
	}

	/**
	 * 
	 * @param lable
	 * @param arrayValues
	 * @return
	 */
	LinearLayout initListDetailMode(ArrayList<Pair<Integer, Integer>> pairs) {
		final LinearLayout linearLayout = new LinearLayout(mActivity);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < pairs.size(); i++) {
			View view = layoutInflater
					.inflate(R.layout.mode_detail_items, null);
			((TextView) view.findViewById(R.id.summary)).setText(pairs.get(i).first);
			((TextView) view.findViewById(R.id.value)).setText(Units.parserIntMode(pairs.get(i).second));
			linearLayout.addView(view);
		}
		return linearLayout;
	}

	private void showModeDetail(View arg0) {
		if (arg0 == hashtable.get(MODE_DEFAULT_LONG)) {
			OnCreateDialog(DIALOG_DETAIL_MODE, MODE_DEFAULT_LONG);
		} else if (arg0 == hashtable.get(MODE_DEFAULT_GENERAL)) {
			OnCreateDialog(DIALOG_DETAIL_MODE, MODE_DEFAULT_GENERAL);
		} else if (arg0 == hashtable.get(MODE_DEFAULT_SLEEP)) {
			OnCreateDialog(DIALOG_DETAIL_MODE, MODE_DEFAULT_SLEEP);
		} else {
			for (int i = 0; i < listAddName.size(); i++) {
				if (arg0 == hashtable.get(listAddName.get(i))) {
					OnCreateDialog(DIALOG_DETAIL_MODE, listAddName.get(i));
					break;
				}
			}
		}
	}
	void initDefautMode(){
		longMode.clear();
		longMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_screen_setting, MscreenManager.SCREEN_BRIGHTNESS_50));
		longMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_screen_timeout_setting, MscreenOffManager.SCREEN_OFF_TIMEOUT_1m));
		longMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_volume_switch, Units.ON));
		longMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_wifinet_switch, Units.ON));
		longMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_bluetooth_switch, Units.OFF));
		longMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_mobiledata_switch, Units.OFF));
		longMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_ariplane_switch, Units.OFF));
		longMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_gps_switch, Units.OFF));
		
		genaralMode.clear();
		genaralMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_screen_setting, MscreenManager.SCREEN_BRIGHTNESS_50));
		genaralMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_screen_timeout_setting, MscreenOffManager.SCREEN_OFF_TIMEOUT_30s));
		genaralMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_volume_switch, Units.ON));
		genaralMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_wifinet_switch, Units.OFF));
		genaralMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_bluetooth_switch, Units.OFF));
		genaralMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_mobiledata_switch, Units.ON));
		genaralMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_ariplane_switch, Units.OFF));
		genaralMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_gps_switch, Units.OFF));
		
		sleepMode.clear();
		sleepMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_screen_setting, MscreenManager.SCREEN_BRIGHTNESS_0));
		sleepMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_screen_timeout_setting, MscreenOffManager.SCREEN_OFF_TIMEOUT_15s));
		sleepMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_volume_switch, Units.OFF));
		sleepMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_wifinet_switch, Units.OFF));
		sleepMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_bluetooth_switch, Units.OFF));
		sleepMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_mobiledata_switch, Units.OFF));
		sleepMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_ariplane_switch, Units.ON));
		sleepMode.add(new Pair<Integer, Integer>(R.string.mode_newmode_gps_switch, Units.OFF));
	}
}
