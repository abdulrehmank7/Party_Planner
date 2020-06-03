package com.arkapp.partyplanner.ui.venueList

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
import com.arkapp.partyplanner.data.models.Venue
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.convertSummary
import com.arkapp.partyplanner.utils.toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
/**
 * Recycler view adapter of venue list venue selection screen
 * */
class VenueListAdapter(
    private val context: Activity,
    private val venueList: List<Venue>,
    private val navController: NavController,
    private val prefRepository: PrefRepository,
    private val lifecycleScope: LifecycleCoroutineScope
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
            val details = prefRepository.currentPartyDetails
            details.selectedDestination = venueData
            prefRepository.currentPartyDetails = details

            if (!prefRepository.currentPartyDetails.checkedItemList.isNullOrEmpty()) {
                context.toast("Please wait saving data...")
                UpdateSummaryAsyncTask(context, prefRepository).execute()
            } else
                navController.navigate(R.id.action_venueListFragment_to_specialSelectionFragment)
        }

    }


    override fun getItemCount() = venueList.size

    override fun getItemId(position: Int): Long {
        return venueList[position].hashCode().toLong()
    }

    //adding the selected venue in the SQL db
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
                .navigate(R.id.action_venueListFragment_to_finalChecklistFragment)
        }
    }
}