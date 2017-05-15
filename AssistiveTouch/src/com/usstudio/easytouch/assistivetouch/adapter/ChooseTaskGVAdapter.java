package com.usstudio.easytouch.assistivetouch.adapter;

import com.usstudio.easytouch.assistivetouch.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseTaskGVAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] data;
	private Context context;

	public ChooseTaskGVAdapter(Context context, String[] data) {
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

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.choose_task_item, null);
		}
		ImageView taskImage = (ImageView) convertView.findViewById(R.id.task_image);
		TextView taskTitle = (TextView) convertView.findViewById(R.id.task_title);
		String[] mData = data[position].split("[.]");
		int currentVersion = Build.VERSION.SDK_INT;
		if ("ic_action_new".equals(mData[0])) {
			if (currentVersion >= Build.VERSION_CODES.JELLY_BEAN) {
				taskImage.setBackground(ContextCompat.getDrawable(context, R.drawable.dash_border_bg));
			} else {
				taskImage.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dash_border_bg));
			}
		}else{
			taskImage.setBackgroundColor(Color.TRANSPARENT);
		}
		int resId = context.getResources().getIdentifier(mData[0], "drawable", context.getPackageName());
		taskImage.setImageResource(resId);
		taskTitle.setText(mData[1]);

		return convertView;
	}
}