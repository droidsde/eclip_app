package com.wolfsoft.repairbattery.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.ldoublem.loadingviewlib.LVBattery;
import com.wolfsoft.repairbattery.FixActivity;
import com.wolfsoft.repairbattery.R;
import com.wolfsoft.repairbattery.RepairActivity;
import com.wolfsoft.repairbattery.myPreference;

/**
 * Created by Tin on 2/23/2016.
 */
public class TwoFragment extends Fragment {
    ArcProgress progress;
    TextView tvTem, tvAvl, tvGra;
    LVBattery mLVBattery;
    Button btnRepair;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_repair, container, false);
        tvTem = (TextView)view.findViewById(R.id.temperature);
        tvAvl = (TextView)view.findViewById(R.id.available);
        tvGra = (TextView)view.findViewById(R.id.grande);
        btnRepair = (Button)view.findViewById(R.id.btnStart);
        btnRepair.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myPreference myPrefrence = myPreference.getInstance(getContext());
			    Boolean check = myPreference.getData();
			    	long history = myPrefrence.getHistory();
			    	Date date = new Date(System.currentTimeMillis());
			        long millis = date.getTime();
			        if((millis < history) && (history!=0))
			        {
			        	showDialog();
			        }else{
			        	Intent repIntent = new Intent(getActivity(), RepairActivity.class);
						startActivity(repIntent);
			        }
			    
			}
		});
        mLVBattery = (LVBattery) view.findViewById(R.id.lv_battery);
        mLVBattery.setBatteryOrientation(LVBattery.BatteryOrientation.VERTICAL);
        mLVBattery.setShowNum(true);
        getActivity().registerReceiver(this.mBatInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        return view;
    }
    
    


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        //When Event is published, onReceive method is called
        public void onReceive(Context c, Intent i) {
            //Get Battery %
            int level = i.getIntExtra("level", 0);
            String tem = String.valueOf(((float)i.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10);
            String vol = String.valueOf(((float) i.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)) / 1000);
            tvAvl.setText( getString(R.string.available)+ " " + vol + "V");
            mLVBattery.setValue(level);
            tvTem.setText(getString(R.string.temperature)+ " " + tem + getString(R.string.nhietdo) + "C");
        }

    };


    public void showDialog(){
    	AlertDialog.Builder b=new AlertDialog.Builder(getContext());
		  b.setTitle(getString(R.string.information));
		  b.setMessage(getString(R.string.noproblems));
		  b.setPositiveButton("OK", new DialogInterface. OnClickListener() {
			  @Override
			  public void onClick(DialogInterface dialog, int which)
			  {
				  dialog.cancel();
			  }});
		  b.create().show();
    }



}
