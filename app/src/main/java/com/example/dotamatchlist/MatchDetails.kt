package com.example.dotamatchlist

import android.view.inspector.IntFlagMapping
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Path


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.net.URL

data class MatchDetails(
    val match_id: Long,
    val duration: Int,
    val radiant_win: Boolean,
    val avg_mmr: Int?,
    val start_time : Long
)

//////////////////Переписать///////////////////////////


fun getFormattedDuration(duration: Int): String {
    val minutes = duration / 60
    val seconds = duration % 60
    return "$minutes:${String.format("%02d", seconds)}"
}

fun getFormattedStartTime(startTime : Long): String {
    val dateFormatter = SimpleDateFormat("dd.MM HH:mm")
    val date = Date(startTime * 1000)
    return dateFormatter.format(date)
}

interface DotaApiService {
    @GET("publicMatches")
    suspend fun getMatches(): List<MatchDetails>

    @GET("matches/{match_id}")
    suspend fun getMatchPlayerDetails(@Path("match_id") matchId: Long): Players

    @GET("heroes")
    suspend fun getHeroes(): List<Hero>

    @GET("players/{account_id}")
    suspend fun getPlayer(@Path("account_id") accountId: Long): Call<PlayerProfile>
}

suspend fun getMatchList() : ArrayList<Match> {

    // Создаем объект Retrofit и указываем базовый URL API
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.opendota.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Создаем экземпляр сервиса API
    val service = retrofit.create(DotaApiService::class.java)

    // Отправляем запрос на получение списка матчей
    val matches = service.getMatches()

    // Получаем список объектов Match из ответа
    val matchesArray = ArrayList<Match>()

    // Обрабатываем результат
    matches.forEach { matchDetails ->
        val matchId = matchDetails.match_id
        val startTime = getFormattedStartTime(matchDetails.start_time)
        val winner = if (matchDetails.radiant_win == true) Team.Radiant.team else Team.Dire.team
        val duration = getFormattedDuration(matchDetails.duration)
        val avgMmr = if (matchDetails.avg_mmr == null) Additional.None.str else matchDetails.avg_mmr.toString()

        val match = Match(matchId, startTime, winner, duration, avgMmr)
        matchesArray.add(match)
    }

    return matchesArray
}

data class PlayerDetails(
    val player_slot : Int,
    val account_id: Long,
    val hero_id: Int,
    val total_gold: Int
)

data class Players(
    val players: List<PlayerDetails>
)

data class Hero(
    val id: Int,
    val localized_name: String
)

data class PlayerProfile(
    @SerializedName("solo_competitive_rank")
    val solo_competitive_rank : Int = 0,
    @SerializedName("competitive_rank")
    val competitive_rank : Int = 0,
    @SerializedName("rank_tier")
    val rank_tier : Int = 0,
    @SerializedName("leaderboard_rank")
    val leaderboard_rank : Int = 0,
    @SerializedName("mmr_estimate")
    val mmr_estimate : Int = 0,
    @SerializedName("profile")
    val profile: Profile = Profile("")
)

data class Profile(
//    @SerializedName("account_id")
//    val account_id: Int = 0,
    @SerializedName("personaname")
    val personaname: String = ""
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("plus")
//    val plus: Boolean,
//    @SerializedName("cheese")
//    val cheese: Int,
//    @SerializedName("steamid")
//    val steamid: String,
//    @SerializedName("avatar")
//    val avatar: String,
//    @SerializedName("avatarmedium")
//    val avatarmedium: String,
//    @SerializedName("avatarfull")
//    val avatarfull: String,
//    @SerializedName("profileurl")
//    val profileurl: String,
//    @SerializedName("last_login")
//    val last_login: String,
//    @SerializedName("loccountrycode")
//    val loccountrycode: String,
//    @SerializedName("is_contributor")
//    val is_contributor: Boolean,
//    @SerializedName("is_subscriber")
//    val is_subscriber: Boolean
)

//suspend fun getPlayerName(accountId: Long): String {
//    val retrofit = Retrofit.Builder()
//        .baseUrl("https://api.opendota.com/api/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    val api = retrofit.create(DotaApiService::class.java)
//
//    val response = api.getPlayer(accountId).execute()
//
//    if (response.isSuccessful) {
//        val player = response.body()
//        if (player != null) {
//            return player.profile.personaname
//        }
//    }
//
//    throw Exception("Failed to get player name for account id $accountId")
//}

fun getPlayerName(accountId: Long): String = runBlocking {
    val url = "https://api.opendota.com/api/players/$accountId"
    val response = async(Dispatchers.IO) { URL(url).readText() }.await()
    val player = JSONObject(response)
    player.getJSONObject("profile").getString("personaname")
}

fun getHeroName(heroes : List<Hero>, heroId: Int): String {

    for (hero in heroes) {
        if (hero.id == heroId) {
            return hero.localized_name
        }
    }
    return ""
}

suspend fun getPlayersFromMatchId(matchId : Long) : ArrayList<Player> {
    var players = ArrayList<Player>()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.opendota.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(DotaApiService::class.java)

    val match = service.getMatchPlayerDetails(matchId)

    val heroes = service.getHeroes()

    match.players.forEach{ player ->
        val team = if (player.player_slot < 128) Team.Radiant.team else Team.Dire.team
        val gold = player.total_gold.toString()
        val nickname = if(player.account_id > 1) getPlayerName(player.account_id) else Additional.None.str
        val hero = getHeroName(heroes, player.hero_id)
//        val nickname = player.account_id.toString()
//        val hero = player.hero_id.toString()

        val pl = Player(team, nickname, hero, gold)

        players.add(pl)
    }


    return players
}
