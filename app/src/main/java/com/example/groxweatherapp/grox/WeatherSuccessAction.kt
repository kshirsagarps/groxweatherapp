package com.example.groxweatherapp.grox

import com.example.groxweatherapp.api.WeatherApiClient.Companion.ForecastMode
import com.example.groxweatherapp.model.WeatherModel
import com.example.groxweatherapp.model.WeatherModel.Companion.WeatherModelState.SUCCESS
import com.example.groxweatherapp.model.WeatherResponse
import com.groupon.grox.Action

class WeatherSuccessAction(private val weatherResponse: WeatherResponse, @ForecastMode private val forecastMode: Int) : Action<WeatherModel> {

    override fun newState(oldState: WeatherModel): WeatherModel {
        return oldState.copy(
                state = SUCCESS,
                forecastMode = forecastMode,
                weatherList = weatherResponse.weathers)
    }
}
