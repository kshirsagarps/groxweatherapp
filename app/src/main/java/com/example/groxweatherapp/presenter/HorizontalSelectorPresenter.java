package com.example.groxweatherapp.presenter;

import com.example.groxweatherapp.api.WeatherApiClient.RequestMode;
import com.example.groxweatherapp.grox.WeatherModelStore;
import com.example.groxweatherapp.grox.WeatherRequestCommand;
import com.example.groxweatherapp.model.WeatherModel;
import com.example.groxweatherapp.view.HorizontalSelectorView;
import com.groupon.grox.Action;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

import static com.example.groxweatherapp.api.WeatherApiClient.FIVE_DAY;
import static com.example.groxweatherapp.api.WeatherApiClient.TEN_DAY;
import static com.example.groxweatherapp.api.WeatherApiClient.TODAY;
import static com.groupon.grox.RxStores.states;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class HorizontalSelectorPresenter {

    private WeatherModelStore weatherModelStore;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private HorizontalSelectorView horizontalSelectorView;

    public void attachPresenter(HorizontalSelectorView horizontalSelectorView, WeatherModelStore weatherModelStore) {
        this.horizontalSelectorView = horizontalSelectorView;
        this.weatherModelStore = weatherModelStore;
        subscriptions.add(isSuccess().subscribe(this::onSuccess, HorizontalSelectorPresenter::onError));
    }

    private void onSuccess(WeatherModel weatherModel) {
        switch (weatherModel.getRequestMode()) {
            case TODAY:
                horizontalSelectorView.onTodaySelected();
                break;
            case FIVE_DAY:
                horizontalSelectorView.onFiveDaySelected();
                break;
            case TEN_DAY:
                horizontalSelectorView.onTenDaySelected();
                break;
        }
    }

    public void detachPresenter() {
        subscriptions.unsubscribe();
    }

    public void onTodayClicked() {
        horizontalSelectorView.onTodaySelected();
        subscriptions.add(createRefreshCommandAction(TODAY).subscribe(weatherModelStore::dispatch, HorizontalSelectorPresenter::onError));
    }

    public void onFiveDayClicked() {
        horizontalSelectorView.onFiveDaySelected();
        subscriptions.add(createRefreshCommandAction(FIVE_DAY).subscribe(weatherModelStore::dispatch, HorizontalSelectorPresenter::onError));
    }

    public void onTenDayClicked() {
        horizontalSelectorView.onTenDaySelected();
        subscriptions.add(createRefreshCommandAction(TEN_DAY).cache().subscribe(weatherModelStore::dispatch, HorizontalSelectorPresenter::onError));
    }

    private Observable<Action> createRefreshCommandAction(@RequestMode int requestMode) {
        final WeatherRequestCommand weatherRequestCommand = new WeatherRequestCommand(requestMode);
        return weatherRequestCommand.actions();
    }

    private Observable<WeatherModel> isSuccess() {
        return states(weatherModelStore)
            .observeOn(mainThread())
            .filter(model -> model.getModelState() == WeatherModel.WeatherModelState.SUCCESS);
    }

    private static void onError(Throwable throwable) {}
}
