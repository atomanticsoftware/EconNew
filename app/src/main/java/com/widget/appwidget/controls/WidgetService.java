package com.widget.appwidget.controls;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.widget.database.DatabaseManager;
import com.widget.helpers.MeListItem;

/**
 * A class to assign Adapter to Widget's ListView with data from Database
 */
public class WidgetService extends RemoteViewsService {

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		Log.i("Go to DataProvider", "Go to DataProvider");
		int appWidgetId = intent.getIntExtra(
				AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);

		return (new DataProvider(this.getApplicationContext(), intent,
				fetchStoredDataStatii(appWidgetId)));
	}

	private ArrayList<MeListItem> fetchStoredDataStatii(int appWidgetId) {
		DatabaseManager db = DatabaseManager.INSTANCE;
		db.init(getApplicationContext());
		Log.i("Fetching stored Data", "@WidgetService");
		return db.fetchStoredDataStatii(String.valueOf(appWidgetId));
	}

}
