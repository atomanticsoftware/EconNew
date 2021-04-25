package com.bigtech.econ;

import android.app.ActivityManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class DataRefresh extends Worker {

    public DataRefresh(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        Log.i("DataRefresh", "Constructor");
    }

    @Override
    public Result doWork() {
        // Do the work here--in this case, upload the images.
        Log.i("DataRefresh", "doWork");
        AppPreferences appPrefs = EconApplication.getAppPreferences();
        appPrefs.getForecast();

        // Indicate whether the task finished successfully with the Result
        return ListenableWorker.Result.success();
    }

    public void StartService() {
        Constraints constraints = new Constraints.Builder().setRequiresCharging(false).build();
        PeriodicWorkRequest saveRequest =  new PeriodicWorkRequest.Builder(com.bigtech.econ.DataRefresh.class, 1, TimeUnit.HOURS).setConstraints(constraints).build();

        WorkManager.getInstance(super.getApplicationContext()).enqueue(saveRequest);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) super.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.i("isMyServiceRunning", serviceClass.getName());

            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

