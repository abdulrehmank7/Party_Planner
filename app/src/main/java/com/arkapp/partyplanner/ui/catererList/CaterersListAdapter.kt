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
import com.arkapp.partyplanner.data.models.Caterer
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.addUnfinishedData
import com.arkapp.partyplanner.utils.convertSummary
import com.arkapp.partyplanner.utils.toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This is a recycler view adapter used to show caterers list
 * */
class CaterersListAdapter(
    private val context: Context,
    private val caterersList: List<Caterer>,
    private val navController: NavController,
    private val prefRepository: PrefRepository,
    private val lifecycleScope: LifecycleCoroutineScope
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val gson = Gson()
    private val type = object : TypeToken<ArrayList<String>>() {}.type!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CatereresListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rv_caterer,
                parent,
                false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as CatereresListViewHolder).viewBinding

        val caterersData = caterersList[position]
        binding.name.text = caterersData.name.trim()
        binding.price.text = "$${caterersData.pricePerPax}"
        binding.totalGuestPriceTv.text = "${prefRepository.getCurrentPartyDetails().partyGuest} Pax total"
        binding.totalGuestPrice.text = "$${caterersData.pricePerPax * prefRepository.getCurrentPartyDetails().partyGuest!!}"

        //This will show all the supported party type of the caterer
        val partyTypes = gson.fromJson<ArrayList<String>>(caterersData.partyType, type)
        var partyTypeString = ""

        for (x in partyTypes) {
            partyTypeString += "$x, "
        }
        binding.partyTypeValue.text = partyTypeString.substring(0, partyTypeString.length - 2)

        //On clicking the caterer it open the different screen according to party detail
        binding.parent.setOnClickListener {
            val details = prefRepository.getCurrentPartyDetails()
            details.selectedCaterer = caterersData
            prefRepository.setCurrentPartyDetails(details)

            when {
                prefRepository.getCurrentPartyDetails().partyDestination == context.getString(R.string.home) ->
                    navController.navigate(R.id.action_caterersListFragment_to_specialSelectionFragment)
                !prefRepository.getCurrentPartyDetails().checkedItemList.isNullOrEmpty() ->
                    updateSummaryData()
                else -> {
                    navController.navigate(R.id.action_caterersListFragment_to_venueLocationFragment)
                    addUnfinishedData(lifecycleScope, context, prefRepository)
                }
            }
        }

    }


    override fun getItemCount() = caterersList.size

    override fun getItemId(position: Int): Long {
        return caterersList[position].hashCode().toLong()
    }

    // Used to update the summary data in SQL table
    private fun updateSummaryData() {
        lifecycleScope.launch(Dispatchers.Main) {
            context.toast("Please wait saving data...")
            val summaryDao = AppDatabase.getDatabase(context).summaryDao()
            summaryDao.delete(prefRepository.getCurrentUser()?.uid!!)
            summaryDao.insert(convertSummary(prefRepository.getCurrentPartyDetails(),
                                             prefRepository.getCurrentUser()?.uid!!))
            navController.navigate(R.id.action_caterersListFragment_to_finalChecklistFragment)

        }
    }

}