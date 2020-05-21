package com.arkapp.partyplanner.ui.options

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.*
import kotlinx.android.synthetic.main.fragment_options.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class OptionsFragment : Fragment(R.layout.fragment_options) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addUserName()

        createChecklistBtn.setOnClickListener {
            CURRENT_SELECTED_OPTION = OPTION_CREATE
            findNavController().navigate(R.id.action_optionsFragment_to_homeFragment)
        }

        unfinishedChecklistBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                val unfinishedDao = AppDatabase.getDatabase(requireContext()).unfinishedDao()
                val unfinishedData = unfinishedDao.getUserUnfinished(prefRepository.getCurrentUser()?.uid!!)

                if (unfinishedData.isNotEmpty()) {
                    CURRENT_SELECTED_OPTION = OPTION_UNFINISHED
                    findNavController().navigate(R.id.action_optionsFragment_to_homeFragment)
                } else
                    requireContext().toast("No unfinished data found!. Please create a new checklist.")
            }
        }

        checklistBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
                val summaryData = summaryDao.getUserSummary(prefRepository.getCurrentUser()?.uid!!)

                if (summaryData.isNotEmpty()) {
                    CURRENT_SELECTED_OPTION = OPTION_CHECKLIST
                    findNavController().navigate(R.id.action_optionsFragment_to_finalChecklistFragment)
                } else
                    requireContext().toast("No checklist found!. Please create a new checklist.")
            }
        }

    }

    private fun addUserName() {
        if (ENTERED_USER_NAME.isNotEmpty()) {
            lifecycleScope.launch(Dispatchers.Main) {
                val userLoginDao = AppDatabase.getDatabase(requireContext()).userLoginDao()
                val userData = userLoginDao.checkLoggedInUser(ENTERED_USER_NAME)

                prefRepository.setCurrentUser(userData[0])
            }
        }
    }
}
