package com.batterydoctor.superfastcharger.fastcharger.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.batterydoctor.superfastcharger.fastcharger.R;

public class MyTimeDisplayDigital extends LinearLayout implements
		View.OnClickListener {
	private TextView hour;
	private TextView minutes;
	private TextView hour_lable;
	private TextView minutes_lable;
	private Context context;
	private Shader shader;
	private float digital_size;
	private float unit_size;
	private float margin_l;
	private int margin_r;

	public MyTimeDisplayDigital(Context contxt, AttributeSet attrs) {
		super(contxt, attrs);
		// TODO Auto-generated constructor stub
		TypedArray array = contxt.obtainStyledAttributes(attrs,
				R.styleable.MyDigitalTimeDisplay);
		this.digital_size = array.getDimension(0, 0.0F);
	    this.unit_size = array.getDimension(1, 0.0F);
	    this.margin_l = array.getDimensionPixelSize(3, 0);
	    this.margin_r = array.getDimensionPixelSize(2, 0);
	    array.recycle();
		init();
	}
	public void setTime(int hour,int min){
		this.hour.setText(String.valueOf(hour));
		this.minutes.setText(String.valueOf(min));
	}
	private void init() {
		context = getContext();
		inflate(context, R.layout.digital_time_display, this);
		Resources resources = this.getResources();
		this.shader = new LinearGradient(0.0F, 0.0F, 0.0F, this.digital_size, resources.getColor(R.color.smart_settings_title_color_start),
				resources.getColor(R.color.smart_settings_title_color_end), Shader.TileMode.CLAMP);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		hour = (TextView) findViewById(R.id.digital_time_text_hours);
		minutes = (TextView) findViewById(R.id.digital_time_text_minutes);
		minutes_lable = (TextView) findViewById(R.id.digital_time_text_minutes_lable);
		hour_lable = (TextView) findViewById(R.id.digital_time_text_hours_lable);
		hour.getPaint().setShader(shader);
		minutes.getPaint().setShader(shader);
		if (this.unit_size!= 0.0F) {
			minutes_lable.setTextSize(TypedValue.TYPE_NULL, this.unit_size);
			hour_lable.setTextSize(TypedValue.TYPE_NULL, this.unit_size);
		}
		if (this.margin_r != 0)
	    {
	      LinearLayout.LayoutParams localLayoutParams2 = (LinearLayout.LayoutParams)this.hour.getLayoutParams();
	      this.hour.setPadding(localLayoutParams2.leftMargin, localLayoutParams2.topMargin, this.margin_r, localLayoutParams2.bottomMargin);
	      this.hour.setPadding(localLayoutParams2.leftMargin, localLayoutParams2.topMargin, this.margin_r, localLayoutParams2.bottomMargin);
	    }
	    if (this.margin_r != 0.0F)
	    {
//	      LinearLayout.LayoutParams localLayoutParams1 = (LinearLayout.LayoutParams)this.k.getLayoutParams();
//	      this.k.setPadding(localLayoutParams1.leftMargin, localLayoutParams1.topMargin, (int)this.q, localLayoutParams1.bottomMargin);
	    }
	    setOnClickListener(this);
	    setClickable(true);
	    setFocusable(true);
	}
}
