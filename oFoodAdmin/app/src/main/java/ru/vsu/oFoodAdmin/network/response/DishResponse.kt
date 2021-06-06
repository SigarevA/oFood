package ru.vsu.oFoodAdmin.network.response

import com.google.gson.annotations.SerializedName

class DishResponse(
    @SerializedName("id")
    val id : String,
    @SerializedName("price")
    val price : Double,
    @SerializedName("name")
    val name : String,
    @SerializedName("photoPath")
    val photoPath : String,
    @SerializedName("description")
    val description : String
)