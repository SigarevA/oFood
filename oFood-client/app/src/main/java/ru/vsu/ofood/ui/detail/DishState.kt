package ru.vsu.ofood.ui.detail

import ru.vsu.ofood.entities.Dish

data class DishState(
    val dish : Dish,
    val count : Int = 1,
    val isAddedToCart : Boolean = false
)

sealed class DishScreenEvent {
    class ClickAddingToBasket(val dish : Dish, val count : Int) : DishScreenEvent()
}