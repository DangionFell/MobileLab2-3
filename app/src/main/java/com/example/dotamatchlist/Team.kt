package com.example.dotamatchlist

enum class Team(val team: String) {
    Radiant("Radiant"),
    Dire("Dire")
}

enum class Additional(val str: String) {
    None("None")
}

enum class MatchDet(val str: String) {
    Start("Начало матча: "),
    AvgMMR("Срединий MMR: "),
    Duration("Длительность: ")
}

enum class PlayerDet(val str: String){
    Nick("Nick: "),
    Hero( "Герой: "),
    Gold("Всего золота: ")
}