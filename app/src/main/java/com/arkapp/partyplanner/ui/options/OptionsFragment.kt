package com.arkapp.partyplanner.ui.options

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import kotlinx.android.synthetic.main.fragment_options.*

/**
 * A simple [Fragment] subclass.
 */
class OptionsFragment : Fragment(R.layout.fragment_options) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createChecklistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_optionsFragment_to_homeFragment)
        }
    }
}
