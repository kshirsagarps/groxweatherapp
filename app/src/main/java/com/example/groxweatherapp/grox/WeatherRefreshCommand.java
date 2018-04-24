package com.example.groxweatherapp.grox;

import com.example.groxweatherapp.api.WeatherApiClient;
import com.example.groxweatherapp.model.WeatherModel;
import com.groupon.grox.commands.rxjava1.SingleActionCommand;

import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.REFRESH;

public class WeatherRefreshCommand extends SingleActionCommand<WeatherModel> {
    private int requestMode;

    public WeatherRefreshCommand(@WeatherApiClient.RequestMode int requestMode) {
        this.requestMode = requestMode;
    }

    @Override
    public WeatherModel newState(WeatherModel oldState) {
        return oldState.toBuilder()
                .setModelState(REFRESH)
                .setRequestMode(requestMode)
                .build();
    }
}
