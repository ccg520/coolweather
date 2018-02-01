package com.study.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 18-2-1.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    @SerializedName("sport")
    public Sport sport;


    public class Comfort{
        //舒适度
        @SerializedName("brf")
        public String extens;

        @SerializedName("txt")
        public String info;
    }

    public class CarWash{

        //舒适度
        @SerializedName("brf")
        public String extens;

        @SerializedName("txt")
        public String info;
    }


    public class Sport{

        //舒适度
        @SerializedName("brf")
        public String extens;

        @SerializedName("txt")
        public String info;

    }

}
