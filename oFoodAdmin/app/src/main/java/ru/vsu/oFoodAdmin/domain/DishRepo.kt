package ru.vsu.oFoodAdmin.domain

import ru.vsu.oFoodAdmin.entities.Dish
import ru.vsu.oFoodAdmin.network.response.DishResponse
import ru.vsu.oFoodAdmin.network.response.StatusResponse
import ru.vsu.oFoodAdmin.ui.addingdish.AddingDishScreenContent

interface DishRepo {
    suspend fun getDishes() : Result<List<Dish>>
    suspend fun getCurrentDishes() : Result<List<DishResponse>>
    suspend fun createDish(addingDishScreenContent: AddingDishScreenContent) : Result<String>
}