package ru.vsu.oFoodAdmin.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoDishInOrder (
    val count : Int,
    val price : Double
) : Parcelable