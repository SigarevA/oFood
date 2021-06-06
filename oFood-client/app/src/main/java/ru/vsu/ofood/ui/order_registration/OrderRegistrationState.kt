package ru.vsu.ofood.ui.order_registration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.w3c.dom.Comment
import java.util.*

@Parcelize
data class OrderRegistrationState(
    val phone: TextField = TextField(),
    val name: TextField = TextField(),
    val city: TextField = TextField(),
    val street: TextField = TextField(),
    val house: TextField = TextField(),
    val entrance: String = "",
    val flat: String = "",
    val comment: String = "",
    val clickButton : Boolean = false,
    val isPreOrder : Boolean = false,
    val date : PreOrderDate? = null,
    val time : Int? = null,
    val isOpenedDatePicker : Boolean = false
) : Parcelable

@Parcelize
data class TextField(
    val text : String = "",
    val invalid : Boolean = false
) : Parcelable

@Parcelize
data class PreOrderDate(
    val date : Date,
    val dateRepresent : String
) : Parcelable

sealed class OrderRegistration {
    object Empty : OrderRegistration()
    object PlaceOrder : OrderRegistration()
    object Success : OrderRegistration()
    class Error(val msg : String) : OrderRegistration()
}

sealed class OrderRegistrationEvent {
    class ClickCreateOrder(val state: OrderRegistrationState) : OrderRegistrationEvent()
}