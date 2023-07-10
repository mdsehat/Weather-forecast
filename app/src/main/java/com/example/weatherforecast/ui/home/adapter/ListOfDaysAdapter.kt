package com.example.weatherforecast.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.HomeFragmentBinding
import com.example.weatherforecast.databinding.Item7daysBinding
import com.example.weatherforecast.utils.BaseDiffUtils
import javax.inject.Inject

class ListOfDaysAdapter @Inject constructor(): RecyclerView.Adapter<ListOfDaysAdapter.Holder>() {

    //Binding
    private lateinit var binding:Item7daysBinding

    private var customList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = Item7daysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder()
    }

    override fun getItemCount() = customList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Holder().bind(customList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setData(newList: MutableList<String>){
        val diffInstance = BaseDiffUtils(customList, newList)
        val calculate = DiffUtil.calculateDiff(diffInstance)
        customList = newList
        calculate.dispatchUpdatesTo(this)
    }


    inner class Holder : RecyclerView.ViewHolder(binding.root){
        fun bind(item:String){
            binding.tvDate.text = item
        }
    }
}