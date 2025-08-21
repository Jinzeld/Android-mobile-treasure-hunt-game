package com.example.mobiletreasurehunt.ui

/*
* Assignment 6 - Mobile Treasure Hunt
* Jinhui Zhen / Zhenjin@oregonstate.edu
* CS 492 / Oregon State University
*/

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobiletreasurehunt.model.TreasureHuntViewModel
import com.example.mobiletreasurehunt.navigation.TreasureScreen

@Composable
fun CompleteScreen(navController: NavController, viewModel: TreasureHuntViewModel) {
    val totalTime = viewModel.getFinalTimeFormatted()
    val finalClue = viewModel.getFinalClue()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎉 Congratulations!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You've completed the Treasure Hunt!",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "⏱️ Total Time: $totalTime",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))


        if (finalClue != null) {
            Text(
                text = "ℹ️ ${finalClue.info}",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = {
                viewModel.resetState()
                viewModel.resetTimer()
                navController.navigate(TreasureScreen.Start.route) {
                    popUpTo(TreasureScreen.Start.route) {
                        inclusive = true
                    }
                }
            }
        ) {
            Text("🏠 Return to Start")
        }
    }
}
