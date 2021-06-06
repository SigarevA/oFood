package ru.vsu.oFoodAdmin.entities

data class Dish (
    val id : String,
    val price : Double,
    val name : String,
    val description : String,
    val photoPath : String,
)