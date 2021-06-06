package ru.vsu.ofood.db

import androidx.room.*
import ru.vsu.ofood.entities.DishInBasket

@Dao
interface DishInBasketDao {
    @Insert
    suspend fun insertDishInBasket(dishInBasket: DishInBasket)

    @Delete
    suspend fun deleteDishInBasket(dishInBasket: DishInBasket)

    @Update
    suspend fun updateDishInBasket(dishInBasket: DishInBasket)

    @Query("DELETE FROM dish_in_basket")
    suspend fun clearBasket()

    @Query("SELECT * FROM dish_in_basket")
    suspend fun getDishesFromBasket() : List<DishInBasket>

    @Query("SELECT * FROM dish_in_basket WHERE dishId = :dishId")
    suspend fun getDishFromBasket(dishId : String) : DishInBasket?

    @Query("DELETE FROM dish_in_basket WHERE dishId = :dishId")
    suspend fun deleteDishInBasketWithDishId(dishId: String) : Int
}