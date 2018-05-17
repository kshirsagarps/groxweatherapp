package com.example.groxweatherapp.model;

import com.example.groxweatherapp.api.WeatherApiClient.RequestMode;
import com.google.auto.value.AutoValue;
import java.util.ArrayList;
import java.util.List;

import static com.example.groxweatherapp.api.WeatherApiClient.TODAY;
import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.INITIATED;

@AutoValue
public abstract class WeatherModel {

    public static WeatherModel INITIAL_STATE = WeatherModel.builder().build();

    public enum WeatherModelState {
        INITIATED, REFRESHING, SUCCESS, ERROR
    }

    @RequestMode
    public abstract int getRequestMode();

    public abstract WeatherModelState getState();

    public abstract List<Weather> getWeather();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_WeatherModel.Builder()
            .setState(INITIATED)
            .setRequestMode(TODAY)
            .setWeather(new ArrayList<>());
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setState(WeatherModelState modelState);

        public abstract Builder setWeather(List<Weather> weathers);

        public abstract Builder setRequestMode(@RequestMode int requestMode);

        public abstract WeatherModel build();
    }
}
