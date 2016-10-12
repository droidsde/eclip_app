package com.example.recorder;

import java.util.Calendar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.util.Log;

public class CallBroadcastReceiver extends BroadcastReceiver
{
	public static String numberToCall = "";
	public static String timeCall = "";
	boolean flag = false;
    long start_time, end_time;
    long total_time;
    
    public void onReceive(Context context, Intent intent) {
        Log.d("CallRecorder", "CallBroadcastReceiver::onReceive got Intent: " + intent.toString());
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            numberToCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//            String xx = intent.getStringExtra(Intent.E);
            Log.d("CallRecorder", "CallBroadcastReceiver intent has EXTRA_PHONE_NUMBER: " + numberToCall);
        }

        PhoneListener phoneListener = new PhoneListener(context);
        TelephonyManager telephony = (TelephonyManager)
            context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        Log.d("PhoneStateReceiver::onReceive", "set PhoneStateListener");
        
        // lay thoi luong cuoc goi
        String action = intent.getAction();
        if (action.equalsIgnoreCase("android.intent.action.PHONE_STATE")) {
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
            TelephonyManager.EXTRA_STATE_RINGING)) {
                start_time = System.currentTimeMillis();
            }
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
            TelephonyManager.EXTRA_STATE_IDLE)) {
                end_time = System.currentTimeMillis();
                total_time = end_time - start_time;
            }
        }
        timeCall = String.valueOf(total_time);
        
        
        
    }
    
    
}
