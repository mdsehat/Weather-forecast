package com.example.weatherforecast.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.databinding.ItemForecast5DayBinding
import com.example.weatherforecast.utils.base.BaseDiffUtils
import com.example.weatherforecast.utils.IconCode
import com.example.weatherforecast.utils.convertTemp
import com.example.weatherforecast.utils.convertUnixToTime
import java.text.SimpleDateFormat
import javax.inject.Inject

class Forecast5DaysAdapter @Inject constructor() : RecyclerView.Adapter<Forecast5DaysAdapter.Holder>() {

    //Binding
    private lateinit var binding: ItemForecast5DayBinding

    @Inject
    lateinit var iconCode: IconCode

    //Other
    private var customList = emptyList<ForecastResponse.Hours>()
    private var myTimezone = 0
    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemForecast5DayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n", "SimpleDateFormat")
        fun bind(item: ForecastResponse.Hours) {
            binding.apply {
                //Icon
                val iconCode = iconCode.convertCodeToIcon(item.weather?.get(0)!!.id!!)
                when(iconCode){
                    800->{
                        if (item.sys?.pod!!.contains("d")){
                            tvIcon.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.sun)
                        } else{
                            tvIcon.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.moon)
                        }
                    }
                    801->{
                        if (item.sys?.pod!!.contains("d"))  {
                            tvIcon.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.sun_behind_cloud)
                        } else{
                            tvIcon.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.moon_behind_cloud)
                        }
                    }
                    else->{
                        tvIcon.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, iconCode)
                    }
                }
                //Temp
                tvTemp.text = convertTemp(item.main!!.temp!!).toString()+"\u00B0"
                val calculateTemp = item.main.feelsLike?.toInt()!! - item.main.temp?.toInt()!!
                tvTempFeel.text = calculateTemp.toString()+"\u00B0"
                //Time
                val df = SimpleDateFormat("dd MMM")
                tvDate.text = convertUnixToTime(item.dt!!.toLong(), myTimezone, df)
                //Day
                val dfDay = SimpleDateFormat("EEEE")
                day.text = convertUnixToTime(item.dt!!.toLong(), myTimezone, dfDay)

            }
        }
    }
}