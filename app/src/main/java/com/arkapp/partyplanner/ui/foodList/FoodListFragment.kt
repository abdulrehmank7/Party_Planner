package com.arkapp.partyplanner.ui.foodList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.utils.*
import kotlinx.android.synthetic.main.fragment_food_list.*

/**
 * A simple [Fragment] subclass.
 */
class FoodListFragment : Fragment(R.layout.fragment_food_list) {

    private lateinit var adapter: FoodListAdapter
    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val budgetLimit = when (prefRepository.getCurrentPartyDetails().partyBudget) {
            getString(R.string.low) -> LOW_BUDGED_LIMIT
            getString(R.string.medium) -> MEDIUM_BUDGED_LIMIT
            getString(R.string.high) -> HIGH_BUDGED_LIMIT
            getString(R.string.very_high) -> HIGH_BUDGED_LIMIT
            else -> HIGH_BUDGED_LIMIT
        }

        /*val toShowAlcohol = prefRepository.getCurrentPartyDetails().partyType != PARTY_TYPE_BABY_SHOWER

        lifecycleScope.launch(Dispatchers.Main) {

            val foodDao = AppDatabase.getDatabase(requireContext()).foodDao()
            val foodList =
                if (toShowAlcohol)
                    foodDao.getFoodListWithAlcohol(budgetLimit)
                else
                    foodDao.getFoodListWithoutAlcohol(budgetLimit)

            adapter = FoodListAdapter(
                foodList,
                requireContext())
            foodListRv.initVerticalAdapter(adapter, true)
        }*/

        proceedBtn.setOnClickListener {

            if (adapter.selectedFoodList.size <= 0) {
                requireContext().toast("Please add some food items!")
                return@setOnClickListener
            }

            val detail = prefRepository.getCurrentPartyDetails()
            //detail.selectedFood = adapter.selectedFoodList
            prefRepository.setCurrentPartyDetails(detail)

            addUnfinishedData(lifecycleScope, requireContext(), prefRepository)
            findNavController().navigate(R.id.action_foodListFragment_to_caterersListFragment)
        }
    }

}
