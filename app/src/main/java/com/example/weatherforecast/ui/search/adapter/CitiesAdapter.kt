package com.example.weatherforecast.ui.search.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.database.CityEntity
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.databinding.ItemCityBinding
import com.example.weatherforecast.databinding.ItemForecast5DayBinding
import com.example.weatherforecast.utils.base.BaseDiffUtils
import com.example.weatherforecast.utils.IconCode
import com.example.weatherforecast.utils.convertTemp
import com.example.weatherforecast.utils.convertUnixToTime
import java.text.SimpleDateFormat
import javax.inject.Inject

class CitiesAdapter @Inject constructor() : RecyclerView.Adapter<CitiesAdapter.Holder>() {

    //Binding
    private lateinit var binding: ItemCityBinding

    @Inject
    lateinit var iconCode: IconCode

    //Other
    private var customList = emptyList<CityEntity>()
    private lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun setData(newList: List<CityEntity>) {
        val diffInstance = BaseDiffUtils(customList.toMutableList(), newList.toMutableList())
        val calculate = DiffUtil.calculateDiff(diffInstance)
        customList = newList
        calculate.dispatchUpdatesTo(this)
    }

    inner class Holder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n", "SimpleDateFormat")
        fun bind(item: CityEntity) {
            binding.apply {
                //City
                tvCity.text = item.name
                //CountryCode
                tvCountry.text = item.countryCode
                //Click
                root.setOnClickListener {
                    clickOnItem?.let {
                        it(item)
                    }
                }
            }
        }
    }

    private var clickOnItem: ((CityEntity) -> Unit) ?= null
    fun setClickOnItem(listener: (CityEntity) -> Unit){
        clickOnItem = listener
    }
}