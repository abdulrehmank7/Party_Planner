package com.arkapp.partyplanner.ui.venueLocation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.*
import kotlinx.android.synthetic.main.fragment_venue_location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class VenueLocationFragment : Fragment(R.layout.fragment_venue_location) {

    val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val details = prefRepository.getCurrentPartyDetails()
        details.locations?.clear()
        prefRepository.setCurrentPartyDetails(details)

        val adapter = LocationAdapter(getLocation(), prefRepository)
        locationRv.initGridAdapter(adapter, true, 2)

        if (!prefRepository.getCurrentPartyDetails().checkedItemList.isNullOrEmpty())
            proceedBtn.text = "Done"

        proceedBtn.setOnClickListener {
            if (!prefRepository.getCurrentPartyDetails().checkedItemList.isNullOrEmpty())
                updateSummaryData()
            else {
                addUnfinishedData(lifecycleScope, requireContext(), prefRepository)
                findNavController().navigate(R.id.action_venueLocationFragment_to_venueListFragment)
            }
        }
    }

    private fun updateSummaryData() {
        lifecycleScope.launch(Dispatchers.Main) {
            requireContext().toast("Please wait saving data...")
            val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
            summaryDao.delete(prefRepository.getCurrentUser()?.uid!!)
            summaryDao.insert(convertSummary(prefRepository.getCurrentPartyDetails(),
                                             prefRepository.getCurrentUser()?.uid!!))
            findNavController().navigate(R.id.action_venueLocationFragment_to_finalChecklistFragment)

        }
    }
}
