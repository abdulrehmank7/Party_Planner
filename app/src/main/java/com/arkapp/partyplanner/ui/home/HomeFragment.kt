package com.arkapp.partyplanner.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.PartyDetails
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.databinding.FragmentHomeBinding
import com.arkapp.partyplanner.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var partyTypeAdapter: PartyTypeAdapter
    private val prefRepository by lazy { PrefRepository(requireContext()) }
    private val gson = Gson()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

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

        binding.guestEt.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                binding.guest.error = null
                val details = prefRepository.getCurrentPartyDetails()
                details.partyGuest = text.toString().toInt()
                prefRepository.setCurrentPartyDetails(details)
            }
        }

        binding.proceedBtn.setOnClickListener {
            if (binding.guestEt.text.toString().isEmpty() ||
                binding.guestEt.text.toString().toInt() <= 0) {
                binding.guest.error = "Please enter guest count!"
                return@setOnClickListener
            }

            if (prefRepository.getCurrentPartyDetails().partyType.isEmpty()) {
                requireContext().toastShort("Please select party type")
                return@setOnClickListener
            }

            binding.guest.error = null

            addUnfinishedData(lifecycleScope, requireContext(), prefRepository)
            findNavController().navigate(R.id.action_homeFragment_to_caterersListFragment)
        }

        if (CURRENT_SELECTED_OPTION == OPTION_UNFINISHED)
            setUnfinishedPartyData()
        else
            setLastEnteredData()

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                prefRepository
                    .setCurrentPartyDetails(
                        PartyDetails(
                            null,
                            null,
                            null,
                            null,
                            null,
                            ArrayList(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            ArrayList())
                    )
                this.remove()
                requireActivity().onBackPressed()
                true
            }
    }

    private fun setUnfinishedPartyData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val unfinishedDao = AppDatabase.getDatabase(requireContext()).unfinishedDao()
            val unfinishedData = unfinishedDao.getUserUnfinished(prefRepository.getCurrentUser()?.uid!!)

            val unfinishedDetail = unfinishedData[0]

            unfinishedDetail.partyDate.also {
                if (it != null) {

                    val time = gson.fromJson(it, Date::class.java)
                    if (time != null) {
                        val selectedDate = Calendar.getInstance()
                        selectedDate.time = time
                        binding.calendarView.date = selectedDate.timeInMillis
                    }
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
                if (it != null) {
                    val type = object : TypeToken<ArrayList<String>>() {}.type
                    partyTypeAdapter.selectedPartyType =
                        getPartyTypeFromStringArray(gson.fromJson(it, type))
                    partyTypeAdapter.notifyDataSetChanged()
                }
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

            binding.lowBudget.background = requireContext().getDrawableRes(R.drawable.bg_selected_start)
            binding.mediumBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = getString(R.string.low)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.mediumBudget.setOnClickListener {
            binding.budget.text = getString(R.string.medium)

            binding.lowBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawableRes(R.drawable.bg_selected)
            binding.highBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = getString(R.string.medium)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.highBudget.setOnClickListener {
            binding.budget.text = getString(R.string.high)

            binding.lowBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawableRes(R.drawable.bg_selected)
            binding.veryHighBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = getString(R.string.high)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.veryHighBudget.setOnClickListener {
            binding.budget.text = getString(R.string.very_high)

            binding.lowBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawableRes(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawableRes(R.drawable.bg_selected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = getString(R.string.very_high)
            prefRepository.setCurrentPartyDetails(details)
        }
    }

    private fun initDestinationBtnListener() {
        binding.homeParty.setOnClickListener {
            binding.homeParty.background = requireContext().getDrawableRes(R.drawable.bg_selected_start)
            binding.venueParty.background = requireContext().getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyDestination = getString(R.string.home)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.venueParty.setOnClickListener {
            binding.homeParty.background = requireContext().getDrawableRes(R.drawable.bg_unselected_start)
            binding.venueParty.background = requireContext().getDrawableRes(R.drawable.bg_selected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyDestination = getString(R.string.other_venue)
            prefRepository.setCurrentPartyDetails(details)
        }
    }

    private fun setLastEnteredData() {
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
            ArrayList(),
            null,
            null,
            null,
            null,
            null,
            ArrayList()
        )
        prefRepository.setCurrentPartyDetails(details)
    }
}
