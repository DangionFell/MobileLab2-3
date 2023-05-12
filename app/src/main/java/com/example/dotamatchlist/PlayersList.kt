package com.example.dotamatchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dotamatchlist.databinding.ActivityPlayersListBinding

class PlayersList : AppCompatActivity() {

    lateinit var binding: ActivityPlayersListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val match_id = intent.getLongExtra("match_id", 1)

//        val bundle = Bundle()
//        bundle.putLong("ID", match_id)
        val fragment = DetailFragment.newInstance(match_id)
//        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()

    }

}