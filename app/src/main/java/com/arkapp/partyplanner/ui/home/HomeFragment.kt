package com.arkapp.partyplanner.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.databinding.FragmentHomeBinding
import com.arkapp.partyplanner.utils.PARTY_TYPE_BABY_SHOWER
import com.arkapp.partyplanner.utils.PARTY_TYPE_OTHER
import com.arkapp.partyplanner.utils.hide
import com.arkapp.partyplanner.utils.show
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPartyTypeBtnListener()
        initBudgetBtnListener()
        initDestinationBtnListener()
        initCalendar()

        binding.guestEt.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                val details = prefRepository.getCurrentPartyDetails()
                details.partyGuest = text.toString().toInt()
                prefRepository.setCurrentPartyDetails(details)
            }
        }

        binding.proceedBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_foodListFragment)
        }

        initOldPartyData()

    }

    private fun initOldPartyData() {
        val detail = prefRepository.getCurrentPartyDetails()
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
            if (it != null) {
                if (it == PARTY_TYPE_BABY_SHOWER)
                    binding.babyShowerBtn.performClick()
                else
                    binding.normalPartyBtn.performClick()
            }
        }
    }

    private fun initCalendar() {
        binding.calendarView.minDate = Calendar.getInstance().timeInMillis

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val selectedPartyDate = Calendar.getInstance()
            selectedPartyDate.set(Calendar.YEAR, year)
            selectedPartyDate.set(Calendar.MONTH, month)
            selectedPartyDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyDate = selectedPartyDate.time
            prefRepository.setCurrentPartyDetails(details)
        }
    }

    private fun initBudgetBtnListener() {
        binding.lowBudget.setOnClickListener {
            binding.budget.text = getString(R.string.low)

            binding.lowBudget.background = requireContext().getDrawable(R.drawable.bg_selected_start)
            binding.mediumBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = getString(R.string.low)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.mediumBudget.setOnClickListener {
            binding.budget.text = getString(R.string.medium)

            binding.lowBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawable(R.drawable.bg_selected)
            binding.highBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = getString(R.string.medium)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.highBudget.setOnClickListener {
            binding.budget.text = getString(R.string.high)

            binding.lowBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawable(R.drawable.bg_selected)
            binding.veryHighBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = getString(R.string.high)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.veryHighBudget.setOnClickListener {
            binding.budget.text = getString(R.string.very_high)

            binding.lowBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawable(R.drawable.bg_selected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = getString(R.string.very_high)
            prefRepository.setCurrentPartyDetails(details)
        }
    }

    private fun initDestinationBtnListener() {
        binding.homeParty.setOnClickListener {
            binding.homeParty.background = requireContext().getDrawable(R.drawable.bg_selected_start)
            binding.venueParty.background = requireContext().getDrawable(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyDestination = getString(R.string.home)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.venueParty.setOnClickListener {
            binding.homeParty.background = requireContext().getDrawable(R.drawable.bg_unselected_start)
            binding.venueParty.background = requireContext().getDrawable(R.drawable.bg_selected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyDestination = getString(R.string.other_venue)
            prefRepository.setCurrentPartyDetails(details)
        }
    }

    private fun initPartyTypeBtnListener() {
        binding.babyShowerBtn.setOnClickListener {
            binding.partyCheck.hide()
            binding.babyShowerCheck.show()

            val details = prefRepository.getCurrentPartyDetails()
            details.partyType = PARTY_TYPE_BABY_SHOWER
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.normalPartyBtn.setOnClickListener {
            binding.partyCheck.show()
            binding.babyShowerCheck.hide()

            val details = prefRepository.getCurrentPartyDetails()
            details.partyType = PARTY_TYPE_OTHER
            prefRepository.setCurrentPartyDetails(details)
        }
    }
}
