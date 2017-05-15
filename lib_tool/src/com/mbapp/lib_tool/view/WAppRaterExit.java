package com.mbapp.lib_tool.view;

import com.mbapp.lib_tool.R;
import com.mbapp.lib_tool.makemoney.WSettings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WAppRaterExit {

	static AlertDialog dialogLaunch;
	String AppName = "";

	 public static void show(Context mContext,String AppName){
		 
		showRateDialog(mContext,AppName);
	       

	 }
	 public static void showRateDialog(final Context mContext,String AppName){
		 LayoutInflater inflater = LayoutInflater.from(mContext);
	     View view = inflater.inflate(R.layout.wdialog_rate_app_layout_exit,null);
	     AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	     Button wbtnOK = (Button) view.findViewById(R.id.wbtnOK);
		 Button wbtnCancel = (Button) view.findViewById(R.id.wbtnCancel);
		 TextView wtxtTitle = (TextView)view.findViewById(R.id.wtxtTitle);
		 wtxtTitle.setText(AppName);
		 wbtnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogLaunch.dismiss();
				WFuntion.WRateApp(mContext, mContext.getPackageName());
				
			//	uninstall();
			}
		});
		
		wbtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogLaunch.dismiss();
				Activity d = (Activity) mContext;
				 d.finish();
			}
		});
		builder.setView(view);
		dialogLaunch = builder.create();
		dialogLaunch.show();
		 
	 }
}
