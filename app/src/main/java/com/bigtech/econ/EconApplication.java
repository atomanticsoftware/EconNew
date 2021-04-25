package com.bigtech.econ;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class EconApplication extends Application {

    private static AppPreferences appPreferences;

    public static Context appContext;

    @Override
    public void onCreate() {
        Log.i("EconApplication", "OnCreate");
        super.onCreate();
        appContext = this;
        RegisterPeriodicWorker();
    }

    public static AppPreferences getAppPreferences() {
        appPreferences = appPreferences == null ? new AppPreferences(appContext) : appPreferences;
        return appPreferences;
    }

    public static void RefreshWidget(int imageResId) {
        Log.i("EconApplication", "RefreshWidget");
        //EconWidgetProvider.adaptAppWidgets(AppWidgetManager.getInstance(EconApplication.appContext), null, imageResId);
    }

    public static void RegisterPeriodicWorker() {
        Log.i("EconApplication", "RegisterPeriodicWorker");
        Constraints constraints = new Constraints.Builder().setRequiresCharging(false).build();

        PeriodicWorkRequest saveRequest = new PeriodicWorkRequest.Builder(DataRefresh.class, 15, TimeUnit.MINUTES).setConstraints(constraints).build();

        WorkManager.getInstance(appContext).enqueue(saveRequest);
    }
}
