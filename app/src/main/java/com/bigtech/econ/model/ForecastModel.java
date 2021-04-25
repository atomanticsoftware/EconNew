package com.bigtech.econ.model;

import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bigtech.econ.R;

import java.util.Date;

public class ForecastModel {
    private Forecast forecast;
    private float forecastWeek;
    private float forecastMonth;
    private float forecastQuarter;
    private Date maxReportDate;
    private Date runDate;
    private int res;

    public ForecastModel(@NonNull Date maxReportDate, @NonNull Date runDate, @NonNull float forecastWeek, @NonNull float forecastMonth, @NonNull float forecastQuarter) {
        this.maxReportDate = maxReportDate;
        this.runDate = runDate;
        this.forecastWeek = forecastWeek;
        this.forecastMonth = forecastMonth;
        this.forecastQuarter = forecastQuarter;


        this.res = 0;
        if( forecastQuarter < 0 ) { this.res = this.res | 4;}
        if( forecastMonth < 0 ) { this.res = this.res | 2;}
        if( forecastWeek < 0 ) { this.res = this.res | 1;}
        Log.i("ForecastMapper res", Integer.toString(this.res));

        this.forecast = ForecastModel.Forecast.values()[this.res];

    }

    public Forecast getForecast() {
        return forecast;
    }

    public @NonNull
    Date getMaxReportDate() {
        return maxReportDate;
    }

    public @NonNull
    Date getRunDate() {
        return runDate;
    }

    public enum Forecast {
        HHH(R.drawable.ic_img_hhh),
        HHL(R.drawable.ic_img_hhl),
        HLH(R.drawable.ic_img_hlh),
        HLL(R.drawable.ic_img_hll),
        LHH(R.drawable.ic_img_lhh),
        LHL(R.drawable.ic_img_lhl),
        LLH(R.drawable.ic_img_llh),
        LLL(R.drawable.ic_img_lll);

        private int imageResId;

        Forecast(@DrawableRes int imageResId) {
            this.imageResId = imageResId;
        }

        public int getImageResId() {
            return imageResId;
        }
    }
}