package com.batterydoctor.superfastcharger.fastcharger.ui;

import com.batterydoctor.superfastcharger.fastcharger.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RorateImageView extends ImageView{
	int resIdImg = R.drawable.setttings_app_screen_light_10;
	public RorateImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setDuplicateParentStateEnabled(false);
	}
	public void startRorate(int resId){
		if(resIdImg == resId){
			return;
		}
		this.clearAnimation();
		this.resIdImg = resId;
		this.setImageResource(resIdImg);
	}
	
}
