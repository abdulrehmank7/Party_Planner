package com.arkapp.partyplanner.ui.finalChecklist.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.databinding.DialogDestinationSelectionBinding
import com.arkapp.partyplanner.utils.getDrawableRes
import com.arkapp.partyplanner.utils.setFullWidth
import com.arkapp.partyplanner.utils.setTransparentEdges
import kotlinx.android.synthetic.main.dialog_destination_selection.*

/**
 * Created by Abdul Rehman on 29-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This dialog is used for changing the destination(Home/Other venue) in the checklist screen
 * */
class DialogDestinationSelection(context: Context,
                                 private val prefRepository: PrefRepository) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = DialogDestinationSelectionBinding.inflate(LayoutInflater.from(context))

        setContentView(binding.root)

        window?.setTransparentEdges()
        window?.setFullWidth()

        binding.homeParty.setOnClickListener {
            binding.homeParty.background = context.getDrawableRes(R.drawable.bg_selected_start)
            binding.venueParty.background = context.getDrawableRes(R.drawable.bg_unselected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyDestination = context.getString(R.string.home)
            prefRepository.setCurrentPartyDetails(details)
        }

        binding.venueParty.setOnClickListener {
            binding.homeParty.background = context.getDrawableRes(R.drawable.bg_unselected_start)
            binding.venueParty.background = context.getDrawableRes(R.drawable.bg_selected_end)

            val details = prefRepository.getCurrentPartyDetails()
            details.partyDestination = context.getString(R.string.other_venue)
            prefRepository.setCurrentPartyDetails(details)
        }

        doneBtn.setOnClickListener {
            dismiss()
        }
    }
}