package ru.vsu.ofood.domain

import ru.vsu.ofood.entities.Dish

interface DishRepo {
    suspend fun getDishes(categoryId : String) : List<Dish>
}