package com.example.groxweatherapp.view;

import com.example.groxweatherapp.model.Weather;
import java.util.List;

public interface WeatherMainView {

    void showSpinner();

    void hideSpinner();

    void showList();

    void hideList();

    void showWeather(List<Weather> weatherList);

    void showErrorDialog();
}
