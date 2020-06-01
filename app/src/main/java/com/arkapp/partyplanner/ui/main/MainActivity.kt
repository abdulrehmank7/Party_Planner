package com.arkapp.partyplanner.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.repository.PrefRepository

class MainActivity : AppCompatActivity() {

    private val prefRepository by lazy { PrefRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initializing the navigation component, Used to open different screen
        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration
            .Builder(R.id.optionsFragment, R.id.finalChecklistFragment)
            .build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        findNavController(R.id.fragment).addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.splashFragment || destination.id == R.id.signupFragment)
                supportActionBar!!.hide()
            else
                supportActionBar!!.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}