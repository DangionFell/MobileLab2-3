package com.example.dotamatchlist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


private lateinit var adapter: MatchAdapter
private lateinit var rc: RecyclerView


class MainFragment : Fragment(), MatchAdapter.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = MainDb.getDb(requireActivity())
        rc = view.findViewById(R.id.rcView)

        lifecycleScope.launch(Dispatchers.IO) {
            var matches = ArrayList<Match>()
            if (isInternetAvailable(requireActivity())){
                matches = getMatchList()
                db.matchDao().deleteAllMatches()
                db.matchDao().insertMatches(matches)
            } else{
                matches = ArrayList(db.matchDao().getAllMatches())
                Collections.reverse(matches)
            }
            withContext(Dispatchers.Main) {
                InitRC(matches)
            }
        }
    }

    fun InitRC(matches : ArrayList<Match>){
        val layoutManager = LinearLayoutManager(context)
        rc.layoutManager = layoutManager
        adapter = MatchAdapter(this@MainFragment, matches)
        rc.adapter = adapter
    }

    override fun onClick(match: Match) {
        startActivity(Intent(requireActivity(), PlayersList::class.java).apply {
            putExtra("match_id", match.matchId)
        })
    }

}