package com.study.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 18-2-1.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    @SerializedName("update")
    public Update update;

    public class Update{

        //当地时间
        @SerializedName("loc")
        public String updateTime;
    }

}
