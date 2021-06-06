package ru.vsu.ofood.domain

import ru.vsu.ofood.entities.Coordinate
import ru.vsu.ofood.entities.Restaurant
import ru.vsu.ofood.network.api.RestaurantAPI
import ru.vsu.ofood.network.responses.RestaurantResponse
import javax.inject.Inject

class RestaurantRepoImpl @Inject constructor(private val restaurantAPI: RestaurantAPI) :
    RestaurantRepo {
    override suspend fun findRestaurantById(id: String): Restaurant {
        return Restaurant(
            "id",
            "oFood",
            "89304141766",
            "test@gmail.com",
            "ул. Кольцовская, 17, Воронеж, Воронежская обл., 394036",
                Coordinate(51.672581, 39.199888),
            600,
            1380
            )
        //return restaurantAPI.getInfoOfRestaurant(id).convertToRestaurant()
    }
}

fun RestaurantResponse.convertToRestaurant(): Restaurant =
    Restaurant(
        id = this.id,
        name = this.name,
        phone = this.phone,
        email = "dsa",
        address = this.address,
        coordinate = Coordinate(51.672581, 39.199888),
        startOfWorkDay = this.startOfWorkDay,
        endOfTheWorkingDay = this.endOfTheWorkingDay
    )