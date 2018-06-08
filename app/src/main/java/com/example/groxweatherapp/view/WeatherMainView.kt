package com.example.groxweatherapp.view

import com.example.groxweatherapp.model.Weather

interface WeatherMainView {

    fun showSpinner()

    fun hideSpinner()

    fun showList()

    fun hideList()

    fun showWeather(weatherList: List<Weather>)

    fun showErrorDialog()
}
