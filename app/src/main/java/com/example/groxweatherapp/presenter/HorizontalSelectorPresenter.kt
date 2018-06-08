package com.example.groxweatherapp.presenter

import com.example.groxweatherapp.api.WeatherApiClient.Companion.FIVE_DAY
import com.example.groxweatherapp.api.WeatherApiClient.Companion.RequestMode
import com.example.groxweatherapp.api.WeatherApiClient.Companion.TEN_DAY
import com.example.groxweatherapp.api.WeatherApiClient.Companion.TODAY
import com.example.groxweatherapp.grox.WeatherRequestCommand
import com.example.groxweatherapp.grox.WeatherStore
import com.example.groxweatherapp.model.WeatherModel
import com.example.groxweatherapp.model.WeatherModel.Companion.WeatherModelState.SUCCESS
import com.example.groxweatherapp.view.HorizontalSelectorView
import com.groupon.grox.Action
import com.groupon.grox.RxStores.states
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subscriptions.CompositeSubscription

class HorizontalSelectorPresenter {

    private lateinit var weatherStore: WeatherStore
    private lateinit var horizontalSelectorView: HorizontalSelectorView
    private val subscriptions = CompositeSubscription()

    fun attachView(horizontalSelectorView: HorizontalSelectorView, weatherStore: WeatherStore) {
        this.horizontalSelectorView = horizontalSelectorView
        this.weatherStore = weatherStore
        subscriptions.add(processSuccess())
    }

    fun detachView() {
        subscriptions.clear()
    }

    private fun onSuccess(weatherModel: WeatherModel) {
        when (weatherModel.requestMode) {
            TODAY -> horizontalSelectorView.onTodaySelected()
            FIVE_DAY -> horizontalSelectorView.onFiveDaySelected()
            TEN_DAY -> horizontalSelectorView.onTenDaySelected()
        }
    }

    fun onTodayClicked() {
        horizontalSelectorView.onTodaySelected()
        subscriptions.add(createRequestCommandActions(TODAY).subscribe(weatherStore::dispatch, this::onError))
    }

    fun onFiveDayClicked() {
        horizontalSelectorView.onFiveDaySelected()
        subscriptions.add(createRequestCommandActions(FIVE_DAY).subscribe(weatherStore::dispatch, this::onError))
    }

    fun onTenDayClicked() {
        horizontalSelectorView.onTenDaySelected()
        subscriptions.add(createRequestCommandActions(TEN_DAY).subscribe(weatherStore::dispatch, this::onError))
    }

    private fun createRequestCommandActions(@RequestMode requestMode: Int): Observable<out Action<WeatherModel>> {
        return WeatherRequestCommand(requestMode).actions()
    }

    private fun processSuccess(): Subscription {
        return states(weatherStore)
                .observeOn(mainThread())
                .filter { it.state == SUCCESS }
                .subscribe(this::onSuccess, this::onError)
    }

    private fun onError(throwable: Throwable) {
        // no ops
    }
}
