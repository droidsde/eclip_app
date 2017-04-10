package com.batterydoctor.superfastcharger.fastcharger.ui;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nvn.log.BroadcastListener;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.ui.data.BatteryInfo;

public class MyBatteryInfoDetails extends LinearLayout {
	public BroadcastListener listener;
	private static final String TAG = "powermanager.ui.MyBatteryInfoDetail";
	private LayoutInflater layoutInflater;
	SparseArray<Pair<String, String>> mArray = new SparseArray<Pair<String, String>>();
	TextView one_sumary;
	TextView one_value;
	TextView two_sumary;
	TextView two_value;
	TextView three_sumary;
	TextView three_value;
	// TextView four_sumary;
	// TextView four_value;

	Context mContext;

	public MyBatteryInfoDetails(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.battery_info_view, this, true);
		mContext = context.getApplicationContext();
		init();
	}

	private void init() {
		one_sumary = (TextView) findViewById(R.id.battery_info_one_summary);
		one_value = (TextView) findViewById(R.id.battery_info_one_value);
		two_sumary = (TextView) findViewById(R.id.battery_info_two_summary);
		two_value = (TextView) findViewById(R.id.battery_info_two_value);
		three_sumary = (TextView) findViewById(R.id.battery_info_three_summary);
		three_value = (TextView) findViewById(R.id.battery_info_three_value);
	}

	public void initView(BatteryInfo batteryInfo) {
		int temperature = batteryInfo.temperature;
		int voltage = batteryInfo.voltage;
		String technology = batteryInfo.technology;
		mArray.clear();
		//temperature
		String string[] = intToArray(getContext(), temperature);
		Object object0[] = new Object[2];
		if(string[0].length()>4) 
			object0[0] = string[0].substring(0, 4);
		else 
			object0[0] = string[0];
		object0[1] = string[1];
		mArray.append(
				0,
				new Pair<String, String>(mContext.getString(R.string.battery_info_temperature), 
						mContext.getString(R.string.battery_info_value_2, object0)));
		one_sumary.setText(mArray.get(0).first);
		one_value.setText(Html.fromHtml(mArray.get(0).second));
		//voltage
		Object object1[] = new Object[2];
		String as = Double.toString(voltage / 1000f);
		if(as.length()>4){
			object1[0] = as.substring(0, 4);
		}else{
			object1[0] = as;
		}
		object1[1] = mContext.getString(R.string.voltage_unit);
		mArray.append(
				1,
				new Pair<String, String>(mContext
						.getString(R.string.battery_info_voltage), mContext
						.getString(R.string.battery_info_value_2, object1)));
		two_sumary.setText(mArray.get(1).first);
		two_value.setText(Html.fromHtml(mArray.get(1).second));
		//techlonology
		mArray.append(
				2,
				new Pair<String, String>(mContext
						.getString(R.string.battery_info_technology), mContext
						.getString(R.string.battery_info_value_1, technology)));
		three_sumary.setText(mArray.get(2).first);
		three_value.setText(Html.fromHtml(mArray.get(2).second));
	}

	private String[] intToArray(Context context, int i) {
		String str = Double.toString(i / 10f);
		if (true) {
			// do C
			String string = context.getString(R.string.celsius);
			return new String[] { str, string };
		} else {
			// do F
			String string = context.getString(R.string.fahrenheit);
			return new String[] { str, string };
		}
	}
}
