package ru.vsu.oFoodAdmin.domain

import ru.vsu.oFoodAdmin.network.api.CategoryAPI
import ru.vsu.oFoodAdmin.network.response.CategoryResponse
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(private val categoryAPI: CategoryAPI) : CategoryRepo {
    override suspend fun getCategory(): Result<List<CategoryResponse>> {
        return try {
            Result.Success(categoryAPI.getCategories())
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}