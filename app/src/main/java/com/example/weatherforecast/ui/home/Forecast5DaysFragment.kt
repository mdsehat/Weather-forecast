package com.example.weatherforecast.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.databinding.FragmentForecast5DaysBinding
import com.example.weatherforecast.databinding.HomeFragmentBinding
import com.example.weatherforecast.ui.home.adapter.Forecast5DaysAdapter
import com.example.weatherforecast.utils.API_KEY
import com.example.weatherforecast.utils.convertUnixToTime
import com.example.weatherforecast.utils.initRv
import com.example.weatherforecast.view.home.HomeIntent
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class Forecast5DaysFragment : Fragment() {
    //Binding
    private var _binding: FragmentForecast5DaysBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var forecastAdapter: Forecast5DaysAdapter

    //Other
    private val args by navArgs<Forecast5DaysFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecast5DaysBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Get data
            val item = args.item
            tvCity.text = item.city?.name
            initList(item)
            rv.initRv(forecastAdapter, LinearLayoutManager(requireContext()))
            //Icon back
            backIcon.setOnClickListener {
                findNavController().popBackStack(R.id.forecast5DaysFragment, true)
            }
        }
    }

    private fun initList(item: ForecastResponse) {
        //Setting of get time
        val calender = Calendar.getInstance()
        val df = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())
        //List of forecast
        val list: MutableList<ForecastResponse.Hours> = mutableListOf()

        for (i in 1..5) {
            val internalList = mutableListOf<ForecastResponse.Hours>()
            var counter = 0
            if (i != 1){
                calender.add(Calendar.DATE, 1)
            }
            val time = df.format(calender.time)
            item.list?.let { listHour ->
                listHour.forEach { items ->
                    val requestTime = convertUnixToTime(items!!.dt!!.toLong(), item.city?.timezone!!, df)
                    if (requestTime.contains(time)) {
                        counter++
                        internalList.add(items)
                    }
                }
            }
            list.add(internalList[counter/2])
        }
        forecastAdapter.setData(list, item.city?.timezone!!)
    }

    //Other
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}