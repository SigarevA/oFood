package ru.vsu.oFoodAdmin.ui.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDTO (
    val id: String,
    val phone: String,
    val name: String
) : Parcelable


sealed class OrderEvent {
    object INIT : OrderEvent()
}