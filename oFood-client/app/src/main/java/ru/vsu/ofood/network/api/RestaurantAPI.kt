package ru.vsu.ofood.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.vsu.ofood.network.responses.RestaurantResponse

interface RestaurantAPI {
    @GET("/restaurant/info/{id}")
    suspend fun getInfoOfRestaurant(@Path("id") id : String) : RestaurantResponse
}