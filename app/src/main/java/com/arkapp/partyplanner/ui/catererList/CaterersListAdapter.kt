package com.arkapp.partyplanner.ui.catererList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Caterers
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.utils.addUnfinishedData
import com.arkapp.partyplanner.utils.loadImage

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

class CaterersListAdapter(
    private val context: Context,
    private val caterersList: List<Caterers>,
    private val navController: NavController,
    private val prefRepository: PrefRepository,
    private val lifecycleScope: LifecycleCoroutineScope
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CatereresListViewHolder(
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
        val binding = (holder as CatereresListViewHolder).viewBinding

        val caterersData = caterersList[position]
        binding.venueName.text = caterersData.name
        binding.venueAdd.text = caterersData.address
        binding.venueImg.loadImage(caterersData.resId)


        binding.parent.setOnClickListener {
            val details = prefRepository.getCurrentPartyDetails()
            details.selectedCaterers = caterersData
            prefRepository.setCurrentPartyDetails(details)

            if (prefRepository.getCurrentPartyDetails().partyDestination == context.getString(R.string.home))
                navController.navigate(R.id.action_caterersListFragment_to_finalChecklistFragment)
            else {
                navController.navigate(R.id.action_caterersListFragment_to_venueListFragment)
                addUnfinishedData(lifecycleScope, context, prefRepository)
            }
        }

    }


    override fun getItemCount() = caterersList.size

    override fun getItemId(position: Int): Long {
        return caterersList[position].hashCode().toLong()
    }

}