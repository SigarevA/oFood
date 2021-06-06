package ru.vsu.oFoodAdmin.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Order (
    val id: String,
    val phone: String,
    val name: String,
    val city: String,
    val street: String,
    val house: String,
    val goods: Map<String, InfoDishInOrder>,
    val entrance: String? = null,
    val flat: String? = null,
    val surrender : Double? = null,
    val date: Date? = null,
) : Parcelable