package com.arkapp.partyplanner.ui.catererList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.utils.initVerticalAdapter
import kotlinx.android.synthetic.main.fragment_venue_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CaterersListFragment : Fragment(R.layout.fragment_venue_list) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {

            val catererDao = AppDatabase.getDatabase(requireContext()).catererDao()
            val catererList = catererDao.getAllCaterers()


            val adapter = CaterersListAdapter(
                requireContext(),
                catererList, findNavController(), prefRepository,
                lifecycleScope)
            venueListRv.initVerticalAdapter(adapter, true)
        }

    }
}
