package com.mbapp.lib_tool.adapter;

import java.util.ArrayList;
import java.util.Random;

import com.mbapp.lib_tool.R;
import com.mbapp.lib_tool.model.WolfModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class WImageAdapterGird extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	ArrayList<WolfModel> ArrApp;

	public WImageAdapterGird(Context ctx, ArrayList<WolfModel> ArrApp) {
		this.context = ctx;
		this.ArrApp = ArrApp;
		this.inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.ArrApp.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.ArrApp.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.witem_grid_ads, null, true);
			viewHolder.imgThumbnail = (ImageView) convertView
					.findViewById(R.id.wimgThumbnail);
			viewHolder.tvName = (TextView)convertView.findViewById(R.id.wtxtSource);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Picasso.with(context)
		.load(this.ArrApp.get(position).getIcon())
		.placeholder(android.R.drawable.ic_popup_sync)
		
		.into(viewHolder.imgThumbnail);
		viewHolder.tvName.setText(ArrApp.get(position).getName());
	
		Animation localAnimation = null;
		int i = randomAds(4);
		switch (i) {
		case 0:
			 localAnimation = AnimationUtils.loadAnimation(context, R.anim.transcale_up);
			 convertView.startAnimation(localAnimation);
			break;
		case 1:
			 localAnimation = AnimationUtils.loadAnimation(context, R.anim.transcale_down);
			 convertView.startAnimation(localAnimation);
			break;
		case 2:
			 localAnimation = AnimationUtils.loadAnimation(context, R.anim.transcale_left);
			 convertView.startAnimation(localAnimation);
			break;

		case 3:
			 localAnimation = AnimationUtils.loadAnimation(context, R.anim.transcale_right);
			 convertView.startAnimation(localAnimation);
			break;
		default:
			break;
		}
		return convertView;
		
	}
	public int randomAds(int mRandom){
    	Random rand = new Random();
    	int  n = rand.nextInt(mRandom);
    	return n;
    }
	private static class ViewHolder {
		public ImageView imgThumbnail;
		public TextView tvName;
	}
}
