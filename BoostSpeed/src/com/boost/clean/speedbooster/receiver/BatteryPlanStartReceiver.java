package com.boost.clean.speedbooster.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boost.clean.speedbooster.model.BatterySaver;
import com.boost.clean.speedbooster.utils.PreferenceUtil;
import com.boost.clean.speedbooster.utils.Utils;

import java.util.List;

public class BatteryPlanStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isEnbale = PreferenceUtil.getBoolean(context, PreferenceUtil.BATTERY_PLAN, false);
        if (!isEnbale) {
            return;
        }
        PreferenceUtil preferenceUtil = new PreferenceUtil();
        int position = PreferenceUtil.getInt(context, PreferenceUtil.PEDIOD_INDEX);
        List<BatterySaver> batterySavers = preferenceUtil.getListBatterySaver(context);
        Utils.setBatterySaverSelected(context, batterySavers.get(position));
    }
}
