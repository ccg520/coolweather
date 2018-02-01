package com.study.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 18-2-1.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    //降水量
    @SerializedName("pcpn")
    public String precipitation;

    //相对湿度
    @SerializedName("hum")
    public String relativeHumidity;

    //能见度
    @SerializedName("vis")
    public String visibility;

    //大气压强
    @SerializedName("pres")
    public String pressure;

    @SerializedName("cond")
    public More more;

    public class More{

        @SerializedName("txt")
        public String info;
    }



}
