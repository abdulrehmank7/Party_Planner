package com.arkapp.partyplanner.ui.catererList

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Caterer
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_venue_list.*


class CaterersListFragment : Fragment(R.layout.fragment_venue_list) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().hideKeyboard()

        //Budget list is identified here
        val budgetLimit = when (prefRepository.currentPartyDetails.partyBudget) {
            getString(R.string.low) -> LOW_BUDGED_LIMIT
            getString(R.string.medium) -> MEDIUM_BUDGED_LIMIT
            getString(R.string.high) -> HIGH_BUDGED_LIMIT
            getString(R.string.very_high) -> VERY_HIGH_BUDGED_LIMIT
            else -> HIGH_BUDGED_LIMIT
        }

        //Fetching the caterer data from SQL in background thread
        CatererAsyncTask(requireActivity(), prefRepository, budgetLimit, lifecycleScope).execute()

    }

    private class CatererAsyncTask(private val context: Activity,
                                   private val prefRepository: PrefRepository,
                                   private val budgetLimit: Double,
                                   private val lifecycleScope: LifecycleCoroutineScope) : AsyncTask<Void, Void, MutableList<Caterer>?>() {

        override fun doInBackground(vararg params: Void?): MutableList<Caterer>? {
            val catererDao = AppDatabase.Companion().getDatabase(context).catererDao()
            return catererDao
                .getAllCaterersInBudget(
                    budgetLimit,
                    prefRepository.currentPartyDetails.partyGuest!!)
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(catererList: MutableList<Caterer>?) {
            //Filtering the caterer after getting the caterer data

            val selectedPartyType = prefRepository.currentPartyDetails.partyType
            val filteredCatererList = ArrayList<Caterer>()
            val type = object : TypeToken<ArrayList<String>>() {}.type
            val gson = Gson()

            for (caterer in catererList!!) {
                val catererPartyType = gson.fromJson<ArrayList<String>>(caterer.partyType, type)
                for (partyType in catererPartyType) {
                    if (selectedPartyType.contains(partyType)) {
                        filteredCatererList.add(caterer)
                        break
                    }
                }
            }

            if (filteredCatererList.isEmpty()) {
                context.noItemFound.text = "No caterer found with the selection.\nChange selection and try again"
                context.noItemFound.show()
                return
            }

            //Setting the caterer using Recycler view in the fragment.
            val adapter =
                CaterersListAdapter(
                    context,
                    filteredCatererList,
                    context.findNavController(R.id.fragment),
                    prefRepository,
                    lifecycleScope)

            context.venueListRv.initVerticalAdapter(adapter, true)
        }
    }
}
