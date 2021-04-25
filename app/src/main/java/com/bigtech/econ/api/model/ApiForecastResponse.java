package com.bigtech.econ.api.model;

import androidx.annotation.Keep;

import java.util.Date;
import java.util.List;

@Keep
public class ApiForecastResponse {
    public List<ApiForecast> ForecastList;
    public Date MaxReportDate;
    public Date RunDate;
}
