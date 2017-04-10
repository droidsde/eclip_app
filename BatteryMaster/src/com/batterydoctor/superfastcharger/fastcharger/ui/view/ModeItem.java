package com.batterydoctor.superfastcharger.fastcharger.ui.view;


import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.batterydoctor.superfastcharger.fastcharger.R;

public class ModeItem extends LinearLayout{
	ImageView icon;
	ImageView arrow;
	TextView lable;
	TextView detail;
	TextView type ;
	public ModeItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		inflate(context, R.layout.mode_item, this);
		icon = (ImageView) findViewById(R.id.icon);
		arrow = (ImageView) findViewById(R.id.arrow);
		lable = (TextView) findViewById(R.id.label);
		detail = (TextView) findViewById(R.id.detail);
		type = (TextView) findViewById(R.id.type);
	}
	public void setTypeText(String string){
		type.setText(string);
	}
	public void setIconClickListen(OnClickListener listener){
		icon.setOnClickListener(listener);
	}
	public void setArrowOnClickListen(OnClickListener listener){
		arrow.setOnClickListener(listener);
	}
	public void setArrowVisible(int i){
		this.arrow.setVisibility(i);
	}
	public void setIcon(int i){
		this.icon.setImageResource(i);
	}
	public void setSelectedIconChange(boolean b){
		this.setSelected(b);
		if(b){
			this.icon.setImageResource(R.drawable.mode_on);
		}else{
			this.icon.setImageResource(R.drawable.mode_off);
		}
	}
	public void setLable(String string){
		this.lable.setText(string);
	}
	public String getLable(){
		return this.lable.getText().toString();
	}
	public void setDetail(String string){
		this.detail.setText(string);
	}
	public void setDetailVisible(int i){
		this.detail.setVisibility(i);
	}

}
