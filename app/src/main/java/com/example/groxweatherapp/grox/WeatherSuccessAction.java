package com.example.groxweatherapp.grox;

import com.example.groxweatherapp.api.WeatherApiClient.RequestMode;
import com.example.groxweatherapp.model.WeatherModel;
import com.example.groxweatherapp.model.WeatherResponse;
import com.groupon.grox.Action;

import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.SUCCESS;

public class WeatherSuccessAction implements Action<WeatherModel> {

    private final WeatherResponse weatherResponse;
    @RequestMode private final int requestMode;

    public WeatherSuccessAction(WeatherResponse weatherResponse, @RequestMode int requestMode) {
        this.weatherResponse = weatherResponse;
        this.requestMode = requestMode;
    }

    @Override
    public WeatherModel newState(WeatherModel oldState) {
        return oldState.toBuilder()
            .setState(SUCCESS)
            .setRequestMode(requestMode)
            .setWeather(weatherResponse.weathers)
            .build();
    }
}
