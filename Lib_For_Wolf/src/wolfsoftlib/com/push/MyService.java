package wolfsoftlib.com.push;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Map;

import wolfsoftlib.com.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class MyService extends Service {
	private static final String NEW_APP_URLVI = "http://wolfsofts.net/push/newappvi.txt";
	private static final String NEW_APP_URLEN = "http://wolfsofts.net/push/newappen.txt";
	private static String NEW_APP_URL ="http://wolfsofts.net/push/newappen.txt" ;
	// private static final String SAVE_STATISTIC_URL;
	private String appName="";
	private String appPackage="";
	private String linkApp="";
	private String appIcon="";
	private String appDes="";
	private String appStatisticUrl="";
	private int idOpen,timeshow;
	//private String[] appList;;
	private String showTimes="";
	private Bitmap bitMap;
	NotificationCompat.Builder builder;
	Notification notification;
	NotificationManager notiManager;
	String [] listPackage;
	boolean flagPackage = false;
	private Handler handler;
	private Runnable run;
	

	Utils utils = new Utils(this);

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		notiManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(
						this.getApplicationContext().NOTIFICATION_SERVICE);
		String mLocale = intent.getStringExtra("LOCALE");
		if(mLocale.contains("vi")){
			NEW_APP_URL = NEW_APP_URLVI;
		}else{
			NEW_APP_URL = NEW_APP_URLEN;
		}
		showNewApp();
		return START_NOT_STICKY;
	}

	private void setNotification(String appName, String appDes, String linkApp) {
		
		Intent intent1;

		intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(linkApp));

		PendingIntent pendingIntent = PendingIntent.getActivity(
				this.getApplicationContext(), 0, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder = new NotificationCompat.Builder(getApplicationContext());
		builder.setContentTitle(appName);
		builder.setContentText(appDes);
		builder.setContentIntent(pendingIntent);

		builder.setSmallIcon(R.drawable.wdownload);
		new LoadImage().execute(appIcon);

	}

	public void showNewApp() {
			new GetDataTask().execute();
		

	}

	private class GetDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			return readTextData();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			String[] data = result.trim().split("\n");
			try {
			if (data.length >= 6) {
				appName = data[0];
				appPackage = data[1];
				linkApp = data[2];
				appIcon = "http://" + data[3];
				appDes = data[4];
				appStatisticUrl = data[5];
				showTimes = data[6];
				timeshow = Integer.parseInt(data[8]);
				idOpen = Integer.parseInt(data[9]);
				try {
					listPackage = data[7].split(",");
					for (int i = 0; i < listPackage.length; i++) {
						if(listPackage[i].contains(getPackageName())){
								flagPackage = true;
							}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
				
				
				SharedPreferences pre = getSharedPreferences("new_app",
						MODE_PRIVATE);
				Long date_firstLaunch = pre.getLong("wfirstLaunchpush", 0);
				if(date_firstLaunch==0){
					SharedPreferences.Editor editor = pre.edit();
					editor.putLong("wfirstLaunchpush", System.currentTimeMillis());
					editor.commit();
				}
				if (!utils.isPackageExisted(appPackage)&&!pre.getBoolean(appPackage, false)) {
					if(flagPackage==false){
						if(handler==null)
							handler = new Handler();
						if(run == null){
							run = new Runnable() {								
							@Override
							public void run() {	
							if (utils.haveNetworkConnection()) {
								if(idOpen==0)
									setNotification(appName, appDes, linkApp);
								else{
									Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(linkApp));
									intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									getApplication().startActivity(intent1);
								}
							}
								stopSelf();
									}
								};
							}
					
						if(showTimes.contains("0")){
								SharedPreferences.Editor editor = pre.edit();
								editor.putLong("wfirstLaunchpush", System.currentTimeMillis());
								editor.commit();
								handler.postDelayed(run, timeshow);
								
						}else{
							 if (System.currentTimeMillis() >= date_firstLaunch + 
					                    Integer.parseInt(showTimes)* 24 * 60 * 60 * 1000) {
								 SharedPreferences.Editor editor = pre.edit();
								 editor.putLong("wfirstLaunchpush", System.currentTimeMillis());
								 editor.commit();
								 handler.postDelayed(run, timeshow);
								 	
					            }else{
					            	stopSelf();
					            }
						}
						
					}else{
						stopSelf();
					}
					
				} else {
					
 					 SharedPreferences.Editor editor = pre.edit();
					if (!pre.getBoolean(appPackage, false)) {
						if (appPackage.equals(getPackageName())) {
							stopSelf();
						} else {
							if(!flagPackage){
								new PostDataTask().execute(appStatisticUrl);
								editor.putBoolean(appPackage, true);
								editor.commit();
							}
						}

					} else {
						stopSelf();
					}
					
				}
				
			}else
				stopSelf();
			
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
			stopSelf();
		}

	}

	private class LoadImage extends AsyncTask<String, String, Bitmap> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Bitmap doInBackground(String... args) {
			try {
				bitMap = BitmapFactory.decodeStream((InputStream) new URL(
						args[0]).getContent());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitMap;
		}

		protected void onPostExecute(Bitmap image) {
			if (image != null) {
				builder.setLargeIcon(image);

				notification = builder.build();
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notiManager.notify(0, notification);
			}
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
			String country = getResources().getConfiguration().locale
					.getDisplayLanguage();
			
			
			URL url;
	       
	        postDataParams.put("time","Time2222: " + time);
	        
	        postDataParams.put("device", "   Device: "
					+ device);
	        postDataParams.put("language",
					"  Language: " + country + "  " + getPackageName());
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
