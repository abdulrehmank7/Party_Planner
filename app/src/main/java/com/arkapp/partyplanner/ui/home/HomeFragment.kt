package com.arkapp.partyplanner.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.PartyDetails
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.databinding.FragmentHomeBinding
import com.arkapp.partyplanner.utils.*
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var partyTypeAdapter: PartyTypeAdapter
    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Creating the binding variable which will contain all the view of the layout.
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetPartyData()
        initPartyTypeUI()
        initBudgetBtnListener()
        initDestinationBtnListener()
        initCalendar()

        //Used to store the guest count
        binding.guestEt.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                binding.guest.error = null
                val details = prefRepository.currentPartyDetails
                details.partyGuest = text.toString().toInt()
                prefRepository.currentPartyDetails = details
            }
        }

        //Validating the screen input on button click
        binding.proceedBtn.setOnClickListener {
            if (binding.guestEt.text.toString().isEmpty() ||
                binding.guestEt.text.toString().toInt() <= 0) {
                binding.guest.error = "Please enter guest count!"
                return@setOnClickListener
            }

            if (prefRepository.currentPartyDetails.partyType.isEmpty()) {
                requireContext().toastShort("Please select party type")
                return@setOnClickListener
            }

            binding.guest.error = null

            AddUnfinishedAsyncTask(requireActivity(), prefRepository).execute()
            findNavController().navigate(R.id.action_homeFragment_to_caterersListFragment)
        }

        if (CURRENT_SELECTED_OPTION == OPTION_UNFINISHED)
            setUnfinishedPartyData()
        else
            setLastEnteredData()

        //Handling back click
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                prefRepository.currentPartyDetails = PartyDetails(
                    null,
                    null,
                    null,
                    null,
                    null,
                    ArrayList<String>(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    ArrayList<String>())
                this.remove()
                requireActivity().onBackPressed()
                true
            }
    }

    private class GetUnfinishedSummaryAsyncTask(private val context: Activity,
                                                private val prefRepository: PrefRepository,
                                                val taskListener: GetUnfinishedSummaryListener) : AsyncTask<Void, Void, PartyDetails?>() {

        override fun doInBackground(vararg params: Void?): PartyDetails? {
            val unfinishedDao = AppDatabase.Companion().getDatabase(context).unfinishedDao()
            val unfinishedData = unfinishedDao.getUserUnfinished(prefRepository.currentUser?.uid!!)
            return convertPartyFromUnfinished(unfinishedData[0])
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(unfinisedSummary: PartyDetails?) {
            taskListener.onTaskEnded(unfinisedSummary)
        }
    }


    //Setting the Unfinished party data in SQL
    private fun setUnfinishedPartyData() {
        val taskListener = GetUnfinishedSummaryListener { unfinishedDetail ->
            unfinishedDetail.partyDate.also {
                if (it != null) {
                    val selectedDate = Calendar.getInstance()
                    selectedDate.time = it
                    binding.calendarView.date = selectedDate.timeInMillis
                }
            }

            unfinishedDetail.partyBudget.also {
                if (it != null) {
                    when (it) {
                        getString(R.string.low) -> binding.lowBudget.performClick()
                        getString(R.string.medium) -> binding.mediumBudget.performClick()
                        getString(R.string.high) -> binding.highBudget.performClick()
                        getString(R.string.very_high) -> binding.veryHighBudget.performClick()
                        else -> binding.lowBudget.performClick()
                    }
                }
            }

            unfinishedDetail.partyDestination.also {
                if (it != null) {
                    if (it == getString(R.string.home)) {
                        binding.homeParty.performClick()
                    } else
                        binding.venueParty.performClick()
                }
            }

            unfinishedDetail.partyGuest.also {
                if (it != null) {
                    binding.guestEt.setText(it.toString())
                }
            }

            unfinishedDetail.partyType.also {
                partyTypeAdapter.selectedPartyType = getPartyTypeFromStringArray(it)
                partyTypeAdapter.notifyDataSetChanged()
                val details = prefRepository.currentPartyDetails
                details.partyType = it
                prefRepository.currentPartyDetails = details
            }
        }
        GetUnfinishedSummaryAsyncTask(requireActivity(), prefRepository, taskListener).execute()
    }

    //Initializing the calendar UI
    private fun initCalendar() {
        binding.calendarView.minDate = Calendar.getInstance().timeInMillis

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val selectedPartyDate = Calendar.getInstance()
            selectedPartyDate.set(Calendar.YEAR, year)
            selectedPartyDate.set(Calendar.MONTH, month)
            selectedPartyDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val details = prefRepository.currentPartyDetails
            details.partyDate = selectedPartyDate.time
            prefRepository.currentPartyDetails = details
        }
    }

    //Initializing the Budget UI
    private fun initBudgetBtnListener() {
        binding.lowBudget.setOnClickListener {
            binding.budget.text = getString(R.string.low)

            binding.lowBudget.background = requireContext().getDrawableRes(R.drawable.bg_selected_start)
            binding.mediumBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.currentPartyDetails
            details.partyBudget = getString(R.string.low)
            prefRepository.currentPartyDetails = details
        }

        binding.mediumBudget.setOnClickListener {
            binding.budget.text = getString(R.string.medium)

            binding.lowBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawableRes(R.drawable.bg_selected)
            binding.highBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.currentPartyDetails
            details.partyBudget = getString(R.string.medium)
            prefRepository.currentPartyDetails = details
        }

        binding.highBudget.setOnClickListener {
            binding.budget.text = getString(R.string.high)

            binding.lowBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawableRes(R.drawable.bg_selected)
            binding.veryHighBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.currentPartyDetails
            details.partyBudget = getString(R.string.high)
            prefRepository.currentPartyDetails = details
        }

        binding.veryHighBudget.setOnClickListener {
            binding.budget.text = getString(R.string.very_high)

            binding.lowBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawableRes(R.drawable.bg_selected_end)

            val details = prefRepository.currentPartyDetails
            details.partyBudget = getString(R.string.very_high)
            prefRepository.currentPartyDetails = details
        }
    }

    //Initializing the Destination selection UI
    private fun initDestinationBtnListener() {
        binding.homeParty.setOnClickListener {
            binding.homeParty.background = requireContext().getDrawableRes(R.drawable.bg_selected_start)
            binding.venueParty.background = requireContext().getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.currentPartyDetails
            details.partyDestination = getString(R.string.home)
            prefRepository.currentPartyDetails = details
        }

        binding.venueParty.setOnClickListener {
            binding.homeParty.background = requireContext().getDrawable(R.drawable.bg_unselected_start)
            binding.venueParty.background = requireContext().getDrawable(R.drawable.bg_selected_end)

            val details = prefRepository.currentPartyDetails
            details.partyDestination = getString(R.string.other_venue)
            prefRepository.currentPartyDetails = details
        }
    }

    //Set the last entered data
    private fun setLastEnteredData() {
        val detail = prefRepository.currentPartyDetails

        detail.partyDate.also {
            if (it != null) {
                val selectedDate = Calendar.getInstance()
                selectedDate.time = it
                binding.calendarView.date = selectedDate.timeInMillis
            }
        }

        detail.partyBudget.also {
            if (it != null) {
                when (it) {
                    getString(R.string.low) -> binding.lowBudget.performClick()
                    getString(R.string.medium) -> binding.mediumBudget.performClick()
                    getString(R.string.high) -> binding.highBudget.performClick()
                    getString(R.string.very_high) -> binding.veryHighBudget.performClick()
                    else -> binding.lowBudget.performClick()
                }
            }
        }

        detail.partyDestination.also {
            if (it != null) {
                if (it == getString(R.string.home)) {
                    binding.homeParty.performClick()
                } else
                    binding.venueParty.performClick()
            }
        }

        detail.partyGuest.also {
            if (it != null) {
                binding.guestEt.setText(it.toString())
            }
        }

        detail.partyType.also {
            partyTypeAdapter.selectedPartyType = getPartyTypeFromStringArray(it)
            partyTypeAdapter.notifyDataSetChanged()
        }
    }

    private fun initPartyTypeUI() {
        partyTypeAdapter = PartyTypeAdapter(getPartyTypes(), prefRepository)
        binding.partyTypeRv.initGridAdapter(partyTypeAdapter, true, 3)
    }

    private fun resetPartyData() {
        val details = PartyDetails(
            null,
            Calendar.getInstance().time,
            getString(R.string.low),
            getString(R.string.home),
            null,
            ArrayList<String>(),
            null,
            null,
            null,
            null,
            null,
            ArrayList<String>()
        )
        prefRepository.currentPartyDetails = details
    }
}
