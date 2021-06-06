package ru.vsu.oFoodAdmin.ui.detailorder

sealed class DetailOrderEvent {
    object INIT : DetailOrderEvent()
    object ACCEPTED_ORDER : DetailOrderEvent()
    object REJECTED_ORDER : DetailOrderEvent()
}