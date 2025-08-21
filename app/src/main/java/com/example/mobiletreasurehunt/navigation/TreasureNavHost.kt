package com.example.mobiletreasurehunt.navigation

/*
* Assignment 6 - Mobile Treasure Hunt
* Jinhui Zhen / Zhenjin@oregonstate.edu
* CS 492 / Oregon State University
*/

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobiletreasurehunt.model.TreasureHuntViewModel
import com.example.mobiletreasurehunt.ui.*

sealed class TreasureScreen(val route: String) {
    object Permissions : TreasureScreen("permissions")
    object Start : TreasureScreen("start")
    object Clue : TreasureScreen("clue")
    object Solved : TreasureScreen("solved")
    object Completed : TreasureScreen("completed")
}

@Composable
fun TreasureNavHost(viewModel: TreasureHuntViewModel, navController: NavHostController) {
    NavHost(navController = navController, startDestination = TreasureScreen.Permissions.route) {

        //Permissions Screen updated to use the new PermissionsScreen
        composable(TreasureScreen.Permissions.route) {
            PermissionsScreen(
                onPermissionGranted = {
                    navController.navigate(TreasureScreen.Start.route) {
                        popUpTo(TreasureScreen.Permissions.route) { inclusive = true }
                    }
                }
            )
        }

        composable(TreasureScreen.Start.route) {
            StartScreen(navController)
        }
        composable(TreasureScreen.Clue.route) {
            ClueScreen(navController, viewModel)
        }
        composable(TreasureScreen.Solved.route) {
            SolvedScreen(navController, viewModel)
        }
        composable(TreasureScreen.Completed.route) {
            CompleteScreen(navController, viewModel)
        }

    }
}
