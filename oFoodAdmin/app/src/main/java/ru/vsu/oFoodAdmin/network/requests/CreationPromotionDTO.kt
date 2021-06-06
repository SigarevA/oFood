package ru.vsu.oFoodAdmin.network.requests

import java.util.*

class CreationPromotionDTO (
    val name : String,
    val describe : String,
    val start : Long,
    val end : Long,
    val discount : Int,
    val dishes : List<String>
)