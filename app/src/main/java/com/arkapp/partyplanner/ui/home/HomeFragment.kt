package com.arkapp.partyplanner.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arkapp.partyplanner.databinding.FragmentHomeBinding
import com.arkapp.partyplanner.utils.hide
import com.arkapp.partyplanner.utils.show
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.babyShowerBtn.setOnClickListener {
            binding.partyCheck.hide()
            binding.babyShowerCheck.show()
        }

        binding.normalPartyBtn.setOnClickListener {
            binding.partyCheck.show()
            binding.babyShowerCheck.hide()
        }

        binding.calendarView.minDate = Calendar.getInstance().timeInMillis

        binding.proceedBtn.setOnClickListener {

        }
    }
}
