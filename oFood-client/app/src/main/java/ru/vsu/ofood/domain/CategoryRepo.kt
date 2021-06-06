package ru.vsu.ofood.domain

import ru.vsu.ofood.entities.Category

interface CategoryRepo {
    suspend fun getCategories(): List<Category>
}