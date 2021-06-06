package ru.vsu.ofood.entities

data class Dish (
    val id : String,
    val price : Double,
    val name : String,
    val description : String,
    val photoPath : String,
    val discount : Int
)