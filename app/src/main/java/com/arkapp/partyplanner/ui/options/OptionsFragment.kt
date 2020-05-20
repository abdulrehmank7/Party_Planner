package com.arkapp.partyplanner.ui.options

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.CURRENT_SELECTED_OPTION
import com.arkapp.partyplanner.utils.ENTERED_USER_NAME
import com.arkapp.partyplanner.utils.OPTION_CREATE
import com.arkapp.partyplanner.utils.OPTION_UNFINISHED
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
            CURRENT_SELECTED_OPTION = OPTION_UNFINISHED
            findNavController().navigate(R.id.action_optionsFragment_to_homeFragment)
        }

    }

    private fun addUserName() {
        lifecycleScope.launch(Dispatchers.Main) {
            val userLoginDao = AppDatabase.getDatabase(requireContext()).userLoginDao()
            val userData = userLoginDao.checkLoggedInUser(ENTERED_USER_NAME)

            prefRepository.setCurrentUser(userData[0])
        }
    }
}
