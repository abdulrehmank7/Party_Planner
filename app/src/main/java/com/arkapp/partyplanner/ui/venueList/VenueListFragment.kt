package com.arkapp.partyplanner.ui.venueList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Venue
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.initVerticalAdapter
import com.arkapp.partyplanner.utils.show
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_venue_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VenueListFragment : Fragment(R.layout.fragment_venue_list) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedLocation = prefRepository.getCurrentPartyDetails().locations
        val selectedPartyType = prefRepository.getCurrentPartyDetails().partyType

        val filteredVenueList = ArrayList<Venue>()
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val gson = Gson()


        //Fetching all the venues from the SQL DB and show a list of the result
        lifecycleScope.launch(Dispatchers.Main) {

            val venueDao = AppDatabase.getDatabase(requireContext()).venueDao()
            val venueList = venueDao.getAllVenues()

            for (venue in venueList) {
                if (selectedLocation!!.contains(venue.location)) {
                    println("venue location ${venue.location}")
                    val venuePartyType = gson.fromJson<ArrayList<String>>(venue.partyType, type)
                    for (partyType in venuePartyType) {
                        if (selectedPartyType.contains(partyType)) {
                            filteredVenueList.add(venue)
                            break
                        }
                    }
                }
            }

            if (filteredVenueList.isEmpty()) {
                noItemFound.text = "No venue found with the selection.\nChange selection and try again"
                noItemFound.show()
                return@launch
            }


            val adapter = VenueListAdapter(
                requireContext(),
                filteredVenueList,
                findNavController(),
                prefRepository,
                lifecycleScope)
            venueListRv.initVerticalAdapter(adapter, true)
        }

    }
}
