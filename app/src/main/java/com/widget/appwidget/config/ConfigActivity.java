package com.widget.appwidget.config;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.bigtech.econ.AppPreferences;
import com.bigtech.econ.EconApplication;
import com.bigtech.econ.R;
import com.bigtech.econ.model.ForecastModel;
import com.widget.appwidget.controls.WidgetProvider;
import com.widget.helpers.SessionStore;

import java.util.Objects;


public class ConfigActivity extends AppCompatActivity {

    private int appWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignAppWidgetId();
        setContentView(R.layout.activity_main);

        Log.i("MainActivity", "onCreate");

        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_title);

        progressIndicator = findViewById(R.id.progress_indicator);
        image = findViewById(R.id.image);

        EconApplication.getAppPreferences().registerOnSharedPreferenceChangeListener(prefListener);

        Button dismiss = findViewById(R.id.dismiss);
        dismiss.setOnClickListener(v -> showAppWidget());
    }

    private static final long updateIntervalMillis = 1000000;

    private void assignAppWidgetId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null)
            return;
        appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        initAlarmManager(updateIntervalMillis, appWidgetId);
        if (intent.hasExtra(WidgetProvider.UPDATE_ACTION)) {
            //notifyNature = intent.getIntExtra(WidgetProvider.UPDATE_ACTION,
            //      WidgetProvider.GO_FOR_UPDATE);
        } else {
            showAppWidget();
        }
    }

    private void initAlarmManager(long intervalMillis, int appWidgetId) {
        SessionStore.storeUpdateInterval(this, intervalMillis,
                appWidgetId);
    }

    private void showAppWidget() {
        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    //

    private ProgressBar progressIndicator;
    private ImageView image;


    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            Log.i("MainActivity", "onSharedPreferenceChanged");
            androidx.lifecycle.Lifecycle.State x = getLifecycle().getCurrentState();
            Log.i("MainActivity", x.name());

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                Log.i("MainActivity", "onSharedPreferenceChanged listener RESUMED");
                updateUI();
            }
        }
    };

    @Override
    protected void onResume() {
        Log.i("MainActivity", "onResume");
        super.onResume();
        updateUI();
    }

    @Override
    protected void onDestroy() {
        Log.i("MainActivity", "onDestroy");
        super.onDestroy();
        EconApplication.getAppPreferences().unregisterOnSharedPreferenceChangeListener(prefListener);
    }

    public void updateUI() {
        Log.i("MainActivity", "updateUI");
        AppPreferences appPrefs = EconApplication.getAppPreferences();
        TextView lastUpdatedView = findViewById(R.id.last_updated);
        String lst = appPrefs.getLastUpdated();
        lastUpdatedView.setText(getString(R.string.update_message, lst));
        ForecastModel.Forecast forecast = appPrefs.getForecast();
        if (forecast != null) {
            progressIndicator.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            image.setImageResource(forecast.getImageResId());
            EconApplication.RefreshWidget(forecast.getImageResId());
        } else {
            progressIndicator.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
        }
    }
}
