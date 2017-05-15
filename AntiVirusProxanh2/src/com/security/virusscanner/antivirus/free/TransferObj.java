package com.security.virusscanner.antivirus.free;

import com.security.virusscanner.antivirus.progress.CircleProgress;

import android.widget.ImageView;
import android.widget.TextView;

public class TransferObj {
	
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tvTime;
	private ImageView iconView;
	private CircleProgress pb;
	
	public TransferObj(TextView t1, TextView t2, TextView t3, CircleProgress p, ImageView iconView, TextView tvTime)
	{
		this.tv1 = t1;
		this.tv2 = t2;
		this.tv3 = t3;
		this.pb = p;
		this.iconView = iconView;
		this.setTvTime(tvTime);
	}
	
	public TextView getTV1()
	{
		return this.tv1;
	}
	
	public TextView getTV2()
	{
		return this.tv2;
	}
	
	public TextView getTV3()
	{
		return this.tv3;
	}
	
	public CircleProgress getPB()
	{
		return this.pb;
	}

	public ImageView getIconView() {
		return iconView;
	}

	public void setIconView(ImageView iconView) {
		this.iconView = iconView;
	}

	public TextView gettvTime() {
		return tvTime;
	}

	public void setTvTime(TextView tvTime) {
		this.tvTime = tvTime;
	}
	

}
