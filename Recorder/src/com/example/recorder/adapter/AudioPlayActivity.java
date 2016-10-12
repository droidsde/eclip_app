package com.example.recorder.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.recorder.CallRecorder;
import com.example.recorder.R;
import com.example.recorder.helper.Contact;
import com.example.recorder.helper.DatabaseHandler;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class AudioPlayActivity extends AppCompatActivity {

    private TextView albumTitleText, artistNameText, songTitleText, tvShowNote, tvUnsave, tvTime, tvSDT;
    private SeekBar songProgress;
    private Toolbar toolbar;
    private ImageView prev, stop, next, ivMessage, ivPhone, ivShare;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    public int oneTimeOnly = 0;
    private Handler myHandler = new Handler();;
    private Boolean isStart = false;
    private TableRow trNote, trSave, trNoRecorder, trDelete, trAutoSave;
    public DatabaseHandler db;
    private Contact contact;
    private String name, root, link, date, phone, time, packageFile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fab_media_play);
        toolbar = (Toolbar)findViewById(R.id.toolbar2);
        toolbar.setTitle("Call details");
        
        Intent callerIntent=getIntent();
        Bundle packageFromCaller=
      		  callerIntent.getBundleExtra("package");
        name = packageFromCaller.getString("link");
        packageFile = packageFromCaller.getString("packageFile");
        getData(name);
        mediaPlayer=new MediaPlayer(); 
        root = Environment.getExternalStorageDirectory().toString();
    	link = root +packageFile + name;
    	  try {
    		
  		mediaPlayer.setDataSource(link);
  		mediaPlayer.prepare();
  		} catch (IllegalArgumentException | SecurityException
  				| IllegalStateException | IOException e) {
  			e.printStackTrace();
  		}
    	  db = new DatabaseHandler(this);
    	  
        findViews();
        setIconView();
        setIconViewShare();
        setEvent();
    }
    
    @Override
    protected void onPause() {
      super.onPause();
      mediaPlayer.stop();
    }
    @Override
    protected void onStop() {
      super.onStop();
      mediaPlayer.stop();
    }
    

    private void findViews() {
    	tvSDT = (TextView)findViewById(R.id.tvSDT);
        tvSDT.setText(phone);
        tvTime = (TextView)findViewById(R.id.tvTimePlay);
    	tvTime.setText(time);
    	tvShowNote = (TextView)findViewById(R.id.tvShowNote);
        if(isNote()==true)
        {
        	tvShowNote.setText(contact.getPhoneNumber());
        }
        
//        play audio
        songProgress = (SeekBar) findViewById(R.id.song_progress_bar);
        styleSeekbar(songProgress);
        stop = (ImageView) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isStart==false)
				{
					Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
		            mediaPlayer.start();
		            stop.setImageResource(R.drawable.ic_stop);
		            isStart=true;
		            finalTime = mediaPlayer.getDuration();
		            startTime = mediaPlayer.getCurrentPosition();
		            if (oneTimeOnly == 0) {
		                songProgress.setMax((int) finalTime);
		                oneTimeOnly = 1;
		             }
		            myHandler.postDelayed(UpdateSongTime,100);
				}
				else{
					mediaPlayer.pause();
		            stop.setImageResource(R.drawable.ic_skip_next);
		            isStart = false;
				}
			}
		});
        
        songProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {                
                    if(mediaPlayer != null && fromUser){
                        mediaPlayer.seekTo(progress);
                        mediaPlayer.start();
                    }
                }
        });
        
    }
    
    private void setIconViewShare()
    {
    	isNote();
    	ivMessage = (ImageView)findViewById(R.id.ivMessage);
    	ivMessage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", "");
				sendIntent.putExtra("address"  , phone);
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			}
		});
    	ivPhone = (ImageView)findViewById(R.id.ivPhone);
    	ivPhone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
				startActivity(intent);
			}
		});
    	ivShare = (ImageView)findViewById(R.id.ivShare);
    	ivShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				    Uri uri = Uri.parse(link);
				    Intent share = new Intent(Intent.ACTION_SEND);
				    share.setType("audio/*");
				    share.putExtra(Intent.EXTRA_STREAM, uri);
				    startActivity(Intent.createChooser(share, "Share Sound File"));
			}
		});
    }
    
    private void setEvent()
    {
    	trNote = (TableRow)findViewById(R.id.trNote);
    	trNote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 // Create custom dialog object
				showWriteNote();
			}
		});
    	trSave = (TableRow)findViewById(R.id.trSave);
    	trSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveFile();
			}
		});
    	trNoRecorder = (TableRow)findViewById(R.id.trNoRecorder);
    	trNoRecorder.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				noRecorder();
			}
		});
    	trDelete = (TableRow)findViewById(R.id.trDelete);
    	trDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				controDeleteFile();
			}
		});
    	trAutoSave = (TableRow)findViewById(R.id.trAutoSave);
    	trAutoSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				autoSavePhone();
			}
		});
    }
    
    private void controDeleteFile()
    {
    	AlertDialog.Builder b=new AlertDialog.Builder(AudioPlayActivity.this);
		LayoutInflater inflater = AudioPlayActivity.this.getLayoutInflater();
	    b.setMessage("Delete this audio?");
		b.setPositiveButton("OK", new DialogInterface. OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(isNote()==false)
				{
					try{
						deleteFile(root +packageFile, name);
					}catch(Exception e)
					{
					}
				}else
				{
					db.deleteContact(contact);
					try{
						deleteFile(root +packageFile, name);
					}catch(Exception e)
					{
					}
				}
				finish();
			}
		});
		b.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		b.create().show();
    }
    
    private void saveFile()
    {
    	AlertDialog.Builder b=new AlertDialog.Builder(AudioPlayActivity.this);
		LayoutInflater inflater = AudioPlayActivity.this.getLayoutInflater();
	    b.setMessage("Save this recording?");
		b.setPositiveButton("SAVE", new DialogInterface. OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String audioSave = root +"/audioSave/";
				moveFile(root +"/ss/", name, audioSave);
				dialog.cancel();
			}
		});
		b.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		b.create().show();
    }
    
    private void autoSavePhone()
    {
    	AlertDialog.Builder b=new AlertDialog.Builder(AudioPlayActivity.this);
		LayoutInflater inflater = AudioPlayActivity.this.getLayoutInflater();
	    b.setMessage("Automatic record this contact?");
		b.setPositiveButton("SAVE", new DialogInterface. OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(isNote()==false)
				{
					db.addContact(new Contact(name, "", 1)); 
				}else
				{
					contact.setStatus(1);
					db.updateContact(contact);
				}
				dialog.cancel();
				
			}
		});
		b.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		b.create().show();
    }
    
    private void noRecorder(){
    	AlertDialog.Builder b=new AlertDialog.Builder(AudioPlayActivity.this);
		LayoutInflater inflater = AudioPlayActivity.this.getLayoutInflater();
	    b.setMessage("Don't record this contact?");
		b.setPositiveButton("SAVE", new DialogInterface. OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(isNote()==false)
				{
					db.addContact(new Contact(name, "", 2)); 
			}else
				{
					contact.setStatus(2);
					db.updateContact(contact);
				}
				dialog.cancel();
				
			}
		});
		b.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		b.create().show();
    }
    
    private void showWriteNote()
    {
    	AlertDialog.Builder b=new AlertDialog.Builder(AudioPlayActivity.this);
		LayoutInflater inflater = AudioPlayActivity.this.getLayoutInflater();
	    final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
	    Drawable dlIcon = getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp); 
    	 dlIcon.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); 
    	 ((ImageView)dialogView.findViewById(R.id.imageDialog)).setImageDrawable(dlIcon);
	    b.setView(dialogView);
	    final EditText edtNote = (EditText) dialogView.findViewById(R.id.etNote);
	    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtNote, InputMethodManager.SHOW_IMPLICIT);
		b.setPositiveButton("Yes", new DialogInterface. OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				
				String content = edtNote.getText().toString();
				if(isNote()==false)
					{
						db.addContact(new Contact(name, content, 0)); 
				}else
					{
						contact.setPhoneNumber(content);
						db.updateContact(contact);
					}
				
				tvShowNote.setText(content);
					dialog.cancel();
			}
		});
		b.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		b.create().show();
         
    }
    
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
           startTime = mediaPlayer.getCurrentPosition();
           songProgress.setProgress((int)startTime);
           myHandler.postDelayed(this, 100);
           
        }
     };
     
     public Boolean isNote()
     {
    	try{
    		contact = db.getContactByName(name);
     		if(contact!=null)
     		{
     			tvShowNote.setText(contact.getPhoneNumber());
     			return true;
     		}
    	}catch(Exception e)
    	{
    		
    	}
 		return false;
     }
     
     
     private void setIconView()
     {
    	 Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_description_white_24dp); 
    	 myIcon1.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); 
    	 ((ImageView)findViewById(R.id.iv1)).setImageDrawable(myIcon1);
    	 Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_save_white_24dp); 
    	 myIcon2.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); 
    	 ((ImageView)findViewById(R.id.iv2)).setImageDrawable(myIcon2);
    	 Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_remove_circle_outline_white_24dp); 
    	 myIcon3.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); 
    	 ((ImageView)findViewById(R.id.iv3)).setImageDrawable(myIcon3);
    	 Drawable myIcon4 = getResources().getDrawable(R.drawable.ic_delete_white_24dp); 
    	 myIcon4.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); 
    	 ((ImageView)findViewById(R.id.iv4)).setImageDrawable(myIcon4);
    	 Drawable myIcon5 = getResources().getDrawable(R.drawable.ic_inbox_white_24dp); 
    	 myIcon5.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); 
    	 ((ImageView)findViewById(R.id.iv5)).setImageDrawable(myIcon5);
     }
     
    private void styleSeekbar(SeekBar songProgress) {
        int color = getResources().getColor(R.color.background);
        songProgress.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        songProgress.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

   

    private void showMainViewItems() {
        scale(albumTitleText, 50);
        scale(artistNameText, 150);
    }

    private void showSecondaryViewItems() {
        scale(songProgress, 0);
        animateSeekBar(songProgress);
        scale(songTitleText, 100);
        scale(prev, 150);
        scale(stop, 100);
        scale(next, 200);
    }

   

    private void scale(View view, long delay){
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(500)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    private void animateSeekBar(SeekBar seekBar){
        seekBar.setProgress(15);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(seekBar, "progress", 15, 0);
        progressAnimator.setDuration(300);
        progressAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        progressAnimator.start();
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

    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath); 
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);        
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

                // write the output file
                out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();  


        } 

             catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
              catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
    
    private void deleteFile(String inputPath, String inputFile) {
        try {
            // delete the original file
            new File(inputPath + inputFile).delete();  

        }
       catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
		
}

