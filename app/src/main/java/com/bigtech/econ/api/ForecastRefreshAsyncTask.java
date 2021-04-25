package com.bigtech.econ.api;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bigtech.econ.AppPreferences;
import com.bigtech.econ.EconApplication;
import com.bigtech.econ.api.model.ApiForecastResponse;
import com.bigtech.econ.error.MappingError;
import com.bigtech.econ.model.ForecastMapper;
import com.bigtech.econ.model.ForecastModel;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class ForecastRefreshAsyncTask extends AsyncTask<Void, Void, ForecastModel> {

    private Exception exception;
    private static final SimpleDateFormat SERVER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected ForecastModel doInBackground(Void... voids) {
        Log.i("ForecastRefreshAsyncTask", "doInBackground");
        try {
            ApiForecastResponse apiForecastResponse = NetworkDataSourceFactory.getNetworkDataSource().forecast().execute().body();
            return new ForecastMapper().map(apiForecastResponse);
        } catch (IOException | MappingError e) {
            e.printStackTrace();
            exception = e;
            // TODO track error. With Crashlytics or something.
            return null;
        }
    }

    @Override
    protected void onPostExecute(ForecastModel forecastModel) {
        Log.i("ForecastRefreshAsyncTask", "onPostExecute");
        if (exception != null) {
            Toast.makeText(EconApplication.appContext, exception.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Log.i("ForecastRefreshAsyncTask", "updating preferences");
            Toast.makeText(EconApplication.appContext, "New data", Toast.LENGTH_SHORT).show();
            AppPreferences appPrefs = EconApplication.getAppPreferences();
            Log.i("ForecastRefreshAsyncTask", "updating preferences getrundate" + forecastModel.getRunDate());
            String runDate = SERVER_DATE_FORMAT.format(forecastModel==null | forecastModel.getRunDate() == null ? "-" : forecastModel.getRunDate());
            appPrefs.setLastUpdated(runDate);
            appPrefs.setForecast(forecastModel.getForecast());

        }
    }
}