package com.example.groxweatherapp.presenter;

import com.example.groxweatherapp.api.WeatherApiClient;
import com.example.groxweatherapp.grox.WeatherModelStore;
import com.example.groxweatherapp.grox.WeatherRequestCommand;
import com.example.groxweatherapp.model.WeatherModel;
import com.example.groxweatherapp.view.MainView;
import com.groupon.grox.Action;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;

import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.ERROR;
import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.REFRESH;
import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.SUCCESS;
import static com.groupon.grox.RxStores.states;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class MainPresenter {

    private WeatherModelStore weatherModelStore;
    private MainView mainView;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    public void attachPresenter(MainView mainView, WeatherModelStore weatherModelStore) {
        this.mainView = mainView;
        this.weatherModelStore = weatherModelStore;
        subscriptions.add(isSuccess().subscribe(this::onSuccess, MainPresenter::onError));
        subscriptions.add(isRefreshing().subscribe(weatherModelStore::dispatch, MainPresenter::onError));
        subscriptions.add(isError().subscribe(this::onError, MainPresenter::onError));
    }

    public void detachPresenter() {
        subscriptions.unsubscribe();
    }

    public void onRetryClick() {
        final WeatherModel state = weatherModelStore.getState();
        subscriptions.add(getRefreshCommandObservable(state.getRequestMode()).subscribe(weatherModelStore::dispatch, MainPresenter::onError));
    }

    private Observable<Action> getRefreshCommandObservable(@WeatherApiClient.RequestMode int requestMode) {
        return new WeatherRequestCommand(requestMode).actions();
    }

    private void onRefresh(WeatherModel weatherModel) {
        mainView.showSpinner();
        mainView.hideList();
    }

    private void onSuccess(WeatherModel weatherModel) {
        mainView.hideSpinner();
        mainView.showList();
        mainView.showWeather(weatherModel.getWeather());
    }

    private void onError(WeatherModel weatherModel) {
        mainView.hideSpinner();
        mainView.showList();
        mainView.showWeather(weatherModel.getWeather());
        mainView.showErrorDialog();
    }

    private Observable<WeatherModel> isSuccess() {
        return states(weatherModelStore)
            .observeOn(mainThread())
            .filter(model -> model.getModelState() == SUCCESS);
    }

    private Observable<WeatherModel> isError() {
        return states(weatherModelStore)
            .observeOn(mainThread())
            .filter(model -> model.getModelState() == ERROR);
    }

    private Observable<Action> isRefreshing() {
        return states(weatherModelStore)
            .observeOn(mainThread())
            .filter(model -> model.getModelState() == REFRESH)
            .doOnNext(this::onRefresh)
            .map(model -> new WeatherRequestCommand(weatherModelStore.getState().getRequestMode()))
            .flatMap(weatherCommand -> weatherCommand.actions());
    }

    private static void onError(Throwable throwable) {
        // no ops
    }
}
