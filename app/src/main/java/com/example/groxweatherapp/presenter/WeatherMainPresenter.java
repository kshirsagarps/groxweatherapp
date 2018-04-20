package com.example.groxweatherapp.presenter;

import com.example.groxweatherapp.grox.WeatherStore;
import com.example.groxweatherapp.grox.WeatherRequestCommand;
import com.example.groxweatherapp.model.WeatherModel;
import com.example.groxweatherapp.view.WeatherMainView;
import com.groupon.grox.Action;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.ERROR;
import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.REFRESH;
import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.SUCCESS;
import static com.groupon.grox.RxStores.states;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class WeatherMainPresenter {

    private WeatherStore weatherStore;
    private WeatherMainView weatherMainView;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    public void attachPresenter(WeatherMainView weatherMainView, WeatherStore weatherStore) {
        this.weatherMainView = weatherMainView;
        this.weatherStore = weatherStore;
        subscriptions.add(isRefresh().subscribe(weatherStore::dispatch, WeatherMainPresenter::onError));
        subscriptions.add(isSuccess().subscribe(this::onSuccess, WeatherMainPresenter::onError));
        subscriptions.add(isError().subscribe(this::onError, WeatherMainPresenter::onError));
    }

    public void detachPresenter() {
        subscriptions.unsubscribe();
    }

    public void onRetryClick() {
        subscriptions.add(
            new WeatherRequestCommand(weatherStore.getState().getRequestMode())
                .actions()
                .subscribe(weatherStore::dispatch, WeatherMainPresenter::onError));
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

    private Observable<Action> isRefresh() {
        return states(weatherStore)
            .observeOn(mainThread())
            .filter(model -> model.getModelState() == REFRESH)
            .doOnNext(model -> onRefresh())
            .map(model -> new WeatherRequestCommand(weatherStore.getState().getRequestMode()))
            .flatMap(weatherCommand -> weatherCommand.actions());
    }

    private Observable<WeatherModel> isSuccess() {
        return states(weatherStore)
            .observeOn(mainThread())
            .filter(model -> model.getModelState() == SUCCESS);
    }

    private Observable<WeatherModel> isError() {
        return states(weatherStore)
            .observeOn(mainThread())
            .filter(model -> model.getModelState() == ERROR);
    }

    private static void onError(Throwable throwable) {
        // no ops
    }
}
