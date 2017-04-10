package com.boost.clean.speedbooster.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.boost.clean.speedbooster.model.BatterySaver;
import com.boost.clean.speedbooster.utils.PreferenceUtil;
import com.boost.clean.speedbooster.utils.Utils;

import java.util.List;

public class BatteryPlanStopReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceUtil preferenceUtil = new PreferenceUtil();
        boolean isEnbale = PreferenceUtil.getBoolean(context, PreferenceUtil.BATTERY_PLAN, false);
        if (!isEnbale) {
            return;
        }
        int position = PreferenceUtil.getInt(context, PreferenceUtil.PEDIOD_OUTSIDE_INDEX);
        List<BatterySaver> batterySavers = preferenceUtil.getListBatterySaver(context);
        Utils.setBatterySaverSelected(context, batterySavers.get(position));
    }
}
