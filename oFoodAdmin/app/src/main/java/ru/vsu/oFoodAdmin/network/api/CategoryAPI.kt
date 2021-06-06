package ru.vsu.oFoodAdmin.network.api

import retrofit2.http.GET
import ru.vsu.oFoodAdmin.network.response.CategoryResponse

interface CategoryAPI {
    @GET("/category/all")
    suspend fun getCategories() : List<CategoryResponse>
}