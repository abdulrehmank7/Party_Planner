package com.arkapp.partyplanner.ui.finalChecklist.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.databinding.DialogChangeBudgetBinding
import com.arkapp.partyplanner.utils.getDrawableRes
import com.arkapp.partyplanner.utils.setFullWidth
import com.arkapp.partyplanner.utils.setTransparentEdges
import kotlinx.android.synthetic.main.dialog_destination_selection.*

/**
 * Created by Abdul Rehman on 29-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This dialog is used for changing the budget in the checklist screen
 * */
class DialogChangeBudget(context: Context,
                         private val prefRepository: PrefRepository) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = DialogChangeBudgetBinding.inflate(LayoutInflater.from(context))

        setContentView(binding.root)

        window?.setTransparentEdges()
        window?.setFullWidth()

        binding.lowBudget.setOnClickListener {

            binding.lowBudget.background = context.getDrawableRes(R.drawable.bg_selected_start)
            binding.mediumBudget.background = context.getDrawableRes(R.drawable.bg_unselected)
            binding.highBudget.background = context.getDrawableRes(R.drawable.bg_unselected)
            binding.veryHighBudget.background = context.getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = context.getString(R.string.low)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.mediumBudget.setOnClickListener {

            binding.lowBudget.background = context.getDrawableRes(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = context.getDrawableRes(R.drawable.bg_selected)
            binding.highBudget.background = context.getDrawableRes(R.drawable.bg_unselected)
            binding.veryHighBudget.background = context.getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = context.getString(R.string.medium)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.highBudget.setOnClickListener {

            binding.lowBudget.background = context.getDrawableRes(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = context.getDrawableRes(R.drawable.bg_unselected)
            binding.highBudget.background = context.getDrawableRes(R.drawable.bg_selected)
            binding.veryHighBudget.background = context.getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = context.getString(R.string.high)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.veryHighBudget.setOnClickListener {

            binding.lowBudget.background = context.getDrawableRes(R.drawable.bg_unselected_start)
            binding.mediumBudget.background = context.getDrawableRes(R.drawable.bg_unselected)
            binding.highBudget.background = context.getDrawableRes(R.drawable.bg_unselected)
            binding.veryHighBudget.background = context.getDrawableRes(R.drawable.bg_selected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyBudget = context.getString(R.string.very_high)
            prefRepository.setCurrentPartyDetails(details)
        }

        doneBtn.setOnClickListener {
            dismiss()
        }
    }
}