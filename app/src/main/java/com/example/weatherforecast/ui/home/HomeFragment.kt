package com.example.weatherforecast.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.CurrentResponse
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.databinding.HomeFragmentBinding
import com.example.weatherforecast.ui.home.adapter.ForecastWeatherAdapter
import com.example.weatherforecast.ui.home.adapter.ListOfDaysAdapter
import com.example.weatherforecast.utils.*
import com.example.weatherforecast.view.home.HomeIntent
import com.example.weatherforecast.view.home.HomeState
import com.example.weatherforecast.view.home.HomeViewModel
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

    @Inject
    lateinit var daysAdapter: ListOfDaysAdapter

    @Inject
    lateinit var forecastAdapter: ForecastWeatherAdapter

    @Inject
    lateinit var iconCode: IconCode

    @Inject
    lateinit var networkChecker: NetworkChecker


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
        lifecycleScope.launch {
            //Load state
            viewModel.state.collect { state ->
                when (state) {
                    is HomeState.ShowLoading -> {
                        showLoading()
                    }
                    is HomeState.Error -> {
                        hideLoading()
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is HomeState.ShowCurrentAndForecastWeather -> {
                        hideLoading()
                        initChipListOfDays(state.listOfDays)
                        setupData(state.pairInfo)
                        showForecast5Days(state.pairInfo.second)
                    }
                    else -> {}
                }
            }
        }
        //Call api
        lifecycleScope.launch {
            //Check network
            networkChecker.checkingNetwork().collect {
                if (it) {
                    checkingNet(true)
                    viewModel.intent.send(
                        HomeIntent.GetCurrentAndForecastWeather(
                            35.71,
                            51.40,
                            API_KEY
                        )
                    )
                } else {
                    checkingNet(false)
                }
            }
        }
    }


    //--InitViews--//
    private fun setupData(item: Pair<CurrentResponse, ForecastResponse>) {
        binding.apply {
            //Default select
            val numberOfDay = 1;
            firstDay.isChecked = true
            initListForecast(numberOfDay, itemCurrent = item.first, itemForecast = item.second)
            //Other select
            firstDay.setOnClickListener {
                initListForecast(1, itemCurrent = item.first, itemForecast = item.second)
            }
            secondDay.setOnClickListener {
                initListForecast(2, itemCurrent = item.first, itemForecast = item.second)
            }
            thirdDay.setOnClickListener {
                initListForecast(3, itemCurrent = item.first, itemForecast = item.second)
            }
        }


    }

    private fun showForecast5Days(itemForecast5Days: ForecastResponse) {
        binding.tv5day.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeTo5Days(itemForecast5Days)
            findNavController().navigate(direction)
        }
    }

    //Current
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setupMainCurrent(response: CurrentResponse) {
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
            tvTemp.text = convertTemp(response.main.temp!!).toString() + "\u00B0"
            //Anim icon
            val iconCode = iconCode.convertCodeToAnimationIcon(response.weather?.get(0)!!.id!!)

            when (iconCode) {
                800 -> {
                    if (response.weather[0]!!.icon!!.contains("d")) setupAnimate(R.raw.sun)
                    else setupAnimate(R.raw.moon)
                }
                801 -> {
                    if (response.weather[0]!!.icon!!.contains("d")) setupAnimate(R.raw.sun_behind_cloud)
                    else setupAnimate(R.raw.moon_behind_cloud)
                }
                else -> {
                    setupAnimate(iconCode)
                }
            }
            //Last updated
            val df = SimpleDateFormat("EEE MM MMMM HH:mm")
            tvLastUpdate.text = convertUnixToTime(response.dt!!.toLong(), response.timezone!!, df)
            //timezone
            myTimezone = response.timezone
        }
    }

    //--Forecast
    @SuppressLint("WeekBasedYear")
    private fun initListForecast(
        numberOfDay: Int,
        itemForecast: ForecastResponse,
        itemCurrent: CurrentResponse
    ) {
        //Setting of get time
        val calender = Calendar.getInstance()
        val df = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())
        //List of forecast
        val list: MutableList<ForecastResponse.Hours> = mutableListOf()
        //Number of day
        lifecycleScope.launch {
            if (numberOfDay == 1) {
                val time = df.format(calender.time)
                itemForecast.list?.let { listHour ->
                    listHour.forEach { items ->
                        val requestTime = convertUnixToTime(
                            items!!.dt!!.toLong(),
                            itemForecast.city?.timezone!!,
                            df
                        )
                        if (requestTime.contains(time)) {
                            list.add(items)
                        }
                    }
                }
                //Fill value of main current
                setupMainCurrent(itemCurrent)
            } else {
                for (i in 2..numberOfDay) {
                    calender.add(Calendar.DATE, 1)
                }
                val time = df.format(calender.time)
                itemForecast.list?.let { listHour ->
                    listHour.forEach { items ->
                        val requestTime = convertUnixToTime(
                            items!!.dt!!.toLong(),
                            itemForecast.city?.timezone!!,
                            df
                        )
                        if (requestTime.contains(time)) {
                            list.add(items)
                        }
                    }
                }
                //Fill value of main forecast
                setupMainForecast(list[4])
            }
            //Fill list of adapter
            forecastAdapter.setData(list, itemForecast.city?.timezone!!)
        }
        //Init rv of forecast
        binding.rvForecastHourly.initRv(
            forecastAdapter, LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setupMainForecast(response: ForecastResponse.Hours) {
        //InitViews
        binding.apply {
            //Wind
            tvWind.text = "${response.wind!!.speed} M/Sec"
            //Humidity
            tvHumidity.text = "${response.main?.humidity}%"
            //Feels like
            tvFeelsLike.text = convertTemp(response.main!!.feelsLike!!).toString() + "\u00B0"
            //Temp
            tvTemp.text = convertTemp(response.main.temp!!).toString() + "\u00B0"
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
        }
    }

    //List of days
    private fun initChipListOfDays(list: MutableList<String>) {
        binding.apply {
            if (list.isNotEmpty()) {
                chipGroup.visibility = View.VISIBLE
                firstDay.text = list[0]
                secondDay.text = list[1]
                thirdDay.text = list[2]
            } else {
                chipGroup.visibility = View.INVISIBLE
            }

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

    private fun checkingNet(connection: Boolean) {
        binding.apply {
            if (connection) {
                contentLay.isVisible = true
                checkNetLay.isVisible = false
            } else {
                contentLay.isVisible = false
                checkNetLay.isVisible = true
            }

        }
    }

    //Other
    private fun setupAnimate(id: Int) {
        binding.apply {
            animForecast.apply {
                setAnimation(id)
                playAnimation()
                repeatCount = 100
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}