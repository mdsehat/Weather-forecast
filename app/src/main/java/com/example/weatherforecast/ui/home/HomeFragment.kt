package com.example.weatherforecast.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.databinding.HomeFragmentBinding
import com.example.weatherforecast.ui.home.adapter.ListOfDaysAdapter
import com.example.weatherforecast.utils.API_KEY
import com.example.weatherforecast.utils.IconCode
import com.example.weatherforecast.utils.initRv
import com.example.weatherforecast.view.home.HomeIntent
import com.example.weatherforecast.view.home.HomeState
import com.example.weatherforecast.view.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    //Binding
    private var _binding : HomeFragmentBinding ?= null
    private val binding get() = _binding!!

    //Other
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var daysAdapter: ListOfDaysAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = HomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }
    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            //Call current api
            viewModel.intent.send(HomeIntent.GetWeatherForecast(44.34, 10.99, API_KEY,0))
            //Get list of days
            viewModel.intent.send(HomeIntent.GetListOfDays)
            //Get data
            viewModel.state.collect{state->
                when(state){
                    is HomeState.ShowLoading->{
                        showLoading()
                    }
                    is HomeState.Error ->{
                        hideLoading()
                    }
                    is HomeState.ListOfDays->{
                        initRvListOfDays(state.list)
                    }
                    is HomeState.ShowWeatherForecast->{
                        hideLoading()
                        initValueByData(state.item)
                    }
                    is HomeState.ShowGeneralInfoOfCity->{
                        hideLoading()
                        initGeneralValue(state.city)
                    }
                    else->{}
                }

            }
        }
    }
    //InitViews
    @SuppressLint("SetTextI18n")
    private fun initValueByData(response:ForecastResponse.Hours){
        //InitViews
        binding.apply {
            //Wind
            tvWind.text = "${response.wind!!.speed} M/Sec"
            //Humidity
            tvHumidity.text = "${response.main?.humidity}%"
            //Feels like
            tvFeelsLike.text = convertTemp(response.main!!.feelsLike!!).toString()+"\u00B0"
            //Temp
            tvTemp.text = convertTemp(response.main!!.temp!!).toString()+"\u00B0"
            //Anim icon
            val iconCode = IconCode().convertCodeToIcon(response.weather?.get(0)!!.id!!)

            when(iconCode){
                800->{
                    if (response.sys?.pod!!.contains("d")) setupAnimate(R.raw.sun)
                    else setupAnimate(R.raw.moon)
                }
                801->{
                    if (response.sys?.pod!!.contains("d")) setupAnimate(R.raw.sun_behind_cloud)
                    else setupAnimate(R.raw.moon_behind_cloud)
                }
                else->{ setupAnimate(iconCode) }
            }
            //Desc
            tvDesc.text = response.weather.get(0)!!.description
        }
    }
    private fun initGeneralValue(city: String) {
        //Name of city
        binding.tvCity.text = city
    }

    //List of days
    private fun initRvListOfDays(list: MutableList<String>) {
        daysAdapter.setData(list)
        binding.rv7days.initRv(daysAdapter,
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))
        //Click
        daysAdapter.setOnClickItem {day->
            lifecycleScope.launch {
                viewModel.intent.send(HomeIntent.GetWeatherForecast(44.34, 10.99, API_KEY,list.indexOf(day)))
            }
        }
    }

    //Loading
    private fun showLoading(){
        binding.apply {
            contentLay.visibility = View.INVISIBLE
            loading.visibility = View.VISIBLE
        }
    }
    private fun hideLoading(){
        binding.apply {
            contentLay.visibility = View.VISIBLE
            loading.visibility = View.INVISIBLE
        }
    }

    //Other
    private fun convertTemp(tempK: Double) = tempK.toInt() - 273
    private fun setupAnimate(id:Int){
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