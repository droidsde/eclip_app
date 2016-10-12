package fastcharging.chargerbooster.faster.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import fastcharging.chargerbooster.faster.R;

/**
 * Created by Tin on 2/23/2016.
 */
public class TwoFragment extends Fragment {
    ArcProgress progress;
    TextView tvNhieDo, tvVol;


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
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        tvNhieDo = (TextView)view.findViewById(R.id.tvNhietDo);
        tvVol = (TextView)view.findViewById(R.id.tvVon);
        progress = (ArcProgress)view.findViewById(R.id.arc_progress);
        getActivity().registerReceiver(this.mBatInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        return view;
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        //When Event is published, onReceive method is called
        public void onReceive(Context c, Intent i) {
            //Get Battery %
            int level = i.getIntExtra("level", 0);
            //Find the progressbar creating in main.xml
            progress.setProgress(level);
            tvVol.setText(String.valueOf(((float) i.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)) / 1000) + "V");
            tvNhieDo.setText(String.valueOf(((float)i.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10) + "*C");
        }

    };






}
