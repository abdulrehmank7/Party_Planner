package com.arkapp.partyplanner.ui.finalChecklist.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.databinding.DialogChangePartyTypeBinding
import com.arkapp.partyplanner.ui.home.PartyTypeAdapter
import com.arkapp.partyplanner.utils.*
import kotlinx.android.synthetic.main.dialog_destination_selection.*

/**
 * Created by Abdul Rehman on 29-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
class DialogChangePartyType(context: Context,
                            private val prefRepository: PrefRepository) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = DialogChangePartyTypeBinding.inflate(LayoutInflater.from(context))

        setContentView(binding.root)

        window?.setTransparentEdges()
        window?.setFullWidth()

        setCanceledOnTouchOutside(false)
        setCancelable(false)

        val partyTypeAdapter = PartyTypeAdapter(getPartyTypes(), prefRepository)
        partyTypeAdapter.selectedPartyType = getPartyTypeFromStringArray(prefRepository.getCurrentPartyDetails().partyType)
        binding.selectedPartyTypeRv.initGridAdapter(partyTypeAdapter, true, 3)

        doneBtn.setOnClickListener {
            if (prefRepository.getCurrentPartyDetails().partyType.isEmpty()) {
                context.toastShort("Please select party type")
                return@setOnClickListener
            }
            dismiss()
        }
    }
}