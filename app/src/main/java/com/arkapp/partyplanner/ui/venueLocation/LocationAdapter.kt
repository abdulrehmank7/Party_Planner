package com.arkapp.partyplanner.ui.venueLocation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Location
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.utils.hide
import com.arkapp.partyplanner.utils.loadImage
import com.arkapp.partyplanner.utils.show

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

class LocationAdapter(private val locations: List<Location>,
                      private val prefRepository: PrefRepository) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var locationSelected = ArrayList<Location>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LocationViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rv_location,
                parent,
                false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as LocationViewHolder).viewBinding

        val location = locations[position]
        binding.title.text = location.name
        binding.icon.loadImage(location.resId)

        if (locationSelected.contains(location))
            binding.tick.show()

        when (position) {
            0 -> binding.icon.rotation = -45f
            1 -> binding.icon.rotation = 135f
            3 -> binding.icon.rotation = -135f
            4 -> binding.icon.rotation = 45f
        }

        binding.parent.setOnClickListener {
            if (binding.tick.isVisible) {
                locationSelected.remove(location)
                binding.tick.hide()

                val details = prefRepository.getCurrentPartyDetails()
                details.locations?.remove(location.name)
                prefRepository.setCurrentPartyDetails(details)

            } else {
                locationSelected.add(location)
                binding.tick.show()

                val details = prefRepository.getCurrentPartyDetails()
                details.locations?.add(location.name)
                prefRepository.setCurrentPartyDetails(details)
            }
        }
    }


    override fun getItemCount() = locations.size

    override fun getItemId(position: Int): Long {
        return locations[position].hashCode().toLong()
    }

}