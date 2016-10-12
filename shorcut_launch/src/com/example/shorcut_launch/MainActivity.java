package com.example.shorcut_launch;



import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {
	Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get package name
        getpackagename();
        
        //create shortcut
        createShortcut();
    }
    
    public void getpackagename(){
    	Intent myIntent = new Intent(MainActivity.this, Myservice.class);
    	this.startService(myIntent);
    	
    	
    	ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();

        Log.i("current_app",tasks.get(0).processName);
//    	Toast.makeText(MainActivity.this, tasks.get(2).processName, 
//				   Toast.LENGTH_LONG).show();
    }
    
    public void createShortcut(){
    	btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent shortcutIntent = new Intent();
				
//				String link = "google.com";
//				Bundle bundle = new Bundle();
//				bundle.putString("link", link);
//				shortcutIntent.putExtra("myBundle", bundle);
		        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        shortcutIntent.setComponent(new ComponentName(getApplicationContext().getPackageName(), ".ActivityToLaunch"));
		        shortcutIntent.setAction(Intent.ACTION_MAIN);

		        Intent addIntent = new Intent();
		        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Test");
		        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher));
		        addIntent.putExtra("info for Main Activity","Hello");
		        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		        getApplicationContext().sendBroadcast(addIntent);
				
			}
		});
    }
    
    public final BroadcastReceiver noteCompletedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "Broadcast received",   Toast.LENGTH_LONG).show();
           //HANDLE HERE THE INTENT
        }
    };
    
}
