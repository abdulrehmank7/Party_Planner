package com.arkapp.partyplanner.ui.finalChecklist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.PartyDetails
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.databinding.FragmentFinalChecklistBinding
import com.arkapp.partyplanner.utils.*
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FinalChecklistFragment : Fragment() {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    private lateinit var details: PartyDetails
    private lateinit var binding: FragmentFinalChecklistBinding
    private val arrayListType = object : TypeToken<ArrayList<String>>() {}.type!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFinalChecklistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (CURRENT_SELECTED_OPTION) {
            OPTION_CHECKLIST -> {
                lifecycleScope.launch(Dispatchers.Main) {
                    val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
                    val summaryData = summaryDao.getUserSummary(prefRepository.getCurrentUser()?.uid!!)

                    details = convertPartyFromSummary(summaryData[0])
                    setAllPartyData()
                }
            }
            OPTION_PAST -> {
                deleteUnfinishedData()
                details = prefRepository.getCurrentPartyDetails()
                setAllPartyData()
            }
            else -> {
                deleteUnfinishedData()
                details = prefRepository.getCurrentPartyDetails()
                setAllPartyData()
                addSummaryData()
                addHistorySummaryData()
            }
        }

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
                            ArrayList(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            ArrayList())
                    )
                this.remove()
                if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                    findNavController().navigate(R.id.action_finalChecklistFragment_to_historySummaryFragment)
                else
                    findNavController().navigate(R.id.action_finalChecklistFragment_to_optionsFragment)
                true
            }
    }

    @SuppressLint("SetTextI18n")
    private fun setAllPartyData() {
        binding.partyDate.text = details.partyDate?.getFormattedDate()
        binding.destinationType.text = details.partyDestination
        binding.totalGuest.text = "${details.partyGuest} Guests"

        setLocations()
        setBudget()
        setSelectedPartyTypes()
        setCatererDetails()
        setVenueDetails()
        setSpecialDetails()
    }

    private fun setLocations() {
        var locationString = ""
        for (x in details.locations!!) {
            locationString += "$x, "
        }
        binding.locationSelected.text = locationString.substring(0, locationString.length - 2)
    }

    @SuppressLint("SetTextI18n")
    private fun setBudget() {
        val budgetLimit = when (details.partyBudget) {
            getString(R.string.low) -> "$200 - $400"
            getString(R.string.medium) -> "$400 - $600"
            getString(R.string.high) -> "$600 - $800"
            getString(R.string.very_high) -> "More than $800"
            else -> "$200 - $400"
        }
        binding.budgetSelected.text = "$budgetLimit (${details.partyBudget})"
        binding.partyBudget.text = "$${details.selectedCaterer!!.pricePerPax * details.partyGuest!!}"
    }

    private fun setSelectedPartyTypes() {
        binding.selectedPartyTypeRv.initGridAdapter(
            SelectedPartyTypeAdapter(getPartyTypeFromStringArray(details.partyType)), true, 3)
    }

    @SuppressLint("SetTextI18n")
    private fun setCatererDetails() {
        binding.include2.name.text = details.selectedCaterer!!.name.trim()
        binding.include2.price.text = "$${details.selectedCaterer!!.pricePerPax}"
        val partyTypes = gson.fromJson<ArrayList<String>>(details.selectedCaterer!!.partyType,
                                                          arrayListType)
        var partyTypeString = ""
        for (x in partyTypes) {
            partyTypeString += "$x, "
        }
        binding.include2.partyTypeValue.text = partyTypeString.substring(0,
                                                                         partyTypeString.length - 2)
        binding.include2.parent.isEnabled = false
    }

    @SuppressLint("SetTextI18n")
    private fun setVenueDetails() {
        if (details.partyDestination != getString(R.string.home)) {
            binding.include.venueName.text = details.selectedDestination!!.name
            binding.include.venueAdd.text = details.selectedDestination!!.address
            binding.include.capacity.text = "${details.selectedDestination!!.capacity} Guest"
            binding.include.contact.text = details.selectedDestination!!.contact
            binding.include.price.text = "$${details.selectedDestination!!.price}"
            binding.include.location.text = details.selectedDestination!!.location
            val partyTypes = gson.fromJson<ArrayList<String>>(details.selectedDestination!!.partyType,
                                                              arrayListType)
            var partyTypeStringVenue = ""

            for (x in partyTypes) {
                partyTypeStringVenue += "$x, "
            }
            binding.include.suitable.text = partyTypeStringVenue.substring(0,
                                                                           partyTypeStringVenue.length - 2)
            binding.include.parent.isEnabled = false
        } else {
            binding.include.parent.hide()
            binding.venueTitle.hide()
        }
    }

    private fun setSpecialDetails() {
        if (details.partyType.contains(PARTY_TYPE_MAGIC_SHOW))
            binding.magicianDetails.show()

        if (details.partyType.contains(PARTY_TYPE_DECORATION))
            binding.decoratorDetails.show()

        if (details.partyType.contains(PARTY_TYPE_ALCOHOL))
            binding.alcoholDetails.show()
    }




    private fun addSummaryData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
            summaryDao.delete(prefRepository.getCurrentUser()?.uid!!)
            summaryDao.insert(convertSummary(prefRepository.getCurrentPartyDetails(),
                                             prefRepository.getCurrentUser()?.uid!!))
        }
    }

    private fun deleteUnfinishedData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val unfinishedDao = AppDatabase.getDatabase(requireContext()).unfinishedDao()
            unfinishedDao.delete(prefRepository.getCurrentUser()?.uid!!)
        }
    }

    private fun addHistorySummaryData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val summaryDao = AppDatabase.getDatabase(requireContext()).historySummaryDao()
            summaryDao.insert(convertHistorySummary(prefRepository.getCurrentPartyDetails(),
                                                    prefRepository.getCurrentUser()?.uid!!))
        }
    }

}
