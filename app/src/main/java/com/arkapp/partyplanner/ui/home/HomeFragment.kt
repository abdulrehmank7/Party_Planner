package com.arkapp.partyplanner.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.databinding.FragmentHomeBinding
import com.arkapp.partyplanner.utils.hide
import com.arkapp.partyplanner.utils.show
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.calendarView.minDate = Calendar.getInstance().timeInMillis

        binding.proceedBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_foodListFragment)
        }

        initPartyTypeBtn()
        initBudgetBtn()
        initDestinationBtn()

    }

    private fun initPartyTypeBtn() {
        binding.babyShowerBtn.setOnClickListener {
            binding.partyCheck.hide()
            binding.babyShowerCheck.show()
        }

        binding.normalPartyBtn.setOnClickListener {
            binding.partyCheck.show()
            binding.babyShowerCheck.hide()
        }
    }

    private fun initBudgetBtn() {
        binding.lowBudget.setOnClickListener {
            binding.budget.text = getString(R.string.low)

            binding.lowBudget.background = requireContext().getDrawable(R.drawable.bg_selected_start)
            binding.mediumBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_end)
        }

        binding.mediumBudget.setOnClickListener {
            binding.budget.text = getString(R.string.medium)

            binding.lowBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawable(R.drawable.bg_selected)
            binding.highBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_end)
        }

        binding.highBudget.setOnClickListener {
            binding.budget.text = getString(R.string.high)

            binding.lowBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawable(R.drawable.bg_selected)
            binding.veryHighBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_end)
        }

        binding.veryHighBudget.setOnClickListener {
            binding.budget.text = getString(R.string.very_high)

            binding.lowBudget.background = requireContext().getDrawable(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.highBudget.background = requireContext().getDrawable(R.drawable.bg_unselected)
            binding.veryHighBudget.background = requireContext().getDrawable(R.drawable.bg_selected_end)
        }
    }

    private fun initDestinationBtn() {
        binding.homeParty.setOnClickListener {
            binding.homeParty.background = requireContext().getDrawable(R.drawable.bg_selected_start)
            binding.venueParty.background = requireContext().getDrawable(R.drawable.bg_unselected_end)
        }

        binding.venueParty.setOnClickListener {
            binding.homeParty.background = requireContext().getDrawable(R.drawable.bg_unselected_start)
            binding.venueParty.background = requireContext().getDrawable(R.drawable.bg_selected_end)
        }
    }
}
