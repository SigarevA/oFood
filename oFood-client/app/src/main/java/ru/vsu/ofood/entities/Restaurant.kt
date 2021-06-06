package ru.vsu.ofood.entities

data class Restaurant(
    val id: String,
    val name : String,
    val phone : String,
    val email : String,
    val address : String,
    val coordinate : Coordinate,
    val startOfWorkDay : Int,
    val endOfTheWorkingDay : Int
)