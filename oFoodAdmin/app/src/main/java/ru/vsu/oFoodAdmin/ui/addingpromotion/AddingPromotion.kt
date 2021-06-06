package ru.vsu.oFoodAdmin.ui.addingpromotion

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.parcelize.Parcelize
import ru.vsu.oFoodAdmin.entities.Dish
import ru.vsu.oFoodAdmin.network.requests.CreationPromotionDTO
import ru.vsu.oFoodAdmin.network.response.DishResponse
import java.util.*

@Parcelize
data class FlagsDatePicker(
    val isOpenedStartDatePicker: Boolean = false,
    val isOpenedEndDatePicker: Boolean = false
) : Parcelable

@Parcelize
data class AddingPromotionStateContent(
    val start: PromotionDate? = null,
    val end: PromotionDate? = null,
    val discount: TextField = TextField(),
    val name: TextField = TextField(),
    val describe: TextField = TextField(),
    val products: List<DishDTO>? = null,
    val flagsDatePicker: FlagsDatePicker = FlagsDatePicker(),
    val isRequest: Boolean = false,
    val isInvalid : Boolean = false,
    val successAdding : Boolean = false,
    val failureAdding : Boolean =false
) : Parcelable

@Parcelize
data class TextField(
    val text: String = "",
    val invalid: Boolean = false
) : Parcelable

@Parcelize
data class PromotionDate(
    val date: Date,
    val represent: String
) : Parcelable

@Parcelize
data class DishDTO(
    val dishID: String,
    val name: String,
    val isSelected: Boolean = false
) : Parcelable

fun DishResponse.toDishDTO(): DishDTO {
    return DishDTO(
        dishID = this.id,
        name = this.name
    )
}

sealed class AddingPromotionEvent {
    object INIT : AddingPromotionEvent()
    object REQUEST : AddingPromotionEvent()
}

fun AddingPromotionStateContent.toCreationPromotionDTO(): CreationPromotionDTO {
    return CreationPromotionDTO(
        name = this.name.text,
        describe = this.describe.text,
        start = this.start!!.date.time,
        end = this.end!!.date.time,
        discount = this.discount.text.toInt(),
        dishes = this.products!!.map { it.dishID }
    )
}