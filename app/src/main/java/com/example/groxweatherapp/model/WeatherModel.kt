package com.example.groxweatherapp.model

import com.example.groxweatherapp.api.WeatherApiClient.Companion.ForecastMode
import com.example.groxweatherapp.api.WeatherApiClient.Companion.TODAY
import com.example.groxweatherapp.model.WeatherModel.Companion.WeatherModelState.INITIATED


data class WeatherModel(@ForecastMode val forecastMode: Int, val state: WeatherModelState, val weather: List<Weather>) {
    companion object {
        enum class WeatherModelState {
            INITIATED, REFRESHING, SUCCESS, ERROR
        }

        val INITIAL_STATE = WeatherModel(TODAY, INITIATED, emptyList())
    }
}
