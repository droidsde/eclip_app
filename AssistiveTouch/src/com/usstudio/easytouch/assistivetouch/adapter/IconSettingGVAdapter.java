package com.usstudio.easytouch.assistivetouch.adapter;

import com.usstudio.easytouch.assistivetouch.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class IconSettingGVAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private String[] data;
	private Context context;
	
	public IconSettingGVAdapter(Context context, String[] data) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            convertView = inflater.inflate(R.layout.icon_setting_item, null);
        }
		ImageView icon = (ImageView) convertView.findViewById(R.id.icon_setting);   
		int resId = context.getResources().getIdentifier(data[position], "drawable", context.getPackageName());
		icon.setImageResource(resId);

        return convertView;
	}
}