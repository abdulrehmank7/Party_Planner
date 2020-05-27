package com.arkapp.partyplanner.ui.finalChecklist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.PartyType
import com.arkapp.partyplanner.ui.home.PartyTypeViewHolder
import com.arkapp.partyplanner.utils.loadImage

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

class SelectedPartyTypeAdapter(private val partyTypes: List<PartyType>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

        binding.parent.isEnabled = false
    }


    override fun getItemCount() = partyTypes.size

    override fun getItemId(position: Int): Long {
        return partyTypes[position].hashCode().toLong()
    }

}