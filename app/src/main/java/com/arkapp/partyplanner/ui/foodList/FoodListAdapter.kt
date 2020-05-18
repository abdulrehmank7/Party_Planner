package com.arkapp.partyplanner.ui.foodList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Food
import com.arkapp.partyplanner.utils.loadImage
import com.arkapp.partyplanner.utils.toast

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

class FoodListAdapter(
    private val foodList: List<Food>,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val selectedFoodList = ArrayList<Food>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FoodListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rv_food_type,
                parent,
                false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as FoodListViewHolder).viewBinding

        val foodData = foodList[position]
        binding.foodName.text = foodData.name
        binding.foodPrice.text = "$${foodData.price}"
        binding.foodImg.loadImage(foodData.resId)

        if (selectedFoodList.contains(foodData))
            binding.addFoodBtn.text = context.getString(R.string.remove_food)
        else
            binding.addFoodBtn.text = context.getString(R.string.add_food)

        binding.addFoodBtn.setOnClickListener {
            if (binding.addFoodBtn.text.toString() == context.getString(R.string.add_food)) {
                selectedFoodList.add(foodData)
                context.toast("Food added!")
                binding.addFoodBtn.text = context.getString(R.string.remove_food)

            } else {
                selectedFoodList.remove(foodData)
                context.toast("Food removed!")
                binding.addFoodBtn.text = context.getString(R.string.add_food)
            }
        }
    }


    override fun getItemCount() = foodList.size

    override fun getItemId(position: Int): Long {
        return foodList[position].hashCode().toLong()
    }

}