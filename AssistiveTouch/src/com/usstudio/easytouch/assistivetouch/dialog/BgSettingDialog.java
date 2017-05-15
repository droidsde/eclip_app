package com.usstudio.easytouch.assistivetouch.dialog;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.adapter.BgSettingGVAdapter;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class BgSettingDialog extends Dialog{
	
	private GridView gridView;
	private Context context;

	public BgSettingDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.color_bg_setting_layout);
	    
	    final String[] colors = context.getResources().getStringArray(R.array.color_bg_code);
	    gridView = (GridView) findViewById(R.id.gridview);
	    BgSettingGVAdapter adapter = new BgSettingGVAdapter(context, colors);
	    gridView.setAdapter(adapter);
	    gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SharedPreference.saveBgColor(context, colors[position]);
				dismiss();
			}
		});
	}
}