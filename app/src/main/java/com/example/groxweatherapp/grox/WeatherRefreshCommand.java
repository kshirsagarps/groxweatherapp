package com.example.groxweatherapp.grox;

import com.example.groxweatherapp.api.WeatherApiClient.RequestMode;
import com.example.groxweatherapp.model.WeatherModel;
import com.groupon.grox.commands.rxjava1.SingleActionCommand;

import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.REFRESH;

public class WeatherRefreshCommand extends SingleActionCommand<WeatherModel> {

    @RequestMode private final int requestMode;

    public WeatherRefreshCommand(@RequestMode int requestMode) {
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
