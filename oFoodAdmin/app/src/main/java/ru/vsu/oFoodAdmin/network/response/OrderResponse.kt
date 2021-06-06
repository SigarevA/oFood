package ru.vsu.oFoodAdmin.network.response

import ru.vsu.oFoodAdmin.entities.InfoDishInOrder
import java.util.*

data class OrderResponse (
    val id: String,
    val phone: String,
    val name: String,
    val city: String,
    val street: String,
    val house: String,
    val goods: Map<String, InfoDishInOrder>,
    val entrance: String? = null,
    val flat: String? = null,
    val surrender : Double? = null,
    val date: Date? = null,
)