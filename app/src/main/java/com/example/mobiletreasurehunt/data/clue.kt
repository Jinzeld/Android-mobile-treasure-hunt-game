package com.example.mobiletreasurehunt.data

/*
* Assignment 6 - Mobile Treasure Hunt
* Jinhui Zhen / Zhenjin@oregonstate.edu
* CS 492 / Oregon State University
*/

data class Clue(
    val clueText: String,
    val hint: String,
    val latitude: Double,
    val longitude: Double,
    val info: String
)

