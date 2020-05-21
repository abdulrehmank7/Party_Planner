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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FinalChecklistFragment : Fragment() {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    private lateinit var details: PartyDetails
    private lateinit var binding: FragmentFinalChecklistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFinalChecklistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (CURRENT_SELECTED_OPTION == OPTION_CHECKLIST) {
            lifecycleScope.launch(Dispatchers.Main) {
                val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
                val summaryData = summaryDao.getUserSummary(prefRepository.getCurrentUser()?.uid!!)

                details = convertPartyFromSummary(summaryData[0])
                setPartyData()
            }
        } else {
            deleteUnfinishedData()
            details = prefRepository.getCurrentPartyDetails()
            setPartyData()
            addSummaryData(prefRepository)
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
                            null,
                            null,
                            null)
                    )
                this.remove()
                findNavController().navigate(R.id.action_finalChecklistFragment_to_optionsFragment)
                true
            }
    }

    @SuppressLint("SetTextI18n")
    private fun setPartyData() {
        binding.partyDate.text = details.partyDate?.getFormattedDate()
        binding.destinationType.text = details.partyDestination
        binding.totalGuest.text = "${details.partyGuest} Guests"
        binding.partyType.text = if (details.partyType == PARTY_TYPE_BABY_SHOWER)
            getString(R.string.baby_shower)
        else
            getString(R.string.party_celebration)

        binding.selectedFoodRv.initVerticalAdapter(
            SelectedFoodListAdapter(details.selectedFood!!),
            true)

        if (details.partyDestination != getString(R.string.home)) {
            binding.include.venueName.text = details.selectedDestination?.name
            binding.include.venueAdd.text = details.selectedDestination?.address
            binding.include.venueImg.loadImage(details.selectedDestination!!.resId)

            binding.include.parent.isEnabled = false
        } else {
            binding.include.parent.hide()
            binding.venueTitle.hide()
        }

        var estimatedBudget = 0.0
        for (food in details.selectedFood!!) {
            estimatedBudget += food.price
        }

        binding.partyBudget.text = "$${estimatedBudget * details.partyGuest!!}"
    }

    private fun addSummaryData(
        prefRepository: PrefRepository) {
        lifecycleScope.launch(Dispatchers.Main) {
            val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
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

}
