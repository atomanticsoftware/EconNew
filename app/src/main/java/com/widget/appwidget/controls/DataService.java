package com.widget.appwidget.controls;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.widget.database.DatabaseManager;

import java.lang.ref.WeakReference;

/**
 * Service class responsible for Status Fetch,load its images ,save it to file
 * for notify app widget for updates
 */
public class DataService extends Service {

    private int appWidgetId;
    private DatabaseManager db;

    public static final String ACTION_MELIST_FETCH = "meListFetch",
            ACTION_TIMELINE_FETCH = "timeLineFetch";

    public static final String ME_LIST_ID = "meListId";
    private int invalidMeListId = -1;
    private StatiiFetchHandler statiiFetchHandler;
    private int statiiFetchStatus = 0;
    private int notifyNature = WidgetProvider.GO_FOR_UPDATE;
    // For convenience the 10 status is only fetched
    private int count = 0;
    private String listName = "";

    /**
     * As per the intent passed to the service either fetch Me List Status or
     * fetch Time Line Status
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        notifyNature = intent.getIntExtra(WidgetProvider.UPDATE_ACTION,
                WidgetProvider.GO_FOR_UPDATE);
        db = DatabaseManager.INSTANCE;
        db.init(getApplicationContext());

        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            if (intent.hasExtra(WidgetProvider.LIST_NAME))
                listName = intent.getStringExtra(WidgetProvider.LIST_NAME);
            else
                listName = "Datas";
            Intent visibilityIntent = new Intent();
            visibilityIntent.setAction(WidgetProvider.WIDGET_VISIBILITY);
            visibilityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    appWidgetId);
            visibilityIntent.putExtra(WidgetProvider.LIST_NAME, listName);
            visibilityIntent.putExtra(WidgetProvider.WIDGET_VISIBILITY, false);
            sendBroadcast(visibilityIntent);


            statiiFetchHandler = new StatiiFetchHandler(this);

            if (intent.getAction().equals(ACTION_MELIST_FETCH)) {
                Log.i("Fetch me List", "@Service");
                //fetchMeListStatii(intent.getIntExtra(ME_LIST_ID,
                //				invalidMeListId));
            } else if (intent.getAction().equals(ACTION_TIMELINE_FETCH)) {
                // fetch timeLine
                Log.i("Fetch time line statii", "@Service");
                //fetchTimeLineStatii();
            }

            Log.i("Fetching Datas", "fetching Datas");
        } else {
            Log.i("stopping self", "stopping self");
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private static final int STATII_FETCHED = 0x1;

    /**
     * Handler to take care of action once Status is fetched
     */
    static class StatiiFetchHandler extends Handler {
        private final WeakReference<DataService> DataServiceHolder;

        public StatiiFetchHandler(DataService dataService) {
            DataServiceHolder = new WeakReference<DataService>(
                    dataService);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STATII_FETCHED:
                    Log.i("Go populate Width ", "Go Populate Widget");
                    DataServiceHolder.get().statiiFetchHandler
                            .removeMessages(msg.what);
                    DataServiceHolder.get().populateWidgetWithStatii();
                    break;

                default:
                    break;
            }
        }
    }


    /**
     * As per the result of Status fetch either populate the widget or show
     * error messages
     */
    private void populateWidgetWithStatii() {
        Intent visibilityIntent = new Intent();
        visibilityIntent.setAction(WidgetProvider.WIDGET_VISIBILITY);
        visibilityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId);
        visibilityIntent.putExtra(WidgetProvider.WIDGET_VISIBILITY, true);
        visibilityIntent.putExtra(WidgetProvider.LIST_NAME, listName);
        sendBroadcast(visibilityIntent);

        if (statiiFetchStatus == 1) {
            Log.i(" Service me list Data size",
                    String.valueOf(statiiFetchStatus));

            Intent widgetUpdate = new Intent();
            widgetUpdate.setAction(WidgetProvider.WIDGET_ME_LIST_STATII_UPDATE);
            widgetUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    appWidgetId);
            widgetUpdate.putExtra(WidgetProvider.UPDATE_ACTION, notifyNature);
            sendBroadcast(widgetUpdate);

        } else {
            Toast.makeText(this, "error fetching Datas",
                    Toast.LENGTH_SHORT).show();
        }
        stopSelf();

    }

}
