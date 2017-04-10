package com.batterydoctor.superfastcharger.fastcharger.ui;

import com.nvn.log.LogBuider;
import com.batterydoctor.superfastcharger.fastcharger.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MyBatteryGraph extends FrameLayout {
	private static final String TAG = "MyBatteryGraph";
	/**
	 * 
	 */
	private static final int HIGHT = 10;
	private static final int LOW_LV1 = 7;
	private static final int LOW_LV2 = 2;
	private static final int LOW_LV3 = -1;

	Context mContext;
	ImageView capacity;
	ImageView battery_shape;
	ImageView charge_sign;
	TextView number;
	Animation animation;
	int cap_margin_top;
	int cap_margin_bot;
	int width_shape  = -1;
	int height_shape = -1;
	int temp = -1;
	boolean temp2 = false;

	public MyBatteryGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		inflate(mContext, R.layout.battery_verical_graph_base, this);
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		capacity = (ImageView) findViewById(R.id.capacity_image);
		battery_shape = (ImageView) findViewById(R.id.new_battery_shape);
		charge_sign = (ImageView) findViewById(R.id.charging_sign);
		number = (TextView) findViewById(R.id.number_text);
		this.cap_margin_top = getResources().getDimensionPixelSize(
				R.dimen.dx_batterygraph_normal_top_small);
		this.cap_margin_bot = getResources().getDimensionPixelSize(
				R.dimen.dx_batterygraph_normal_bottom_small);
		this.battery_shape.setBackgroundResource(R.drawable.bat_bg_shell_small);
		this.animation = AnimationUtils.loadAnimation(mContext, R.anim.breathe_anim);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width_shape = getMeasuredWidth();
		height_shape = getMeasuredHeight();
		if(temp!=-1){
			setBatteryLevel(temp2, temp);
		}
	}

	public void setBatteryLevel(boolean ischarging, int level) {
		if(width_shape == -1 || height_shape == -1){
			temp = level;
			temp2 = ischarging;
			return;
		}
		android.view.ViewGroup.LayoutParams layoutParams = this.capacity
				.getLayoutParams();
		layoutParams.width = width_shape;
		int h = height_shape - cap_margin_bot - cap_margin_top;

		int h2 = cap_margin_bot + (level * h / 100);
		layoutParams.height = h2;
		if (level > HIGHT) {
			this.capacity.setBackgroundResource(R.drawable.bat_cap_high);
		}else if(level >LOW_LV1){
			this.capacity.setBackgroundResource(R.drawable.bat_less_7percent_small);
		}else if(level >LOW_LV2){
			this.capacity.setBackgroundResource(R.drawable.bat_less_7percent_small);
		}else if(level >LOW_LV3){
			this.capacity.setBackgroundResource(R.drawable.bat_less_2percent_small);
		}
		this.number.setText(level + "%");
		if(ischarging){
			charge_sign.startAnimation(animation);
		}else{
			charge_sign.clearAnimation();
		}
	}

}
