package com.example.groxweatherapp.grox

import com.example.groxweatherapp.api.WeatherApiClient.Companion.RequestMode
import com.example.groxweatherapp.model.WeatherModel
import com.example.groxweatherapp.model.WeatherModel.Companion.WeatherModelState.ERROR
import com.groupon.grox.Action


class WeatherErrorAction(@RequestMode private val requestMode: Int) : Action<WeatherModel> {

    override fun newState(oldState: WeatherModel): WeatherModel {
        return oldState.copy(
                state = ERROR,
                requestMode = requestMode,
                weather = emptyList())
    }
}
