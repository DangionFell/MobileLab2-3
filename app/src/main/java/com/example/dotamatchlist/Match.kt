package com.example.dotamatchlist

import androidx.room.Entity
import androidx.room.PrimaryKey

// Определяем модель матча
@Entity (tableName = "matches")
data class Match(
    @PrimaryKey val matchId: Long,
    val startTime: String,
    val winner: String,
    val duration: String,
    val avgMmr: String,
    val players_id : String = ""
)



