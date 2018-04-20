package com.example.groxweatherapp;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.groxweatherapp.model.Weather;
import java.util.ArrayList;
import java.util.List;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.WeatherListViewHolder> {

    private List<Weather> weatherList = new ArrayList<>();

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList.clear();
        this.weatherList.addAll(weatherList);
    }

    @Override
    public WeatherListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new WeatherListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherListViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.date.setText(weather.date);
        holder.description.setText(weather.description);
        holder.temMax.setText(weather.temperatureMax);
        holder.tempMin.setText(weather.temperatureMin);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static class WeatherListViewHolder extends ViewHolder {

        @BindView(R.id.date) public TextView date;
        @BindView(R.id.description) public TextView description;
        @BindView(R.id.temp_max) public TextView temMax;
        @BindView(R.id.temp_min) public TextView tempMin;

        public WeatherListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
