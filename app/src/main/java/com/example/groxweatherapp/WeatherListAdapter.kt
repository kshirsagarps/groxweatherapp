package com.example.groxweatherapp

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.ButterKnife
import com.example.groxweatherapp.WeatherListAdapter.WeatherListViewHolder
import com.example.groxweatherapp.model.Weather
import kotterknife.bindView
import java.util.*

class WeatherListAdapter : RecyclerView.Adapter<WeatherListViewHolder>() {

    private val weatherList = ArrayList<Weather>()

    fun setWeatherList(weatherList: List<Weather>) {
        this.weatherList.clear()
        this.weatherList.addAll(weatherList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherListViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherListViewHolder, position: Int) {
        val (date, temperatureMin, temperatureMax, description) = weatherList[position]
        holder.date.text = date
        holder.description.text = description
        holder.temMax.text = temperatureMax
        holder.tempMin.text = temperatureMin
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    class WeatherListViewHolder(itemView: View) : ViewHolder(itemView) {

        val date: TextView by bindView(R.id.date)
        val description: TextView by bindView(R.id.description)
        val temMax: TextView by bindView(R.id.temp_max)
        val tempMin: TextView by bindView(R.id.temp_min)

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
