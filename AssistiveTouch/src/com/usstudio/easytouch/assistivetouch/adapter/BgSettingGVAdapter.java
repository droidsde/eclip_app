package com.usstudio.easytouch.assistivetouch.adapter;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

public class BgSettingGVAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private String[] data;
	private Context context;
	
	public BgSettingGVAdapter(Context context, String[] data) {
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
            convertView = inflater.inflate(R.layout.color_bg_setting_item, null);
        }
		FrameLayout frameLayout = (FrameLayout) convertView.findViewById(R.id.item_bg_setting);        
        GradientDrawable bgShape = (GradientDrawable) frameLayout.getBackground();
		bgShape.setColor(Color.parseColor(data[position]));		
		
		FrameLayout checkLayout = (FrameLayout) convertView.findViewById(R.id.check_layout);
		if(SharedPreference.readBgColor(context).equals(data[position])){
			checkLayout.setVisibility(View.VISIBLE);
		}

        return convertView;
	}

}
