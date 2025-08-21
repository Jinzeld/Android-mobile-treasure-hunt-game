package com.example.mobiletreasurehunt.ui

/*
 * Assignment 6 - Mobile Treasure Hunt
 * Jinhui Zhen / Zhenjin@oregonstate.edu
 * CS 492 / Oregon State University
 */

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.mobiletreasurehunt.model.TreasureHuntViewModel
import com.example.mobiletreasurehunt.navigation.TreasureScreen

@Composable
fun ClueScreen(navController: NavController, viewModel: TreasureHuntViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    var showHint by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


// Starts the timer
    LaunchedEffect(uiState.currentClueIndex) {
            viewModel.startTimer()
    }

    LaunchedEffect(uiState.isGameComplete) {
        if (uiState.isGameComplete) {
            navController.navigate(TreasureScreen.Completed.route) {
                popUpTo(TreasureScreen.Start.route) { inclusive = false }
            }
        }
    }

    val currentClue = uiState.clues[uiState.currentClueIndex]

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Title / progress
            Text(
                text = "Clue ${uiState.currentClueIndex + 1} of ${uiState.clues.size}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            // Clue text
            Text(
                text = currentClue.clueText.ifEmpty { "No clue available" },
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Timer display (from ViewModel)
            Text(
                text = "Time: ${viewModel.formatTime(elapsedTime)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Hint toggle
            Button(
                onClick = { showHint = !showHint },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(if (showHint) "Hide Hint" else "Hint")
            }

            if (showHint) {
                Text(
                    text = currentClue.hint,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Fixed "Found It!" button logic in ClueScreen
            Button(
                onClick = {
                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        viewModel.requestLocation(context) { location ->
                            if (location != null) {
                                val correct = viewModel.checkLocation(location)
                                if (correct) {
                                    // Only stop timer and navigate if location is correct
                                    viewModel.stopTimer()
                                    navController.navigate(TreasureScreen.Solved.route)
                                } else {
                                    // Keep timer running and show error message
                                    Toast.makeText(context, "Wrong location, try again.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Failed to get location.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        navController.navigate(TreasureScreen.Permissions.route)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Found It!", fontSize = 18.sp)
            }

            // Quit button
            OutlinedButton(
                onClick = {
                    viewModel.stopTimer()
                    navController.navigate(TreasureScreen.Start.route)
                    viewModel.resetState()
                    viewModel.resetTimer()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Quit")
            }
        }
    }
}
