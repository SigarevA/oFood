package ru.vsu.ofood.domain

import ru.vsu.ofood.entities.Restaurant

interface RestaurantRepo {
    suspend fun findRestaurantById(id : String) : Restaurant
}