package com.widget.appwidget.controls;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.widget.database.DatabaseManager;
import com.widget.helpers.MeListItem;

import java.util.ArrayList;

/**
 * Class similar to ArrayAdapter or any list adapter its main task is to
 * populate ListView of the appwidget
 */
public class DataProvider implements RemoteViewsFactory {
    private ArrayList<MeListItem> meList = new ArrayList<MeListItem>();
    private Context context = null;
    private int appWidgetId;
    private int count = 0;


    public DataProvider(Context context, Intent intent,
                        ArrayList<MeListItem> meList) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        if (meList != null) {
            this.meList = (ArrayList<MeListItem>) meList.clone();
            meList.clear();
        }
    }

    /*
     * returns the total size of the ArrayList<MeListItem>
     */
    @Override
    public int getCount() {
        return meList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
        //		return new RemoteViews(context.getPackageName(),
//				R.layout.loading_layout);
    }

    @Override
    public RemoteViews getViewAt(int position) {
//		final RemoteViews remoteView = new RemoteViews(
//				context.getPackageName(), R.layout.list_row);
//		MeListItem meListItem = meList.get(position);
//		remoteView.setTextViewText(R.id.DataHeading, meListItem.fullName);
//		remoteView.setTextViewText(R.id.DataTimeText, meListItem.DataDate);
//		remoteView.setTextViewText(R.id.DataContent, meListItem.description);
//
//		//Log.i("decoded fileName", meListItem.imagePath);
//		Bitmap bitmap = BitmapFactory.decodeFile(meListItem.imagePath);
//		if (bitmap != null)
//			remoteView.setImageViewBitmap(R.id.imageView, bitmap);

//		return remoteView;
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        Log.i("onCreate called", "onCreate @DataProvider");
        Log.i("Me list size", String.valueOf(meList.size()));

    }

    /*
     * This method is called on first initialization and called from
     * AppWidgetProvider notifyAppWidgetViewDataChanged so ignoring first
     * intialization via count but taking into account AppWidgetProvider
     * notifyAppWidgetViewDataChanged this method is important,if you want to
     * make updates to ListView,otherwise ListView data won't change unless and
     * until this is called and its data is fetched again
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onDataSetChanged() {
        Log.i("Dataset changed", "dataset changed");
        count++;
        if (count > 1) {
            DatabaseManager dbManager = DatabaseManager.INSTANCE;
            dbManager.init(context);
            if (meList != null)
                meList.clear();
            meList = dbManager.fetchStoredDataStatii(String
                    .valueOf(appWidgetId));

        }

    }

    @Override
    public void onDestroy() {
        if (meList != null)
            meList.clear();
    }

}
