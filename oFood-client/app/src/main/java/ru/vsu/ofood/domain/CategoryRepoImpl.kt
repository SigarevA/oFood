package ru.vsu.ofood.domain

import ru.vsu.ofood.entities.Category
import ru.vsu.ofood.network.api.CategoryAPI
import ru.vsu.ofood.utils.toCategories
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(private val categoryAPI: CategoryAPI) : CategoryRepo {
    override suspend fun getCategories(): List<Category> {
        val response = categoryAPI.getCategories()
        val categories = ArrayList<Category>(response.size);
        response.forEach { category ->
            categories.add(
                Category(
                    id = category.id!!,
                    name = category.name!!
                )
            )
        }
        return categories
    }
}