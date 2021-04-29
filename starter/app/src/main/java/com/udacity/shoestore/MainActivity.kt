package com.udacity.shoestore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.udacity.shoestore.databinding.ActivityMainBinding
import com.udacity.shoestore.databinding.AppBarBinding
import com.udacity.shoestore.databinding.ContentMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val toolBinding = AppBarBinding.inflate(layoutInflater, binding.root, true)
        val mainContent = ContentMainBinding.inflate(layoutInflater, toolBinding.root, true)
        binding.apply {
            setContentView(root)
            setSupportActionBar(toolBinding.toolbar)
            navController = findNavController(R.id.nav_host_fragment)
            setupWithNavController(navigationView, navController)
            appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}
