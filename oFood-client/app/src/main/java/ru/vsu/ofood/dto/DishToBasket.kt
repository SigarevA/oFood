package ru.vsu.ofood.dto

import ru.vsu.ofood.entities.Dish

data class DishToBasketDTO(
    val dish: Dish,
    val count : Int
)
