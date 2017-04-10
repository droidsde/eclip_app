package com.batterydoctor.superfastcharger.fastcharger;

import wolfsolflib.com.view.WTypeView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.fastercharger.ChargerSetting;

public class ManagerDialog {
	static AlertDialog DialogDone,dialogSetting,DialogConnect;
	 public static void DialogSetting(final Context mContext){
		 LayoutInflater inflater = LayoutInflater.from(mContext);
	     View view = inflater.inflate(R.layout.settings,null);
	     AlertDialog.Builder builder = new Builder(mContext);
	     Button btnOK = (Button) view.findViewById(R.id.btnOK);
	     final CheckBox cbSound = (CheckBox)view.findViewById(R.id.checkBox2);
	     final CheckBox cbFull = (CheckBox)view.findViewById(R.id.checkBox3);
	     final CheckBox cbOpen = (CheckBox)view.findViewById(R.id.checkBox1);
	     TextView txt1 = (TextView) view.findViewById(R.id.textView1);
	     TextView txt2 = (TextView) view.findViewById(R.id.textView2);
	     TextView txt3 = (TextView) view.findViewById(R.id.textView3);
	     
	     txt1.setTypeface(WTypeView.WgetTypeface(mContext, "fonts/Roboto-Medium.ttf"));
	     txt2.setTypeface(WTypeView.WgetTypeface(mContext, "fonts/Roboto-Medium.ttf"));
	     txt3.setTypeface(WTypeView.WgetTypeface(mContext, "fonts/Roboto-Medium.ttf"));
	     cbFull.setTypeface(WTypeView.WgetTypeface(mContext, "fonts/Roboto-Medium.ttf"));
	     cbSound.setTypeface(WTypeView.WgetTypeface(mContext, "fonts/Roboto-Medium.ttf"));
	     btnOK.setTypeface(WTypeView.WgetTypeface(mContext, "fonts/Roboto-Medium.ttf"));
	     
	     cbSound.setChecked(ChargerSetting.readEnableSound(mContext));
	     cbFull.setChecked(ChargerSetting.readFull(mContext));
	     cbOpen.setChecked(ChargerSetting.readOPEN(mContext));
		 btnOK.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialogSetting.dismiss();
					ChargerSetting.saveEnableSound(mContext, cbSound.isChecked());
					ChargerSetting.saveFull(mContext, cbFull.isChecked());
					ChargerSetting.saveOpen(mContext, cbOpen.isChecked());
				
				}
			});
			
		  builder.setView(view);
		  dialogSetting = builder.create();
		  dialogSetting.setCancelable(false);
			
		  dialogSetting.show();
      
     
	 }
	

}
