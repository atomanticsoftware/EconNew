package com.bigtech.econ.model;

import android.util.Log;

import com.bigtech.econ.api.model.ApiForecastResponse;
import com.bigtech.econ.error.MappingError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ForecastMapper {

    public ForecastModel map(ApiForecastResponse apiForecastResponse) {
        Log.i("ForecastMapper", "map");
        if (apiForecastResponse == null) {
            throw new MappingError("apiForecastResponse is null");
        }
        if (apiForecastResponse.ForecastList.isEmpty()) {
            throw new MappingError("ForecastList is empty");
        }

        Float week = 0f;
        Float month = 0f;
        Float quarter = 0f;

        for (int i = 0; i < apiForecastResponse.ForecastList.size(); i++) {
            Float value = apiForecastResponse.ForecastList.get(i).Value;
            String scope = apiForecastResponse.ForecastList.get(i).Scope;
            if(scope.equalsIgnoreCase("week")) {week = value;}
            if(scope.equalsIgnoreCase("month")) {month = value;}
            if(scope.equalsIgnoreCase("quarter")) {quarter = value;}
        }

        return new ForecastModel(apiForecastResponse.MaxReportDate, apiForecastResponse.RunDate, week, month, quarter );
    }
}
