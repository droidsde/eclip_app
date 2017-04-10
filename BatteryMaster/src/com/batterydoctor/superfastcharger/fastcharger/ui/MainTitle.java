package com.batterydoctor.superfastcharger.fastcharger.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import wolfsolflib.com.view.WTypeView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.batterydoctor.superfastcharger.fastcharger.AboutActivity;
import com.batterydoctor.superfastcharger.fastcharger.ManagerDialog;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.SettingActivity;
import com.batterydoctor.superfastcharger.fastcharger.fastercharger.ChargerSetting;

@SuppressLint("NewApi")
public class MainTitle extends RelativeLayout {
	Context context;
	ImageView title_left;
	ImageButton ibSetting;
	View space;
	TextView text_mid;
	TextView text_title;
	Drawable right;
	Drawable left;
	String string;
	public MainTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		 
		// TODO Auto-generated constructor stub
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.MainTitle);
		string = typedArray.getString(0);
		right = typedArray.getDrawable(1);
		left = typedArray.getDrawable(2);
		typedArray.recycle();
		init();
	}
	
	private void init() {
		context = getContext();
		inflate(context, R.layout.main_title, this);
		title_left = (ImageView) findViewById(R.id.main_title_left_button);
		ibSetting = (ImageButton) findViewById(R.id.ibSettings);
		space = findViewById(R.id.space_holder);
		text_title = (TextView) findViewById(R.id.main_title_text);
		text_mid = (TextView) findViewById(R.id.middle_text);
		if (string != null && string.equals("")) {
			text_title.setText(string);
		}

		ibSetting.setOnClickListener(new OnSettingButtonCLick());
		// setRightButtonIcon(right);
		setLeftButtonIcon(left);
	}

	class OnSettingButtonCLick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
			PopupMenu popup = new PopupMenu(context, v);
			popup.inflate(R.menu.popup_menu);

			try {
				Field[] fields = popup.getClass().getDeclaredFields();
				for (Field field : fields) {
					if ("mPopup".equals(field.getName())) {
						field.setAccessible(true);
						Object menuPopupHelper = field.get(popup);
						Class<?> classPopupHelper = Class
								.forName(menuPopupHelper.getClass().getName());
						Method setForceIcons = classPopupHelper.getMethod(
								"setForceShowIcon", boolean.class);
						setForceIcons.invoke(menuPopupHelper, true);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					int id = item.getItemId();
					switch (id) {
					case R.id.actionsetting:
//						Intent i2 = new Intent(context,SettingActivity.class);
//						context.startActivity(i2);
						ManagerDialog.DialogSetting(context);
						break;
					case R.id.actionRate:
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(Uri.parse("market://details?id="
								+ context.getPackageName()));
						context.startActivity(intent);
						break;
					case R.id.actionFeedback:
						Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("message/rfc822");
						i.putExtra(Intent.EXTRA_EMAIL,
								new String[] { "usamobilestudio@gmail.com" });
						i.putExtra(Intent.EXTRA_SUBJECT,
								"Feedback from "+context.getResources().getString(R.string.app_name));
						i.putExtra(Intent.EXTRA_TEXT, "");
						try {
							context.startActivity(Intent.createChooser(i,
									"Send mail..."));
						} catch (android.content.ActivityNotFoundException ex) {
							Toast.makeText(context,
									"There are no email clients installed.",
									Toast.LENGTH_SHORT).show();
						}

						break;
					case R.id.actionAbout:
						Intent intentAbout = new Intent(context,AboutActivity.class);
						context.startActivity(intentAbout);
					
						break;
					case R.id.actionOther:
						Intent intentOther = new Intent();
						intentOther.setAction(Intent.ACTION_VIEW);
						intentOther.setData(Uri.parse("market://search?q=pub:USA+Mobile+Studio"));
						context.startActivity(intentOther);
						break;

					default:
						break;
					}
					return true;
				}
			});
			popup.show();
		}
	}

	public void setTextMid() {
		text_title.setVisibility(View.GONE);
		text_mid.setVisibility(View.VISIBLE);
		text_mid.setText(text_title.getText());
	}

	// public void setRightButtonIcon(Drawable drawable) {
	// if (drawable != null) {
	// title_right.setImageDrawable(drawable);
	// title_right.setVisibility(View.VISIBLE);
	// return;
	// }
	// title_right.setVisibility(View.GONE);
	// title_right.setOnClickListener(null);
	// }

	// public void setRightButtonIcon(int i) {
	// Drawable drawable = null;
	// if (i != 0) {
	// drawable = getContext().getResources().getDrawable(i);
	// }
	// setRightButtonIcon(drawable);
	// }

	public void setLeftButtonIcon(Drawable drawable) {
		if (drawable != null) {
			title_left.setImageDrawable(drawable);
			title_left.setVisibility(View.VISIBLE);
			return;
		}
		title_left.setVisibility(View.GONE);
		title_left.setOnClickListener(null);
	}

	public void setLeftButtonIcon(int i) {
		Drawable drawable = null;
		if (i != 0) {
			drawable = getContext().getResources().getDrawable(i);
		}
		setLeftButtonIcon(drawable);
	}

	public void setLeftButtonOnClickListen(View.OnClickListener clickListener) {
		this.title_left.setOnClickListener(clickListener);
	}

	// public void setRightButtonOnClickListen(View.OnClickListener
	// clickListener) {
	// this.title_right.setOnClickListener(clickListener);
	// }
	//
	// public void setRightButtonClickable(boolean paramBoolean) {
	// this.title_right.setClickable(paramBoolean);
	// }

	public void setLeèttButtonClickable(boolean paramBoolean) {
		this.title_left.setClickable(paramBoolean);
	}

	public void setTitleTextMid(int i) {
		this.text_mid.setText(i);
		this.text_mid.setVisibility(View.VISIBLE);
	}

	public void setTitleText(int i) {
		this.text_title.setText(i);
		this.text_title.setVisibility(View.VISIBLE);
	}

	public void setTitleText(String string) {
		this.text_title.setText(string);
		this.text_title.setVisibility(View.VISIBLE);
	}
}
