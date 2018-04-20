package com.example.groxweatherapp.model;

import com.example.groxweatherapp.api.WeatherApiClient.RequestMode;
import java.util.List;

public class WeatherResponse {
    @RequestMode public int requestMode;
    public List<Weather> weathers;
}
