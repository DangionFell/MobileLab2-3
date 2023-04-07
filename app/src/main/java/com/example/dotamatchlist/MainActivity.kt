package com.example.dotamatchlist

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotamatchlist.databinding.ActivityMainBinding

import java.util.*

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), MatchAdapter.Listener {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            var matches : ArrayList<Match> = getMatchList()
            initRcView(matches)
        }
    }

    private fun initRcView(matches : ArrayList<Match>){
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(this@MainActivity)
            val adapter = MatchAdapter(this@MainActivity, matches)
            rcView.adapter = adapter

        }
    }

    override fun onClick(match: Match) {
        startActivity(Intent(this, PlayersList::class.java).apply {
            putExtra("match_id", match.matchId)
        })
    }

}




