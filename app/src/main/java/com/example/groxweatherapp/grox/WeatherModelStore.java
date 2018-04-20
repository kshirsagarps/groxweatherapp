
package com.example.groxweatherapp.grox;

import com.example.groxweatherapp.model.WeatherModel;
import com.groupon.grox.Store;

public class WeatherModelStore extends Store<WeatherModel> {

    public WeatherModelStore(WeatherModel weatherModel) {
        super(weatherModel);
    }
}