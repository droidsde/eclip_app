package com.usstudio.easytouch.assistivetouch.adapter;

import java.util.ArrayList;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.util.App;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppSettingGVAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<App> data;
	private Context context;

	public AppSettingGVAdapter(Context context, ArrayList<App> data) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.app_setting_item, null);
		}
		ImageView icon = (ImageView) convertView.findViewById(R.id.app_setting);
		TextView title = (TextView) convertView.findViewById(R.id.app_title);
		icon.setImageDrawable(data.get(position).getIcon());
		title.setText(data.get(position).getTitle());

		return convertView;
	}
}
