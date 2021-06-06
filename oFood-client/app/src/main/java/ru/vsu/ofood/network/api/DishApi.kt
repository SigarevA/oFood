package ru.vsu.ofood.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.vsu.ofood.network.responses.DishResponse

interface DishApi {
    @GET("/all-dishes-by-category/{id}")
    suspend fun getDishesByCategoryId(@Path("id") id : String) : List<DishResponse>
}