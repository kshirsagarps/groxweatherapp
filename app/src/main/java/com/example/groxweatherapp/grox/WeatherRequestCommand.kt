package com.example.groxweatherapp.grox

import com.example.groxweatherapp.api.WeatherApiClient
import com.example.groxweatherapp.api.WeatherApiClient.Companion.RequestMode
import com.example.groxweatherapp.model.WeatherModel
import com.groupon.grox.Action
import com.groupon.grox.commands.rxjava1.Command
import rx.Observable
import rx.schedulers.Schedulers

class WeatherRequestCommand(@RequestMode private var requestMode: Int) : Command {

    override fun actions(): Observable<Action<WeatherModel>> {
        return WeatherApiClient()
                .getWeather(requestMode)
                .subscribeOn(Schedulers.io())
                .map { weatherResponse -> WeatherSuccessAction(weatherResponse, requestMode) as Action<WeatherModel>}
                .onErrorReturn { WeatherErrorAction(requestMode) }
                .startWith(WeatherRefreshAction())
    }
}
