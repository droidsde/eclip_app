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
import wolfsoftlib.com.model.WTinyDB;
import wolfsoftlib.com.model.WolfModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;

public class Wadslistexit {
	String mUrl;
	Display display;
	Context mContext;
	AlertDialog dialogAdsGrid;
	ListView lv;
	LinearLayout linearRetry;
	WProgressWheel pb;
	WMoreAppAdapterList adapter;
	WJSONParser jParser = new WJSONParser();
	public static ArrayList<WolfModel> ArrApp = new ArrayList<WolfModel>() ;
	WTinyDB tinyDB;
	int mwidth; 
	int mheight;
	public Wadslistexit(Context mContext,String Url){
		this.mContext = mContext;
		this.mUrl = Url;
		display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
		mwidth = display.getWidth();
		mheight = display.getHeight();
		tinyDB = new WTinyDB(mContext); 
		
		if(!WCheckNetworkConnection.isConnectionAvailable(mContext)){
			ArrayList<String> arrstr= tinyDB.getList("MYOJECT");
			Gson gson = new Gson();
			if(ArrApp.size()==0){
			for (int i = 0; i < arrstr.size(); i++) {
				ArrApp.add(gson.fromJson(arrstr.get(i), WolfModel .class));
			}
			}
        }else{
        	new GetAppFrist().execute();
        }
		intdialog();
	}
	public void intdialog(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
	    View view = inflater.inflate(R.layout.wdialog_more_app_list_exit,null);
	    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	    lv = (ListView)view.findViewById(R.id.wlist_app_more);
	    linearRetry = (LinearLayout)view.findViewById(R.id.wlinear_connect);
	    pb= (WProgressWheel) view.findViewById(R.id.wprogress_image);
	    Button btnYes = (Button)view.findViewById(R.id.wbtn_yes);
	    Button btnNo = (Button)view.findViewById(R.id.wbtn_no);
	    Button btnRate = (Button)view.findViewById(R.id.wbtn_rate);
	    Button btnRetry = (Button)view.findViewById(R.id.wbtnRetry);
	    btnYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity) mContext).finish();
			}
		});
	    btnRate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WFuntion.WOpenBrower(mContext, "https://play.google.com/store/apps/details?id="+mContext.getPackageName());
			}
		});
	    btnNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdsGrid.dismiss();
			}
		});
	    if(ArrApp.size()==0){
	    	if(WCheckNetworkConnection.isConnectionAvailable(mContext)){
	    		new GetListApp().execute();
	    	}else{
	    		linearRetry.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
	    	}
			
        }else{
        	pb.setVisibility(View.GONE);
			linearRetry.setVisibility(View.GONE);
        	setListView();
        }
	    btnRetry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!WCheckNetworkConnection.isConnectionAvailable(mContext)&&ArrApp.size()==0){
					linearRetry.setVisibility(View.VISIBLE);
					pb.setVisibility(View.GONE);
		        }else{
		        	
		        	new GetListApp().execute();
		        }
			}
		});
	    lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				WFuntion.WOpenBrower(mContext, ArrApp.get(position).getPackage());
				
			}
		});
	    builder.setView(view);
	    dialogAdsGrid = builder.create();
	}
	public void showads(){
		if(ArrApp.size()==0){
			intdialog();
		};
	    dialogAdsGrid.show();
		if(mwidth<mheight)
			dialogAdsGrid.getWindow().setLayout(mwidth,(int)(mheight/1.2));
		else 
			dialogAdsGrid.getWindow().setLayout((int)( mwidth/1.6),mheight);
	  
	}
	
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
			saveData();
			pb.setVisibility(View.GONE);
			linearRetry.setVisibility(View.GONE);
			setListView();
		}
		
	}
public class GetAppFrist extends AsyncTask<Void, Void, Void>{

	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
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
		saveData();
	}
	
}
	public void setListView() {
		
		adapter = new WMoreAppAdapterList(ArrApp,mContext);
		((ListView) lv).setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}
	public void saveData(){
	
		Gson gson = new Gson();
	   
	    ArrayList<String> temp = new ArrayList<String>();
	    for (int i = 0; i < ArrApp.size(); i++) {
	    	 String json = gson.toJson(ArrApp.get(i));
	    	 temp.add(json);
		}
	    tinyDB.putList("MYOJECT", temp);
	}

	

}
