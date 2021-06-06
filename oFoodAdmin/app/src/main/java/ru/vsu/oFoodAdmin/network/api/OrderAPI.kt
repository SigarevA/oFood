package ru.vsu.oFoodAdmin.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.vsu.oFoodAdmin.entities.Order
import ru.vsu.oFoodAdmin.network.requests.ManageOrderRequest
import ru.vsu.oFoodAdmin.network.response.DetailOrderResponse
import ru.vsu.oFoodAdmin.network.response.StatusResponse

interface OrderAPI {
    @GET("/order-by-status/{status}")
    suspend fun getOrdersWithStatus(@Path("status") status : String) : List<Order>

    @GET("/order-by-id/{id}")
    suspend fun getOrderById(@Path("id") id : String) : DetailOrderResponse

    @POST("/order/manage")
    suspend fun manageOrder(@Body body : ManageOrderRequest) : StatusResponse
}