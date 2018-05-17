package com.example.groxweatherapp.presenter;

import com.example.groxweatherapp.api.WeatherApiClient.RequestMode;
import com.example.groxweatherapp.grox.WeatherRequestCommand;
import com.example.groxweatherapp.grox.WeatherStore;
import com.example.groxweatherapp.model.WeatherModel;
import com.example.groxweatherapp.view.HorizontalSelectorView;
import com.groupon.grox.Action;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.example.groxweatherapp.api.WeatherApiClient.FIVE_DAY;
import static com.example.groxweatherapp.api.WeatherApiClient.TEN_DAY;
import static com.example.groxweatherapp.api.WeatherApiClient.TODAY;
import static com.example.groxweatherapp.model.WeatherModel.WeatherModelState.SUCCESS;
import static com.groupon.grox.RxStores.states;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class HorizontalSelectorPresenter {

    private WeatherStore weatherStore;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private HorizontalSelectorView horizontalSelectorView;

    public void attachView(HorizontalSelectorView horizontalSelectorView, WeatherStore weatherStore) {
        this.horizontalSelectorView = horizontalSelectorView;
        this.weatherStore = weatherStore;
        subscriptions.add(processSuccess());
    }

    public void detachView() {
        subscriptions.clear();
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
        return new WeatherRequestCommand(requestMode).actions();
    }

    private Subscription processSuccess() {
        return states(weatherStore)
            .observeOn(mainThread())
            .filter(model -> model.getState() == SUCCESS)
            .subscribe(this::onSuccess, HorizontalSelectorPresenter::onError);
    }

    private static void onError(Throwable throwable) {}
}
