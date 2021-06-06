package ru.vsu.oFoodAdmin.network.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import ru.vsu.oFoodAdmin.network.response.DishResponse

interface DishAPI {
    @GET("/all-dishes-by-category/{id}")
    suspend fun getDishesByCategoryId(@Path("id") id: String): List<DishResponse>

    @GET("/all-current-dishes")
    suspend fun getCurrentDishes(): List<DishResponse>

    @Multipart
    @POST("/dish/create")
    suspend fun createDish(
        @Part("name") name: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("description") other: RequestBody
    ): String
}