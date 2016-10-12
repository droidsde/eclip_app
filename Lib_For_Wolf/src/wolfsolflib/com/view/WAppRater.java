package wolfsolflib.com.view;

import wolfsoftlib.com.R;
import wolfsolflib.com.makemoney.WSettings;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WAppRater extends WSettings {

	public static String  DONTSHOWAGAIN="dontshowagain";
	public static String  LAUCHCOUNT="launch_count";
	public static String  DATEFIRST="date_firstlaunch";
	static AlertDialog dialogLaunch;
	public static void savedDontShowAgain(Context context, boolean check){
		save(context, DONTSHOWAGAIN, check);
	}
	public static boolean readDontShowAgain(Context mContext){
		return read(mContext, DONTSHOWAGAIN, false);
	} 
	
	public static void savedLauchCount(Context mContext, long check){
		saveLong(mContext, LAUCHCOUNT, check);
	}
	public static long readLauchCount(Context mContext){
		return readLong(mContext, LAUCHCOUNT, 0);
	}
	
	public static void savedDateLaunch(Context mContext, long check){
		saveLong(mContext, DATEFIRST, check);
	}
	public static long readDateLaunch(Context mContext){
		return readLong(mContext, DATEFIRST, 0);
	} 
	
	 public static void app_launched(Context mContext,String AppName,
			 int LAUNCHES_UNTIL_PROMPT,int DAYS_UNTIL_PROMPT){
		 
		 if (readDontShowAgain(mContext)) { return ; }
		// Increment launch counter
		 long launch_count = readLauchCount(mContext)+1;
		 savedLauchCount(mContext, launch_count);
		 //date
		 Long date_firstLaunch =  readDateLaunch(mContext);
		 if (date_firstLaunch == 0) {
	            date_firstLaunch = System.currentTimeMillis();
	            savedDateLaunch(mContext,date_firstLaunch);
	        }
		 //show dialog
		 if (launch_count >= LAUNCHES_UNTIL_PROMPT){
			 if (System.currentTimeMillis() >= date_firstLaunch + 
	                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
	                showRateDialog(mContext,AppName);
	            }
		 }

	 }
	 public static void showRateDialog(final Context mContext,String AppName){
		 LayoutInflater inflater = LayoutInflater.from(mContext);
	     View view = inflater.inflate(R.layout.wdialog_rate_app_layout,null);
	     AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	     Button wbtnOK = (Button) view.findViewById(R.id.wbtnOK);
		 Button wbtnCancel = (Button) view.findViewById(R.id.wbtnCancel);
		 TextView wtxtTitle = (TextView)view.findViewById(R.id.wtxtTitle);
		 wtxtTitle.setText(AppName);
		 wbtnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogLaunch.dismiss();
				WFuntion.WRateApp(mContext, mContext.getPackageName());
				savedDontShowAgain(mContext,true);
			//	uninstall();
			}
		});
		
		wbtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogLaunch.dismiss();
			}
		});
		builder.setView(view);
		dialogLaunch = builder.create();
		dialogLaunch.show();
		 
	 }
}
