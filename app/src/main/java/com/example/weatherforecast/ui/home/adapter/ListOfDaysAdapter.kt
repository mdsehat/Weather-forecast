package com.example.weatherforecast.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.Item6daysBinding
import com.example.weatherforecast.utils.BaseDiffUtils
import javax.inject.Inject

class ListOfDaysAdapter @Inject constructor() : RecyclerView.Adapter<ListOfDaysAdapter.Holder>() {

    //Binding
    private lateinit var binding: Item6daysBinding

    var customList = emptyList<String>()

    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = Item6daysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun setData(newList: MutableList<String>) {
        val diffInstance = BaseDiffUtils(customList.toMutableList(), newList)
        val calculate = DiffUtil.calculateDiff(diffInstance)
        customList = newList
        calculate.dispatchUpdatesTo(this)
    }

    inner class Holder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: String) {
            binding.apply {
                tvDate.text = item
                tvDate.setOnClickListener { root ->

                    onClickItem?.let {
                        it(item)
                    }
                }
               /* if (selected == adapterPosition){
                    tvDate.background = context.getDrawable(R.drawable.bg_rounded_magenta)
                    tvDate.setTextColor(ContextCompat.getColor(context, R.color.white))
                }else{
                    tvDate.background = context.getDrawable(R.drawable.bg_rounded_white_alpha)
                    tvDate.setTextColor(ContextCompat.getColor(context, R.color.gray))
                }*/
            }
        }
    }
    private var onClickItem: ((String) -> Unit)? = null

    fun setOnClickItem(listener: (String) -> Unit) {
        onClickItem = listener
    }
}