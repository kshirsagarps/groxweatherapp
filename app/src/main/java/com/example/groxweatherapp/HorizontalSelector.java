package com.example.groxweatherapp;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.groxweatherapp.grox.WeatherModelStore;
import com.example.groxweatherapp.presenter.HorizontalSelectorPresenter;
import com.example.groxweatherapp.view.HorizontalSelectorView;

public class HorizontalSelector extends LinearLayout implements HorizontalSelectorView {

    private HorizontalSelectorPresenter horizontalSelectorPresenter;

    @BindView(R.id.today) Button today;
    @BindView(R.id.five_day) Button fiveDay;
    @BindView(R.id.ten_day) Button tenDay;

    public HorizontalSelector(Context context) {
        super(context);
        onFinishInflate();
    }

    public HorizontalSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.horizontal_weather_view, this);
        ButterKnife.bind(this);
        horizontalSelectorPresenter = new HorizontalSelectorPresenter();
    }

    public void attachWeatherModelStore(WeatherModelStore weatherModelStore) {
        horizontalSelectorPresenter.attachPresenter(this, weatherModelStore);
    }

    public void detachWeatherModelStore() {
        horizontalSelectorPresenter.detachPresenter();
    }

    @OnClick(R.id.today)
    public void todayClicked() {
        horizontalSelectorPresenter.onTodayClicked();
    }

    @OnClick(R.id.five_day)
    public void fiveDayClicked() {
        horizontalSelectorPresenter.onFiveDayClicked();
    }

    @OnClick(R.id.ten_day)
    public void tenDayClicked() {
        horizontalSelectorPresenter.onTenDayClicked();
    }

    @Override
    public void onTodaySelected() {
        setTodayButtonSelected(getSelectionColor());
        setFiveDayButtonSelected(getUnSelectionCOlor());
        setTenDayButtonSelected(getUnSelectionCOlor());
    }

    @Override
    public void onFiveDaySelected() {
        setTodayButtonSelected(getUnSelectionCOlor());
        setFiveDayButtonSelected(getSelectionColor());
        setTenDayButtonSelected(getUnSelectionCOlor());
    }

    @Override
    public void onTenDaySelected() {
        setTodayButtonSelected(getUnSelectionCOlor());
        setFiveDayButtonSelected(getUnSelectionCOlor());
        setTenDayButtonSelected(getSelectionColor());
    }

    public void setTodayButtonSelected(@ColorRes int backgroundColor) {
        today.setBackgroundTintList(getResources().getColorStateList(backgroundColor));
    }

    public void setFiveDayButtonSelected(@ColorRes int backgroundColor) {
        fiveDay.setBackgroundTintList(getResources().getColorStateList(backgroundColor));
    }

    public void setTenDayButtonSelected(@ColorRes int backgroundColor) {
        tenDay.setBackgroundTintList(getResources().getColorStateList(backgroundColor));
    }

    @ColorRes
    private int getSelectionColor() {
        return R.color.colorAccent;
    }

    @ColorRes
    private int getUnSelectionCOlor() {
        return android.R.color.darker_gray;
    }
}
