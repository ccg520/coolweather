package com.study.coolweather;


import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by root on 18-1-31.
 */

public class CoolWeatherApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
