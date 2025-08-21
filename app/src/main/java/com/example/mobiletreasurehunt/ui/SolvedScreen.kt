package com.example.mobiletreasurehunt.ui

/*
* Assignment 6 - Mobile Treasure Hunt
* Jinhui Zhen / Zhenjin@oregonstate.edu
* CS 492 / Oregon State University
*/

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobiletreasurehunt.R
import com.example.mobiletreasurehunt.model.TreasureHuntViewModel
import com.example.mobiletreasurehunt.navigation.TreasureScreen
import androidx.compose.runtime.*


@Composable
fun SolvedScreen(navController: NavController, viewModel: TreasureHuntViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val currentClueIndex = uiState.currentClueIndex
    val totalClues = uiState.clues.size
    val isLastClue = currentClueIndex == totalClues - 1
    val currentClue = uiState.clues.getOrNull(currentClueIndex)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎉 Clue #${currentClueIndex + 1} Solved!",
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "⏱️ Time for this clue: ${viewModel.getPausedTimeFormatted()}",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (currentClue != null) {
                Text(
                    text = "ℹ️ ${currentClue.info}",
                    fontSize = 18.sp
                )
            }

            Image(
                painter = painterResource(id = R.drawable.treasure_icon),
                contentDescription = "Treasure",
                modifier = Modifier
                    .size(150.dp)
                    .padding(16.dp)
            )

            Text(
                text = "Nice job! Ready for the next clue?",
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Button(
                onClick = {
                    if (uiState.isGameComplete) {
                        navController.navigate(TreasureScreen.Completed.route) {
                            popUpTo(TreasureScreen.Start.route) { inclusive = false }
                        }
                    } else {
                        viewModel.moveToNextClue()
                        viewModel.resumeTimer()
                        navController.navigate(TreasureScreen.Clue.route)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = if (isLastClue) "Finish" else "Continue",
                    fontSize = 16.sp
                )
            }
        }
    }
}
