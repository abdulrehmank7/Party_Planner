package com.arkapp.partyplanner.ui.venueList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Venue
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

class VenueListAdapter(
    private val venueList: List<Venue>,
    private val navController: NavController,
    private val prefRepository: PrefRepository
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val gson = Gson()
    private val type = object : TypeToken<ArrayList<String>>() {}.type!!

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
        binding.capacity.text = "${venueData.capacity} Guest"
        binding.contact.text = venueData.contact
        binding.price.text = "$${venueData.price}"
        binding.location.text = venueData.location

        val partyTypes = gson.fromJson<ArrayList<String>>(venueData.partyType, type)
        var partyTypeString = ""

        for (x in partyTypes) {
            partyTypeString += "$x, "
        }
        binding.suitable.text = partyTypeString.substring(0, partyTypeString.length - 2)


        binding.parent.setOnClickListener {
            val details = prefRepository.getCurrentPartyDetails()
            details.selectedDestination = venueData
            prefRepository.setCurrentPartyDetails(details)

            navController.navigate(R.id.action_venueListFragment_to_specialSelectionFragment)
        }

    }


    override fun getItemCount() = venueList.size

    override fun getItemId(position: Int): Long {
        return venueList[position].hashCode().toLong()
    }

}