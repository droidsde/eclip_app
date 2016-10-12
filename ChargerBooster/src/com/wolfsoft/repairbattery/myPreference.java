package com.wolfsoft.repairbattery;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.SharedPreferences;
import android.widget.Toast;
import android.content.Context;

public class myPreference {
	
	private static myPreference myPreference;
    private static SharedPreferences sharedPreferences;
    String prefname="my_data";
    
    public static myPreference getInstance(Context context) {
        if (myPreference == null) {
            myPreference = new myPreference(context);
        }
        return myPreference;
    }

    private myPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(prefname,Context.MODE_PRIVATE);
    }

    public static void saveData() {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean("checked", true);
        Date date = new Date(System.currentTimeMillis());
        long millis = date.getTime();
        millis = millis + 5*24*60*60*1000;
        prefsEditor.putLong("time", millis);
        prefsEditor.commit();           
    }

    public static Boolean getData() {
        if (sharedPreferences!= null) {
           return sharedPreferences.getBoolean("checked", false);
        }
        return false;         
    }
    
    public static Long getHistory() {
    	if (sharedPreferences!= null) {
            return sharedPreferences.getLong("time", 0);
         }
         return null;
    }


}
