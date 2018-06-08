package com.example.groxweatherapp

import android.app.AlertDialog.Builder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.*
import android.widget.ProgressBar
import butterknife.ButterKnife
import com.example.groxweatherapp.grox.WeatherStore
import com.example.groxweatherapp.model.Weather
import com.example.groxweatherapp.model.WeatherModel
import com.example.groxweatherapp.presenter.WeatherMainPresenter
import com.example.groxweatherapp.view.WeatherMainView
import kotterknife.bindView

class WeatherMainActivity : AppCompatActivity(), WeatherMainView {

    private lateinit var weatherStore: WeatherStore
    private lateinit var weatherMainPresenter: WeatherMainPresenter
    private lateinit var weatherListAdapter: WeatherListAdapter

    private val horizontalSelector: HorizontalSelector by bindView(R.id.horizontal_selector)
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherStore = WeatherStore(WeatherModel.INITIAL_STATE)
        ButterKnife.bind(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        weatherListAdapter = WeatherListAdapter()
        recyclerView.adapter = weatherListAdapter
        weatherMainPresenter = WeatherMainPresenter()
    }

    override fun onStart() {
        super.onStart()
        horizontalSelector.attachWeatherModelStore(weatherStore)
        weatherMainPresenter.attachView(this, weatherStore)
    }

    override fun onStop() {
        horizontalSelector.detachWeatherModelStore()
        weatherMainPresenter.detachView()
        super.onStop()
    }

    override fun showSpinner() {
        progressBar.visibility = VISIBLE
    }

    override fun hideSpinner() {
        progressBar.visibility = GONE
    }

    override fun showList() {
        recyclerView.visibility = VISIBLE
    }

    override fun hideList() {
        recyclerView.visibility = INVISIBLE
    }

    override fun showWeather(weatherList: List<Weather>) {
        weatherListAdapter.setWeatherList(weatherList)
        weatherListAdapter.notifyDataSetChanged()
    }

    override fun showErrorDialog() {
        Builder(this).setTitle(R.string.error_occurred)
                .setCancelable(false)
                .setMessage(R.string.retry_again_to_continue)
                .setPositiveButton(R.string.retry, { dialog, which -> weatherMainPresenter.onRetryClick() })
                .show()
    }
}
