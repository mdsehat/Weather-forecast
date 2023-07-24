package com.example.weatherforecast.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.databinding.ItemForecastHourlyBinding
import com.example.weatherforecast.utils.BaseDiffUtils
import com.example.weatherforecast.utils.IconCode
import com.example.weatherforecast.utils.convertTemp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ForecastWeatherAdapter @Inject constructor() : RecyclerView.Adapter<ForecastWeatherAdapter.Holder>() {

    //Binding
    private lateinit var binding: ItemForecastHourlyBinding

    @Inject
    lateinit var iconCode: IconCode

    //Other
    private var customList = emptyList<ForecastResponse.Hours>()
    private var myTimezone = 0
    private lateinit var context:Context
    private val TAG = "tagHr"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemForecastHourlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return Holder()
    }

    override fun getItemCount() = customList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Holder().bind(customList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setData(newList: MutableList<ForecastResponse.Hours>, timezone:Int) {
        val diffInstance = BaseDiffUtils(customList.toMutableList(), newList)
        val calculate = DiffUtil.calculateDiff(diffInstance)
        customList = newList
        calculate.dispatchUpdatesTo(this)
        //Timezone
        myTimezone = timezone
    }

    inner class Holder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: ForecastResponse.Hours) {
            binding.apply {
                //Icon
                val iconCode = iconCode.convertCodeToIcon(item.weather?.get(0)!!.id!!)
                when(iconCode){
                    800->{
                        if (item.sys?.pod!!.contains("d")){
                            tvTimeAndIcon.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.sun)
                        } else{
                            tvTimeAndIcon.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.moon)
                        }
                    }
                    801->{
                        if (item.sys?.pod!!.contains("d"))  {
                            tvTimeAndIcon.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.sun_behind_cloud)
                        } else{
                            tvTimeAndIcon.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.moon_behind_cloud)
                        }
                    }
                    else->{
                        tvTimeAndIcon.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, iconCode)
                    }
                }
                //Temp
                tvTemp.text = convertTemp(item.main!!.temp!!).toString()+"\u00B0"
                //Time
               // val timezone = TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000L
                val dateOfCity = item.dt!!.toLong() * 1000L + myTimezone
                val date = Date(dateOfCity)
                val df = SimpleDateFormat("HH:mm ")
                tvTimeAndIcon.text = df.format(date)
                Log.i(TAG, "bind: " + item.dt!!.toLong() + "," + myTimezone + "," +
                        "\n" + date.toString())
            }
        }
    }
}