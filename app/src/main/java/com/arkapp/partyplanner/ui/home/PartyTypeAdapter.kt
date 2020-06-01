package com.arkapp.partyplanner.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.PartyType
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.utils.hide
import com.arkapp.partyplanner.utils.loadImage
import com.arkapp.partyplanner.utils.show

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * Recycler view adapter of selected party type in the home screen
 * */
class PartyTypeAdapter(private val partyTypes: List<PartyType>,
                       private val prefRepository: PrefRepository) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedPartyType = ArrayList<PartyType>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PartyTypeViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rv_party_type,
                parent,
                false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as PartyTypeViewHolder).viewBinding

        val party = partyTypes[position]
        binding.title.text = party.title
        binding.icon.loadImage(party.resId)

        if (selectedPartyType.contains(party))
            binding.tick.show()

        binding.parent.setOnClickListener {
            if (binding.tick.isVisible) {
                selectedPartyType.remove(party)
                binding.tick.hide()

                val details = prefRepository.getCurrentPartyDetails()
                details.partyType.remove(party.title)
                prefRepository.setCurrentPartyDetails(details)

            } else {
                selectedPartyType.add(party)
                binding.tick.show()

                val details = prefRepository.getCurrentPartyDetails()
                details.partyType.add(party.title)
                prefRepository.setCurrentPartyDetails(details)
            }
        }
    }


    override fun getItemCount() = partyTypes.size

    override fun getItemId(position: Int): Long {
        return partyTypes[position].hashCode().toLong()
    }

}