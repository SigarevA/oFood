package ru.vsu.ofood.network.responses

import com.google.gson.annotations.SerializedName

class CategoryResponse(
    @SerializedName("id")
    val id : String?,
    @SerializedName("name")
    val name : String?
)