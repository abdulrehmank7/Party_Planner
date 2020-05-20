package com.arkapp.partyplanner.ui.venueList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.initVerticalAdapter
import kotlinx.android.synthetic.main.fragment_venue_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VenueListFragment : Fragment(R.layout.fragment_venue_list) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {

            val venueDao = AppDatabase.getDatabase(requireContext()).venueDao()
            val venueList = venueDao.getAllVenues()


            val adapter = VenueListAdapter(venueList, findNavController(), prefRepository)
            venueListRv.initVerticalAdapter(adapter, true)
        }

    }
}
