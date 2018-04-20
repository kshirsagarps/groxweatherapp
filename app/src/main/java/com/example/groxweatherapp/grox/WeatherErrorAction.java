package com.example.groxweatherapp.grox;

import com.example.groxweatherapp.model.WeatherModel;
import com.example.groxweatherapp.model.WeatherResponse;
import com.groupon.grox.Action;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.ERROR;

public class WeatherErrorAction implements Action<WeatherModel> {

    public WeatherErrorAction(Throwable throwable) {
        // no ops
    }

    @Override
    public WeatherModel newState(WeatherModel oldState) {
        return oldState.toBuilder()
            .setModelState(ERROR)
            .setWeather(Collections.unmodifiableList(new ArrayList<>()))
            .build();
    }
}
