package com.example.groxweatherapp.presenter;

import com.example.groxweatherapp.grox.WeatherRequestCommand;
import com.example.groxweatherapp.grox.WeatherStore;
import com.example.groxweatherapp.model.WeatherModel;
import com.example.groxweatherapp.view.WeatherMainView;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.example.groxweatherapp.api.WeatherApiClient.TODAY;
import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.ERROR;
import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.INITIATED;
import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.REFRESHING;
import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.SUCCESS;
import static com.groupon.grox.RxStores.states;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class WeatherMainPresenter {

    private WeatherStore weatherStore;
    private WeatherMainView weatherMainView;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    public void attachView(WeatherMainView weatherMainView, WeatherStore weatherStore) {
        this.weatherMainView = weatherMainView;
        this.weatherStore = weatherStore;
        subscriptions.add(processInitiated());
        subscriptions.add(processRefreshing());
        subscriptions.add(processSuccess());
        subscriptions.add(processError());
    }

    public void detachView() {
        subscriptions.clear();
    }

    public void onRetryClick() {
        subscriptions.add(
            new WeatherRequestCommand(weatherStore.getState().getRequestMode())
                .actions()
                .subscribe(weatherStore::dispatch, WeatherMainPresenter::onError));
    }

    private Subscription processInitiated() {
        return states(weatherStore)
            .observeOn(mainThread())
            .filter(model -> model.getState() == INITIATED)
            .map(model -> new WeatherRequestCommand(TODAY))
            .flatMap(weatherCommand -> weatherCommand.actions())
            .subscribe(weatherStore::dispatch, WeatherMainPresenter::onError);
    }

    private Subscription processRefreshing() {
        return states(weatherStore)
            .observeOn(mainThread())
            .filter(model -> model.getState() == REFRESHING)
            .subscribe(ignore -> onRefresh(), WeatherMainPresenter::onError);
    }

    private Subscription processSuccess() {
        return states(weatherStore)
            .observeOn(mainThread())
            .filter(model -> model.getState() == SUCCESS)
            .subscribe(this::onSuccess, WeatherMainPresenter::onError);
    }

    private Subscription processError() {
        return states(weatherStore)
            .observeOn(mainThread())
            .filter(model -> model.getState() == ERROR)
            .subscribe(this::onError, WeatherMainPresenter::onError);
    }

    private void onRefresh() {
        weatherMainView.showSpinner();
        weatherMainView.hideList();
    }

    private void onSuccess(WeatherModel weatherModel) {
        weatherMainView.hideSpinner();
        weatherMainView.showList();
        weatherMainView.showWeather(weatherModel.getWeather());
    }

    private void onError(WeatherModel weatherModel) {
        weatherMainView.hideSpinner();
        weatherMainView.showList();
        weatherMainView.showWeather(weatherModel.getWeather());
        weatherMainView.showErrorDialog();
    }

    private static void onError(Throwable throwable) {
        // no ops
    }
}
