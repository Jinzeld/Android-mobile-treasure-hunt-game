package com.example.mobiletreasurehunt.ui

/*
* Assignment 6 - Mobile Treasure Hunt
* Jinhui Zhen / Zhenjin@oregonstate.edu
* CS 492 / Oregon State University
*/

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.mobiletreasurehunt.model.TreasureHuntViewModel
import com.example.mobiletreasurehunt.navigation.TreasureNavHost
import com.example.mobiletreasurehunt.ui.theme.MobileTreasureHuntTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create ViewModel instance
        val viewModel: TreasureHuntViewModel by viewModels()

        setContent {
            MobileTreasureHuntTheme {
                val navController = rememberNavController()
                TreasureNavHost(viewModel = viewModel, navController = navController)
            }
        }
    }
}