package com.usstudio.easytouch.assistivetouch.util;

import android.graphics.drawable.Drawable;

public class App {

	private String title;
	private String packageName;
	private Drawable icon;

	public App() {

	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
