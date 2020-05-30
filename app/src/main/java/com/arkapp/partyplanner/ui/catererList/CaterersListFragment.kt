package com.arkapp.partyplanner.ui.catererList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Caterer
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_venue_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CaterersListFragment : Fragment(R.layout.fragment_venue_list) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().hideKeyboard()

        val budgetLimit = when (prefRepository.getCurrentPartyDetails().partyBudget) {
            getString(R.string.low) -> LOW_BUDGED_LIMIT
            getString(R.string.medium) -> MEDIUM_BUDGED_LIMIT
            getString(R.string.high) -> HIGH_BUDGED_LIMIT
            getString(R.string.very_high) -> VERY_HIGH_BUDGED_LIMIT
            else -> HIGH_BUDGED_LIMIT
        }

        val selectedPartyType = prefRepository.getCurrentPartyDetails().partyType
        val filteredCatererList = ArrayList<Caterer>()
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val gson = Gson()

        lifecycleScope.launch(Dispatchers.Main) {

            val catererDao = AppDatabase.getDatabase(requireContext()).catererDao()
            val catererList =
                catererDao
                    .getAllCaterersInBudget(
                        budgetLimit,
                        prefRepository.getCurrentPartyDetails().partyGuest!!)

            for (caterer in catererList) {
                val catererPartyType = gson.fromJson<ArrayList<String>>(caterer.partyType, type)
                for (partyType in catererPartyType) {
                    if (selectedPartyType.contains(partyType)) {
                        filteredCatererList.add(caterer)
                        break
                    }
                }
            }

            if (filteredCatererList.isEmpty()) {
                noItemFound.text = "No caterer found with the selection.\nChange selection and try again"
                noItemFound.show()
                return@launch
            }

            val adapter =
                CaterersListAdapter(
                    requireContext(),
                    filteredCatererList,
                    findNavController(),
                    prefRepository,
                    lifecycleScope)

            venueListRv.initVerticalAdapter(adapter, true)
        }

    }
}
