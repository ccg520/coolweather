package com.study.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.study.coolweather.gson.Forecast;
import com.study.coolweather.gson.Weather;
import com.study.coolweather.service.AutoUpdateService;
import com.study.coolweather.util.HttpResource;
import com.study.coolweather.util.HttpUtil;
import com.study.coolweather.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {


    public SwipeRefreshLayout swipeRefreshLayout;

    private Button navBtn;

    public DrawerLayout drawerLayout;

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView biyingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        swipeRefreshLayout = findViewById(R.id.id_swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = findViewById(R.id.id_drawerLayout);
        navBtn = findViewById(R.id.id_nav_button);
        weatherLayout = findViewById(R.id.id_weather_layout);
        titleCity = findViewById(R.id.id_title_city);
        titleUpdateTime = findViewById(R.id.id_title_update_time);
        degreeText = findViewById(R.id.id_degree_text);
        weatherInfoText = findViewById(R.id.id_weather_info_text);
        forecastLayout = findViewById(R.id.id_forecast_layout);
        aqiText = findViewById(R.id.id_aqi_text);
        pm25Text = findViewById(R.id.id_pm25_text);
        comfortText = findViewById(R.id.id_comfort_text);
        carWashText = findViewById(R.id.id_car_wash_text);
        sportText = findViewById(R.id.id_sport_text);
        biyingPicImg = findViewById(R.id.id_biying_pic_img);

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString(HttpResource.KEY_WEATHER,null);
        final String weatherId;
        if (weatherString != null){
            //缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{
            //无缓存时去服务器查询天气
            weatherId = getIntent().getStringExtra(HttpResource.KEY_WEATHER_ID);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        //刷新天气数据
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });

        String biyingPic = preferences.getString(HttpResource.KEY_BINGPIC,null);
        if (biyingPic != null){
            Glide.with(this).load(biyingPic).into(biyingPicImg);
        }else{
            loadBiYingImg();
        }

    }

    /**
     * 加载必应每日一图
     */
    private void loadBiYingImg() {
        HttpUtil.sendOkHttpRequest(HttpResource.BIYINGPICURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String biyingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this)
                        .edit();
                editor.putString(HttpResource.KEY_BINGPIC,biyingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(biyingPic).into(biyingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 根据天气id获取城市天气信息
     * @param weatherId
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+
                "&key="+ HttpResource.KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败"
                                ,Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 final String responseText = response.body().string();
                 final Weather weather = Utility.handleWeatherResponse(responseText);
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         if (weather != null && "ok".equals(weather.status)){
                             SharedPreferences.Editor editor = PreferenceManager
                                     .getDefaultSharedPreferences(WeatherActivity.this)
                                     .edit();
                             editor.putString(HttpResource.KEY_WEATHER,responseText);
                             editor.apply();//提交
                             showWeatherInfo(weather);
                         }else{
                             Toast.makeText(WeatherActivity.this,"获取天气信息失败"
                                     ,Toast.LENGTH_SHORT).show();
                         }
                         swipeRefreshLayout.setRefreshing(false);
                     }
                 });
            }
        });
        loadBiYingImg();
    }

    /**
     * 处理并展示weather实体类的数据
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        if (weather != null && "ok".equals(weather.status)){
            String cityName = weather.basic.cityName;
            String updateTime = weather.basic.update.updateTime.split(" ")[1];
            String degree = weather.now.temperature+"℃";
            String weatherInfo = weather.now.more.info;
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherInfo);

            forecastLayout.removeAllViews();
            for (Forecast forecast : weather.forecastsList){
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
                TextView dateText = view.findViewById(R.id.id_date_text);
                TextView infoText = view.findViewById(R.id.id_info_text);
                TextView maxText = view.findViewById(R.id.id_max_text);
                TextView minText = view.findViewById(R.id.id_min_text);
                dateText.setText(forecast.date);
                infoText.setText(forecast.more.info);
                maxText.setText(forecast.temperature.max);
                minText.setText(forecast.temperature.min);
                forecastLayout.addView(view);
            }

            if (weather.aqi != null){
                aqiText.setText(weather.aqi.city.aqi);
                pm25Text.setText(weather.aqi.city.pm25);
            }

            String comfort = "舒适度："+weather.suggestion.comfort.info;
            String carWash = "洗车指数；"+weather.suggestion.carWash.info;
            String sport = "运动建议："+weather.suggestion.sport.info;
            comfortText.setText(comfort);
            carWashText.setText(carWash);
            sportText.setText(sport);

            weatherLayout.setVisibility(View.VISIBLE);
            //开启后台定时服务
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        }else{
            Toast.makeText(this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }

    }


}
