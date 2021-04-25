package com.bigtech.econ;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import java.util.Date;
import androidx.annotation.Nullable;

import com.bigtech.econ.api.ForecastRefreshAsyncTask;
import com.bigtech.econ.model.ForecastModel;

public class AppPreferences {

    private static final String PREF_LAST_UPDATED = "LAST_UPDATED";
    private static final String PREF_FORECAST = "FORECAST";
    private static final String PREF_ELAPSED = "ELAPSED";

    private SharedPreferences preferences;
    private boolean refreshing=false;

    AppPreferences(Context context) {
        this.preferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    private void storeElapsedTimeSinceDataRefresh() {
        Log.i("AppPreferences", "storeElapsedTimeSinceDataRefresh");
        long elapsedRealtime = SystemClock.elapsedRealtime();
        preferences.edit().putLong(PREF_ELAPSED, elapsedRealtime).apply();
        refreshing=false;
    }


    public void setLastUpdated(@Nullable String lastUpdated) {
        Log.i("AppPreferences", "setLastUpdated");
        preferences.edit().putString(PREF_LAST_UPDATED, lastUpdated).apply();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        preferences.edit().putLong(PREF_ELAPSED, elapsedRealtime).apply();
        refreshing=false;
    }

    private void RefreshData() {

        long elapsedHours = 0;

        Log.i("AppPreferences", "RefreshData");
        if (preferences.contains(PREF_ELAPSED)) {
            long elapsedSavedTime = preferences.getLong(PREF_ELAPSED, 0 );
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long elapsedMilliseconds = elapsedRealtime - elapsedSavedTime;
            //long elapsedHours = (elapsedTime > 0) ? (elapsedMilliseconds / 360000) + 1: 0;
            elapsedHours = (elapsedSavedTime > 0) ? (elapsedMilliseconds / 60000) + 1: 0; // minutes for testing
            Log.i("AppPreferences", "RefreshData elapsed hours: " + Long.toString(elapsedHours));
        }

        if ( (elapsedHours< 1|| elapsedHours > 24) && refreshing==false) {
            Log.i("AppPreferences", "RefreshData triggered");
            refreshing=true;
            new ForecastRefreshAsyncTask().execute();
        }
    }

    public @Nullable
    String getLastUpdated() {
        Log.i("AppPreferences", "getLastUpdated");
        RefreshData();
        return preferences.getString(PREF_LAST_UPDATED, null);
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        Log.i("AppPreferences", "registerOnSharedPreferenceChangeListener");
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        Log.i("AppPreferences", "unregisterOnSharedPreferenceChangeListener");
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void setForecast(ForecastModel.Forecast forecast) {
        Log.i("AppPreferences", "setForecast");
        preferences.edit().putString(PREF_FORECAST, forecast.name()).apply();
        storeElapsedTimeSinceDataRefresh();
    }

    public ForecastModel.Forecast getForecast() {
        Log.i("AppPreferences", "getForecast");
        RefreshData();
        if (preferences.contains(PREF_FORECAST)) {
            Log.i("AppPreferences", "getForecast preferences.contains(PREF_FORECAST)");
            return ForecastModel.Forecast.valueOf(preferences.getString(PREF_FORECAST, null));
        } else {
            return null;
        }
    }

}
