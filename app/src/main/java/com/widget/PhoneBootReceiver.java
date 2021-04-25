package com.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.widget.database.DatabaseManager;
import com.widget.helpers.GeneralUtils;
import com.widget.helpers.SessionStore;

import java.util.HashMap;
import java.util.Map;

public class PhoneBootReceiver extends BroadcastReceiver {

    public static boolean isPhoneReboot = true;

    /*
     * This method is required to initiate all update interval of widgets
     * and update the widget list after phone reboot
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
            return;
        isPhoneReboot = true;

        Log.i("phone boot done", "phone boot done");
        HashMap<String, Long> updateIntervalsMap = SessionStore
                .getUpdateIntervalWithAppWidgetId(context);
        if (updateIntervalsMap != null) {
            DatabaseManager dbManager = DatabaseManager.INSTANCE;
            dbManager.init(context);
            for (Map.Entry<String, Long> mapEntry : updateIntervalsMap
                    .entrySet()) {
                int appWidgetId = Integer.parseInt(mapEntry.getKey());
                //Re-initializing all the update interval of widgets
                GeneralUtils.initAlarmManager(context, appWidgetId);
            }
            updateIntervalsMap.clear();
        }
    }
}
