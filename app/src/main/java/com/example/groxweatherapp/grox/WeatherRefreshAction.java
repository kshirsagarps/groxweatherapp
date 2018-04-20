package com.example.groxweatherapp.grox;

import com.example.groxweatherapp.model.WeatherModel;
import com.groupon.grox.Action;

import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.REFRESHING;

public class WeatherRefreshAction implements Action<WeatherModel> {

    public WeatherRefreshAction() {
        // no ops
    }

    @Override
    public WeatherModel newState(WeatherModel oldState) {
        return oldState.toBuilder()
            .setModelState(REFRESHING)
            .build();
    }
}
