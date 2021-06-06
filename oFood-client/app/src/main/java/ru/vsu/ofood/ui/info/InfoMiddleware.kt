package ru.vsu.ofood.ui.info

import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.ofood.domain.RestaurantRepo
import ru.vsu.ofood.ui.Event
import ru.vsu.ofood.ui.Interpret

private const val TAG = "InfoMiddleware"

class InfoMiddleware(
    private val restaurantRepo: RestaurantRepo,
    private val processEvent: (Event) -> Unit
) : Interpret<InfoAction> {


    private val mutableInfoState = MutableStateFlow<InfoState>(InfoState.Empty)
    val infoState: StateFlow<InfoState>
        get() = mutableInfoState.asStateFlow()

    private val middlewareScope = MainScope()

    init {
        this.complete(InfoAction.Init)
    }

    override fun complete(action: InfoAction) {
        Log.d(TAG, "complete fun")
        when (action) {
            is InfoAction.Init -> {
                middlewareScope.launch {
                    try {
                        val restaurant = restaurantRepo.findRestaurantById("")
                        mutableInfoState.value = InfoState.Content(restaurant)
                    } catch (ex : Exception) {
                        Log.e(TAG, "error", ex)
                    }
                }
            }
            is InfoAction.ClickLocalization -> {
            }
            is InfoAction.ClickPhoneBtn -> {
                if (infoState.value is InfoState.Content) {
                    processEvent(
                        Event.NavigateToActivityCallNumber(
                            (infoState.value as InfoState.Content).restaurant.phone
                        )
                    )
                }
            }
        }
    }
}