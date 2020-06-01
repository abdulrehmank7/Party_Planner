package com.arkapp.partyplanner.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.utils.hide
import com.arkapp.partyplanner.utils.isDoubleClicked
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * First screen fragment of the app. This is opened every time app is launched
 * */
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    //Checking if the user is signed up or not
    private fun initSignUpBtn() {
        if (!prefRepository.setLoggedIn()) {
            signUpBtn.setOnClickListener {
                if (isDoubleClicked(1000)) return@setOnClickListener
                findNavController().navigate(R.id.action_splashFragment_to_signupFragment)
            }
        } else
            signUpBtn.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSignUpBtn()
    }

    override fun onStart() {
        super.onStart()

        loadSplash()
    }

    private fun loadSplash() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            if (prefRepository.setLoggedIn())
                findNavController().navigate(R.id.action_splashFragment_to_optionsFragment)
        }
    }
}
