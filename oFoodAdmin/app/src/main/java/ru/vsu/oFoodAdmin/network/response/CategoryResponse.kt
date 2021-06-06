package ru.vsu.oFoodAdmin.network.response

import java.util.*

class CategoryResponse (
    val id : String,
    val name : String,
    val numberPage : Int?,
    val dateAdded : Date?,
    val deletionDate : Date?
)