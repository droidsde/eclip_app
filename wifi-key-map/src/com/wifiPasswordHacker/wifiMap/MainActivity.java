/*
 * Copyright (C) 2014
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wifiPasswordHacker.wifiMap;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import wolfsolflib.com.activity.AppCompatActivityAds;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

public class MainActivity extends AppCompatActivityAds {

	String pwd="";
	String type="";
	String open="";
	ListView lvData;
	ArrayList<String> arr = new ArrayList<String>();
	WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WsetAdmobId("ca-app-pub-9358104111199046/2624232415","ca-app-pub-9358104111199046/7054432014");
		WloadAdsBanner(R.id.loQuanCao);
		WloadAdsInterstitial();
		
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        
        

		pwd=getResources().getString(R.string.pwd);
		type=getResources().getString(R.string.type);
		open=getResources().getString(R.string.open);

		RunAsRoot("cat /data/misc/wifi/wpa_supplicant.conf | busybox grep -E \"key_mgmt=|ssid=|psk=\"");
		if(arr.size()==0)
		{
			RunAsRoot("cat /data/misc/wifi/wpa_supplicant_hisi.conf | busybox grep -E \"key_mgmt=|ssid=|psk=\"");
			RunAsRoot("cat /data/wifi/bcm_supp.conf | busybox grep -E \"key_mgmt=|ssid=|psk=\"");
		}
		
		if(arr.size()==0)
		{
			RunAsRoot("cat /system/etc/wifi/wpa_supplicant_wcn.conf | busybox grep -E \"key_mgmt=|ssid=|psk=\"");
			RunAsRoot("cat /data/customization/wpa_supplicant.conf | busybox grep -E \"key_mgmt=|ssid=|psk=\"");
		}
		
		if(arr.size()==0)
		{
			RunAsRoot("cat /system/etc/wifi/wpa_supplicant.conf | busybox grep -E \"key_mgmt=|ssid=|psk=\"");
			RunAsRoot("cat /etc/wifi/wpa_supplicant.conf | busybox grep -E \"key_mgmt=|ssid=|psk=\"");
		}
		
		if(arr.size()==0)
		{
			alertBao();
		}
		
		for(int i=0; i<6; i++)
		{
			arr.add("");
		}
		lvData = (ListView)findViewById(R.id.lvData_text);

		LvAdapter adapter = new LvAdapter(this, arr);
		lvData.setAdapter(adapter);
		
		mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
		mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
		  @Override public void onRefresh() {
		    // Do work to refresh the list here.
		    new Task().execute();
		  }
		});
	}

	private void RunAsRoot(String cmd) {
        int id=1;
		try {
		    Process process = Runtime.getRuntime().exec("su");
			DataOutputStream dos = new DataOutputStream(process.getOutputStream());
			InputStream is = process.getInputStream();

			dos.writeBytes(cmd + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();

            String tmp="";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int read = 0;
			while((read = is.read(buffer)) > 0){
				baos.write(buffer, 0, read);
				tmp = baos.toString("UTF-8");
				tmp = tmp.replace("\t", "");
				tmp = tmp.replace("\"", "");
				tmp = tmp.replace("ssid=", "SSID: ");
				tmp = tmp.replace("psk=", pwd+": ");
				tmp = tmp.replace("key_mgmt=NONE", type+": "+open+"\n");
				tmp = tmp.replace("key_mgmt=WPA-PSK", type+": "+"WPA-PSK"+"\n");
				
				String[] datas;
				datas = tmp.split("\n\n");
				for(int i =0; i<datas.length; i++){
					String aa = datas[i];
					arr.add(aa);
				}
				 
				
			}
			
			String[] datas = tmp.split("\n");
//			arr.add(tmp);
//			arr.add("test");
		    process.waitFor();
		    
//		    ret = id == 0 ? "NULL" : tmp;
		} catch (IOException e) {
		    throw new RuntimeException(e);
		} catch (InterruptedException e) {
		    throw new RuntimeException(e);
		}
	}
	
	private void alertBao()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      alertDialogBuilder.setMessage(getResources().getString(R.string.alert));
	      
	      alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	        	
	         }
	      });
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      
	      alertDialog.show();
	}
	
	private class Task extends AsyncTask<Void, Void, String[]> {
		  
		  @Override protected void onPostExecute(String[] result) {
		    // Call setRefreshing(false) when the list has been refreshed.
		    mWaveSwipeRefreshLayout.setRefreshing(false);
		    super.onPostExecute(result);
		  }

		@Override
		protected String[] doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
