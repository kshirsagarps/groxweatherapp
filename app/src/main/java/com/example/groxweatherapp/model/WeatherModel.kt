package com.example.groxweatherapp.model

import com.example.groxweatherapp.api.WeatherApiClient.Companion.RequestMode
import com.example.groxweatherapp.api.WeatherApiClient.Companion.TODAY
import com.example.groxweatherapp.model.WeatherModel.Companion.WeatherModelState.INITIATED


data class WeatherModel(@RequestMode val requestMode: Int, val state: WeatherModelState, val weather: List<Weather>) {
    companion object {
        enum class WeatherModelState {
            INITIATED, REFRESHING, SUCCESS, ERROR
        }

        val INITIAL_STATE = WeatherModel(TODAY, INITIATED, emptyList())
    }
}
