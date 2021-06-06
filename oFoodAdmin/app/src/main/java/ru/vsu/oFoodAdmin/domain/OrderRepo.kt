package ru.vsu.oFoodAdmin.domain

import ru.vsu.oFoodAdmin.entities.Order
import ru.vsu.oFoodAdmin.network.response.DetailOrderResponse
import ru.vsu.oFoodAdmin.network.response.StatusResponse

interface OrderRepo {
    suspend fun getNewOrder() : Result<List<Order>>
    suspend fun getOrderById(id : String) : Result<DetailOrderResponse>
    suspend fun acceptOrder(orderId : String) : Result<StatusResponse>
    suspend fun rejectedOrder(orderId: String) : Result<StatusResponse>
}