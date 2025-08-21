package com.example.mobiletreasurehunt.model

/*
* Assignment 6 - Mobile Treasure Hunt
* Jinhui Zhen / Zhenjin@oregonstate.edu
* CS 492 / Oregon State University
*/

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiletreasurehunt.R
import com.example.mobiletreasurehunt.data.Clue
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.math.*

class TreasureHuntViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(TreasureUiState())
    val uiState: StateFlow<TreasureUiState> = _uiState

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private var timerJob: Job? = null

    var isTimerRunning = false

    private var pausedTime: Long = 0L

    private var finalTime: Long = 0L

    init {
        loadClues(application)
    }

    private fun loadClues(context: Context) {
        try {
            val json = context.resources.openRawResource(R.raw.clues).bufferedReader().use { it.readText() }
            val clues = Gson().fromJson(json, Array<Clue>::class.java).toList()
            _uiState.update { it.copy(clues = clues) }
        } catch (e: Exception) {
            Log.e("TreasureViewModel", "Error loading clues", e)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun requestLocation(context: Context, onResult: (Location?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { location ->
                onResult(location) //return location if found
            }.addOnFailureListener {
                onResult(null)
            }
        } catch (e: SecurityException) {
            onResult(null)
        }
    }


    fun checkLocation(current: Location): Boolean {
        val state = _uiState.value
        val clue = state.clues[state.currentClueIndex]

        // Calculate straight-line distance from current location to clue location
        val distance = haversine(current.latitude, current.longitude, clue.latitude, clue.longitude)

        // Check if this is the last clue
        val isLastClue = state.currentClueIndex == state.clues.lastIndex

        val ACCEPTABLE_METERS = 50.0

        return if (distance <= ACCEPTABLE_METERS) {
            // Clue found → mark solved (and complete if last clue)
            _uiState.update {
                it.copy(
                    isGameComplete = isLastClue,
                    isClueSolved = true,
                    isWrongLocation = false
                )
            }
            true
        } else {
            // Too far from target → mark wrong
            _uiState.update { it.copy(isWrongLocation = true, isClueSolved = false) }
            false
        }
    }


    fun moveToNextClue() {
        val current = _uiState.value.currentClueIndex
        if (current < _uiState.value.clues.lastIndex) {
            _uiState.update {
                it.copy(
                    currentClueIndex = current + 1,
                    isClueSolved = false,
                    isWrongLocation = false
                )
            }
        }
    }

    fun getFinalClue(): Clue? {
        return _uiState.value.clues.lastOrNull()
    }

    fun resetState() {
        _uiState.value = TreasureUiState(clues = _uiState.value.clues)
        timerJob?.cancel()
    }

    fun startTimer() {
        if (isTimerRunning) return
        isTimerRunning = true
        timerJob = viewModelScope.launch {
            while (isTimerRunning) {
                delay(1000)
                _elapsedTime.value += 1
            }
        }
    }

    fun resumeTimer() {
        if (isTimerRunning) return
        isTimerRunning = true
        timerJob = viewModelScope.launch {
            while (isTimerRunning) {
                delay(1000)
                _elapsedTime.value += 1
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        isTimerRunning = false
        pausedTime = _elapsedTime.value
        finalTime = _elapsedTime.value
    }

    fun resetTimer() {
        _elapsedTime.value = 0
    }

    fun getPausedTimeFormatted(): String {
        return formatTime(pausedTime)
    }

    fun getFinalTimeFormatted(): String {
        return formatTime(finalTime)
    }

    @SuppressLint("DefaultLocale")
    fun formatTime(seconds: Long): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", minutes, secs)
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371e3
        val Lat1 = Math.toRadians(lat1)
        val Lat2 = Math.toRadians(lat2)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) + cos(Lat1) * cos(Lat2) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }
}

data class TreasureUiState(
    val clues: List<Clue> = emptyList(),
    val currentClueIndex: Int = 0,
    val isClueSolved: Boolean = false,
    val isWrongLocation: Boolean = false,
    val isGameComplete: Boolean = false,
)
