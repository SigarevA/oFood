package ru.vsu.ofood.domain

import ru.vsu.ofood.entities.Dish
import ru.vsu.ofood.network.api.DishApi
import javax.inject.Inject

class DishRepoImpl @Inject constructor(private val dishApi: DishApi) : DishRepo {
    override suspend fun getDishes(categoryId: String): List<Dish> {
       return dishApi.getDishesByCategoryId(categoryId).map { resp ->
            Dish(
                resp.id,
                resp.price,
                resp.name,
                resp.description,
                resp.photoPath,
                resp.discount
            )
        }
    }
}