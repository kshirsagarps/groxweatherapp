package com.example.groxweatherapp.grox;

import com.example.groxweatherapp.model.WeatherModel;
import com.example.groxweatherapp.model.WeatherResponse;
import com.groupon.grox.Action;

import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.SUCCESS;

public class WeatherSuccessAction implements Action<WeatherModel> {

    private final WeatherResponse weatherResponse;

    public WeatherSuccessAction(WeatherResponse weatherResponse) {
        this.weatherResponse = weatherResponse;
    }

    @Override
    public WeatherModel newState(WeatherModel oldState) {
        return oldState.toBuilder()
            .setModelState(SUCCESS)
            .setWeather(weatherResponse.weathers)
            .build();
    }
}
