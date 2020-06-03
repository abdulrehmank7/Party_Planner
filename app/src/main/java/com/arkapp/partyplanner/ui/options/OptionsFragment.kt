package com.arkapp.partyplanner.ui.options

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.SummaryDetails
import com.arkapp.partyplanner.data.models.UnfinishedDetails
import com.arkapp.partyplanner.data.models.UserLogin
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.*
import kotlinx.android.synthetic.main.fragment_options.*

/**
 * A simple [Fragment] subclass.
 */


class OptionsFragment : Fragment(R.layout.fragment_options) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OPENED_GUEST_LIST = false

        addUserName()

        //Opening different screen on different options

        createChecklistBtn.setOnClickListener {
            CURRENT_SELECTED_OPTION = OPTION_CREATE
            findNavController().navigate(R.id.action_optionsFragment_to_homeFragment)
        }

        unfinishedChecklistBtn.setOnClickListener {
            requireContext().toastShort("Fetching data...")
            GetUnfinishedSummaryAsyncTask(requireActivity(), prefRepository).execute()
        }

        checklistBtn.setOnClickListener {
            requireContext().toastShort("Fetching data...")
            SummaryAsyncTask(requireActivity(), prefRepository).execute()
        }

        logoutBtn.setOnClickListener {
            requireContext().showAlertDialog(
                "Logout",
                "Do you want to logout?",
                "Logout",
                "Cancel",
                DialogInterface.OnClickListener { dialog, _ ->
                    prefRepository.clearData()
                    findNavController().navigate(R.id.action_optionsFragment_to_splashFragment)
                    requireContext().toastShort("Logged Out!")
                    dialog.dismiss()
                }
            )
        }

        pastChecklistBtn.setOnClickListener {
            CURRENT_SELECTED_OPTION = OPTION_PAST
            findNavController().navigate(R.id.action_optionsFragment_to_historySummaryFragment)
        }
    }

    //Adding username on starting app
    private fun addUserName() {
        if (ENTERED_USER_NAME.isNotEmpty())
            CheckLoggedInUserAsyncTask(requireActivity(), prefRepository).execute()
    }

    private class CheckLoggedInUserAsyncTask(private val context: Activity,
                                             private val prefRepository: PrefRepository) : AsyncTask<Void, Void, MutableList<UserLogin>?>() {

        override fun doInBackground(vararg params: Void?): MutableList<UserLogin>? {
            val userLoginDao = AppDatabase.Companion().getDatabase(context).userLoginDao()
            return userLoginDao.checkLoggedInUser(ENTERED_USER_NAME)
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(data: MutableList<UserLogin>?) {
            prefRepository.setCurrentUser(data!![0])
        }
    }

    private class SummaryAsyncTask(private val context: Activity,
                                   private val prefRepository: PrefRepository) : AsyncTask<Void, Void, MutableList<SummaryDetails>?>() {

        override fun doInBackground(vararg params: Void?): MutableList<SummaryDetails>? {
            val summaryDao = AppDatabase.Companion().getDatabase(context).summaryDao()
            return summaryDao.getUserSummary(prefRepository.currentUser?.uid!!)
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(summaryData: MutableList<SummaryDetails>?) {

            if (summaryData!!.isNotEmpty()) {
                CURRENT_SELECTED_OPTION = OPTION_CHECKLIST
                context.findNavController(R.id.fragment)
                    .navigate(R.id.action_optionsFragment_to_finalChecklistFragment)
            } else
                context.toast("No checklist found!. Please create a new checklist.")
        }
    }


    private class GetUnfinishedSummaryAsyncTask(private val context: Activity,
                                                private val prefRepository: PrefRepository) : AsyncTask<Void, Void, MutableList<UnfinishedDetails>?>() {

        override fun doInBackground(vararg params: Void?): MutableList<UnfinishedDetails>? {
            val unfinishedDao = AppDatabase.Companion().getDatabase(context)
                .unfinishedDao()
            return unfinishedDao.getUserUnfinished(prefRepository.currentUser?.uid!!)
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(unfinisedSummary: MutableList<UnfinishedDetails>?) {
            if (!unfinisedSummary.isNullOrEmpty()) {
                CURRENT_SELECTED_OPTION = OPTION_UNFINISHED
                context.findNavController(R.id.fragment)
                    .navigate(R.id.action_optionsFragment_to_homeFragment)
            } else
                context.toast("No unfinished data found!. Please create a new checklist.")
        }
    }
}
