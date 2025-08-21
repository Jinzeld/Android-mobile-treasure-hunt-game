package com.example.mobiletreasurehunt.ui

/*
* Assignment 6 - Mobile Treasure Hunt
* Jinhui Zhen / Zhenjin@oregonstate.edu
* CS 492 / Oregon State University
*/

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobiletreasurehunt.navigation.TreasureScreen

@Composable
fun StartScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Title
        Text(
            text = "Mobile Treasure Hunt",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 32.dp)
        )

        // Scrollable Rules Section
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Game Rules:",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = """
                1. Allow location access to play.
                2. Follow the clues to find real-world locations.
                3. Press "Found It!" when you reach a location.
                4. Solve all clues to complete the hunt.
                5. You can quit anytime and restart from the beginning.
                """.trimIndent(),
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        }

        // Start Button
        Button(
            onClick = {
                navController.navigate(TreasureScreen.Clue.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Start", fontSize = 18.sp)
        }
    }
}
