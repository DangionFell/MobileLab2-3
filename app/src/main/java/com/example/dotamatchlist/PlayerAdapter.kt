package com.example.dotamatchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dotamatchlist.databinding.PlayerItemBinding

class PlayerAdapter(private val playerList : ArrayList<Player>): RecyclerView.Adapter<PlayerAdapter.PlayerHolder>() {

    class PlayerHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = PlayerItemBinding.bind(item)
        fun bind(player: Player) = with(binding) {
            nickname.text = "Nick: " + player.nickname
            when(player.team){
                "Radiant" -> team.setBackgroundResource(R.drawable.radiant_background)
                "Dire" -> team.setBackgroundResource(R.drawable.dire_background)
            }
            heroName.text = "Герой: " + player.hero
            gold.text = "Всего золота: " + player.gold
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false)
        return PlayerHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
        holder.bind(playerList[position])
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

}