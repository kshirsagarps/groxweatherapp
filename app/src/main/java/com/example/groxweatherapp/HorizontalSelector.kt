package com.example.groxweatherapp

import android.content.Context
import android.support.annotation.ColorRes
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import butterknife.ButterKnife
import com.example.groxweatherapp.grox.WeatherStore
import com.example.groxweatherapp.presenter.HorizontalSelectorPresenter
import com.example.groxweatherapp.view.HorizontalSelectorView
import kotterknife.bindView

class HorizontalSelector : LinearLayout, HorizontalSelectorView {

    private lateinit var horizontalSelectorPresenter: HorizontalSelectorPresenter

    private val today: Button by bindView(R.id.today)
    private val fiveDay: Button by bindView(R.id.five_day)
    private val tenDay: Button by bindView(R.id.ten_day)

    private val selectionColor: Int
        @ColorRes
        get() = R.color.colorAccent

    private val unSelectionColor: Int
        @ColorRes
        get() = android.R.color.darker_gray

    constructor(context: Context) : super(context) {
        onFinishInflate()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onFinishInflate() {
        super.onFinishInflate()
        inflate(context, R.layout.horizontal_weather_view, this)
        ButterKnife.bind(this)
    }

    fun attachWeatherModelStore(weatherStore: WeatherStore) {
        horizontalSelectorPresenter = HorizontalSelectorPresenter(weatherStore)
        horizontalSelectorPresenter.attachView(this)
        addClickListeners()
    }

    fun detachWeatherModelStore() {
        horizontalSelectorPresenter.detachView()
    }

    override fun onTodaySelected() {
        setTodayButtonSelected(selectionColor)
        setFiveDayButtonSelected(unSelectionColor)
        setTenDayButtonSelected(unSelectionColor)
    }

    override fun onFiveDaySelected() {
        setTodayButtonSelected(unSelectionColor)
        setFiveDayButtonSelected(selectionColor)
        setTenDayButtonSelected(unSelectionColor)
    }

    override fun onTenDaySelected() {
        setTodayButtonSelected(unSelectionColor)
        setFiveDayButtonSelected(unSelectionColor)
        setTenDayButtonSelected(selectionColor)
    }

    private fun addClickListeners() {
        today.setOnClickListener { horizontalSelectorPresenter.onTodayClicked() }
        fiveDay.setOnClickListener { horizontalSelectorPresenter.onFiveDayClicked() }
        tenDay.setOnClickListener { horizontalSelectorPresenter.onTenDayClicked() }
    }

    private fun setTodayButtonSelected(@ColorRes backgroundColor: Int) {
        today.backgroundTintList = resources.getColorStateList(backgroundColor)
    }

    private fun setFiveDayButtonSelected(@ColorRes backgroundColor: Int) {
        fiveDay.backgroundTintList = resources.getColorStateList(backgroundColor)
    }

    private fun setTenDayButtonSelected(@ColorRes backgroundColor: Int) {
        tenDay.backgroundTintList = resources.getColorStateList(backgroundColor)
    }
}
