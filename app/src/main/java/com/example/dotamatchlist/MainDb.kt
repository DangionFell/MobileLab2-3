package com.example.dotamatchlist

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(entities = [Match::class, Player::class], version = 1)
@TypeConverters(Converters::class)
abstract class MainDb : RoomDatabase() {
    abstract fun matchDao() : MatchDao
    abstract fun playerDao() : PlayerDao

    companion object{
        fun getDb(context: Context): MainDb{
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "dotaApi.db"
            ).build()
        }
    }
}


object Converters {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String): ArrayList<Long> {
        val listType = object : TypeToken<ArrayList<Long>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun toArrayList(list: ArrayList<Long>): String {
        return Gson().toJson(list)
    }
}