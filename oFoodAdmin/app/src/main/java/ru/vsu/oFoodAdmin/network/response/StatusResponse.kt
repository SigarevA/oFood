package ru.vsu.oFoodAdmin.network.response

import com.google.gson.annotations.SerializedName

class StatusResponse(
    @SerializedName("status")
    val status : String,
    @SerializedName("message")
    val message : String
)