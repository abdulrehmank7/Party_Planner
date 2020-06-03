package com.arkapp.partyplanner.ui.venueLocation

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.*
import kotlinx.android.synthetic.main.fragment_venue_location.*

/**
 * A simple [Fragment] subclass.
 */
class VenueLocationFragment : Fragment(R.layout.fragment_venue_location) {

    val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val details = prefRepository.currentPartyDetails
        details.locations?.clear()
        prefRepository.currentPartyDetails = details

        //Location Recycler view to show all the location type
        val adapter = LocationAdapter(getLocation(), prefRepository)
        locationRv.initGridAdapter(adapter, true, 2)

        if (!prefRepository.currentPartyDetails.checkedItemList.isNullOrEmpty())
            proceedBtn.text = "Done"

        proceedBtn.setOnClickListener {
            if (!prefRepository.currentPartyDetails.checkedItemList.isNullOrEmpty()) {
                requireContext().toast("Please wait saving data...")
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
            } else {
                AddUnfinishedAsyncTask(requireActivity(), prefRepository).execute()
                findNavController().navigate(R.id.action_venueLocationFragment_to_venueListFragment)
            }
        }
    }

    private class UpdateSummaryAsyncTask(private val context: Activity,
                                         private val prefRepository: PrefRepository) : AsyncTask<Void, Void, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val summaryDao = AppDatabase.Companion().getDatabase(context).summaryDao()
            summaryDao.delete(prefRepository.currentUser?.uid!!)
            summaryDao.insert(convertSummary(prefRepository.currentPartyDetails,
                                             prefRepository.currentUser?.uid!!))
            return null
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(summaryData: Void?) {
            context.findNavController(R.id.fragment)
                .navigate(R.id.action_venueLocationFragment_to_finalChecklistFragment)
        }
    }
}
