package com.study.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 18-2-1.
 */

public class Weather {

    @SerializedName("status")
    public String status;

    @SerializedName("basic")
    public Basic basic;

    @SerializedName("aqi")
    public AQI aqi;

    @SerializedName("now")
    public Now now;

    @SerializedName("suggestion")
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastsList;

}
