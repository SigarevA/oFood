package ru.vsu.ofood.network.api

import retrofit2.http.GET
import ru.vsu.ofood.network.responses.CategoryResponse

private const val prefix = "category"

interface CategoryAPI {
    @GET("$prefix/all")
    suspend fun getCategories() : List<CategoryResponse>
}