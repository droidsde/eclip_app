package com.batterydoctor.superfastcharger.fastcharger.dialog;

import android.content.Context;
import android.content.res.Resources;

import com.batterydoctor.superfastcharger.fastcharger.R;

public class DialogDelete extends DialogCustomer{

	public DialogDelete(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		Resources resources = context.getResources();
		setTitle(resources
				.getString(R.string.dialog_delete_item_title));
		setLeftButtonText(resources
				.getString(R.string.dialog_delete));
		setMessageContent(resources
				.getString(R.string.dialog_delete_item_message));
	}

}
