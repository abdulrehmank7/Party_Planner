package com.arkapp.partyplanner.ui.foodList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Food
import com.arkapp.partyplanner.utils.loadImage

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

class FoodListAdapter(
    private val foodList: List<Food>,
    private val navController: NavController
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as FoodListViewHolder).viewBinding

        val foodData = foodList[position]
        binding.foodName.text = foodData.name
        binding.foodPrice.text = "$${foodData.price}"
        binding.foodImg.loadImage(foodData.resId)

        /*binding.parent.setOnClickListener {
            prefRepository.openedBook(foodData)
            navController.navigate(R.id.action_favouritesFragment_to_bookDetailsFragment)
        }*/
    }


    override fun getItemCount() = foodList.size

    override fun getItemId(position: Int): Long {
        return foodList[position].hashCode().toLong()
    }

}