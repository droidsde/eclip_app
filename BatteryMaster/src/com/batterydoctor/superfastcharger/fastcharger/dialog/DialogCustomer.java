package com.batterydoctor.superfastcharger.fastcharger.dialog;

import com.batterydoctor.superfastcharger.fastcharger.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogCustomer extends Dialog {
	TextView title;
	Button rightButton;
	Button leftButton;
	TextView message;
	IDialogCloseListen dialogCloseListen;

	public DialogCustomer(Context context) {
		super(context, R.style.Theme_Dialog);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.dialog_default);
		title = (TextView) findViewById(R.id.title);
		rightButton = (Button) findViewById(R.id.right_btn);
		leftButton = (Button) findViewById(R.id.left_btn);
		message = (TextView) findViewById(R.id.message);
		title.setText(getContext().getResources().getString(R.string.dialog_title_default));
		message.setText(getContext().getResources().getString(R.string.dialog_message_default));
		leftButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogCustomer.this.dismiss();
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogCustomer.this.dismiss();
			}
		});
	}

	public void setTitle(CharSequence charSequence) {
		title.setText(charSequence);
	}
	public void setMessageContent(int id){
		message.setText(id);
	}
	public void setMessageContent(String string) {
		message.setText(string);
	}
	public void setButtonLeftVisible(int id){
		leftButton.setVisibility(id);
	}
	public void setButtonRightVisible(int id){
		rightButton.setVisibility(id);
	}
	public void setViewContent(View view) {
		message.setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.dialog_body)).addView(view);
	}
	public void setDialogCloseListen(final IDialogCloseListen closeListen){
		if(closeListen!=null){
			leftButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					closeListen.OnLeftButtonDialogClick();
					DialogCustomer.this.dismiss();
				}
			});
			rightButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					closeListen.OnRightButtonDialogClick();
					DialogCustomer.this.dismiss();
				}
			});
		}
	}
	/**
	 * defaut is Ok
	 * 
	 * @param string
	 */
	public void setLeftButtonText(String string) {
		leftButton.setText(string);
	}
	public void setLeftButtonTextColor(int id){
		leftButton.setTextColor(id);
	}

	/**
	 * defaut is Canel
	 * 
	 * @param string
	 */
	public void setRightButtonText(String string) {
		rightButton.setText(string);
	}

	public void setLeftButtonOnClickListen(View.OnClickListener listener) {
		if (listener != null) {
			leftButton.setVisibility(View.VISIBLE);
			leftButton.setOnClickListener(listener);
		}
	}

	public void setRightButtonOnClickListen(View.OnClickListener listener) {
		if (listener != null) {
			rightButton.setVisibility(View.VISIBLE);
			leftButton.setOnClickListener(listener);
		}
	}
	/**
	 * visible all button : View.GONE, View.INVISIBLE,View.VISIBLE
	 * @param isvisible
	 */
	public void setVisibleButtonPanel(int visible){
		findViewById(R.id.button_panel).setVisibility(visible);
	}
	public interface IDialogCloseListen {
		public void OnRightButtonDialogClick();
		public void OnLeftButtonDialogClick();
	}
}
