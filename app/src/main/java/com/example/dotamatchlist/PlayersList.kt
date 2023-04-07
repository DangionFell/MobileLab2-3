package com.example.dotamatchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotamatchlist.databinding.ActivityPlayersListBinding
import kotlinx.coroutines.launch

class PlayersList : AppCompatActivity() {

    lateinit var binding: ActivityPlayersListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val match_id = intent.getLongExtra("match_id", 1)

        lifecycleScope.launch {
            val players: ArrayList<Player> = getPlayersFromMatchId(match_id)
            initRcView(players)
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