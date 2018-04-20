package com.example.groxweatherapp.presenter;

import com.example.groxweatherapp.api.WeatherApiClient.RequestMode;
import com.example.groxweatherapp.grox.WeatherStore;
import com.example.groxweatherapp.grox.WeatherRefreshCommand;
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

    private WeatherStore weatherStore;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private HorizontalSelectorView horizontalSelectorView;

    public void attachPresenter(HorizontalSelectorView horizontalSelectorView, WeatherStore weatherStore) {
        this.horizontalSelectorView = horizontalSelectorView;
        this.weatherStore = weatherStore;
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
        subscriptions.add(createRequestCommandActions(TODAY).subscribe(weatherStore::dispatch, HorizontalSelectorPresenter::onError));
    }

    public void onFiveDayClicked() {
        horizontalSelectorView.onFiveDaySelected();
        subscriptions.add(createRequestCommandActions(FIVE_DAY).subscribe(weatherStore::dispatch, HorizontalSelectorPresenter::onError));
    }

    public void onTenDayClicked() {
        horizontalSelectorView.onTenDaySelected();
        subscriptions.add(createRequestCommandActions(TEN_DAY).subscribe(weatherStore::dispatch, HorizontalSelectorPresenter::onError));
    }

    private Observable<? extends Action> createRequestCommandActions(@RequestMode int requestMode) {
        return new WeatherRefreshCommand(requestMode).actions();
    }

    private Observable<WeatherModel> isSuccess() {
        return states(weatherStore)
            .observeOn(mainThread())
            .filter(model -> model.getModelState() == WeatherModel.WeatherModelState.SUCCESS);
    }

    private static void onError(Throwable throwable) {}
}
