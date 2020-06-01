package com.arkapp.partyplanner.ui.guestList

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.*
import kotlinx.android.synthetic.main.fragment_guest_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
                val partyDetails = prefRepository.getCurrentPartyDetails()
                partyDetails.guestNameList = GUEST_LIST_NAMES
                prefRepository.setCurrentPartyDetails(partyDetails)

                //Storing the guest check list in the SQL when back is pressed.
                this.remove()
                if (CURRENT_SELECTED_OPTION == OPTION_CHECKLIST || CURRENT_SELECTED_OPTION == OPTION_CREATE)
                    updateSummaryData()
                else
                    updateHistorySummaryData()

                true
            }
    }

    //Storing summary in SQL
    private fun updateSummaryData() {
        lifecycleScope.launch(Dispatchers.Main) {
            requireContext().toast("Please wait... Saving data!")
            val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
            summaryDao.delete(prefRepository.getCurrentUser()?.uid!!)
            summaryDao.insert(convertSummary(prefRepository.getCurrentPartyDetails(),
                                             prefRepository.getCurrentUser()?.uid!!))
            findNavController().navigate(R.id.action_guestListFragment_to_finalChecklistFragment)

        }
    }

    //Storing History in SQL
    private fun updateHistorySummaryData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val summaryDao = AppDatabase.getDatabase(requireContext()).historySummaryDao()
            summaryDao.delete(prefRepository.getCurrentPartyDetails().id!!)
            summaryDao.insert(convertHistorySummary(prefRepository.getCurrentPartyDetails(),
                                                    prefRepository.getCurrentUser()?.uid!!))
            findNavController().navigate(R.id.action_guestListFragment_to_finalChecklistFragment)
        }
    }

}
