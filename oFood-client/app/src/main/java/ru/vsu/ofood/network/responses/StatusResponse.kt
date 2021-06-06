package ru.vsu.ofood.network.responses

import com.google.gson.annotations.SerializedName

class StatusResponse(
    @SerializedName("status")
    val status : String,
    @SerializedName("message")
    val message : String?
)