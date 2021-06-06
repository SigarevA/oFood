package ru.vsu.oFoodAdmin.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

class PromotionResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("start")
    val start : Date?,
    @SerializedName("end")
    val end : Date?
)