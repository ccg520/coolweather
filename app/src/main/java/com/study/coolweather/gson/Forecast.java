package com.study.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 18-2-1.
 */

public class Forecast {

    @SerializedName("date")
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{

        @SerializedName("max")
        public String max;

        @SerializedName("min")
        public String min;
    }

    public class More{

        @SerializedName("txt_d")
        public String info;
    }

}
