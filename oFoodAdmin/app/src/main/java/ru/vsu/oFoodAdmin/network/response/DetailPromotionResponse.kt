package ru.vsu.oFoodAdmin.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

class DetailPromotionResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("describe")
    val describe: String,
    @SerializedName("start")
    val start: Date?,
    @SerializedName("end")
    val end: Date?,
    @SerializedName("discount")
    val discount: Int,
    @SerializedName("canceled")
    val canceled: Boolean,
    @SerializedName("dishes")
    val dishes: List<DishInDetailPromotionDTO>
)