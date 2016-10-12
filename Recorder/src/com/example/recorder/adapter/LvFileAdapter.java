package com.example.recorder.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.example.recorder.CallRecorder;
import com.example.recorder.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LvFileAdapter  extends BaseAdapter{ 
	ArrayList<String> result;
	Context context;
	String packageFile;
	String date = "";
	String time = "";
	String phone = "";
	String root = "";
	public static String linkFile, name;
	private MediaPlayer mediaPlayer;
	private double finalTime = 0;
	
	private static LayoutInflater inflater=null;
    public LvFileAdapter(Activity activity, ArrayList<String> prgmNameList, String packageFile) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        this.packageFile = packageFile;
        context=activity;
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
        TextView tvName, tvDate, tvTime, tvSec;
        LinearLayout lnItem;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView = inflater.inflate(R.layout.custom_list_file, parent, false);
             holder.tvName=(TextView) rowView.findViewById(R.id.tvSDT);
             holder.tvDate = (TextView)rowView.findViewById(R.id.tvDate);
             holder.tvTime = (TextView)rowView.findViewById(R.id.tvTime);
             holder.lnItem = (LinearLayout)rowView.findViewById(R.id.lnItem);
             holder.tvSec = (TextView)rowView.findViewById(R.id.tvSec);
             
             getData(result.get(position));
             
         holder.tvName.setText(phone);
         holder.tvDate.setText(date);
         holder.tvTime.setText(time);

         mediaPlayer=new MediaPlayer(); 
         root = Environment.getExternalStorageDirectory().toString();
     	  String link = root + packageFile + result.get(position);
     	  try {
     		
   		mediaPlayer.setDataSource(link);
   		mediaPlayer.prepare();
   		} catch (IllegalArgumentException | SecurityException
   				| IllegalStateException | IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
         finalTime = mediaPlayer.getDuration();
         
         holder.tvSec.setText(String.format("%d min, %d sec",
                 TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                 TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                 TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                 );
         
         rowView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					Intent intent = new Intent(context, AudioPlayActivity.class);
					 Bundle bundle=new Bundle();
					 bundle.putString("link",result.get(position));
					 bundle.putString("packageFile", packageFile);
					 intent.putExtra("package", bundle);
					 context.startActivity(intent);
			}
		});
         
         rowView.setOnLongClickListener(new View.OnLongClickListener() {
 			@Override
 			public boolean onLongClick(View v) {
 				CallRecorder.isChangeMenu = true;
 	            if (context instanceof CallRecorder) {
 	                ((CallRecorder) context).onPrepareOptionsMenu(CallRecorder.menu);
 	                linkFile = root + packageFile + result.get(position);
 	                name = result.get(position);
 	            }
 				return true;
 			}
 		});
         
        return rowView;
    }
    
    
   


    
    
    
    public void getData(String data)
    {
    	
    	String[] parts = data.split("_");
    	date = parts[0];
    	String data2 = parts[1];
    	String[] parts2 = data2.split("-date-");
    	String time1 = parts2[0];
    	String data3 = parts2[1];
    	String[]part3 = data3.split("-phone-");
    	phone = part3[0];
    	String[]part4 = time1.split("-");
    	time = part4[0]+":" +part4[1];
    	
    }

}
