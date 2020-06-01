package com.arkapp.partyplanner.ui.historySummary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.HistorySummary
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.utils.convertPartyFromHistorySummary
import com.arkapp.partyplanner.utils.getFormattedDate

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * Recycler view adapter of history screen
 * */
class HistorySummaryListAdapter(
    private val foodList: List<HistorySummary>,
    private val navController: NavController,
    private val prefRepository: PrefRepository
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HistorySummaryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rv_history_summary,
                parent,
                false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as HistorySummaryViewHolder).viewBinding

        val summary = foodList[position]
        val partyData = convertPartyFromHistorySummary(summary)

        binding.partyDate.text = partyData.partyDate?.getFormattedDate()
        binding.partyDestination.text = partyData.partyDestination
        binding.guest.text = "${partyData.partyGuest} Guests"

        //On clicking the history will open the check list screen.
        binding.parent.setOnClickListener {
            prefRepository.setCurrentPartyDetails(partyData)
            navController.navigate(R.id.action_historySummaryFragment_to_finalChecklistFragment)
        }
    }


    override fun getItemCount() = foodList.size

    override fun getItemId(position: Int): Long {
        return foodList[position].hashCode().toLong()
    }

}