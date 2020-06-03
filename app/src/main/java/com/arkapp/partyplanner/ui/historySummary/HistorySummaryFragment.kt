package com.arkapp.partyplanner.ui.historySummary

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.HistorySummary
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.initVerticalAdapter
import com.arkapp.partyplanner.utils.show
import kotlinx.android.synthetic.main.fragment_history_summary.*

class HistorySummaryFragment : Fragment(R.layout.fragment_history_summary) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Fetching the history data from the SQL in background thread
        HistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
    }

    private class HistorySummaryAsyncTask(private val context: Activity,
                                          private val prefRepository: PrefRepository) : AsyncTask<Void, Void, MutableList<HistorySummary>?>() {

        override fun doInBackground(vararg params: Void?): MutableList<HistorySummary>? {
            val historySummaryDao = AppDatabase.Companion().getDatabase(context).historySummaryDao()
            return historySummaryDao.getHistorySummary(prefRepository.currentUser?.uid!!)
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(summaryList: MutableList<HistorySummary>?) {
            //Setting the Recycler view adapter for the History screen
            if (summaryList!!.isNotEmpty()) {
                val adapter = HistorySummaryListAdapter(summaryList,
                                                        context.findNavController(R.id.fragment),
                                                        prefRepository)
                context.rvHistorySummary.initVerticalAdapter(adapter, true)
            } else
                context.noHistoryTv.show()
        }
    }
}
