package com.usstudio.easytouch.assistivetouch.dialog;

import java.util.ArrayList;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.adapter.AppSettingGVAdapter;
import com.usstudio.easytouch.assistivetouch.util.App;
import com.usstudio.easytouch.assistivetouch.util.Constants;
import com.usstudio.easytouch.assistivetouch.util.MyHelper;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class AppSettingDialog extends Dialog {

	private GridView gridView;
	private Context context;
	private int page;
	private ArrayList<App> apps;

	public AppSettingDialog(Context context, ArrayList<App> apps, int page) {
		super(context);
		this.context = context;
		this.apps = apps;
		this.page = page;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_setting_layout);

		gridView = (GridView) findViewById(R.id.gridview);
		AppSettingGVAdapter adapter = new AppSettingGVAdapter(context, apps);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				App app = apps.get(position);
				SharedPreference.saveAppPage(context, Constants.APP + Constants.SPECIAL_CHAR + app.getPackageName()
						+ Constants.SPECIAL_CHAR + app.getTitle(), page);
				Bitmap bm = MyHelper.drawableToBitmap(app.getIcon());
				MyHelper.saveFile(context, bm, app.getPackageName());
				dismiss();
			}
		});
	}
}