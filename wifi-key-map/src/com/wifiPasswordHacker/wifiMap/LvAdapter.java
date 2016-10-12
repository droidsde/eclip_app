package com.wifiPasswordHacker.wifiMap;

import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LvAdapter extends BaseAdapter {
	
	ArrayList<String> result;
	Context context;
	private static LayoutInflater inflater=null;
	public LvAdapter(Activity activity, ArrayList<String> prgmNameList) {
        // TODO Auto-generated constructor stub
        this.result=prgmNameList;
        this.context=activity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

	@Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
	
	 public class Holder
	    {
	        TextView tvData;
	        ImageView img;
	    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder=new Holder();
		View rowView = inflater.inflate(R.layout.custom_lv, parent, false);
		holder.tvData = (TextView) rowView.findViewById(R.id.tvWifi);
		holder.img = (ImageView) rowView.findViewById(R.id.ivLogo);
		holder.tvData.setText(result.get(position));
		if(!result.get(position).equals(""))
		{
			holder.img.setImageResource(R.mipmap.ic_logo);
		}
		holder.tvData.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String data = holder.tvData.getText().toString();
				
				if(!data.equals(""))
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				      alertDialogBuilder.setMessage(context.getResources().getString(R.string.copy));
				      alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					         @Override
					         public void onClick(DialogInterface arg0, int arg1) {
					        	
					         }
					      });
				      alertDialogBuilder.setPositiveButton("COPY", new DialogInterface.OnClickListener() {
				         @Override
				         public void onClick(DialogInterface arg0, int arg1) {
				        	copy(data);
				         }
				      });
				      AlertDialog alertDialog = alertDialogBuilder.create();
				      
				      alertDialog.show();
				}
				
			}
		});
		return rowView;
		
	}
	
	private void copy(String tvData)
	{
		String data = tvData;
		String getPass[] = data.split("Password: ");
		data = getPass[1];
		String getPass2[] = data.split("\n");
		data = getPass2[0];
		try {
			if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
			    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
			    clipboard.setText(data);
			} else {
			    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
			    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", data);
			            clipboard.setPrimaryClip(clip);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}



}
