package com.arkapp.partyplanner.ui.finalChecklist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

class SelectedFoodListAdapter(private val foodList: List<Food>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SelectedFoodListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rv_selected_food,
                parent,
                false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as SelectedFoodListViewHolder).viewBinding

        val foodData = foodList[position]
        binding.foodName.text = foodData.name
        binding.foodPrice.text = "$${foodData.price}"
    }


    override fun getItemCount() = foodList.size

    override fun getItemId(position: Int): Long {
        return foodList[position].hashCode().toLong()
    }

}