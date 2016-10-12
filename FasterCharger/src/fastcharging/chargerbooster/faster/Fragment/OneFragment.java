package fastcharging.chargerbooster.faster.Fragment;

/**
 * Created by Tin on 2/23/2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.drive.internal.GetMetadataRequest;
import fastcharging.chargerbooster.faster.R;

import fastcharging.chargerbooster.faster.Adapter.Adapter;


public class OneFragment extends Fragment{
    ViewPager viewPager;
    LinearLayout ln1, ln2, ln3, ln4;
    TextView tvTitle1, tvTitle2, tvTitle3, tvTitle4, tvCharge;
    ImageView img1, img2, img3, img4,imgStatutCharge;
    Boolean isClick1, isClick2, isClick3, isClick4;
    View view;
    int brightnessmode;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        getViewFrom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_one, container, false);
        viewPager = (ViewPager)  view.findViewById(R.id.view_pager1);
        viewPager.setAdapter(new Adapter(getFragmentManager()));
        getViewFrom();

        try {
            getActivity().registerReceiver(this.mBatInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        }catch (Exception e)
        {
            Toast toast = Toast.makeText(getActivity(), "Can't register receiver", Toast.LENGTH_SHORT);
            toast.show();
        }

        //event
        ln1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick1==false)
                { 
                	
                    img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness_true));
                    tvTitle1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    isClick1=true;
                    Settings.System.putInt(getActivity().getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                }
                else
                {
                    img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness_min));
                    tvTitle1.setTextColor(getResources().getColor(R.color.text));
                    isClick1=false;
                    Settings.System.putInt(getActivity().getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                }
            }
        });
        ln2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClick2==false)
                {
                    getActivity().startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));
                } else
                {
                    getActivity().startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));
                }
            }
        });
        ln3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClick3==false)
                {
                    img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_rotate_true));
                    tvTitle3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tvTitle3.setText(getResources().getText(R.string.rotate_true));
                    isClick3=true;
                    Settings.System.putInt(getActivity().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);

                }
                else
                {
                    img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_rotate));
                    tvTitle3.setTextColor(getResources().getColor(R.color.text));
                    tvTitle3.setText(getResources().getText(R.string.rotate));
                    isClick3=false;
                    Settings.System.putInt(getActivity().getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0);
                }
            }
        });
        ln4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick4 == false) {
                    getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                } else {
                    getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }
        });
        return view;

    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        //When Event is published, onReceive method is called
        public void onReceive(Context c, Intent i) {
            int status = i.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            switch (status){
                case 1:
                    tvCharge.setText("Unknown Chargin");
                    imgStatutCharge.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery));
                    break;
                case 2:
                    tvCharge.setText("Charging");
                    imgStatutCharge.setImageDrawable(getResources().getDrawable(R.drawable.ic_charging));
                    break;
                case 3:
                    tvCharge.setText("Discharging");
                    imgStatutCharge.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery));
                    break;
                case 4:
                    tvCharge.setText("Not Charging");
                    imgStatutCharge.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery));
                    break;
                case 5:
                    tvCharge.setText("Full Battery");
                    imgStatutCharge.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery));
                    break;
            }

        }

    };

    public void getViewFrom()
    {
        //get view
        ln1 = (LinearLayout)view.findViewById(R.id.lo1);
        ln2 = (LinearLayout)view.findViewById(R.id.lo2);
        ln3 = (LinearLayout)view.findViewById(R.id.lo3);
        ln4 = (LinearLayout)view.findViewById(R.id.lo4);
        tvTitle1 = (TextView)view.findViewById(R.id.tvTitle1);
        tvTitle2 = (TextView)view.findViewById(R.id.tvTitle2);
        tvTitle3 = (TextView)view.findViewById(R.id.tvTitle3);
        tvTitle4 = (TextView)view.findViewById(R.id.tvTitle4);
        tvCharge = (TextView)view.findViewById(R.id.tvCharge);
        img1 = (ImageView)view.findViewById(R.id.icon1);
        img2 = (ImageView)view.findViewById(R.id.icon2);
        img3 = (ImageView)view.findViewById(R.id.icon3);
        img4 = (ImageView)view.findViewById(R.id.icon4);
        imgStatutCharge = (ImageView)view.findViewById(R.id.imgStatutCharging);
        isClick1 = false;
        isClick2 = false;
        isClick3 = false;
        isClick4 = false;
        getBrightMode();
        if(brightnessmode==1)
        {
            isClick1=true;
            img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness_true));
            tvTitle1.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if (Settings.System.getInt(getActivity().getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0) == 1)
        {
            img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_rotate_true));
            tvTitle3.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvTitle3.setText(getResources().getText(R.string.rotate_true));
            isClick3=true;
        }
        if(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1)
        {
            img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_airplane_true));
            tvTitle2.setTextColor(getResources().getColor(R.color.colorPrimary));
            isClick2=true;
        }
        else {
            img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_airplane));
            tvTitle2.setTextColor(getResources().getColor(R.color.text));
            isClick2=false;
        }
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}


        if (gps_enabled==true)
        {
            img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_gps_true));
            tvTitle4.setTextColor(getResources().getColor(R.color.colorPrimary));
            isClick4 = true;
        }
        else {
            img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_gps));
            tvTitle4.setTextColor(getResources().getColor(R.color.text));
            isClick4 = false;
        }
    }

    protected void getBrightMode() {
// TODO Auto-generated method stub
        try {
            brightnessmode = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception e) {
            Log.d("tag", e.toString());
        }
    }




}