package com.example.dotamatchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dotamatchlist.databinding.MatchItemBinding

class MatchAdapter(val listener : Listener, private val matchList : ArrayList<Match>): RecyclerView.Adapter<MatchAdapter.MatchHolder>() {

    class MatchHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = MatchItemBinding.bind(item)
        fun bind(match: Match, listener: Listener) = with(binding) {
            startTime.text = "Начало матча: " + match.startTime
            when(match.winner){
                "Radiant" -> winner.setBackgroundResource(R.drawable.radiant_background)
                "Dire" -> winner.setBackgroundResource(R.drawable.dire_background)
            }
            winner.text = match.winner
            avgMmr.text = "Срединий MMR: " + match.avgMmr
            duration.text = "Длительность: " + match.duration

            itemView.setOnClickListener{
                listener.onClick(match)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.match_item, parent, false)
        return MatchHolder(view)
    }

    override fun onBindViewHolder(holder: MatchHolder, position: Int) {
        holder.bind(matchList[position], listener)
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    interface Listener{
        fun onClick(match: Match)
    }
}