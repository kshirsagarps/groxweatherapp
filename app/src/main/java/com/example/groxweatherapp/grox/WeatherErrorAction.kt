package com.example.groxweatherapp.grox

import com.example.groxweatherapp.api.WeatherApiClient.Companion.ForecastMode
import com.example.groxweatherapp.model.WeatherModel
import com.example.groxweatherapp.model.WeatherModel.Companion.WeatherModelState.ERROR
import com.groupon.grox.Action


class WeatherErrorAction(@ForecastMode private val forecastMode: Int) : Action<WeatherModel> {

    override fun newState(oldState: WeatherModel): WeatherModel {
        return oldState.copy(
                state = ERROR,
                forecastMode = forecastMode,
                weather = emptyList())
    }
}
