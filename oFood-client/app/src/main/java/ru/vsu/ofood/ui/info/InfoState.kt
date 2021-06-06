package ru.vsu.ofood.ui.info

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.vsu.ofood.entities.Restaurant

@Parcelize
data class InfoStateContent(
    val phone: String,
    val email: String
) : Parcelable

sealed class InfoState {
    object Empty : InfoState()
    object Loading : InfoState()
    class Error(val ex: Throwable) : InfoState()
    class Content(val restaurant: Restaurant) : InfoState()
}

sealed class InfoAction {
    object Init : InfoAction()
    object ClickPhoneBtn : InfoAction()
    object ClickLocalization : InfoAction()
}