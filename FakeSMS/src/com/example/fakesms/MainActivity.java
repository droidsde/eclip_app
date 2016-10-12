package com.example.fakesms;

import android.support.v7.app.ActionBarActivity;
import android.telephony.gsm.SmsMessage;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent a = new Intent("android.provider.Telephony.SMS_RECEIVED");

        byte[] by =(byte[])(SmsMessage.getSubmitPdu("12345", "1234", "hello", false).encodedMessage);
        Object[] vrs = {by};
        a.putExtra("pdus",vrs);
        sendBroadcast(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
