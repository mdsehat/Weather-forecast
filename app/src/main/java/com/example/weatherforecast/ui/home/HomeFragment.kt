package com.example.weatherforecast.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.CurrentResponse
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.databinding.HomeFragmentBinding
import com.example.weatherforecast.ui.home.adapter.ForecastWeatherAdapter
import com.example.weatherforecast.ui.home.adapter.ListOfDaysAdapter
import com.example.weatherforecast.utils.API_KEY
import com.example.weatherforecast.utils.IconCode
import com.example.weatherforecast.utils.convertTemp
import com.example.weatherforecast.utils.initRv
import com.example.weatherforecast.view.home.HomeIntent
import com.example.weatherforecast.view.home.HomeState
import com.example.weatherforecast.view.home.HomeViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    //Binding
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    //Other
    private val viewModel: HomeViewModel by viewModels()
    private var myTimezone = 0
    private val TAG = "tagDay"
    private var counterChip = 1

    @Inject
    lateinit var daysAdapter: ListOfDaysAdapter

    @Inject
    lateinit var forecastAdapter: ForecastWeatherAdapter

    @Inject
    lateinit var iconCode: IconCode


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            //Call api
            viewModel.intent.send(HomeIntent.GetCurrentWeather(35.71, 51.40, API_KEY))
            viewModel.intent.send(HomeIntent.GetWeatherForecast(35.71, 51.40, API_KEY))
            //Get list of days
            viewModel.intent.send(HomeIntent.GetListOfDays)
            //Load state
            viewModel.state.collect { state ->
                when (state) {
                    is HomeState.ShowLoading -> {
                        showLoading()
                    }
                    is HomeState.Error -> {
                        hideLoading()
                    }
                    is HomeState.ListOfDays -> {
                        initChipListOfDays(state.list)
                    }
                    is HomeState.ShoWeatherForecast -> {
                        initValueForecastList(state.itemForecast)
                    }
                    is HomeState.ShowCurrentWeather -> {
                        hideLoading()
                        initValueCurrent(state.itemCurrent)
                    }
                    else -> {}
                }

            }
        }
    }

    //--InitViews--//
    //Current
    @SuppressLint("SetTextI18n")
    private fun initValueCurrent(response: CurrentResponse) {
        //InitViews
        binding.apply {
            //Name
            tvCity.text = response.name
            //Wind
            tvWind.text = "${response.wind!!.speed} M/Sec"
            //Humidity
            tvHumidity.text = "${response.main?.humidity}%"
            //Feels like
            tvFeelsLike.text = convertTemp(response.main!!.feelsLike!!).toString() + "\u00B0"
            //Temp
            tvTemp.text = convertTemp(response.main!!.temp!!).toString() + "\u00B0"
            //Anim icon
            val iconCode = iconCode.convertCodeToAnimationIcon(response.weather?.get(0)!!.id!!)

            when (iconCode) {
                800 -> {
                    if (response.weather!!.get(0)!!.icon!!.contains("d")) setupAnimate(R.raw.sun)
                    else setupAnimate(R.raw.moon)
                }
                801 -> {
                    if (response.weather!!.get(0)!!.icon!!.contains("d")) setupAnimate(R.raw.sun_behind_cloud)
                    else setupAnimate(R.raw.moon_behind_cloud)
                }
                else -> {
                    setupAnimate(iconCode)
                }
            }
            //Desc
            tvDesc.text = response.weather.get(0)!!.description
            //timezone
            myTimezone = response.timezone!!
        }
    }

    //Forecast
    private fun initValueForecast(response: ForecastResponse.Hours) {
        //InitViews
        binding.apply {
            //Wind
            tvWind.text = "${response.wind!!.speed} M/Sec"
            //Humidity
            tvHumidity.text = "${response.main?.humidity}%"
            //Feels like
            tvFeelsLike.text = convertTemp(response.main!!.feelsLike!!).toString() + "\u00B0"
            //Temp
            tvTemp.text = convertTemp(response.main!!.temp!!).toString() + "\u00B0"
            //Anim icon
            val iconCode = iconCode.convertCodeToAnimationIcon(response.weather?.get(0)!!.id!!)

            when (iconCode) {
                800 -> {
                    if (response.sys?.pod!!.contains("d")) setupAnimate(R.raw.sun)
                    else setupAnimate(R.raw.moon)
                }
                801 -> {
                    if (response.sys?.pod!!.contains("d")) setupAnimate(R.raw.sun_behind_cloud)
                    else setupAnimate(R.raw.moon_behind_cloud)
                }
                else -> {
                    setupAnimate(iconCode)
                }
            }
            //Desc
            tvDesc.text = response.weather.get(0)!!.description
        }
    }

    private fun initValueForecastList(item: ForecastResponse) {
        //Click
        daysAdapter.setOnClickItem { day ->
            //Setting of get time
            val calender = Calendar.getInstance()
            val df = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())
            //List of forecast
            var list: MutableList<ForecastResponse.Hours> = mutableListOf()
            //Number of day
            var numberOfDay = daysAdapter.customList.indexOf(day)

            lifecycleScope.launch {
                if (numberOfDay == 0) {
                    val time = df.format(calender.time)
                    item.list?.let {
                        it.forEach { item ->
                            if (item?.dtTxt?.contains(time)!!) {
                                list.add(item)
                            }
                        }
                    }
                    //Fill value of current weather
                    viewModel.intent.send(HomeIntent.GetCurrentWeather(35.71, 51.40, API_KEY))
                } else {
                    for (i in 1..numberOfDay) {
                        calender.add(Calendar.DATE, 1)
                    }
                    val time = df.format(calender.time)
                    item.list?.let {
                        it.forEach { item ->
                            if (item?.dtTxt?.contains(time)!!) {
                                list.add(item)
                            }
                        }
                    }
                    //Fill value of main forecast
                    initValueForecast(list[4])
                }
                //Fill list of adapter
                forecastAdapter.setData(list, myTimezone)
            }
            //Init rv of forecast
            binding.rvForecastHourly.initRv(forecastAdapter, LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false))
        }

    }

    //List of days
    private fun initChipListOfDays(list: MutableList<String>) {
        setupChip(list, binding.chipGroup)
    }
    //Set up chip
    private fun setupChip(list: MutableList<String>, view: ChipGroup){
        list.forEach {
            val chip = Chip(requireContext())
            val chipDrawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.chipStyle)
            chip.setChipDrawable(chipDrawable)
            chip.text = it
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            chip.id = counterChip++
            view.addView(chip)
        }
    }
    //Loading
    private fun showLoading() {
        binding.apply {
            contentLay.visibility = View.INVISIBLE
            loading.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            contentLay.visibility = View.VISIBLE
            loading.visibility = View.INVISIBLE
        }
    }

    //Other
    private fun setupAnimate(id: Int) {
        binding.apply {
            animForecast.apply {
                setAnimation(id)
                playAnimation()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}