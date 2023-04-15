package com.example.dotamatchlist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "players")
data class Player(
    @PrimaryKey val account_id : Long,
    val team : String,
    val nickname : String,
    val hero : String,
    val gold : String,
)
