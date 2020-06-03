package com.arkapp.partyplanner.ui.guestList

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.*
import kotlinx.android.synthetic.main.fragment_guest_list.*


class GuestListFragment : Fragment(R.layout.fragment_guest_list) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OPENED_GUEST_LIST = true

        val adapter = GuestListAdapter(GUEST_LIST_NAMES)
        guestListRv.initVerticalAdapter(adapter, true)

        //Handling the back click
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                println("guest list back pressed")
                val partyDetails = prefRepository.currentPartyDetails
                partyDetails.guestNameList = GUEST_LIST_NAMES
                prefRepository.currentPartyDetails = partyDetails

                //Storing the guest check list in the SQL when back is pressed.
                this.remove()
                if (CURRENT_SELECTED_OPTION == OPTION_CHECKLIST || CURRENT_SELECTED_OPTION == OPTION_CREATE) {
                    requireContext().toast("Please wait... Saving data!")
                    UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
                } else
                    UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()

                true
            }
    }

    //Storing summary in SQL
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
                .navigate(R.id.action_guestListFragment_to_finalChecklistFragment)
        }
    }

    //Storing History in SQL
    private class UpdateHistorySummaryAsyncTask(private val context: Activity,
                                                private val prefRepository: PrefRepository) : AsyncTask<Void, Void, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val summaryDao = AppDatabase.Companion().getDatabase(context).historySummaryDao()
            summaryDao.delete(prefRepository.currentPartyDetails.id!!)
            summaryDao.insert(convertHistorySummary(prefRepository.currentPartyDetails,
                                                    prefRepository.currentUser?.uid!!))
            return null
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(summaryList: Void?) {
            context.findNavController(R.id.fragment)
                .navigate(R.id.action_guestListFragment_to_finalChecklistFragment)
        }
    }

}
