package com.usstudio.easytouch.assistivetouch.util;

import android.widget.ImageView;
import android.widget.TextView;

public class TaskObject {

	private String taskRes;
	private ImageView taskImage;
	private TextView textView;

	public TaskObject(String taskRes, TextView textView, ImageView taskImage) {
		this.taskRes = taskRes;
		this.textView = textView;
		this.taskImage = taskImage;
	}

	public String getTaskRes() {
		return taskRes;
	}

	public void setTaskRes(String taskRes) {
		this.taskRes = taskRes;
	}

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public ImageView getTaskImage() {
		return taskImage;
	}

	public void setTaskImage(ImageView taskImage) {
		this.taskImage = taskImage;
	}
}
