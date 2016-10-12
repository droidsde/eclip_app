package net.thomascannon.smsspoofer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class ActivityToLaunch extends Activity{
	public static Boolean isCreate = false;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
//	        setContentView(R.layout.test);
	        Intent i = new Intent();
    		i.setAction(Intent.ACTION_VIEW);
    		i.setData(Uri.parse("http://www.google.com"));
    		startActivity(i);
	 }
}
