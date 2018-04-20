package com.example.groxweatherapp;

import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.groxweatherapp.grox.WeatherStore;
import com.example.groxweatherapp.model.Weather;
import com.example.groxweatherapp.model.WeatherModel;
import com.example.groxweatherapp.presenter.WeatherMainPresenter;
import com.example.groxweatherapp.view.WeatherMainView;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.groxweatherapp.model.WeatherModel.INITIAL_STATE;

public class WeatherMainActivity extends AppCompatActivity implements WeatherMainView {

    private WeatherStore weatherStore;
    private WeatherMainPresenter weatherMainPresenter;

    @BindView(R.id.horizontal_selector) HorizontalSelector horizontalSelector;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private WeatherListAdapter weatherListAdapter;
    private Unbinder unBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherStore = new WeatherStore(INITIAL_STATE);
        unBinder = ButterKnife.bind(this);

        horizontalSelector.attachWeatherModelStore(weatherStore);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        weatherListAdapter = new WeatherListAdapter();
        recyclerView.setAdapter(weatherListAdapter);
        weatherMainPresenter = new WeatherMainPresenter();
        weatherMainPresenter.attachPresenter(this, weatherStore);
    }

    @Override
    protected void onDestroy() {
        horizontalSelector.detachWeatherModelStore();
        weatherMainPresenter.detachPresenter();
        unBinder.unbind();
        super.onDestroy();
    }

    @Override
    public void showSpinner() {
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void hideSpinner() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void showList() {
        recyclerView.setVisibility(VISIBLE);
    }

    @Override
    public void hideList() {
        recyclerView.setVisibility(INVISIBLE);
    }

    @Override
    public void showWeather(List<Weather> weatherList) {
        weatherListAdapter.setWeatherList(weatherList);
        weatherListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorDialog() {
        Builder builder;
        builder = new Builder(this);
        builder.setTitle(R.string.error_occurred)
            .setMessage(R.string.retry_again_to_continue)
            .setPositiveButton(R.string.retry, (dialog, which) -> weatherMainPresenter.onRetryClick())
            .show();
    }
}
