package com.study.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.study.coolweather.db.City;
import com.study.coolweather.db.County;
import com.study.coolweather.db.Province;
import com.study.coolweather.gson.Weather;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 18-2-1.
 */

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces = new JSONArray(response);
                Log.d("handleProvinceResponse", "data: "+allProvinces.toString());
                for (int i=0;i<allProvinces.length();i++){
                    JSONObject jsonObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                Log.d("handleCityResponse", "data: "+allCities.toString());
                for (int i=0;i<allCities.length();i++){
                    JSONObject jsonObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理返回的县级数据
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountryResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                Log.d("handleCountryResponse", "data: "+allCounties.toString());
                for (int i=0;i<allCounties.length();i++){
                    JSONObject jsonObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountryName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
