package ru.vsu.oFoodAdmin.network.response

import com.google.gson.annotations.SerializedName

data class DishInOrderDTO (
    @SerializedName("id")
    val id : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("price")
    val price : Double,
    @SerializedName("count")
    val count : Int
)