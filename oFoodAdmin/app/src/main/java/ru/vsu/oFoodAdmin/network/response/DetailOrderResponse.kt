package ru.vsu.oFoodAdmin.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class DetailOrderResponse (
    @SerializedName("id")
    val id : String,
    @SerializedName("date")
    val date : Date?,
    @SerializedName("phone")
    val phone : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("address")
    val address : String,
    @SerializedName("status")
    val status : String,
    @SerializedName("goods")
    val goods : List<DishInOrderDTO>,
    val surrender : Double,
    val totalPrice : Double
)