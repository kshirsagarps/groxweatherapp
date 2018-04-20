
package com.example.groxweatherapp.grox;

import com.example.groxweatherapp.model.WeatherModel;
import com.groupon.grox.Store;

public class WeatherStore extends Store<WeatherModel> {

    public WeatherStore(WeatherModel weatherModel) {
        super(weatherModel);
    }
}