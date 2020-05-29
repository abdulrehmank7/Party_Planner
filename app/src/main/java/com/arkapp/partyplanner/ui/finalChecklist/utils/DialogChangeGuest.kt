package com.arkapp.partyplanner.ui.finalChecklist.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.widget.doAfterTextChanged
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.databinding.DialogChangeGuestBinding
import com.arkapp.partyplanner.utils.setFullWidth
import com.arkapp.partyplanner.utils.setTransparentEdges
import kotlinx.android.synthetic.main.dialog_destination_selection.*

/**
 * Created by Abdul Rehman on 29-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
class DialogChangeGuest(context: Context,
                        private val prefRepository: PrefRepository) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = DialogChangeGuestBinding.inflate(LayoutInflater.from(context))

        setContentView(binding.root)

        window?.setTransparentEdges()
        window?.setFullWidth()

        binding.guestEt.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                binding.guest.error = null
                val details = prefRepository.getCurrentPartyDetails()
                details.partyGuest = text.toString().toInt()
                prefRepository.setCurrentPartyDetails(details)
            }
        }


        doneBtn.setOnClickListener {
            if (binding.guestEt.text.toString().isEmpty() ||
                binding.guestEt.text.toString().toInt() <= 0) {
                binding.guest.error = "Please enter guest count!"
                return@setOnClickListener
            }

            dismiss()
        }
    }
}