package com.usstudio.easytouch.assistivetouch.customview;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.dialog.ChooseTaskDialog;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainPageView implements OnClickListener {

	private View view;
	private Context context;
	private LinearLayout taskLayout1;
	private LinearLayout taskLayout2;
	private LinearLayout taskLayout3;
	private LinearLayout taskLayout4;
	private LinearLayout taskLayout5;
	private LinearLayout taskLayout6;
	private LinearLayout taskLayout7;
	private LinearLayout taskLayout8;
	private LinearLayout taskLayout9;
	private ImageView taskImage1;
	private ImageView taskImage2;
	private ImageView taskImage3;
	private ImageView taskImage4;
	private ImageView taskImage5;
	private ImageView taskImage6;
	private ImageView taskImage7;
	private ImageView taskImage8;
	private ImageView taskImage9;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView textView4;
	private TextView textView5;
	private TextView textView6;
	private TextView textView7;
	private TextView textView8;
	private TextView textView9;
	private int currentVersion;
	private String[][] data;

	public MainPageView(View view, Context context) {
		this.view = view;
		this.context = context;
		currentVersion = Build.VERSION.SDK_INT;
	}

	public void init() {
		taskLayout1 = (LinearLayout) view.findViewById(R.id.task_layout1);
		taskLayout2 = (LinearLayout) view.findViewById(R.id.task_layout2);
		taskLayout3 = (LinearLayout) view.findViewById(R.id.task_layout3);
		taskLayout4 = (LinearLayout) view.findViewById(R.id.task_layout4);
		taskLayout5 = (LinearLayout) view.findViewById(R.id.task_layout5);
		taskLayout6 = (LinearLayout) view.findViewById(R.id.task_layout6);
		taskLayout7 = (LinearLayout) view.findViewById(R.id.task_layout7);
		taskLayout8 = (LinearLayout) view.findViewById(R.id.task_layout8);
		taskLayout9 = (LinearLayout) view.findViewById(R.id.task_layout9);
		taskImage1 = (ImageView) view.findViewById(R.id.task_image1);
		taskImage2 = (ImageView) view.findViewById(R.id.task_image2);
		taskImage3 = (ImageView) view.findViewById(R.id.task_image3);
		taskImage4 = (ImageView) view.findViewById(R.id.task_image4);
		taskImage5 = (ImageView) view.findViewById(R.id.task_image5);
		taskImage6 = (ImageView) view.findViewById(R.id.task_image6);
		taskImage7 = (ImageView) view.findViewById(R.id.task_image7);
		taskImage8 = (ImageView) view.findViewById(R.id.task_image8);
		taskImage9 = (ImageView) view.findViewById(R.id.task_image9);
		textView1 = (TextView) view.findViewById(R.id.task_title1);
		textView2 = (TextView) view.findViewById(R.id.task_title2);
		textView3 = (TextView) view.findViewById(R.id.task_title3);
		textView4 = (TextView) view.findViewById(R.id.task_title4);
		textView5 = (TextView) view.findViewById(R.id.task_title5);
		textView6 = (TextView) view.findViewById(R.id.task_title6);
		textView7 = (TextView) view.findViewById(R.id.task_title7);
		textView8 = (TextView) view.findViewById(R.id.task_title8);
		textView9 = (TextView) view.findViewById(R.id.task_title9);
		taskLayout1.setOnClickListener(this);
		taskLayout2.setOnClickListener(this);
		taskLayout3.setOnClickListener(this);
		taskLayout4.setOnClickListener(this);
		taskLayout5.setOnClickListener(this);
		taskLayout6.setOnClickListener(this);
		taskLayout7.setOnClickListener(this);
		taskLayout8.setOnClickListener(this);
		taskLayout9.setOnClickListener(this);

		data = new String[9][2];
		for (int i = 0; i < data.length; i++) {
			data[i] = SharedPreference.readMainPage(context, i + 1).split("[.]");
		}

		initLayout(taskImage1, textView1, data[0][0], data[0][1]);
		initLayout(taskImage2, textView2, data[1][0], data[1][1]);
		initLayout(taskImage3, textView3, data[2][0], data[2][1]);
		initLayout(taskImage4, textView4, data[3][0], data[3][1]);
		initLayout(taskImage5, textView5, data[4][0], data[4][1]);
		initLayout(taskImage6, textView6, data[5][0], data[5][1]);
		initLayout(taskImage7, textView7, data[6][0], data[6][1]);
		initLayout(taskImage8, textView8, data[7][0], data[7][1]);
		initLayout(taskImage9, textView9, data[8][0], data[8][1]);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.task_layout1:
			showDialog(1, taskImage1, textView1);
			break;
		case R.id.task_layout2:
			showDialog(2, taskImage2, textView2);
			break;
		case R.id.task_layout3:
			showDialog(3, taskImage3, textView3);
			break;
		case R.id.task_layout4:
			showDialog(4, taskImage4, textView4);
			break;
		case R.id.task_layout5:
			showDialog(5, taskImage5, textView5);
			break;
		case R.id.task_layout6:
			showDialog(6, taskImage6, textView6);
			break;
		case R.id.task_layout7:
			showDialog(7, taskImage7, textView7);
			break;
		case R.id.task_layout8:
			showDialog(8, taskImage8, textView8);
			break;
		case R.id.task_layout9:
			showDialog(9, taskImage9, textView9);
			break;

		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void initLayout(ImageView taskImage, TextView textView, String taskRes, String taskTitle) {
		taskImage.setImageResource(context.getResources().getIdentifier(taskRes, "drawable", context.getPackageName()));
		if ("ic_action_new".equals(taskRes)) {
			if (currentVersion >= Build.VERSION_CODES.JELLY_BEAN) {
				taskImage.setBackground(ContextCompat.getDrawable(context, R.drawable.dash_border_bg));
			} else {
				taskImage.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dash_border_bg));
			}
		} else {
			taskImage.setBackgroundColor(Color.TRANSPARENT);
		}
		textView.setText(taskTitle);
	}

	private void showDialog(final int mainPage, final ImageView taskImage, final TextView textView) {
		ChooseTaskDialog dialog = new ChooseTaskDialog(context, mainPage, 1);
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				data[mainPage - 1] = SharedPreference.readMainPage(context, mainPage).split("[.]");
				initLayout(taskImage, textView, data[mainPage - 1][0], data[mainPage - 1][1]);
			}
		});
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();
	}
}
