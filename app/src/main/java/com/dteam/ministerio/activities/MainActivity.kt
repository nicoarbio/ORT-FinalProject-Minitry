package com.dteam.ministerio.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.dteam.ministerio.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navHostFragment : NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment

        bottomNavView = findViewById(R.id.bottomNav)

        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)

        setupNav()
    }

    private fun setupNav() {
        val navController = findNavController(R.id.fragment)
        findViewById<BottomNavigationView>(R.id.bottomNav).setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _,  destination, _ ->
            when(destination.id) {
                //Acá definimos las pantallas que queremos que muestre la BottomNav y en cuales no
                R.id.logIn -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        bottomNavView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNavView.visibility = View.GONE

    }

}