package wolfsolflib.com.makemoney;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import wolfsoftlib.com.R;
import wolfsoftlib.com.push.Utils;
import wolfsolflib.com.view.WProgressWheel;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class WolfAdsExit {
	private String appName="";
	private String appPackage="";
	private String linkApp="";
	private String appIcon="";
	private String appDes="";
	private String appStatisticUrl="";
	
	
	int styleDialog = 0;
	boolean shouldShow = true;
	String [] listPackage;
	boolean flagPackage = false;
	Context c;
	Display display;
	int mwidth; 
	int mheight;
	AlertDialog dialogAdsGrid, dialogLaunch;
	String appmain;
	Utils utils;
	private static final String NEW_APP_URLVI = "http://wolfsofts.net/push/wclick/apibackvi.txt";
	private static final String NEW_APP_URLEN = "http://wolfsofts.net/push/wclick/apibacken.txt";
	private static String NEW_APP_URL ="http://wolfsofts.net/push/wclick/apibacken.txt" ;
	public WolfAdsExit(Context mcContext, String nameapp){
		c = mcContext;
		appmain =nameapp ;
		utils = new Utils(c);
	
		display = ((Activity) c).getWindowManager().getDefaultDisplay();
		mwidth = display.getWidth();
		mheight = display.getHeight();
		String locale = Locale.getDefault().getLanguage()
				.toLowerCase(Locale.ENGLISH);
		if(utils.haveNetworkConnection()){
			if (locale.contains("vi")) {
				NEW_APP_URL = NEW_APP_URLVI;
			}else{
				NEW_APP_URL = NEW_APP_URLEN;
			}
			new GetDataTask().execute();
		}
		
		
		
		
	}
	public void intdialog1(){
		LayoutInflater inflater = LayoutInflater.from(c);
	    View view = inflater.inflate(R.layout.wdialog_ads1,null);
	    AlertDialog.Builder builder = new AlertDialog.Builder(c);
	    TextView wtxtTitle = (TextView)view.findViewById(R.id.wtxtTitle);
	    TextView wtxtContent = (TextView)view.findViewById(R.id.wtxtContent);
	    Button wbtnOK = (Button)view.findViewById(R.id.wbtnOK);
	    Button wbtnCancel = (Button)view.findViewById(R.id.wbtnCancel);
	    Button wDownload = (Button)view.findViewById(R.id.wDownload);
	    ImageView WimageAds = (ImageView) view.findViewById(R.id.WimageAds);
	   final WProgressWheel spinner = (WProgressWheel) view.findViewById(R.id.wprogress_image);
		Picasso.with(c)
		   .load(appIcon)
		   .error(R.drawable.wic_error)       
		   .resize(100, 100)
		
		.into(WimageAds, new Callback() {
			@Override
			public void onSuccess() {
				spinner.setVisibility(View.GONE);
			}

			@Override
			public void onError() {
				spinner.setVisibility(View.GONE);
				
			}
		});
	    
	    wtxtTitle.setText(appName);
	    wtxtContent.setText(appDes);
	    WimageAds.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdsGrid.dismiss();
				new PostDataTask().execute(appStatisticUrl);
				Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(linkApp));				
				c.startActivity(intent1);
				
			}
		});
	    wDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdsGrid.dismiss();
				new PostDataTask().execute(appStatisticUrl);
				Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(linkApp));				
				c.startActivity(intent1);
				
				
				
			}
		});
	    wbtnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdsGrid.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+c.getPackageName()));				
				c.startActivity(intent1);
			}
		});
	    wbtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Activity) c).finish();
				
			}
		});
	    builder.setView(view);
	    dialogAdsGrid = builder.create();
	    dialogAdsGrid.show();
		
	    
	    
	}
	public void intdialog2(){
		LayoutInflater inflater = LayoutInflater.from(c);
	    View view = inflater.inflate(R.layout.wdialog_ads2,null);
	    AlertDialog.Builder builder = new AlertDialog.Builder(c);
	    TextView wtxtTitle = (TextView)view.findViewById(R.id.wtxtTitle);
	    TextView wtxtContent = (TextView)view.findViewById(R.id.wtxtContent);
	    Button wbtnOK = (Button)view.findViewById(R.id.wbtnOK);
	    Button wbtnCancel = (Button)view.findViewById(R.id.wbtnCancel);
	    Button wDownload = (Button)view.findViewById(R.id.wDownload);
	    ImageView WimageAds = (ImageView) view.findViewById(R.id.WimageAds);
	   final WProgressWheel spinner = (WProgressWheel) view.findViewById(R.id.wprogress_image);
		Picasso.with(c)
		   .load(appIcon)
		   .error(R.drawable.wic_error)
		
		.into(WimageAds, new Callback() {
			@Override
			public void onSuccess() {
				spinner.setVisibility(View.GONE);
			}

			@Override
			public void onError() {
				spinner.setVisibility(View.GONE);
				
			}
		});
	    
	    wtxtTitle.setText(appName);
	    wtxtContent.setText(appDes);
	    WimageAds.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdsGrid.dismiss();
				new PostDataTask().execute(appStatisticUrl);
				Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(linkApp));				
				c.startActivity(intent1);
				
			}
		});
	    wDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdsGrid.dismiss();
				new PostDataTask().execute(appStatisticUrl);
				Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(linkApp));				
				c.startActivity(intent1);
				
				
				
			}
		});
	    wbtnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdsGrid.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+c.getPackageName()));				
				c.startActivity(intent1);
			}
		});
	    wbtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Activity) c).finish();
				
			}
		});
	    builder.setView(view);
	    dialogAdsGrid = builder.create();
	    dialogAdsGrid.show();
		
	    
	    
	}
	private void RateApp(){
		LayoutInflater inflater = LayoutInflater.from(c);
	     View view = inflater.inflate(R.layout.wdialog_rate_app_layout_exit,null);
	     AlertDialog.Builder builder = new AlertDialog.Builder(c);
	     Button wbtnOK = (Button) view.findViewById(R.id.wbtnOK);
		 Button wbtnCancel = (Button) view.findViewById(R.id.wbtnCancel);
		 TextView wtxtTitle = (TextView)view.findViewById(R.id.wtxtTitle);
		 wtxtTitle.setText(appmain);
		 wbtnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogLaunch.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+c.getPackageName()));				
				c.startActivity(intent1);
				
			//	uninstall();
			}
		});
		
		wbtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogLaunch.dismiss();
				Activity d = (Activity) c;
				 d.finish();
			}
		});
		builder.setView(view);
		dialogLaunch = builder.create();
		dialogLaunch.show();
		 
	 }
	
	public void intdialog3(){
		
	}
	public void showDialog(){
		if(utils.haveNetworkConnection()){
			if(styleDialog==0) {
				// Show dialog rate
				RateApp();
			}else if(styleDialog ==1){
				if(!flagPackage)
					intdialog1();
				else{
					RateApp();
				}
				//Show dialog loại 1 
			} else if(styleDialog == 2){
				if(!flagPackage)
					intdialog2();
				else{
					RateApp();
				}
			}else{
				((Activity) c).finish();
			}
		}else{
			((Activity) c).finish();
		}
	}
	private String readTextData() {
		String result = "";
		try {
			// Create a URL for the desired page
			URL url = new URL(NEW_APP_URL);

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			while ((str = in.readLine()) != null) {
				result += str;
				result += "\n";
			}
			result.trim();
			in.close();

			// items = all.split(",");

		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return result;
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			return readTextData();
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			SharedPreferences pre = c.getSharedPreferences("new_app",
					c.MODE_PRIVATE);
			
			try {
				String[] data = result.trim().split("wolfsoftapp");
				try {
					listPackage = data[1].trim().split(",");
					for (int i = 0; i < listPackage.length; i++) {
						if(listPackage[i].contains(c.getPackageName())){
								flagPackage = true;
							}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				for (int i =2;i<=data.length;i++){
					String [] temp = data[i].trim().split("\n");
					appPackage = temp[1];
					if(!utils.isPackageExisted(appPackage)&&!pre.getBoolean(appPackage, false)){
						appName = temp[0];					
						linkApp = temp[2];
						appIcon = temp[3];
						appDes = temp[4];
						appStatisticUrl = temp[5];
						styleDialog = Integer.parseInt(temp[6]);
						break;
					 }else{
						 
						 SharedPreferences.Editor editor = pre.edit();						 
						 editor.putBoolean(appPackage, true);
							editor.commit();
						 appPackage ="";
					 }
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	private class PostDataTask extends AsyncTask<String, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				postTextData(params[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
		}

	}
	private void postTextData(String statisticUrl) throws IOException {
		//	URL url;
			HashMap<String, String> postDataParams = new HashMap<String, String>();;
			Calendar now = Calendar.getInstance();
			String strDateFormat3 = "HH:mm:ss MMM dd yyyy";
			SimpleDateFormat sdf;
			sdf = new SimpleDateFormat(strDateFormat3);
			String time = sdf.format(now.getTime());
			String device = android.os.Build.MANUFACTURER + " "
					+ android.os.Build.MODEL;
			String country = c.getResources().getConfiguration().locale
					.getDisplayLanguage();
			
			
			URL url;
	       
	        postDataParams.put("time","Time2222: " + time);
	        
	        postDataParams.put("device", "   Device: "
					+ device);
	        postDataParams.put("language",
					"  Language: " + country + "  " + c.getPackageName());
	        try {
	            url = new URL(statisticUrl);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setReadTimeout(15000);
	            conn.setConnectTimeout(15000);
	            conn.setRequestMethod("POST");
	            conn.setDoInput(true);
	            conn.setDoOutput(true);


	            OutputStream os = conn.getOutputStream();
	            BufferedWriter writer = new BufferedWriter(
	                    new OutputStreamWriter(os, "UTF-8"));
	            writer.write(getPostDataString(postDataParams));

	            writer.flush();
	            writer.close();
	            os.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			 
		}
		
		private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
	        StringBuilder result = new StringBuilder();
	        boolean first = true;
	        for(Map.Entry<String, String> entry : params.entrySet()){
	            if (first)
	                first = false;
	            else
	                result.append("&");

	            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
	            result.append("=");
	            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
	        }

	        return result.toString();
	    }

	
}
