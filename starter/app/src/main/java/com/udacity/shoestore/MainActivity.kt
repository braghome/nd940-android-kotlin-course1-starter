package com.udacity.shoestore

import android.os.Bundle
import android.view.ActionMode
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.udacity.shoestore.databinding.ActivityMainBinding
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var viewModel: ListShoesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)
            setSupportActionBar(toolbar)
            navController = findNavController(R.id.nav_host_fragment)
            setupWithNavController(navigationView, navController)
            appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        ViewModelProvider(this).get(ListShoesViewModel::class.java).also { viewModel = it }
    }

    override fun onStart() {
        super.onStart()
        viewModel.shoeList.observe(this, {
            if (Collections.EMPTY_LIST == it) {
                Timber.i("no shoe data passed")
            } else {
                Timber.i("shoeList status $it")
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}
