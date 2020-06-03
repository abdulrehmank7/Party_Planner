package com.arkapp.partyplanner.ui.venueList

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Venue
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.initVerticalAdapter
import com.arkapp.partyplanner.utils.show
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_venue_list.*


class VenueListFragment : Fragment(R.layout.fragment_venue_list) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Fetching all the venues from the SQL DB and show a list of the result
        VenueAsyncTask(requireActivity(), prefRepository, lifecycleScope).execute()
    }

    private class VenueAsyncTask(private val context: Activity,
                                 private val prefRepository: PrefRepository,
                                 private val lifecycleScope: LifecycleCoroutineScope) : AsyncTask<Void, Void, MutableList<Venue>?>() {

        override fun doInBackground(vararg params: Void?): MutableList<Venue>? {
            val venueDao = AppDatabase.Companion().getDatabase(context).venueDao()
            return venueDao.allVenues
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(venueList: MutableList<Venue>?) {
            val selectedLocation = prefRepository.currentPartyDetails.locations
            val selectedPartyType = prefRepository.currentPartyDetails.partyType

            val filteredVenueList = ArrayList<Venue>()
            val type = object : TypeToken<ArrayList<String>>() {}.type
            val gson = Gson()

            for (venue in venueList!!) {
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
                context.noItemFound.text = "No venue found with the selection.\nChange selection and try again"
                context.noItemFound.show()
                return
            }

            val adapter = VenueListAdapter(
                context,
                filteredVenueList,
                context.findNavController(R.id.fragment),
                prefRepository,
                lifecycleScope)

            context.venueListRv.initVerticalAdapter(adapter, true)
        }
    }
}
