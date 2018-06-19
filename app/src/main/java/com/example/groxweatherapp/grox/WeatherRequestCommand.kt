package com.example.groxweatherapp.grox

import com.example.groxweatherapp.api.WeatherApiClient
import com.example.groxweatherapp.api.WeatherApiClient.Companion.ForecastMode
import com.example.groxweatherapp.model.WeatherModel
import com.groupon.grox.Action
import com.groupon.grox.commands.rxjava1.Command
import rx.Observable
import rx.schedulers.Schedulers

class WeatherRequestCommand(@ForecastMode private var forecastMode: Int) : Command {

    override fun actions(): Observable<Action<WeatherModel>> {
        return WeatherApiClient()
                .getWeather(forecastMode)
                .subscribeOn(Schedulers.io())
                .map { weatherResponse -> WeatherSuccessAction(weatherResponse, forecastMode) as Action<WeatherModel>}
                .onErrorReturn { WeatherErrorAction(forecastMode) }
                .startWith(WeatherRefreshAction())
    }
}
