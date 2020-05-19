package com.arkapp.partyplanner.ui.venueList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Venue
import com.arkapp.partyplanner.utils.loadImage

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

class VenueListAdapter(
    private val venueList: List<Venue>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VenueListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rv_venue,
                parent,
                false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as VenueListViewHolder).viewBinding

        val venueData = venueList[position]
        binding.venueName.text = venueData.name
        binding.venueAdd.text = venueData.address
        binding.venueImg.loadImage(venueData.resId)

    }


    override fun getItemCount() = venueList.size

    override fun getItemId(position: Int): Long {
        return venueList[position].hashCode().toLong()
    }

}