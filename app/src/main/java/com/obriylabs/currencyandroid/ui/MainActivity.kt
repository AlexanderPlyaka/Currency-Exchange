package com.obriylabs.currencyandroid.ui

import android.os.Bundle

import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.obriylabs.currencyandroid.R
import com.obriylabs.currencyandroid.ui.base.BaseActivity


class MainActivity : BaseActivity(R.layout.activity_maps), INavigation {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun toMapsFragment() {
        navController.navigate(R.id.mapsFragment)
    }
}
