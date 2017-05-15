package com.usstudio.easytouch.assistivetouch.dialog;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.adapter.IconSettingGVAdapter;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class IconSettingDialog extends Dialog{
	
	private GridView gridView;
	private Context context;
	
	public IconSettingDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.icon_setting_layout);
		
		final String[] icons = context.getResources().getStringArray(R.array.icon_name);
		gridView = (GridView) findViewById(R.id.gridview);
	    IconSettingGVAdapter adapter = new IconSettingGVAdapter(context, icons);
	    gridView.setAdapter(adapter);
	    gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SharedPreference.saveIconName(context, icons[position]);
				dismiss();
			}
		});
	}
}