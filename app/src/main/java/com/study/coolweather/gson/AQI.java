package com.study.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 18-2-1.
 */

public class AQI {

    @SerializedName("city")
    public AQICity city;

    public class AQICity{

        @SerializedName("aqi")
        public String aqi;

        @SerializedName("pm25")
        public String pm25;

        //重度污染
        @SerializedName("qlty")
        public String highLevelOfPollution;

    }

}
