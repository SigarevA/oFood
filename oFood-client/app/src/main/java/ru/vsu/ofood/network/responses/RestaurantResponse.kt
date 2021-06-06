package ru.vsu.ofood.network.responses

import com.google.gson.annotations.SerializedName

class RestaurantResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name : String,
    @SerializedName("phone")
    val phone : String,
    @SerializedName("address")
    val address : String,
    @SerializedName("coordinate")
    val coordinate : CoordinateResponse,
    @SerializedName("startOfWorkDay")
    val startOfWorkDay : Int,
    @SerializedName("endOfTheWorkingDay")
    val endOfTheWorkingDay : Int
)