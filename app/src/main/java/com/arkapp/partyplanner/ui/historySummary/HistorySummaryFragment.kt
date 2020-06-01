package com.arkapp.partyplanner.ui.historySummary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.initVerticalAdapter
import com.arkapp.partyplanner.utils.show
import kotlinx.android.synthetic.main.fragment_history_summary.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistorySummaryFragment : Fragment(R.layout.fragment_history_summary) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Fetching the history data from the SQL in background thread
        lifecycleScope.launch(Dispatchers.Main) {

            val historySummaryDao = AppDatabase.getDatabase(requireContext()).historySummaryDao()
            val summaryList = historySummaryDao.getHistorySummary(prefRepository.getCurrentUser()?.uid!!)

            //Setting the Recycler view adapter for the History screen
            if (summaryList.isNotEmpty()) {
                val adapter = HistorySummaryListAdapter(summaryList,
                                                        findNavController(),
                                                        prefRepository)
                rvHistorySummary.initVerticalAdapter(adapter, true)
            } else
                noHistoryTv.show()
        }
    }

}
