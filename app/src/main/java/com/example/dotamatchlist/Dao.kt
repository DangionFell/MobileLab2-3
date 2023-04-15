package com.example.dotamatchlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayers(players: List<Player>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: Player)

    @Query("SELECT * FROM players")
    fun getAllPlayers(): List<Player>

    @Query("SELECT * FROM players WHERE account_id = :accountId")
    fun getPlayerById(accountId: Long): Player

    @Query("SELECT * FROM players WHERE account_id IN (:playerIds)")
    fun getPlayersByIds(playerIds: List<Long>): List<Player>

    @Query("DELETE FROM players")
    fun deleteAllPlayers()
}

@Dao
interface MatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatch(match: Match)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatches(matches: List<Match>)

    @Query("SELECT * FROM matches")
    fun getAllMatches(): List<Match>

    @Query("SELECT players_id FROM matches WHERE matchId = :matchId")
    fun getPlayersIdsByMatchId(matchId: Long): String

    @Query("UPDATE matches SET players_id = :playersIds WHERE matchId = :matchId")
    fun updatePlayersIdsByMatchId(matchId: Long, playersIds: String)

    @Query("DELETE FROM matches")
    fun deleteAllMatches()
}
