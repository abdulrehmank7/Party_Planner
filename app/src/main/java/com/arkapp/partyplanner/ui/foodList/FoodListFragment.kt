package com.arkapp.partyplanner.ui.foodList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.initVerticalAdapter
import kotlinx.android.synthetic.main.fragment_food_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FoodListFragment : Fragment(R.layout.fragment_food_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {

            val foodDao = AppDatabase.getDatabase(requireContext()).foodDao()
            val foodList = foodDao.getAllFood()

            val adapter = FoodListAdapter(
                foodList,
                findNavController()
            )
            foodListRv.initVerticalAdapter(adapter, true)
        }
    }

}
