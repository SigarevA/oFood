package ru.vsu.ofood.utils

import android.util.Log
import ru.vsu.ofood.entities.Category
import ru.vsu.ofood.entities.DishInBasket
import ru.vsu.ofood.network.request.InfoDishInOrderRequest
import ru.vsu.ofood.network.request.OrderCreationRequest
import ru.vsu.ofood.network.responses.CategoryResponse
import ru.vsu.ofood.ui.order_registration.OrderRegistrationState

fun List<CategoryResponse>.toCategories() : List<Category> {
    val categories = ArrayList<Category>(this.size);
    this.forEach {
        categories.add(
            Category(
                id = it.id!!,
                name = it.name!!
            )
        )
    }
    return categories
}

fun List<DishInBasket>.toListInfoDishInOrderRequest() : List<InfoDishInOrderRequest> {
    return this.map {
        Log.d("mapper", "dish : $it")
        InfoDishInOrderRequest(
            count = it.count,
            dishId = it.dishId
        )
    }
}

fun OrderRegistrationState.toOrderCreationRequest(
    registrationId : String,
    dishes : List<InfoDishInOrderRequest>
) : OrderCreationRequest {
    return OrderCreationRequest(
        this.phone.text,
        this.name.text,
        this.city.text,
        this.street.text,
        this.house.text,
        this.entrance,
        this.flat,
        this.comment,
        this.date?.date?.time,
        registrationId,
        dishes
    )
}