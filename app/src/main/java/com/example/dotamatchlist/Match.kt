package com.example.dotamatchlist

// Определяем модель матча
data class Match(
    val matchId: Long,
    val startTime: String,
    val winner: String,
    val duration: String,
    val avgMmr: String
)



