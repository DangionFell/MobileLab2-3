package com.example.dotamatchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotamatchlist.databinding.ActivityPlayersListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayersList : AppCompatActivity() {

    lateinit var binding: ActivityPlayersListBinding
    lateinit var db: MainDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val match_id = intent.getLongExtra("match_id", 1)
        db = MainDb.getDb(this)

        lifecycleScope.launch(Dispatchers.IO) {
            val players: ArrayList<Player>

            if (isInternetAvailable(this@PlayersList)) {

                db.playerDao().deleteAllPlayers()

                players = getPlayersFromMatchId(match_id)

                var playersIds = ""
                players.forEach{ player ->
                    playersIds += player.account_id.toString() + " "
                }

                db.matchDao().updatePlayersIdsByMatchId(match_id, playersIds)
                db.playerDao().insertPlayers(players)

                withContext(Dispatchers.Main) {
                    initRcView(players)
                }
            } else {
                val playerIdsString = db.matchDao().getPlayersIdsByMatchId(match_id)

                val playerIds = ArrayList<Long>()
                playerIdsString.split(" ").forEach{value ->
                    val longValue = value.toLongOrNull()
                    if (longValue != null) {
                        playerIds.add(longValue)
                    }
                }
                players = ArrayList(db.playerDao().getPlayersByIds(playerIds))
                if(players.size > 1){
                    withContext(Dispatchers.Main) {
                        initRcView(players)
                    }
                }
                else{
                    binding.rcView.visibility = View.GONE
                    binding.textView.text = Additional.NoInternet.str
                }

            }

        }
    }

    private fun initRcView(players : java.util.ArrayList<Player>){
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(this@PlayersList)
            val adapter = PlayerAdapter(players)
            rcView.adapter = adapter

        }
    }
}