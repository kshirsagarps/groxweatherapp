package com.example.groxweatherapp.api

import android.support.annotation.IntDef
import com.example.groxweatherapp.model.Weather
import com.example.groxweatherapp.model.WeatherResponse
import rx.Observable
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.SOURCE
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

class WeatherApiClient {

    fun getWeather(@ForecastMode forecastMode: Int): Observable<WeatherResponse> {
        val weathers: List<Weather>  = createWeatherForRequest(forecastMode)
        val weatherResponse = WeatherResponse(weathers)
        var observable = Observable.just(weatherResponse)
        if (random.nextInt() % ERROR_RATE == 0) {
            observable = Observable.error(RuntimeException(ERROR_MESSAGE))
        }

        return observable.delay(LATENCY_IN_MS.toLong(), TimeUnit.MILLISECONDS)
    }

    private fun createWeatherForRequest(@ForecastMode forecastMode: Int): List<Weather> {
        val weatherList = ArrayList<Weather>()

        when (forecastMode) {
            TODAY -> return createAndAddWeatherToList(1, weatherList)
            FIVE_DAY -> return createAndAddWeatherToList(5, weatherList)
            TEN_DAY -> return createAndAddWeatherToList(10, weatherList)
        }

        return weatherList
    }

    private fun createAndAddWeatherToList(numOfDays: Int, weatherList: MutableList<Weather>): List<Weather> {
        for (i in 0 until numOfDays) {
            weatherList.add(createWeather(DAYS[i % (DAYS.size - 1)] + " " + DATES[i]))
        }
        return weatherList
    }

    private fun createWeather(date: String): Weather {
        val maxTemp = getRandom(MIN, MAX)
        val temperatureMax = maxTemp.toString() + 0x00B0.toChar()
        val temperatureMin = getRandom(MIN, maxTemp + 1).toString() + 0x00B0.toChar()
        val description = DESCRIPTION[getRandom(MIN, MAX) % (DESCRIPTION.size - 1)]
        return Weather(date, temperatureMax, temperatureMin, description)
    }

    private fun getRandom(min: Int, max: Int): Int {
        return ThreadLocalRandom.current().nextInt(min, max)
    }

    companion object {
        private const val SEED = 7
        private const val ERROR_RATE = 5
        private const val LATENCY_IN_MS = 1000
        private const val ERROR_MESSAGE = "Client Exception"

        private const val MAX = 100
        private const val MIN = 40
        private val random = Random(SEED.toLong())

        private val DAYS = arrayOf("TODAY", "SUN", "MON", "TUES", "THURS", "FRI", "SAT")
        private val DESCRIPTION = arrayOf("SUNNY", "OVERCAST", "CLOUDY", "PARTLY SUNNY", "PARTLY CLOUD")
        private val DATES = arrayOf("MAR 1", "MAR 2", "MAR 3", "MAR 4", "MAR 5", "MAR 6", "MAR 7", "MAR 8", "MAR 9", "MAR 10")

        @IntDef(TODAY.toLong(), FIVE_DAY.toLong(), TEN_DAY.toLong())
        @Retention(SOURCE)
        annotation class ForecastMode

        const val TODAY = 0
        const val FIVE_DAY = 1
        const val TEN_DAY = 2
    }
}
