package ru.vsu.ofood.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish_in_basket")
data class DishInBasket (
    @PrimaryKey
    val dishId : String,
    val count : Int
)