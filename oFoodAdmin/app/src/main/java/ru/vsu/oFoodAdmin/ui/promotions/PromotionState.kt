package ru.vsu.oFoodAdmin.ui.promotions

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromotionState (
    val id: String,
    val name: String,
    val startRep : String,
    val endRep : String
) : Parcelable

sealed class PromotionEvent {
    object INIT : PromotionEvent()
}