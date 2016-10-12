package wolfsolflib.com.view;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import wolfsoftlib.com.R;
import wolfsoftlib.com.Json.WCheckNetworkConnection;
import wolfsoftlib.com.Json.WContentValue;
import wolfsoftlib.com.Json.WJSONParser;
import wolfsoftlib.com.Json.WJsonReader;
import wolfsoftlib.com.adapter.WMoreAppAdapterList;
import wolfsoftlib.com.model.WolfModel;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WAdsSmart {
	AlertDialog dialogAdsList;
	public static ArrayList<WolfModel> ArrApp = new ArrayList<WolfModel>() ;
	WJSONParser jParser = new WJSONParser();
	WProgressWheel pb;
	ListView lv;
	LinearLayout linearRetry;
	WMoreAppAdapterList adapter;
	Context mContext;

	Button btnRetry;
	String mUrl;
	Typeface face;
	Display display;
	public WAdsSmart(Context mContext,String Url){
		this.mContext = mContext;
		this.mUrl = Url;
		display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
		
	}
	public void AdsList(){
		int mwidth = display.getWidth();
		int mheight = display.getHeight();
		LayoutInflater inflater = LayoutInflater.from(mContext);
	    View view = inflater.inflate(R.layout.wdialog_more_app_list,null);
	    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	    lv = (ListView)view.findViewById(R.id.wlist_app_more);
	    linearRetry = (LinearLayout)view.findViewById(R.id.wlinear_connect);
	    ImageView imageClose = (ImageView)view.findViewById(R.id.wimage_close_ads);
	    pb= (WProgressWheel) view.findViewById(R.id.wprogress_image);
	    btnRetry = (Button)view.findViewById(R.id.wbtnRetry);
	    TextView tvTitle = (TextView)view.findViewById(R.id.wtitle_more);
	    tvTitle.setTypeface(WTypeView.WgetTypeface(mContext, "wroboto/Roboto-Bold.ttf"));
	    imageClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdsList.dismiss();
			}
		});
	    btnRetry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!WCheckNetworkConnection.isConnectionAvailable(mContext)){
					linearRetry.setVisibility(View.VISIBLE);
					pb.setVisibility(View.GONE);
		        }else{
		        	
		        	new GetListApp().execute();
		        }
			}
		});
	    if(!WCheckNetworkConnection.isConnectionAvailable(mContext)){
			linearRetry.setVisibility(View.VISIBLE);
			pb.setVisibility(View.GONE);
        }else{
        	
        	new GetListApp().execute();
        }
	    lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				WFuntion.WOpenBrower(mContext, ArrApp.get(position).getPackage());
				
			}
		});
	    
	   
		 

			builder.setView(view);
			dialogAdsList = builder.create();
			dialogAdsList.setCancelable(false);
			dialogAdsList.show();
			if(mwidth<mheight)
				dialogAdsList.getWindow().setLayout(mwidth,(int)(mheight/1.2));
			else 
				dialogAdsList.getWindow().setLayout((int)( mwidth/1.6),mheight);
		
	}
@SuppressLint("NewApi")
public class GetListApp extends AsyncTask<Void, Void, Void>{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			linearRetry.setVisibility(View.GONE);
        	pb.setVisibility(View.VISIBLE);
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (ArrApp.size()==0) {
				
				JSONObject json = null;
				json = jParser.makeHttpRequest(mUrl);	
				if(json!=null){
					try {
	    				
						if(json.getInt(WContentValue.SUCCESS)==1)					
							ArrApp =WJsonReader.getHome(json,mContext.getPackageName());
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					return null;
				}
				return null;
				}else
					return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pb.setVisibility(View.GONE);
			linearRetry.setVisibility(View.GONE);
			setListView();
		}
		
	}
	public void setListView() {
		adapter = new WMoreAppAdapterList(ArrApp, mContext);
		((ListView) lv).setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

}
