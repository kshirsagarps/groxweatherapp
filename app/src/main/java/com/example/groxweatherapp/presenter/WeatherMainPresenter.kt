package com.example.groxweatherapp.presenter

import com.example.groxweatherapp.api.WeatherApiClient.Companion.TODAY
import com.example.groxweatherapp.grox.WeatherRequestCommand
import com.example.groxweatherapp.grox.WeatherStore
import com.example.groxweatherapp.model.WeatherModel
import com.example.groxweatherapp.model.WeatherModel.Companion.WeatherModelState.*
import com.example.groxweatherapp.view.WeatherMainView
import com.groupon.grox.RxStores.states
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subscriptions.CompositeSubscription

class WeatherMainPresenter {

    private lateinit var weatherStore: WeatherStore
    private lateinit var weatherMainView: WeatherMainView
    private val subscriptions = CompositeSubscription()

    fun attachView(weatherMainView: WeatherMainView, weatherStore: WeatherStore) {
        this.weatherMainView = weatherMainView
        this.weatherStore = weatherStore
        subscriptions.add(processInitiated())
        subscriptions.add(processRefreshing())
        subscriptions.add(processSuccess())
        subscriptions.add(processError())
    }

    fun detachView() {
        subscriptions.clear()
    }

    fun onRetryClick() {
        subscriptions.add(
                WeatherRequestCommand(weatherStore.state.forecastMode)
                        .actions()
                        .subscribe(weatherStore::dispatch, this::onError))
    }

    private fun processInitiated(): Subscription {
        return states(weatherStore)
                .observeOn(mainThread())
                .filter { it.state == INITIATED }
                .map { WeatherRequestCommand(TODAY) }
                .flatMap(WeatherRequestCommand::actions)
                .subscribe(weatherStore::dispatch, this::onError)
    }

    private fun processRefreshing(): Subscription {
        return states(weatherStore)
                .observeOn(mainThread())
                .filter { it.state == REFRESHING }
                .subscribe({ onRefresh() }, this::onError)
    }

    private fun processSuccess(): Subscription {
        return states(weatherStore)
                .observeOn(mainThread())
                .filter { it.state == SUCCESS }
                .subscribe(this::onSuccess, this::onError)
    }

    private fun processError(): Subscription {
        return states(weatherStore)
                .observeOn(mainThread())
                .filter { it.state == ERROR }
                .subscribe(this::onError, this::onError)
    }

    private fun onRefresh() {
        weatherMainView.showSpinner()
        weatherMainView.hideList()
    }

    private fun onSuccess(weatherModel: WeatherModel) {
        weatherMainView.hideSpinner()
        weatherMainView.showList()
        weatherMainView.showWeather(weatherModel.weather)
    }

    private fun onError(weatherModel: WeatherModel) {
        weatherMainView.hideSpinner()
        weatherMainView.showList()
        weatherMainView.showWeather(weatherModel.weather)
        weatherMainView.showErrorDialog()
    }

    private fun onError(throwable: Throwable) {
        // no ops
    }
}
