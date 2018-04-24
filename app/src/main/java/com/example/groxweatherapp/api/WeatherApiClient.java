package com.example.groxweatherapp.api;

import android.support.annotation.IntDef;
import com.example.groxweatherapp.model.Weather;
import com.example.groxweatherapp.model.WeatherResponse;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import rx.Observable;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class WeatherApiClient {
    private static final int SEED = 7;
    private static final int ERROR_RATE = 5;
    public static final int LATENCY_IN_MS = 1000;
    private static Random random = new Random(SEED);
    private static final String ERROR_MESSAGE = "Client Exception";

    private static final String DAYS[] = {
        "TODAY", "SUN", "MON", "TUES", "THURS", "FRI", "SAT"
    };
    private static final String DESCRIPTION[] = {
        "SUNNY", "OVERCAST", "CLOUDY", "PARTLY SUNNY", "PARTLY CLOUD"
    };
    private static final String DATES[] = {
        "MAR 1", "MAR 2", "MAR 3", "MAR 4", "MAR 5", "MAR 6", "MAR 7", "MAR 8", "MAR 9", "MAR 10"
    };

    public static int MAX = 100;
    public static int MIN = 40;

    @Retention(SOURCE)
    @IntDef({TODAY, FIVE_DAY, TEN_DAY})
    public @interface RequestMode {}

    public static final int TODAY = 0;
    public static final int FIVE_DAY = 1;
    public static final int TEN_DAY = 2;

    public Observable<WeatherResponse> getWeather(@RequestMode int requestMode) {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.weathers = createWeatherForRequest(requestMode);
        Observable<WeatherResponse> observable = Observable.just(weatherResponse);
        if (random.nextInt() % ERROR_RATE == 0) {
            observable = Observable.error(new RuntimeException(ERROR_MESSAGE));
        }

        return observable.delay(LATENCY_IN_MS, TimeUnit.MILLISECONDS);
    }

    private List<Weather> createWeatherForRequest(@RequestMode int requestMode) {
        final List<Weather> weatherList = new ArrayList<>();

        switch (requestMode) {
            case TODAY:
                return createAndAddWeatherToList(1, weatherList);
            case FIVE_DAY:
                return createAndAddWeatherToList(5, weatherList);
            case TEN_DAY:
                return createAndAddWeatherToList(10, weatherList);
        }

        return weatherList;
    }

    private List<Weather> createAndAddWeatherToList(int numOfDays, List<Weather> weatherList) {
        for (int i = 0; i < numOfDays; i++) {
            weatherList.add(createWeather(DAYS[i % (DAYS.length - 1)] + " " + DATES[i]));
        }
        return weatherList;
    }

    private Weather createWeather(String date) {
        final Weather weather = new Weather();
        weather.date = date;
        int index = getRandom(MIN, MAX) % (DESCRIPTION.length - 1);
        weather.description = DESCRIPTION[index];
        final int maxTemp = getRandom(MIN, MAX);
        weather.temperatureMax = String.valueOf(maxTemp) + (char) 0x00B0;
        weather.temperatureMin = String.valueOf(getRandom(MIN, maxTemp + 1)) + (char) 0x00B0;
        return weather;
    }

    private int getRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
