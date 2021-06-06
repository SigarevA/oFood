package ru.vsu.oFoodAdmin.ui.addingdish

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.vsu.oFoodAdmin.network.response.CategoryResponse
import ru.vsu.oFoodAdmin.ui.addingpromotion.TextField

data class AddingDishState(
    val categories : List<CategoryDTO>,
    val content : AddingDishScreenContent
)

@Parcelize
data class CategoryDTO (
    val categoryId: String,
    val name : String
) : Parcelable

@Parcelize
data class AddingDishScreenContent (
    val imageUri : Uri? = null,
    val name : TextField = TextField(),
    val description : TextField = TextField(),
    val categoryId : String? = null,
    val price : TextField = TextField(),
    val isError : Boolean = false
) : Parcelable

sealed class AddingDishEvent {
    object Init : AddingDishEvent()
    object PlaceDish : AddingDishEvent()
    class ChangeName(val value : String) : AddingDishEvent()
    class ChangeDescription(val value : String) : AddingDishEvent()
    class ChangePrice(val value : String) : AddingDishEvent()
    class SelectCategory(val categoryId : String ) : AddingDishEvent()
    class GetUri(val uri : Uri) : AddingDishEvent()
}

fun CategoryResponse.toCategoryDTO() : CategoryDTO =
    CategoryDTO(this.id, this.name)