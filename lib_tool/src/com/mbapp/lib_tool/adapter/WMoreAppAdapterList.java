package com.mbapp.lib_tool.adapter;

import java.util.ArrayList;

import com.mbapp.lib_tool.R;
import com.mbapp.lib_tool.model.WolfModel;
import com.mbapp.lib_tool.view.WProgressWheel;
import com.mbapp.lib_tool.view.WTypeView;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;





public class WMoreAppAdapterList extends BaseAdapter {
	
	ArrayList<WolfModel> ArrApp;
	Context context;
	//Typeface face;
	private LayoutInflater inflater;
	
	public WMoreAppAdapterList(ArrayList<WolfModel> ArrApp, Context mContext){
		this.ArrApp = ArrApp;
		context = mContext;
		
		inflater = (LayoutInflater) context
			    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ArrApp.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	private static class ViewHolder {
		TextView text,detail;
		ImageView image;
		WProgressWheel spinner;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.witem_more_app_list, parent, false);	
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.wtv_title);
			holder.detail = (TextView) view.findViewById(R.id.wtv_detail);
			holder.image = (ImageView) view.findViewById(R.id.wimage_app);
			holder.spinner = (WProgressWheel) view.findViewById(R.id.wprogress_image);
			holder.text.setTypeface(WTypeView.WgetTypeface(context, "wroboto/Roboto-Medium.ttf"));
			holder.detail.setTypeface(WTypeView.WgetTypeface(context, "wroboto/Roboto-Regular.ttf"));
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.text.setText(ArrApp.get(position).getName());
		holder.detail.setText(Html.fromHtml(ArrApp.get(position).getDescription()));
		Picasso
		.with(context)
		.load(ArrApp.get(position).getIcon())
		.skipMemoryCache()
		.into(holder.image, new Callback() {
			@Override
			public void onSuccess() {
				holder.spinner.setVisibility(View.GONE);
			}

			@Override
			public void onError() {
				holder.spinner.setVisibility(View.GONE);
				
			}
		});
		
		return view;
	
	}
	
}
