package com.usstudio.easytouch.assistivetouch.dialog;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.adapter.ChooseTaskGVAdapter;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ChooseTaskDialog extends Dialog {

	private GridView gridView;
	private Context context;
	private int page;
	private int fromWhere;

	public ChooseTaskDialog(Context context, int page, int fromWhere) {
		super(context);
		this.context = context;
		this.page = page;
		this.fromWhere = fromWhere;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_task_layout);

		final String[] data = context.getResources().getStringArray(R.array.list_task);
		gridView = (GridView) findViewById(R.id.gridview);
		ChooseTaskGVAdapter adapter = new ChooseTaskGVAdapter(context, data);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (fromWhere == 1) {
					SharedPreference.saveMainPage(context, data[position], page);
				} else if (fromWhere == 2) {
					SharedPreference.saveSettingPage(context, data[position], page);
				}
				dismiss();
			}
		});
	}
}