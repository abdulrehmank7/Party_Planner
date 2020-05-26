package com.arkapp.partyplanner.ui.venueLocation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.utils.addUnfinishedData
import com.arkapp.partyplanner.utils.getLocation
import com.arkapp.partyplanner.utils.initGridAdapter
import kotlinx.android.synthetic.main.fragment_venue_location.*

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

        proceedBtn.setOnClickListener {
            addUnfinishedData(lifecycleScope, requireContext(), prefRepository)
            findNavController().navigate(R.id.action_venueLocationFragment_to_venueListFragment)
        }
    }

}
