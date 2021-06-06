package ru.vsu.ofood.network.request

import java.util.*

class OrderCreationRequest(
    val phone : String,
    val name : String,
    val city : String,
    val street : String,
    val house : String,
    val entrance : String?,
    val flat : String?,
    val comment : String?,
    val date : Long?,
    val registrationId : String,
    val goods : List<InfoDishInOrderRequest>
)