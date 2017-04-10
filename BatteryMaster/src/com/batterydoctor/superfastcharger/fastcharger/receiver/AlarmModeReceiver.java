package com.batterydoctor.superfastcharger.fastcharger.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.batterydoctor.superfastcharger.fastcharger.mode.ModeFragment;
import com.batterydoctor.superfastcharger.fastcharger.shedule.AlarmSchedule;
import com.nvn.log.LogBuider;
/**
 * dùng cho việc lập lịch
 * @author nghia
 *
 */
public class AlarmModeReceiver extends BroadcastReceiver {
	private static final String TAG = "AlarmModeReceiver";
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		String action = arg1.getAction();
		if(action.equals(AlarmSchedule.ALARMRESTORE)){
			String idMode = arg1.getStringExtra(AlarmSchedule.RESTORE_KEY);
			if(idMode!=null){
				Intent intent = new Intent();
				intent.setAction(ModeFragment.NOTIFY_USER_CHANGE_MODE);
				intent.putExtra(ModeFragment.MODE_CHANGE,idMode);
				arg0.sendBroadcast(intent);
			}
//			Toast.makeText(arg0, AlarmSchedule.ALARMRESTORE, Toast.LENGTH_LONG).show();
		}
		if(action.equals(AlarmSchedule.ALARMSWITCH)){
			String idMode = arg1.getStringExtra(AlarmSchedule.SWITCH_KEY);
			if(idMode!=null){
				Intent intent = new Intent();
				intent.setAction(ModeFragment.NOTIFY_USER_CHANGE_MODE);
				intent.putExtra(ModeFragment.MODE_CHANGE,idMode);
				arg0.sendBroadcast(intent);
			}
//			Toast.makeText(arg0, AlarmSchedule.ALARMSWITCH, Toast.LENGTH_LONG).show();
		}
		LogBuider.e(TAG, action);
		
	}
}
