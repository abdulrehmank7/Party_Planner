package com.arkapp.partyplanner.ui.foodList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.*
import kotlinx.android.synthetic.main.fragment_food_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FoodListFragment : Fragment(R.layout.fragment_food_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val budgetLimit = when (selectionData.partyBudget) {
            getString(R.string.low) -> LOW_BUDGED_LIMIT
            getString(R.string.medium) -> MEDIUM_BUDGED_LIMIT
            getString(R.string.high) -> HIGH_BUDGED_LIMIT
            getString(R.string.very_high) -> HIGH_BUDGED_LIMIT
            else -> HIGH_BUDGED_LIMIT
        }

        val toShowAlcohol = selectionData.partyType != PARTY_TYPE_BABY_SHOWER

        lifecycleScope.launch(Dispatchers.Main) {

            val foodDao = AppDatabase.getDatabase(requireContext()).foodDao()
            val foodList =
                if (toShowAlcohol)
                    foodDao.getFoodListWithAlcohol(budgetLimit)
                else
                    foodDao.getFoodListWithoutAlcohol(budgetLimit)

            val adapter = FoodListAdapter(
                foodList,
                requireContext()
            )
            foodListRv.initVerticalAdapter(adapter, true)
        }
    }

}
