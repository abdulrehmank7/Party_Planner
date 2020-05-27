package com.arkapp.partyplanner.ui.specialSelection

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.utils.*
import kotlinx.android.synthetic.main.fragment_special_selection.*
import kotlinx.android.synthetic.main.rv_location.view.*


class SpecialSelectionFragment : Fragment(R.layout.fragment_special_selection) {

    val prefRepository by lazy { PrefRepository(requireContext()) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        magician.icon.loadImage(R.drawable.ic_tarot)
        magician.title.text = PARTY_TYPE_MAGIC_SHOW

        decoration.icon.loadImage(R.drawable.ic_balloon)
        decoration.title.text = PARTY_TYPE_DECORATION

        magician.setOnClickListener {
            if (magician.tick.isVisible) {

                val details = prefRepository.getCurrentPartyDetails()
                details.partyType.remove(PARTY_TYPE_MAGIC_SHOW)
                prefRepository.setCurrentPartyDetails(details)

                magician.tick.hide()
            } else {

                val details = prefRepository.getCurrentPartyDetails()
                details.partyType.add(PARTY_TYPE_MAGIC_SHOW)
                prefRepository.setCurrentPartyDetails(details)

                magician.tick.show()
            }
        }

        decoration.setOnClickListener {
            if (decoration.tick.isVisible) {

                val details = prefRepository.getCurrentPartyDetails()
                details.partyType.remove(PARTY_TYPE_DECORATION)
                prefRepository.setCurrentPartyDetails(details)

                decoration.tick.hide()
            } else {

                val details = prefRepository.getCurrentPartyDetails()
                details.partyType.add(PARTY_TYPE_DECORATION)
                prefRepository.setCurrentPartyDetails(details)

                decoration.tick.show()
            }
        }

        proceedBtn.setOnClickListener {
            findNavController().navigate(R.id.action_specialSelectionFragment_to_finalChecklistFragment)
        }
    }
}
