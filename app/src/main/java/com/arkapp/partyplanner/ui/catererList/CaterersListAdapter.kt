package com.arkapp.partyplanner.ui.catererList

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Caterer
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.AddUnfinishedAsyncTask
import com.arkapp.partyplanner.utils.convertSummary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This is a recycler view adapter used to show caterers list
 * */
class CaterersListAdapter(
    private val context: Activity,
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
        binding.totalGuestPriceTv.text = "${prefRepository.currentPartyDetails.partyGuest} Pax total"
        binding.totalGuestPrice.text = "$${caterersData.pricePerPax * prefRepository.currentPartyDetails.partyGuest!!}"

        //This will show all the supported party type of the caterer
        val partyTypes = gson.fromJson<ArrayList<String>>(caterersData.partyType, type)
        var partyTypeString = ""

        for (x in partyTypes) {
            partyTypeString += "$x, "
        }
        binding.partyTypeValue.text = partyTypeString.substring(0, partyTypeString.length - 2)

        //On clicking the caterer it open the different screen according to party detail
        binding.parent.setOnClickListener {
            val details = prefRepository.currentPartyDetails
            details.selectedCaterer = caterersData
            prefRepository.currentPartyDetails = details

            when {
                prefRepository.currentPartyDetails.partyDestination == context.getString(R.string.home) ->
                    navController.navigate(R.id.action_caterersListFragment_to_specialSelectionFragment)
                !prefRepository.currentPartyDetails.checkedItemList.isNullOrEmpty() ->
                    UpdateSummaryAsyncTask(context, prefRepository).execute()
                else -> {
                    navController.navigate(R.id.action_caterersListFragment_to_venueLocationFragment)
                    AddUnfinishedAsyncTask(context, prefRepository).execute()
                }
            }
        }

    }


    override fun getItemCount() = caterersList.size

    override fun getItemId(position: Int): Long {
        return caterersList[position].hashCode().toLong()
    }

    // Used to update the summary data in SQL table
    private class UpdateSummaryAsyncTask(private val context: Activity,
                                         private val prefRepository: PrefRepository) : AsyncTask<Void, Void, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val summaryDao = AppDatabase.Companion().getDatabase(context).summaryDao()
            summaryDao.delete(prefRepository.currentUser?.uid!!)
            summaryDao.insert(convertSummary(prefRepository.currentPartyDetails,
                                             prefRepository.currentUser?.uid!!))
            return null
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(summaryData: Void?) {
            context.findNavController(R.id.fragment)
                .navigate(R.id.action_caterersListFragment_to_finalChecklistFragment)
        }
    }

}