package com.hoopcarpool.archexample.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hoopcarpool.archexample.R
import com.hoopcarpool.archexample.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: MainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    /** Returns Activity's nav controller */
    protected val navController: NavController
        get() = findNavController(R.id.nav_host_fragment)

    override fun onResume() {
        super.onResume()

        navController.navigate(R.id.loginFragment)
    }
}
