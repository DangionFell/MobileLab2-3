package com.example.dotamatchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_MATCH_ID = "match_id"

private lateinit var adapter: PlayerAdapter
private lateinit var rc: RecyclerView

class DetailFragment : Fragment() {

    private var match_id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            match_id = it.getLong(ARG_MATCH_ID)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Long) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_MATCH_ID, param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rc = view.findViewById(R.id.rcView)
//        val match_id = arguments?.getLong("ID")

        val db = MainDb.getDb(requireActivity())

        lifecycleScope.launch(Dispatchers.IO) {
            val players: ArrayList<Player>

            if (isInternetAvailable(requireActivity())) {

                db.playerDao().deleteAllPlayers()

                players = getPlayersFromMatchId(match_id!!)

                var playersIds = ""
                players.forEach{ player ->
                    playersIds += player.account_id.toString() + " "
                }

                db.matchDao().updatePlayersIdsByMatchId(match_id!!, playersIds)
                db.playerDao().insertPlayers(players)

                withContext(Dispatchers.Main) {
                    InitRC(players)
                }
            } else {
                val playerIdsString = db.matchDao().getPlayersIdsByMatchId(match_id!!)

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
                        InitRC(players)
                    }
                }
                else{
                    rc.visibility = View.GONE
                    val textView : TextView = view.findViewById(R.id.textView)
                    textView.text = Additional.NoInternet.str
                }

            }

        }
    }

    fun InitRC(players : ArrayList<Player>){
        val layoutManager = LinearLayoutManager(context)
        rc.layoutManager = layoutManager
        adapter = PlayerAdapter(players)
        rc.adapter = adapter
    }
}