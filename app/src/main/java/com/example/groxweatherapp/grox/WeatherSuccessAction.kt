package com.example.groxweatherapp.grox

import com.example.groxweatherapp.api.WeatherApiClient.Companion.RequestMode
import com.example.groxweatherapp.model.WeatherModel
import com.example.groxweatherapp.model.WeatherModel.Companion.WeatherModelState.SUCCESS
import com.example.groxweatherapp.model.WeatherResponse
import com.groupon.grox.Action

class WeatherSuccessAction(private val weatherResponse: WeatherResponse, @RequestMode private val requestMode: Int) : Action<WeatherModel> {

    override fun newState(oldState: WeatherModel): WeatherModel {
        return oldState.copy(
                state = SUCCESS,
                requestMode = requestMode,
                weather = weatherResponse.weathers)
    }
}
