package ru.vsu.oFoodAdmin.domain

import ru.vsu.oFoodAdmin.network.response.CategoryResponse

interface CategoryRepo {
    suspend fun getCategory() : Result<List<CategoryResponse>>
}