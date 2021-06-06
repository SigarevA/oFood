package ru.vsu.ofood.network.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.vsu.ofood.network.request.OrderCreationRequest
import ru.vsu.ofood.network.responses.StatusResponse

interface OrderAPI {
    @POST("/order/create")
    suspend fun createOrder(@Body orderCreationRequest: OrderCreationRequest): StatusResponse
}