package com.example.groxweatherapp.grox;

import com.example.groxweatherapp.api.WeatherApiClient;
import com.example.groxweatherapp.api.WeatherApiClient.RequestMode;
import com.groupon.grox.Action;
import com.groupon.grox.commands.rxjava1.Command;
import rx.Observable;
import rx.schedulers.Schedulers;

public class WeatherRequestCommand implements Command {

    private final int requestMode;

    public WeatherRequestCommand(@RequestMode int requestMode) {
        this.requestMode = requestMode;
    }

    @Override
    public Observable<Action> actions() {
        return new WeatherApiClient()
            .getWeather(requestMode)
            .subscribeOn(Schedulers.io())
            .map(weatherResponse -> new WeatherSuccessAction(weatherResponse, requestMode))
            .cast(Action.class)
            .onErrorReturn(throwable -> new WeatherErrorAction(throwable, requestMode))
            .startWith(new WeatherRefreshAction());
    }
}
