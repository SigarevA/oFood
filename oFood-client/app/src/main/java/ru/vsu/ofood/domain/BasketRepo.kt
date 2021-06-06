package ru.vsu.ofood.domain

import ru.vsu.ofood.entities.Dish
import ru.vsu.ofood.entities.DishInBasket
import ru.vsu.ofood.network.responses.StatusResponse
import ru.vsu.ofood.ui.order_registration.OrderRegistrationState

interface BasketRepo {
    suspend fun addDishToBasket(dish : Dish, count : Int)
    suspend fun getDishes() : List<DishInBasket>
    suspend fun deleteDishFromBasket(dish : Dish) : Boolean
    suspend fun changeCount(dish : Dish, flag : Int) : Int
    suspend fun clearBasket()
    suspend fun createOrder( orderRegistrationState : OrderRegistrationState) : StatusResponse
}