package com.example.dotamatchlist

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotamatchlist.databinding.ActivityMainBinding

import java.util.*

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}

class MainActivity : AppCompatActivity(), MatchAdapter.Listener {

    lateinit var binding: ActivityMainBinding
    lateinit var db: MainDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //this.deleteDatabase("dotaApi.db")
        db = MainDb.getDb(this)

        lifecycleScope.launch(Dispatchers.IO) {
            var matches = ArrayList<Match>()
            if (isInternetAvailable(this@MainActivity)){
                matches = getMatchList()
                db.matchDao().deleteAllMatches()
                db.matchDao().insertMatches(matches)
            } else{
                matches = ArrayList(db.matchDao().getAllMatches())
                Collections.reverse(matches)
            }
            withContext(Dispatchers.Main) {
                initRcView(matches)
            }
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




