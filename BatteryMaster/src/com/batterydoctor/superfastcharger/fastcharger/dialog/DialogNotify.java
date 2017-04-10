package com.batterydoctor.superfastcharger.fastcharger.dialog;

import com.batterydoctor.superfastcharger.fastcharger.R;

import android.content.Context;
import android.view.View;

public class DialogNotify extends DialogCustomer{

	public DialogNotify(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setButtonRightVisible(View.GONE);
		setTitle(R.string.dialog_notify_title);
	}

}
