package com.example.groxweatherapp.grox;

import com.example.groxweatherapp.api.WeatherApiClient.RequestMode;
import com.example.groxweatherapp.model.WeatherModel;
import com.groupon.grox.Action;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.ERROR;

public class WeatherErrorAction implements Action<WeatherModel> {

    @RequestMode private final int requestMode;

    public WeatherErrorAction(Throwable throwable, @RequestMode int requestMode) {
        this.requestMode = requestMode;
    }

    @Override
    public WeatherModel newState(WeatherModel oldState) {
        return oldState.toBuilder()
            .setState(ERROR)
            .setRequestMode(requestMode)
            .setWeather(Collections.unmodifiableList(new ArrayList<>()))
            .build();
    }
}
