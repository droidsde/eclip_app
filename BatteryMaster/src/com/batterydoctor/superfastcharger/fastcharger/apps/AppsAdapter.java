package com.batterydoctor.superfastcharger.fastcharger.apps;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.batterydoctor.superfastcharger.fastcharger.R;

public class AppsAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	Resources resources;
	IButtonClick buttonClick;
	ArrayList<ParserPackage> parserPackages;
	private long maxUsageMemory = -1;
	public AppsAdapter(Context context, ArrayList<ParserPackage> list) {
		// TODO Auto-generated constructor stub
		mContext = context.getApplicationContext();
		mInflater = LayoutInflater.from(mContext);
		resources = mContext.getResources();
		this.parserPackages = list;
	}


	public void setButtonStopClickListen(IButtonClick arg1) {
		this.buttonClick = arg1;
	}
	public void setMaxUsageMemory(long l){
		this.maxUsageMemory  = l;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return parserPackages.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder mHolder;
		final ParserPackage parserPackage = parserPackages.get(arg0);
		if (arg1 == null) {
			LayoutInflater layoutInflater = mInflater;
			arg1 = layoutInflater.inflate(R.layout.power_app_usage_list_item,
					arg2, false);
			mHolder = new Holder();
			mHolder.icon = (ImageView) arg1.findViewById(R.id.icon);
			mHolder.lable = (TextView) arg1.findViewById(R.id.label);
			mHolder.des = (TextView) arg1.findViewById(R.id.desc);
			mHolder.progress = (ImageView) arg1
					.findViewById(R.id.progress_image);
			mHolder.progess_text = (TextView) arg1.findViewById(R.id.progress);
			mHolder.action = (TextView) arg1.findViewById(R.id.action);
			arg1.setTag(mHolder);
		} else {
			mHolder = (Holder) arg1.getTag();
		}
		// icon
		mHolder.icon.setImageDrawable(parserPackage.getIcon());
		mHolder.lable.setText(parserPackage.getName());
		mHolder.progress.setImageDrawable(parserPackage.getProgess());
		if (parserPackage.isAppRunning()) {
			mHolder.action.setTextColor(resources
					.getColor(R.color.monitor_button_text_color));
			if(maxUsageMemory!=-1){
				mHolder.des.setText(String.valueOf((int)(maxUsageMemory*parserPackage.getUsage()))+"MB");
			}
			mHolder.action.setEnabled(true);
			mHolder.action.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (buttonClick != null) {
						buttonClick.OnButtonCLickListien(parserPackage);
					}
				}
			});
		} else {
			mHolder.action.setTextColor(resources
					.getColor(R.color.usage_color_grey));
			mHolder.action.setEnabled(false);
		}
		return arg1;
	}

	class Holder {
		ImageView icon;
		TextView lable;
		TextView des;
		ImageView progress;
		TextView progess_text;
		TextView action;
		int g;
		public double h;

	}

	public interface IButtonClick {
		void OnButtonCLickListien(ParserPackage parserPackage);
	}

}
