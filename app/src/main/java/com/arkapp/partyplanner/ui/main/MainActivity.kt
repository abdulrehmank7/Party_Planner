package com.arkapp.partyplanner.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.authentication.RC_SIGN_IN
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.utils.toast
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val prefRepository by lazy { PrefRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findNavController(R.id.fragment).addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.splashFragment)
                supportActionBar!!.hide()
            else
                supportActionBar!!.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                prefRepository.setLoggedIn(true)

            } else {
                toast(getString(R.string.login_failed))
                finish()
            }
        }
    }
}