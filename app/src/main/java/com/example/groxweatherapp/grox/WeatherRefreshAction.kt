package com.example.groxweatherapp.grox

import com.example.groxweatherapp.model.WeatherModel
import com.example.groxweatherapp.model.WeatherModel.Companion.WeatherModelState.REFRESHING
import com.groupon.grox.Action

class WeatherRefreshAction : Action<WeatherModel> {

    override fun newState(oldState: WeatherModel): WeatherModel {
        return oldState.copy(state = REFRESHING)
    }
}
